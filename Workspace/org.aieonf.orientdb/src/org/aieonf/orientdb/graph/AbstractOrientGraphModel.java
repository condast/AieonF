package org.aieonf.orientdb.graph;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import org.aieonf.commons.graph.IVertex;
import org.aieonf.commons.strings.StringStyler;
import org.aieonf.commons.transaction.AbstractTransaction;
import org.aieonf.commons.transaction.ITransaction;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.body.BodyFactory;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.file.ProjectFolderUtils;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.concept.loader.LoaderAieon;
import org.aieonf.model.core.IModelListener;
import org.aieonf.model.core.ModelEvent;
import org.aieonf.model.provider.IModelDatabase;
import org.aieonf.model.provider.IModelProvider;

import com.orientechnologies.orient.core.metadata.security.ORole;
import com.orientechnologies.orient.core.metadata.security.OSecurity;
import com.orientechnologies.orient.core.metadata.security.OUser;
//import com.orientechnologies.orient.core.metadata.security.ORole;
//import com.orientechnologies.orient.core.metadata.security.OSecurity;
//import com.orientechnologies.orient.core.metadata.security.OUser;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;

/**
 * Handles the Orient Databae
 * @See :https://orientdb.com/docs/2.2/Graph-Database-Tinkerpop.html
 * @author Kees
 *
 * @param <D>
 * @param <U>
 */
public abstract class AbstractOrientGraphModel<D extends IDomainAieon, U extends IDescribable<IDescriptor>> implements IModelDatabase<D, U> {
	
	public static final String S_BUNDLE_ID = "org.aieonf.orientdb";
	public static final String S_IDENTIFIER = "GraphModel";
	
	private static final String S_LOCAL = "plocal:";
	private static final String S_FILE = "file:";
	protected static final String S_ROOT = "Root";

	protected static final String S_IS_CHILD = "isChild";

	private OrientGraphFactory factory;
	private OrientGraph graph;
	private Vertex root;
	private String source;
	private boolean connected;
	
	private Collection<IModelListener<U>> listeners;
	
	protected AbstractOrientGraphModel() {
		listeners = new ArrayList<IModelListener<U>>();
		this.connected = false;
	}

	/**
	 * Connect to the database
	 * 
	 * @param loader
	 */
	protected void connect( IDomainAieon domain ){
		if( connected )
			return;
		String user = domain.getUserName();
		String pwd = domain.getPassword();
		ILoaderAieon loader = new LoaderAieon( domain);
		loader.set( IConcept.Attributes.SOURCE, S_BUNDLE_ID);
		loader.setIdentifier( domain.getDomain() );
		source = ProjectFolderUtils.getDefaultUserDir( loader, true).toString();
		source = source.replace( S_FILE, S_LOCAL);
		factory = new OrientGraphFactory( source, user, pwd ).setupPool(1, 10);
		factory.setAutoStartTx(false);
		OSecurity security = factory.getDatabase().getMetadata().getSecurity();
		OUser ouser = security.getUser( user );
		if(ouser == null )
			security.createUser( user, pwd, ORole.ADMIN );
		this.connected = true;
	}
	
	@Override
	public String getIdentifier(){
		return S_IDENTIFIER;
	}
	
	protected OrientGraph getGraph() {
		return graph;
	}

	protected Vertex getRoot() {
		return root;
	}


	@Override
	public void addListener(IModelListener<U> listener) {
		this.listeners.add(listener);
	}

	@Override
	public void removeListener(IModelListener<U> listener) {
		this.listeners.remove(listener);
	}

	protected final void notifyListeners( ModelEvent<U> event ){
		for( IModelListener<U> listener: this.listeners )
			listener.notifyChange(event);
	}
	
	@Override
	public void open( IDomainAieon domain){
		try{
			this.connect(domain);
			if(!connected )
				return;
			this.graph = factory.getTx();
		}
		catch( Exception ex ){
			ex.printStackTrace();
			if( graph != null )
				graph.rollback();
		}
	}

	@Override
	public boolean isOpen(){
		return !this.graph.isClosed();
	}

	@Override
	public void sync(){
		try{
			graph.commit();
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
		finally{
			if( graph != null )
				graph.rollback();
		}
	}

	public ITransaction<U,IModelProvider<D, U>> createTransaction() {
		Transaction transaction = new Transaction( this );
		transaction.create();
		return transaction;
	}

	@Override
	public void close(){
		this.sync();
		if( graph != null )
			graph.shutdown();
	}

	@Override
	public void deactivate() {
		factory.close();
	}

	/**
	 * Print the given database
	 */
	public String printDatabase(){
		Iterator<Vertex> iterator = graph.getVertices().iterator();
		StringBuffer buffer = new StringBuffer();
		buffer.append("DATABASE: " + source + "\n" );
		while( iterator.hasNext() ){
			Vertex vertex = iterator.next();
			buffer.append("vertex( " + vertex.getId() + ")" );
			VertexDescriptor descriptor = new VertexDescriptor( vertex );
			buffer.append( descriptor.toString());
			buffer.append("\n");
			Iterator<Edge> edges = vertex.getEdges( com.tinkerpop.blueprints.Direction.BOTH ).iterator();
			while( edges.hasNext() ){
				Edge edge = edges.next();
				Vertex last = edge.getVertex( com.tinkerpop.blueprints.Direction.OUT );
				buffer.append("\t - edge " + edge.getLabel() + " => " + last.getId() + "\n");
			}
		}
		return buffer.toString();
	}
	
	/**
	 * Create a new descriptor
	 * @param graph
	 * @return
	 */
	protected static IDescriptor createDescriptor( OrientGraph graph, String id ){
		return createDescriptor( graph, graph.addVertex( id ));		
	}

	/**
	 * Create a descriptor from the given vertex
	 * @param graph
	 * @param vertex
	 * @return
	 */
	protected static IDescriptor createDescriptor( OrientGraph graph, Vertex vertex ){
		IVertex<IDescriptor> vtx = null;
		IDescriptor descriptor = vtx.get();
		String date = String.valueOf( Calendar.getInstance().getTimeInMillis());
		BodyFactory.IDFactory( descriptor );
		descriptor.set( IDescriptor.Attributes.CREATE_DATE, date );
		return descriptor;		
	}

	/**
	 * Convert an IVertex to a tinkerpop vertex
	 * @param graph
	 * @param vertex
	 * @param meaning
	 * @return
	 */
	public static Vertex convert( OrientGraph graph, IDescriptor descriptor, String meaning ){
		Vertex vtx = graph.addVertex( descriptor.getID()); // 1st OPERATION: IMPLICITLY BEGIN A TRANSACTION
		Iterator<String> iterator = descriptor.iterator();
		while( iterator.hasNext() ){
			String key = iterator.next();
			String value = descriptor.get( key );
			key = StringStyler.fromPackageString(key);
			vtx.setProperty(key, value );
		}  
		createDescriptor(graph, vtx);
		return vtx;
	}

	/**
	 * Convert an IVertex to a tinkerpop vertex
	 * @param graph
	 * @param vertex
	 * @param meaning
	 * @return
	 */
	public static Vertex convert( OrientGraph graph, IVertex<? extends IDescriptor> vertex, String meaning ){
		  return convert( graph, vertex.get(), meaning );
	}
	
	protected static class VertexDescriptor extends Descriptor{
		private static final long serialVersionUID = 1L;

		public static String GRAPH_ID = "GraphID";
		
		public VertexDescriptor( Vertex vertex ) {
			super(new VertexConceptBase( vertex ));
			super.set( GRAPH_ID, String.valueOf( vertex.getId()));
		}
		
	}

	protected class Transaction extends AbstractTransaction<U, IModelProvider<D, U>>{

		protected Transaction( IModelProvider<D,U> provider) {
			super( provider );
		}

		public void close() {
			super.getProvider().close();
			if( !super.getProvider().isOpen())
				super.close();
		}

		@Override
		protected boolean onCreate(IModelProvider<D, U> provider) {
			return super.getProvider().isOpen();
		}
	}
}