package org.aieonf.orientdb.graph;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.body.BodyFactory;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.concept.security.IPasswordAieon;
import org.aieonf.concept.security.PasswordAieon;
import org.aieonf.model.IModelProvider;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.util.StringStyler;
import org.aieonf.util.graph.IVertex;
import org.aieonf.util.transaction.AbstractTransaction;
import org.aieonf.util.transaction.ITransaction;
import org.condast.aieonf.osgi.utils.ProjectFolderUtils;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Parameter;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;

public abstract class AbstractOrientGraphModel<T extends IDescriptor, U extends Object> implements IModelProvider<T,U> {
	
	public static final String S_IDENTIFIER = "GraphModel";
	
	private static final String S_LOCAL = "local:";
	private static final String S_FILE = "file:";
	protected static final String S_ROOT = "Root";
	private static final String S_CLASS = "class:";

	protected static final String S_IS_CHILD = "isChild";

	private OrientGraphFactory factory;
	private OrientGraph graph;
	private Vertex root;
	private String source;
	
	private Collection<IModelBuilderListener> listeners;
	
	public AbstractOrientGraphModel( ILoaderAieon loader ) {
		IPasswordAieon password = new PasswordAieon( loader );
		String user = "admin";//password.getUserName();
		String pwd = "admin";//password.getPassword();
		source = ProjectFolderUtils.getDefaultUserDir(loader, true).toString();
		source = source.replace( S_FILE, S_LOCAL);
		factory = new OrientGraphFactory( source, user, pwd ).setupPool(1,10);
		listeners = new ArrayList<IModelBuilderListener>();
		this.setup();
	}

	public String getIdentifier(){
		return S_IDENTIFIER;
	}
	
	protected OrientGraph getGraph() {
		return graph;
	}

	protected Vertex getRoot() {
		return root;
	}

	public void addListener( IModelBuilderListener listener ){
		this.listeners.add( listener );
	}

	public void removeListener( IModelBuilderListener listener ){
		this.listeners.remove( listener );
	}

	protected final void notifyListeners( ModelBuilderEvent event ){
		for( IModelBuilderListener listener: this.listeners )
			listener.notifyChange(event);
	}
	
	protected void setup(){
		try{
			this.graph = factory.getTx();
			Set<String> indices = graph.getIndexedKeys( Vertex.class );
			if( !indices.contains( S_ROOT ))
				graph.createKeyIndex( S_ROOT, Vertex.class, new Parameter<String, String>("type", "UNIQUE"));
			if( graph.countVertices() == 0 ){
				graph.addVertex( S_CLASS + S_ROOT  );			
			}
			graph.commit();
			graph.shutdown();
		}
		catch( Exception ex ){
			graph.rollback();
			ex.printStackTrace();
		}
		
	}

	public void open(){
		try{
			this.graph = factory.getTx();
			if( graph.countVertices() == 0 ){
				root = graph.addVertex( S_CLASS + S_ROOT  );		
			}else{
				root = graph.getVerticesOfClass( S_ROOT ).iterator().next();
			}
		}
		catch( Exception ex ){
			ex.printStackTrace();
			graph.rollback();
		}
	}

	public boolean isOpen(){
		return !this.graph.isClosed();
	}

	public void sync(){
		try{
			graph.commit();
		}
		catch( Exception ex ){
			ex.printStackTrace();
			graph.rollback();
		}
	}

	public ITransaction<U,IModelProvider<T,U>> createTransaction() {
		Transaction transaction = new Transaction( this );
		transaction.create();
		return transaction;
	}


	public void close(){
		this.sync();
		graph.shutdown();
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
	protected static IDescriptor createDescriptor( OrientGraph graph ){
		return createDescriptor( graph, graph.addVertex( null ));		
	}

	/**
	 * Create a descriptor from the given vertex
	 * @param graph
	 * @param vertex
	 * @return
	 */
	protected static IDescriptor createDescriptor( OrientGraph graph, Vertex vertex ){
		IVertex<IDescriptor> vtx = new VertexImpl<IDescriptor>( vertex );
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
		Vertex vtx = graph.addVertex(null); // 1st OPERATION: IMPLICITLY BEGIN A TRANSACTION
		Iterator<String> iterator = descriptor.iterator();
		while( iterator.hasNext() ){
			String key = iterator.next();
			String value = descriptor.get( key );
			key = StringStyler.fromPackageString(key);
			vtx.setProperty(key, value );
			String retval = vtx.getProperty( key );
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

	protected class Transaction extends AbstractTransaction<U, IModelProvider<T,U>>{

		protected Transaction( IModelProvider<T,U> provider) {
			super( provider );
		}

		@Override
		public void close() {
			super.getProvider().close();
			if( !super.getProvider().isOpen())
				super.close();
		}

		@Override
		protected boolean onCreate(IModelProvider<T, U> provider) {
			super.getProvider().open();
			return super.getProvider().isOpen();
		}
	}
}