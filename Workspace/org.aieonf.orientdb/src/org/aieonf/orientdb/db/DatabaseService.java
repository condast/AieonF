package org.aieonf.orientdb.db;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.aieonf.commons.parser.ParseException;
import org.aieonf.commons.security.ILoginListener.LoginEvents;
import org.aieonf.commons.security.ILoginUser;
import org.aieonf.commons.security.LoginEvent;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.commons.transaction.AbstractTransaction;
import org.aieonf.commons.transaction.ITransaction;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.body.BodyFactory;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.core.IModelListener;
import org.aieonf.model.core.ModelEvent;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.model.provider.IModelDatabase;
import org.aieonf.model.provider.IModelProvider;
import org.aieonf.orientdb.cache.ODescriptor;
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
public class DatabaseService implements IModelDatabase<IDomainAieon, IDescriptor> {
	
	public static final String S_BUNDLE_ID = "org.aieonf.orientdb";
	public static final String S_IDENTIFIER = "documenttxModel";
	
	protected static final String S_ROOT = "Root";
	protected static final String S_CACHE = "Cache";
	protected static final String S_DESCRIPTORS = "Descriptors";

	private Collection<IModelListener<IDescriptor>> listeners;
	
	private static DatabaseService cache = new DatabaseService();
	private static DatabasePersistenceService persistence;
	
	private ODatabaseDocumentTx database;
	private boolean connected;
	private IDomainAieon domain;
	
	private DatabaseService() {
		OrientDBFactory factory = OrientDBFactory.getInstance();
		factory.createTemplate();
		domain = factory.getDomain();
		persistence = new DatabasePersistenceService(domain);
		listeners = new ArrayList<IModelListener<IDescriptor>>();
		this.connected = false;
	}

	public static DatabaseService getInstance(){
		return cache;
	}
	
	/**
	 * Register a new user to the database
	 * @param domain
	 * @param login
	 * @return
	 */
	protected boolean register( ODatabaseDocumentTx dbdoc, LoginEvent login ) {
		OSecurity sm = database.getMetadata().getSecurity();
		ILoginUser loginUser = login.getUser();
		switch( login.getLoginEvent() ) {
		case REGISTER:
			OUser user = sm.createUser( loginUser.getUserName(), login.getPassword(), new String[] { ILoginUser.Roles.ADMIN.name().toLowerCase() });
			return true;
		case LOGIN:
			List<ODocument> users = sm.getAllUsers();
			for( ODocument doc: users ) {
				OUser ouser = new OUser( doc );
				if(ouser.getName().equals( loginUser.getUserName() ))
					return true;
			}
			break;
		default:
			break;
		}
		return false;
	}
	
	/**
	 * Connect to the database
	 * 
	 * @param loader
	 */
	public void open( ){
		if( !persistence.isConnected() )
			return;
		ILoginUser user = persistence.getUser();
		database = persistence.getDatabase().open(user.getUserName(), String.valueOf( user.getToken()));
		this.connected = true;
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
		this.connected = false;
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