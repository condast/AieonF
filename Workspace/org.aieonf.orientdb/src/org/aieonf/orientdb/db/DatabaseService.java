package org.aieonf.orientdb.db;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.aieonf.commons.security.ILoginUser;
import org.aieonf.commons.security.LoginEvent;
import org.aieonf.commons.transaction.AbstractTransaction;
import org.aieonf.commons.transaction.ITransaction;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.core.IModelListener;
import org.aieonf.model.core.IModelNode;
import org.aieonf.model.core.ModelEvent;

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
public class DatabaseService implements Closeable{
	
	public static final String S_BUNDLE_ID = "org.aieonf.orientdb";
	public static final String S_IDENTIFIER = "documenttxModel";
	
	protected static final String S_ROOT = "Root";

	private Collection<IModelListener<IModelNode<IDescriptor>>> listeners;
	
	private static DatabaseService service = new DatabaseService();
	
	private static DatabasePersistenceService persistence = DatabasePersistenceService.getInstance();

	private OrientGraph graph;
	
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
	
	protected String getDomainName( IDomainAieon domain  ) {
		return domain.getDomain().replace("." , "_");
	}
	
	/**
	 * Connect to the database
	 * 
	 * @param loader
	 */
	public boolean open( IDomainAieon domain ){
		if( !persistence.isConnected() )
			return false;
		this.graph = persistence.createDatabase();
		if( graph.getVertexType(IDescriptor.DESCRIPTORS) == null )
			graph.createVertexType( IDescriptor.DESCRIPTORS );
		String domain_id = getDomainClass(domain);
		if( graph.getVertexType( domain_id) == null )
			graph.createVertexType( domain_id );
		return true;
	}
	
	public String getDomainClass( IDomainAieon domain ) {
		return domain.getDomain().replace("." , "_");
	}
	public String getIdentifier(){
		return S_IDENTIFIER;
	}
	
	public void addListener(IModelListener<IModelNode<IDescriptor>> listener) {
		this.listeners.add(listener);
	}

	public void removeListener(IModelListener<IModelNode<IDescriptor>> listener) {
		this.listeners.remove(listener);
	}

	protected final void notifyListeners( ModelEvent<IModelNode<IDescriptor>> event ){
		for( IModelListener<IModelNode<IDescriptor>> listener: this.listeners )
			listener.notifyChange(event);
	}
	
	public boolean isOpen(){
		return (this.graph != null ) && !this.graph.isClosed();
	}

	
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

	public ITransaction<IModelNode<IDescriptor>,DatabaseService> createTransaction() {
		Transaction transaction = new Transaction( this );
		transaction.create();
		return transaction;
	}

	@Override
	public synchronized void close(){
		if( graph == null )
			return;
		graph.commit();
		graph.shutdown();
		graph = null;
	}

	protected class Transaction extends AbstractTransaction<IModelNode<IDescriptor>, DatabaseService>{

		protected Transaction( DatabaseService provider) {
			super( provider );
		}

		@Override
		public void close() {
			super.getProvider().close();
			if( !super.getProvider().isOpen())
				super.close();
		}

		@Override
		protected boolean onCreate(DatabaseService service) {
			return super.getProvider().isOpen();
		}
	}
}