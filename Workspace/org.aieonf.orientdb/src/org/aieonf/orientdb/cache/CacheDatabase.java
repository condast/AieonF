package org.aieonf.orientdb.cache;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.aieonf.commons.parser.ParseException;
import org.aieonf.commons.security.ILoginListener.LoginEvents;
import org.aieonf.commons.security.LoginEvent;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.commons.transaction.AbstractTransaction;
import org.aieonf.commons.transaction.ITransaction;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.body.BodyFactory;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.file.ProjectFolderUtils;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.concept.loader.LoaderAieon;
import org.aieonf.model.core.IModelListener;
import org.aieonf.model.core.ModelEvent;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.model.provider.IModelDatabase;
import org.aieonf.model.provider.IModelProvider;
import com.orientechnologies.orient.core.db.OPartitionedDatabasePool;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.metadata.security.OSecurity;
import com.orientechnologies.orient.core.metadata.security.OUser;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.*;

/**
 * Handles the Orient Databae
 * @See :https://orientdb.com/docs/2.2/documenttx-Database-Tinkerpop.html
 * @author Kees
 *
 * @param <D>
 * @param <Descriptor>
 */
public class CacheDatabase implements IModelDatabase<IDomainAieon, IDescriptor> {
	
	public static final String S_BUNDLE_ID = "org.aieonf.orientdb";
	public static final String S_IDENTIFIER = "documenttxModel";
	
	private static final String S_LOCAL = "plocal:";
	private static final String S_FILE = "file:";
	protected static final String S_ROOT = "Root";
	protected static final String S_CACHE = "Cache";
	protected static final String S_DESCRIPTORS = "Descriptors";

	private ODatabaseDocumentTx database;
	private String source;
	private boolean connected;
	
	private Collection<IModelListener<IDescriptor>> listeners;
	
	private static CacheDatabase cache = new CacheDatabase();
	
	private CacheDatabase() {
		listeners = new ArrayList<IModelListener<IDescriptor>>();
		this.connected = false;
	}

	public static CacheDatabase getInstance(){
		return cache;
	}
	
	/**
	 * Register a new user to the database
	 * @param domain
	 * @param login
	 * @return
	 */
	protected boolean register( ODatabaseDocumentTx dbdoc, IDomainAieon domain, LoginEvent login ) {
		if( !LoginEvents.REGISTER.equals( login.getLoginEvent() ))
			return false;
		String user = login.getUser().getUserName();
		if( StringUtils.isEmpty(user))
			return false;
		String pwd = null;//login.getUser().getPassword();
		if( StringUtils.isEmpty(pwd))
			return false;
		OSecurity sm = dbdoc.getMetadata().getSecurity();
		List<ODocument> users = sm.getAllUsers();
		for( ODocument doc: users ) {
			OUser ouser = new OUser( doc );
			if(ouser.getName().equals( user ))
				return false;
		}
		OUser ouser = sm.createUser( user, pwd, new String[]{ Roles.ADMIN.toString()});	
		return ( ouser != null );
	}
	
	/**
	 * Connect to the database
	 * 
	 * @param loader
	 */
	@SuppressWarnings("resource")
	public void connect( IDomainAieon domain, LoginEvent login ){
		if( connected )
			return;
		String user = login.getUser().getUserName();
		String pwd = null;//login.getPassword();
		ILoaderAieon loader = new LoaderAieon( domain);
		loader.set( IConcept.Attributes.SOURCE, S_BUNDLE_ID);
		loader.setIdentifier( S_CACHE );
		File file = ProjectFolderUtils.getDefaultUserFile( loader, true); 
		source = file.toURI().toString();
		source = source.replace( S_FILE, S_LOCAL);
		ODatabaseDocumentTx doc = new ODatabaseDocumentTx (source); 
		switch( login.getLoginEvent() ) {
		case REGISTER:
			if(!doc.exists() ) {
				database = doc.create();
				database.addCluster(S_DESCRIPTORS);
				register( database, domain, login );
			}else {
				OPartitionedDatabasePool pool =  new OPartitionedDatabasePool(source , user, pwd  );
				database = pool.acquire();				
			}
			break;
		case LOGIN:
			OPartitionedDatabasePool pool =  new OPartitionedDatabasePool(source , user, pwd  );
			database = pool.acquire();
			break;
		default:
			database.close();
			break;
		}
		this.connected = true;
	}
	
	public void disconnect() {
		close();
		this.connected = false;
	}

	@Override
	public String getIdentifier(){
		return S_IDENTIFIER;
	}
	
	@Override
	public void addListener(IModelListener<IDescriptor> listener) {
		this.listeners.add(listener);
	}

	@Override
	public void removeListener(IModelListener<IDescriptor> listener) {
		this.listeners.remove(listener);
	}

	protected final void notifyListeners( ModelEvent<IDescriptor> event ){
		for( IModelListener<IDescriptor> listener: this.listeners )
			listener.notifyChange(event);
	}
	
	@Override
	public void open( IDomainAieon domain){
		try{
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

	public ITransaction<IDescriptor,IModelProvider<IDomainAieon, IDescriptor>> createTransaction() {
		Transaction transaction = new Transaction( this );
		transaction.create();
		return transaction;
	}

	@Override
	public void close(){
		//database.commit();
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

	@SuppressWarnings("unchecked")
	public Collection<IDescriptor> query( String query ){
		Collection<ODocument> docs = (Collection<ODocument>) this.database.query(new OSQLSynchQuery<ODocument>(query));
		Collection<IDescriptor> results = new ArrayList<IDescriptor>();
		for( ODocument doc: docs )
			results.add( new ODescriptor( doc ));
		return results;
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
	public Collection<IDescriptor> search(IModelFilter<IDescriptor, IDescriptor> filter) throws ParseException {
		Collection<IDescriptor> results = new ArrayList<IDescriptor>();
		for (ODocument document : database.browseCluster( S_DESCRIPTORS )) {
			ODescriptor descriptor = new ODescriptor(document);    
			if( filter.accept( descriptor ))
				results.add( descriptor );
		}		
		return results;
	}

	@Override
	public boolean hasFunction(String function) {
		return IModelProvider.DefaultModels.DESCRIPTOR.equals( function );
	}

	@Override
	public boolean add(IDescriptor descriptor) {
		ODocument odesc= createDocument( descriptor );
		odesc.save( /*S_DESCRIPTORS*/ );//Add to cluster descriptors
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

	protected class Transaction extends AbstractTransaction<IDescriptor, IModelProvider<IDomainAieon, IDescriptor>>{

		protected Transaction( IModelProvider<IDomainAieon,IDescriptor> provider) {
			super( provider );
		}

		public void close() {
			super.getProvider().close();
			if( !super.getProvider().isOpen())
				super.close();
		}

		@Override
		protected boolean onCreate(IModelProvider<IDomainAieon, IDescriptor> provider) {
			return super.getProvider().isOpen();
		}
	}
}