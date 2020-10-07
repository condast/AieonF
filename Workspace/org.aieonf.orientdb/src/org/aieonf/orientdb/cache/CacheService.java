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
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelListener;
import org.aieonf.model.core.IModelNode;
import org.aieonf.model.core.ModelEvent;
import org.aieonf.model.core.ModelLeaf;
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

	public static final String S_ERR_NO_DESCRIPTORS = "No descriptors were found for id: ";

	private Collection<IModelListener<IDescriptor>> listeners;
		
	private ODatabaseDocumentTx database;
	
	public CacheService( ODatabaseDocumentTx database ) {
		this.database = database;
		listeners = new ArrayList<IModelListener<IDescriptor>>();
	}
		
	/**
	 * Connect to the database
	 * 
	 * @param loader
	 */
	public boolean open( ){
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
			String idstr = document.field( IDescriptor.Attributes.ID.name().toLowerCase());  
			long id = StringUtils.isEmpty(idstr)?-1:Long.parseLong(idstr);
			if( descriptor.getID() == id )
				return true;
		}		
		return false;
	}

	/**
	 * Create a descriptor from the given vertex
	 * @param database
	 * @param vertex
	 * @return
	 */
	public void add( IDescriptor[] descriptors ){
		for( IDescriptor descriptor: descriptors ) {
			ODocument doc = new ODocument( );
			Iterator<String> iterator = descriptor.keySet();
			while( iterator.hasNext()) {
				String attr = iterator.next();
				attr = attr.replace(".", "@8");
				String value = descriptor.get( attr );
				if( !StringUtils.isEmpty( value ))
					doc.field( attr, value);
			}
			String date = String.valueOf( Calendar.getInstance().getTimeInMillis());
			descriptor.set( IDescriptor.Attributes.CREATE_DATE.name(), date );
			descriptor.set( IDescriptor.Attributes.ID.name(), doc.getIdentity().toString());
			doc.save( S_CACHE);
		}
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
			results.add( createDescriptor( doc ));
		return results;
	}

	public IDescriptor[] get( long id) throws ParseException {
		Collection<IDescriptor> results = query( "SELECT FROM CLUSTER:" + S_CACHE );//+ " WHERE ID = " + id);
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

	public Collection<IDescriptor> search(IModelFilter<IDescriptor> filter) throws ParseException {
		Collection<IDescriptor> results = new ArrayList<IDescriptor>();
		for (ODocument document : database.browseCluster( S_CACHE )) {
			IDescriptor descriptor = createDescriptor(document);    
			if( filter.accept( descriptor ))
				results.add( descriptor );
		}		
		return results;
	}

	@SuppressWarnings("unchecked")
	public void fill(ModelLeaf<IDescriptor> model) throws ParseException {
		String id_str = model.get( IDescriptor.DESCRIPTOR);
		long id = StringUtils.isEmpty(id_str)?-1: Long.parseLong(id_str);
		IDescriptor[] descriptors = get( id );
		if( Utils.assertNull(descriptors))
			throw new NullPointerException( S_ERR_NO_DESCRIPTORS + id);
		model.setData(descriptors[0]);
		if( model.isLeaf())
			return;
		IModelNode<IDescriptor> node = (IModelNode<IDescriptor>) model;
		for( IModelLeaf<? extends IDescriptor> child: node.getChildren().keySet()) {
			fill( (ModelLeaf<IDescriptor>) child );
		}
	}

	@SuppressWarnings("unchecked")
	public void fill(Collection<IModelLeaf<IDescriptor>> models) throws ParseException {
		for( IModelLeaf<? extends IDescriptor> model: models )
			fill( (ModelLeaf<IDescriptor>) model);
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
	protected static IDescriptor createDescriptor( ODocument document ){
		Long id = document.field(IDescriptor.Attributes.ID.name());
		IDescriptor descriptor = new Descriptor((id == null)?-1:id );
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