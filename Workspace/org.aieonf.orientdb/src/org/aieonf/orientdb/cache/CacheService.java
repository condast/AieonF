package org.aieonf.orientdb.cache;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import org.aieonf.commons.parser.ParseException;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.body.BodyFactory;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.model.core.IModelListener;
import org.aieonf.model.core.ModelEvent;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.model.provider.IModelProvider;
import org.aieonf.orientdb.core.DocumentConceptBase;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.exception.OQueryParsingException;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.*;

/**
 * Handles the Orient Database
 * @See :https://orientdb.com/docs/2.2/documenttx-Database-Tinkerpop.html
 * @author Kees
 *
 * @param <D>
 * @param <Descriptor>
 */
public class CacheService implements Closeable{
	
	public static final String S_BUNDLE_ID = "org.aieonf.orientdb";
	public static final String S_IDENTIFIER = "documenttxModel";
	
	protected static final String S_ROOT = "Root";
	protected static final String S_CACHE = "Cache";

	private Collection<IModelListener<IDescriptor>> listeners;
	
	private static CacheService cache = new CacheService();
	private static CachePersistenceService persistence = CachePersistenceService.getInstance();
	
	private ODatabaseDocumentTx database;
	
	private CacheService() {
		listeners = new ArrayList<IModelListener<IDescriptor>>();
	}

	public static CacheService getInstance(){
		return cache;
	}
		
	/**
	 * Connect to the database
	 * 
	 * @param loader
	 */
	public void open( ){
		if( !persistence.isConnected() )
			return;
		database = persistence.getDatabase();	
		if (!database.isActiveOnCurrentThread()) {
			database.activateOnCurrentThread();
		}
		if(! database.existsCluster(S_CACHE))
			database.addCluster(S_CACHE);
	}
	
	public void addListener(IModelListener<IDescriptor> listener) {
		this.listeners.add(listener);
	}

	public void removeListener(IModelListener<IDescriptor> listener) {
		this.listeners.remove(listener);
	}

	protected final void notifyListeners( ModelEvent<IDescriptor> event ){
		for( IModelListener<IDescriptor> listener: this.listeners )
			listener.notifyChange(event);
	}
	
	public boolean isOpen(){
		return !this.database.isClosed();
	}

	public void sync(){
		try{
			this.database.commit();
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
		finally{
			if( this.database != null )
				this.database.rollback();
		}
	}

	public void close(){
		try {
			if(( database != null ) && !database.isClosed() ) {
				database.close();
			}
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
	}

	public boolean contains(IDescriptor descriptor) {
		for (ODocument document : database.browseClass( descriptor.getName())) {
			long id = document.field( IDescriptor.Attributes.ID.name().toLowerCase());    
			if( descriptor.getID() ==  id )
				return true;
		}		
		return false;
	}

	@SuppressWarnings("unchecked")
	public Collection<IDescriptor> query( String query ){
		Collection<ODocument> docs = new ArrayList<>();
		try{
			docs = (Collection<ODocument>)this.database.query(new OSQLSynchQuery<ODocument>(query));
		}
		catch( OQueryParsingException pex ) {
			/* NOTHING WAS FOUND */
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		Collection<IDescriptor> results = new ArrayList<IDescriptor>();
		for( ODocument doc: docs )
			results.add( new DocumentDescriptor( doc ));
		return results;
	}

	public IDescriptor[] get( String id) throws ParseException {
		Collection<IDescriptor> results = query( "SELECT FROM " + S_CACHE + " WHERE ID IS " + id);
		return results.toArray( new IDescriptor[ results.size()]);
	}

	public IDescriptor[] get(IDescriptor descriptor) throws ParseException {
		Collection<IDescriptor> results = query( "SELECT FROM " + descriptor.getName());
		return results.toArray( new IDescriptor[ results.size()]);
	}

	public Collection<IDescriptor> search(IModelFilter<IDescriptor, IDescriptor> filter) throws ParseException {
		Collection<IDescriptor> results = new ArrayList<IDescriptor>();
		for (ODocument document : database.browseCluster( S_CACHE )) {
			IDescriptor descriptor = new DocumentDescriptor(document);    
			if( filter.accept( descriptor ))
				results.add( descriptor );
		}		
		return results;
	}

	public boolean hasFunction(String function) {
		return IModelProvider.DefaultModels.DESCRIPTOR.equals( function );
	}

	public IDescriptor add(IDescriptor descriptor) {
		ODocument odesc= createDocument( descriptor );
		odesc.save( S_CACHE );
		return new DocumentDescriptor( odesc );
	}

	public void remove(IDescriptor descriptor) {
		DocumentDescriptor odesc= (DocumentDescriptor) descriptor;
		odesc.getDocument().delete();
	}

	public boolean update(IDescriptor descriptor ){
		DocumentDescriptor odesc= (DocumentDescriptor) descriptor;
		odesc.getDocument().save();
		return true;
	}

	public static IDescriptor transform( IDescriptor descriptor ) {
		return new DocumentDescriptor( createDocument(descriptor));
	}
	
	/**
	 * Create a descriptor from the given vertex
	 * @param database
	 * @param vertex
	 * @return
	 */
	protected static ODocument createDocument( IDescriptor describable ){
		IDescriptor descriptor = describable.getDescriptor();
		ODocument doc = new ODocument( descriptor.getName());
		Iterator<String> iterator = descriptor.keySet();
		while( iterator.hasNext()) {
			String attr = iterator.next();
			attr = attr.replace(".", "@8");
			String value = descriptor.get( attr );
			if( !StringUtils.isEmpty( value ))
				doc.field( attr, value);
		}
		BodyFactory.IDFactory( descriptor );
		String date = String.valueOf( Calendar.getInstance().getTimeInMillis());
		descriptor.set( IDescriptor.Attributes.CREATE_DATE, date );
		return doc;		
	}

	private static class DocumentDescriptor extends Descriptor{
		private static final long serialVersionUID = -1242830383589783796L;
		
		private ODocument document;
		
		protected DocumentDescriptor( ODocument document) {
			super( new DocumentConceptBase( document ));
			this.document = document;
		}
		
		private ODocument getDocument() {
			return document;
		}	
	}
}