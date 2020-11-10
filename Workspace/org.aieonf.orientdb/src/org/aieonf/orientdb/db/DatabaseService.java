package org.aieonf.orientdb.db;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.aieonf.commons.number.NumberUtils;
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
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
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
		if( graph.getEdgeType(IDescriptor.DESCRIPTOR) == null )
			graph.createEdgeType( IDescriptor.DESCRIPTOR );
		String domain_id = domain.getDomain().replace("." , "_");
		if( graph.getEdgeType( domain_id ) == null )
			graph.createEdgeType( domain_id );
		return true;
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

	public int remove( long id ) {
		return remove( id, true );
	}
	
	public int remove( long id, boolean removeChildren ) {
		int counter = 0;
		Iterable<Vertex> iterable = this.graph.getVertices(IDescriptor.Attributes.ID.name(), String.valueOf(id));
		if( iterable == null )
			return counter;
		Iterator<Vertex> iterator = iterable.iterator();
		while( iterator.hasNext() ) {
			Vertex vertex = iterator.next();			
			counter += remove( vertex, counter, removeChildren );
		}		
		return counter;
	}

	public int remove( long[] ids ) {
		return remove( ids, true );
	}

	public int remove( long[] ids, boolean removeChildren ) {
		int counter = 0;
		Iterable<Vertex> iterable = this.graph.getVertices();
		if( iterable == null )
			return counter;
		Iterator<Vertex> iterator = iterable.iterator();
		while( iterator.hasNext() ) {
			Vertex vertex = iterator.next();			
			String idstr = vertex.getProperty(IDescriptor.Attributes.ID.name());
			long id = Long.parseLong(idstr);
			for( long check: ids ) {
				if( check == id )
					counter += remove( vertex, counter, removeChildren );
			}
		}		
		return counter;
	}

	protected int remove( Vertex vertex, int counter, boolean removeChildren ) {
		if( vertex == null )
			return counter;
		
		Iterator<Edge> iterator = vertex.getEdges(Direction.OUT).iterator();
		int index=  counter;
		while( iterator.hasNext()) {
			Edge edge = iterator.next();
			Vertex child = edge.getVertex(Direction.IN);
			graph.removeEdge(edge);
			if(removeChildren && countEdges( child,  Direction.OUT) > 0)
				index += remove( child, index, removeChildren );
			}
		this.graph.removeVertex(vertex);
		return index;
	}

	public int removeChildren( long id, long[] children ) {
		int counter = 0;
		Iterable<Vertex> iterable = this.graph.getVertices(IDescriptor.Attributes.ID.name(), String.valueOf(id));
		if( iterable == null )
			return counter;
		Iterator<Vertex> iterator = iterable.iterator();
		while( iterator.hasNext() ) {
			Vertex vertex = iterator.next();			
			Iterator<Edge> edges = vertex.getEdges(Direction.BOTH).iterator();
			while( edges.hasNext()) {
				Edge edge = edges.next();
				Vertex other = getOther( edge, vertex);
				for( long check: children ) {
					if( check == getId(other))
						graph.removeEdge(edge);
				}
				if( !hasEdges( other))
					graph.removeVertex(other);
			}
		}		
		return counter;
	}

	protected int countEdges( Vertex vertex, Direction direction ) {
		int counter = 0;
		Iterator<Edge> iterator = vertex.getEdges(direction).iterator();
		while( iterator.hasNext()) {
			iterator.next();
			counter++;
		}
		return counter;
	}
	
	public static long getId( Vertex vertex ) {
		return NumberUtils.parseLong( vertex.getProperty(IDescriptor.Attributes.ID.name())); 
	}

	public static boolean hasEdges( Vertex vertex ) {
		Iterator<Edge> edges = vertex.getEdges(Direction.BOTH).iterator();		
		return edges.hasNext(); 
	}

	public static boolean hasEdges( Vertex vertex, Direction direction ) {
		Iterator<Edge> edges = vertex.getEdges(direction).iterator();		
		return edges.hasNext(); 
	}

	public static Vertex getOther( Edge edge, Vertex vertex ) {
		Vertex check = edge.getVertex( Direction.IN); 
		return !check.equals(vertex)?check: edge.getVertex(Direction.OUT);
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