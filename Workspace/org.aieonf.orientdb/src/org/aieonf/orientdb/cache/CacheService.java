package org.aieonf.orientdb.cache;

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
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.body.BodyFactory;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.security.IPasswordAieon;
import org.aieonf.concept.security.PasswordAieon;
import org.aieonf.model.core.IModelListener;
import org.aieonf.model.core.ModelEvent;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.model.provider.IModelDatabase;
import org.aieonf.model.provider.IModelProvider;
import org.aieonf.orientdb.factory.OrientDBFactory;

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
public class CacheService {
	
	public static final String S_BUNDLE_ID = "org.aieonf.orientdb";
	public static final String S_IDENTIFIER = "documenttxModel";
	
	protected static final String S_ROOT = "Root";
	protected static final String S_CACHE = "Cache";
	protected static final String S_DESCRIPTORS = "Descriptors";

	private Collection<IModelListener<IDescriptor>> listeners;
	
	private static CacheService cache = new CacheService();
	private static CachePersistenceService persistence;
	
	private ODatabaseDocumentTx database;
	private boolean connected;
	private IDomainAieon domain;
	
	private CacheService() {
		OrientDBFactory factory = OrientDBFactory.getInstance();
		factory.createTemplate();
		domain = factory.getDomain();
		persistence = new CachePersistenceService(domain);
		listeners = new ArrayList<IModelListener<IDescriptor>>();
		this.connected = false;
	}

	public static CacheService getInstance(){
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
		OUser ouser = sm.createUser( user, pwd, new String[]{ IModelDatabase.Roles.ADMIN.toString()});	
		return ( ouser != null );
	}
	
	/**
	 * Connect to the database
	 * 
	 * @param loader
	 */
	public void open( ){
		if( !persistence.isConnected() )
			return;
		IPasswordAieon password = new PasswordAieon( domain );
		database = persistence.getDatabase().open(password.getUserName(), password.getPassword() );
		this.connected = true;
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
	
	public void open( IDomainAieon domain){
		try{
			if(!connected )
				return;
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
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
		this.connected = false;
		//database.commit();
		if( database != null )
			database.close();
	}

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
	
	public Collection<IDescriptor> get(IDescriptor descriptor) throws ParseException {
		Collection<IDescriptor> results = new ArrayList<IDescriptor>();
		for (ODocument document : database.browseClass( descriptor.getName())) {
			String id = document.field( IDescriptor.Attributes.ID.name().toLowerCase());    
			if( descriptor.getID().equals( id ))
				results.add( descriptor );
		}		
		return results;
	}

	public Collection<IDescriptor> search(IModelFilter<IDescriptor, IDescriptor> filter) throws ParseException {
		Collection<IDescriptor> results = new ArrayList<IDescriptor>();
		for (ODocument document : database.browseCluster( S_DESCRIPTORS )) {
			ODescriptor descriptor = new ODescriptor(document);    
			if( filter.accept( descriptor ))
				results.add( descriptor );
		}		
		return results;
	}

	public boolean hasFunction(String function) {
		return IModelProvider.DefaultModels.DESCRIPTOR.equals( function );
	}

	public boolean add(IDescriptor descriptor) {
		ODocument odesc= createDocument( descriptor );
		odesc.save( /*S_DESCRIPTORS*/ );//Add to cluster descriptors
		return true;
	}

	public void remove(IDescriptor descriptor) {
		ODescriptor odesc= (ODescriptor) descriptor;
		odesc.getDocument().delete();
	}

	public boolean update(IDescriptor descriptor ){
		ODescriptor odesc= (ODescriptor) descriptor;
		odesc.getDocument().save();
		return true;
	}

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