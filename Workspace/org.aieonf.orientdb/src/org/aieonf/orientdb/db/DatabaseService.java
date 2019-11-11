package org.aieonf.orientdb.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.aieonf.commons.parser.ParseException;
import org.aieonf.commons.security.ILoginUser;
import org.aieonf.commons.security.LoginEvent;
import org.aieonf.commons.transaction.AbstractTransaction;
import org.aieonf.commons.transaction.ITransaction;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.core.IModelListener;
import org.aieonf.model.core.IModelNode;
import org.aieonf.model.core.ModelEvent;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.model.provider.IModelDatabase;
import org.aieonf.model.provider.IModelProvider;
import org.aieonf.orientdb.graph.OrientDBModelLeaf;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.metadata.security.OSecurity;
import com.orientechnologies.orient.core.metadata.security.OUser;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

/**
 * Handles the Orient Databae
 * @See :https://orientdb.com/docs/2.2/documenttx-Database-Tinkerpop.html
 * @author Kees
 *
 * @param <D>
 * @param <Descriptor>
 */
public class DatabaseService implements IModelDatabase<IDomainAieon, IModelNode<IDescriptor>> {
	
	public static final String S_BUNDLE_ID = "org.aieonf.orientdb";
	public static final String S_IDENTIFIER = "documenttxModel";
	
	protected static final String S_ROOT = "Root";
	protected static final String S_DESCRIPTORS = "Descriptors";

	private Collection<IModelListener<IModelNode<IDescriptor>>> listeners;
	
	private static DatabaseService service = new DatabaseService();
	
	private static DatabasePersistenceService persistence = DatabasePersistenceService.getInstance();

	private OrientGraph graph;
	private boolean open;
	
	private DatabaseService() {
		listeners = new ArrayList<>();
	}

	public static DatabaseService getInstance(){
		return service;
	}
	
	public OrientGraph getGraph() {
		return graph;
	}

	/**
	 * Register a new user to the database
	 * @param domain
	 * @param login
	 * @return
	 */
	protected boolean register( ODatabaseDocumentTx dbdoc, LoginEvent login ) {
		OSecurity sm = graph.getRawGraph().getMetadata().getSecurity();
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
	public boolean open( ){
		if( !persistence.isConnected() )
			return false;
		graph = persistence.getDatabase();
		//graph.begin();
		this.open = true;
		return true;
	}
	
	@Override
	public String getIdentifier(){
		return S_IDENTIFIER;
	}
	
	@Override
	public void addListener(IModelListener<IModelNode<IDescriptor>> listener) {
		this.listeners.add(listener);
	}

	@Override
	public void removeListener(IModelListener<IModelNode<IDescriptor>> listener) {
		this.listeners.remove(listener);
	}

	protected final void notifyListeners( ModelEvent<IModelNode<IDescriptor>> event ){
		for( IModelListener<IModelNode<IDescriptor>> listener: this.listeners )
			listener.notifyChange(event);
	}
	
	@Override
	public void open( IDomainAieon domain){
		try{
			if(!open )
				return;
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
	}

	@Override
	public boolean isOpen(){
		return !this.graph.isClosed();
	}

	@Override
	public void sync(){
		try{
			this.graph.commit();
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
		finally{
			if( this.graph != null )
				this.graph.rollback();
		}
	}

	public ITransaction<IModelNode<IDescriptor>,IModelProvider<IDomainAieon, IModelNode<IDescriptor>>> createTransaction() {
		Transaction transaction = new Transaction( this );
		transaction.create();
		return transaction;
	}

	@Override
	public void close(){
		this.open = false;
		//graph.commit();
	}

	@Override
	public boolean contains(IModelNode<IDescriptor> model ) {
		/*
		for (ODocument document : graph.gbrowseClass( model.getID())) {
			String id = document.field( IDescriptor.Attributes.ID.name().toLowerCase());    
			if( model.getID().equals( id ))
				return true;
		}	
		*/	
		return false;
	}

	@Override
	public Collection<IModelNode<IDescriptor>> get(IDescriptor descriptor) throws ParseException {
		Collection<IModelNode<IDescriptor>> results = new ArrayList<>();
		/*
		for (ODocument document : graph.browseClass( descriptor.getName())) {
			String id = document.field( IDescriptor.Attributes.ID.name().toLowerCase());    
			if( descriptor.getID().equals( id ))
				results.add( descriptor );
		}	
		*/	
		return results;
	}

	
	@Override
	public Collection<IModelNode<IDescriptor>> search(IModelFilter<IDescriptor, IModelNode<IDescriptor>> filter)
			throws ParseException {
		Collection<IModelNode<IDescriptor>> results = new ArrayList<>();
/*
		for (ODocument document : graph.browseCluster( S_DESCRIPTORS )) {
			ODescriptor descriptor = new ODescriptor(document);    
			if( filter.accept( descriptor ))
				results.add( descriptor );
		}		
		*/
		return results;
	}

	@Override
	public boolean hasFunction(String function) {
		return IModelProvider.DefaultModels.DESCRIPTOR.equals( function );
	}

	@Override
	public boolean add(IModelNode<IDescriptor> model) {
		OrientDBModelLeaf odesc= null;//createDocument( model.getDescriptor() );
		//odesc.save( /*S_DESCRIPTORS*/ );//Add to cluster descriptors
		return true;
	}

	@Override
	public void remove(IModelNode<IDescriptor> model) {
		//ODescriptor odesc= null;//(ODescriptor) model;
		//odesc.getDocument().delete();
	}

	@Override
	public boolean update(IModelNode<IDescriptor> model ){
		//ODescriptor odesc= null;//(ODescriptor) model;
		//odesc.getDocument().save();
		return true;
	}

	@Override
	public void deactivate() {
		graph.shutdown();
	}

	protected class Transaction extends AbstractTransaction<IModelNode<IDescriptor>, IModelProvider<IDomainAieon, IModelNode<IDescriptor>>>{

		protected Transaction( IModelProvider<IDomainAieon,IModelNode<IDescriptor>> provider) {
			super( provider );
		}

		public void close() {
			super.getProvider().close();
			if( !super.getProvider().isOpen())
				super.close();
		}

		@Override
		protected boolean onCreate(IModelProvider<IDomainAieon, IModelNode<IDescriptor>> provider) {
			return super.getProvider().isOpen();
		}
	}
}