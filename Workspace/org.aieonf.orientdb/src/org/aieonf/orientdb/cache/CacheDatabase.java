package org.aieonf.orientdb.cache;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import org.aieonf.commons.parser.ParseException;
import org.aieonf.commons.transaction.AbstractTransaction;
import org.aieonf.commons.transaction.ITransaction;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.body.BodyFactory;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.file.ProjectFolderUtils;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.concept.loader.LoaderAieon;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.model.provider.IModelDatabase;
import org.aieonf.model.provider.IModelProvider;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;

/**
 * Handles the Orient Databae
 * @See :https://orientdb.com/docs/2.2/documenttx-Database-Tinkerpop.html
 * @author Kees
 *
 * @param <D>
 * @param <Descriptor>
 */
public class CacheDatabase<D extends IDomainAieon> implements IModelDatabase<D, IDescriptor> {
	
	public static final String S_BUNDLE_ID = "org.aieonf.orientdb";
	public static final String S_IDENTIFIER = "documenttxModel";
	
	private static final String S_LOCAL = "plocal:";
	private static final String S_FILE = "file:";
	protected static final String S_ROOT = "Root";
	protected static final String S_CACHE = "Cache";

	private ODatabaseDocumentTx database;
	private String source;
	private boolean connected;
	
	private Collection<IModelBuilderListener<IDescriptor>> listeners;
	
	public CacheDatabase() {
		listeners = new ArrayList<IModelBuilderListener<IDescriptor>>();
		this.connected = false;
	}

	/**
	 * Connect to the database
	 * 
	 * @param loader
	 */
	@SuppressWarnings("resource")
	protected void connect( IDomainAieon domain ){
		if( connected )
			return;
		String user = domain.getUserName();
		String pwd = domain.getPassword();
		ILoaderAieon loader = new LoaderAieon( domain);
		loader.set( IConcept.Attributes.SOURCE, S_BUNDLE_ID);
		loader.setIdentifier( S_CACHE );
		source = ProjectFolderUtils.getDefaultUserDir( loader, true).toString();
		source = source.replace( S_FILE, S_LOCAL);
		database = new ODatabaseDocumentTx (source).open( user, pwd );
		this.connected = true;
	}
	
	@Override
	public String getIdentifier(){
		return S_IDENTIFIER;
	}
	
	@Override
	public void addListener(IModelBuilderListener<IDescriptor> listener) {
		this.listeners.add(listener);
	}

	@Override
	public void removeListener(IModelBuilderListener<IDescriptor> listener) {
		this.listeners.remove(listener);
	}

	protected final void notifyListeners( ModelBuilderEvent<IDescriptor> event ){
		for( IModelBuilderListener<IDescriptor> listener: this.listeners )
			listener.notifyChange(event);
	}
	
	@Override
	public void open( IDomainAieon domain){
		try{
			this.connect(domain);
			if(!connected )
				return;
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
	}

	@Override
	public boolean isOpen(){
		return !this.database.isClosed();
	}

	@Override
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

	public ITransaction<IDescriptor,IModelProvider<D, IDescriptor>> createTransaction() {
		Transaction transaction = new Transaction( this );
		transaction.create();
		return transaction;
	}

	@Override
	public void close(){
		this.sync();
		if( database != null )
			database.close();
	}

	@Override
	public boolean contains(IDescriptor descriptor) {
		for (ODocument document : database.browseClass( descriptor.getName())) {
			String id = document.field( IDescriptor.Attributes.ID.name().toLowerCase());    
			if( descriptor.getID().equals( id ))
				return true;
		}		
		return false;
	}

	@Override
	public Collection<IDescriptor> get(IDescriptor descriptor) throws ParseException {
		Collection<IDescriptor> results = new ArrayList<IDescriptor>();
		for (ODocument document : database.browseClass( descriptor.getName())) {
			String id = document.field( IDescriptor.Attributes.ID.name().toLowerCase());    
			if( descriptor.getID().equals( id ))
				results.add( descriptor );
		}		
		return results;
	}

	@Override
	public Collection<IDescriptor> search(IModelFilter<IDescriptor> filter) throws ParseException {
		// NOT IMPLEMENTED; not needed for cache!
		return null;
	}

	@Override
	public boolean hasFunction(String function) {
		return IModelProvider.DefaultModels.DESCRIPTOR.equals( function );
	}

	@Override
	public boolean add(IDescriptor descriptor) {
		ODocument odesc= createDocument( descriptor );
		odesc.save();
		return true;
	}

	@Override
	public void remove(IDescriptor descriptor) {
		ODescriptor odesc= (ODescriptor) descriptor;
		odesc.getDocument().delete();
	}

	@Override
	public boolean update(IDescriptor descriptor ){
		ODescriptor odesc= (ODescriptor) descriptor;
		odesc.getDocument().save();
		return true;
	}

	@Override
	public void deactivate() {
		database.close();
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
		Iterator<String> iterator = descriptor.iterator();
		while( iterator.hasNext()) {
			String attr = iterator.next();
			doc.field( attr, descriptor.get( attr ));
		}
		BodyFactory.IDFactory( descriptor );
		String date = String.valueOf( Calendar.getInstance().getTimeInMillis());
		descriptor.set( IDescriptor.Attributes.CREATE_DATE, date );
		return doc;		
	}

	protected class Transaction extends AbstractTransaction<IDescriptor, IModelProvider<D, IDescriptor>>{

		protected Transaction( IModelProvider<D,IDescriptor> provider) {
			super( provider );
		}

		public void close() {
			super.getProvider().close();
			if( !super.getProvider().isOpen())
				super.close();
		}

		@Override
		protected boolean onCreate(IModelProvider<D, IDescriptor> provider) {
			return super.getProvider().isOpen();
		}
	}
}