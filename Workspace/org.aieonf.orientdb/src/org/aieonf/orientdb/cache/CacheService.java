package org.aieonf.orientdb.cache;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.aieonf.commons.Utils;
import org.aieonf.commons.parser.ParseException;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.model.core.IModelListener;
import org.aieonf.model.core.ModelEvent;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.model.provider.IModelProvider;

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
	public boolean open( ){
		boolean result = persistence.open();
		if(!result )
			return result;
		database = persistence.getDatabase();
		if(!database.isActiveOnCurrentThread())
			database.activateOnCurrentThread();
		if(! database.existsCluster(S_CACHE))
			database.addCluster(S_CACHE);
		return true;
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
			this.database.activateOnCurrentThread();
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
			results.add( createDescriptor( doc ));
		return results;
	}

	public IDescriptor[] get( long id) throws ParseException {
		Collection<IDescriptor> results = query( "SELECT FROM CLUSTER:" + S_CACHE + " WHERE ID = " + id);
		return results.toArray( new IDescriptor[ results.size()]);
	}

	public Map<Long, IDescriptor> get( long[] ids ) throws ParseException {
		Map<Long,IDescriptor> results = new HashMap<>();
		for( long id: ids ) {
			IDescriptor[] temp = get( id );
			if(!Utils.assertNull(temp))
				results.put( id, temp[0] );
		}
		return results;
	}

	public Map<Long, IDescriptor> get( Collection<Long> ids ) throws ParseException {
		Map<Long,IDescriptor> results = new HashMap<>();
		for( long id: ids ) {
			IDescriptor[] temp = get( id );
			if(!Utils.assertNull(temp))
				results.put( id, temp[0] );
		}
		return results;
	}

	public IDescriptor[] get(IDescriptor descriptor) throws ParseException {
		Collection<IDescriptor> results = query( "SELECT FROM " + descriptor.getName());
		return results.toArray( new IDescriptor[ results.size()]);
	}

	public Collection<IDescriptor> search(IModelFilter<IDescriptor, IDescriptor> filter) throws ParseException {
		Collection<IDescriptor> results = new ArrayList<IDescriptor>();
		for (ODocument document : database.browseCluster( S_CACHE )) {
			IDescriptor descriptor = createDescriptor(document);    
			if( filter.accept( descriptor ))
				results.add( descriptor );
		}		
		return results;
	}

	public boolean hasFunction(String function) {
		return IModelProvider.DefaultModels.DESCRIPTOR.equals( function );
	}
	
	/**
	 * Create a descriptor from the given vertex
	 * @param database
	 * @param vertex
	 * @return
	 */
	protected static void createDocuments( IDescriptor[] descriptors ){
		for( IDescriptor descriptor: descriptors ) {
			ODocument doc = new ODocument( descriptor.getName());
			Iterator<String> iterator = descriptor.keySet();
			while( iterator.hasNext()) {
				String attr = iterator.next();
				attr = attr.replace(".", "@8");
				String value = descriptor.get( attr );
				if( !StringUtils.isEmpty( value ))
					doc.field( attr, value);
			}
			String date = String.valueOf( Calendar.getInstance().getTimeInMillis());
			descriptor.set( IDescriptor.Attributes.CREATE_DATE, date );
		}
	}

	/**
	 * Create a descriptor from the given vertex
	 * @param database
	 * @param vertex
	 * @return
	 */
	protected static IDescriptor createDescriptor( ODocument document ){
		long id = document.field(IDescriptor.Attributes.ID.name());
		IDescriptor descriptor = new Descriptor(id, document.getClassName() );
		descriptor.setVersion( document.getVersion());
		Iterator<String> iterator = descriptor.keySet();
		while( iterator.hasNext()) {
			String attr = iterator.next();
			attr = attr.replace("@8", ".");
			String value = descriptor.get( attr );
			if( !StringUtils.isEmpty( value ))
				descriptor.set( attr, value);
		}
		return descriptor;
	}

}