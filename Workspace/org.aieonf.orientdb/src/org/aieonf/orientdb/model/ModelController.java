package org.aieonf.orientdb.model;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.aieonf.commons.graph.IModel;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.orientdb.cache.CacheController;
import org.aieonf.orientdb.cache.ODescriptor;

import com.orientechnologies.orient.core.record.impl.ODocument;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;

public class ModelController implements Closeable{

	private String url;
	private OrientGraphFactory factory;
	private OrientGraph graph;
	private CacheController<IDescriptor> cache;

	public ModelController() {
		this.cache = new CacheController<>();
	}
	
	public void connect( String url, String username, String password ){
		this.url = url;
		this.cache.connect(url, username, password);
	}
	
	public void disconnect(){
		this.url = null;
		this.cache.disconnect();
	}
	
	public void open(){
		try{
			factory = new OrientGraphFactory( this.url );
			this.graph = factory.getTx();
			this.cache.open();
		}
		catch( Exception ex ){
			ex.printStackTrace();
			factory.close();
		}		
	}
	
	public boolean isOpen(){
		return this.cache.isOpen();
	}
	
	/**
	 * Add a descriptor to the cache
	 * @param leaf
	 */
	public void add( IModelLeaf<IDescriptor> leaf ){
		Vertex vertex = graph.addVertex( leaf.getID(), leaf.getIdentifier() );
		cache.add( leaf.getDescriptor());
		try{
			if( !( leaf instanceof IModel ))
				return;
			IModelNode<IDescriptor> model = (IModelNode<IDescriptor>) leaf;
			for( IModelLeaf<?> child: model.getChildren() ){
				Vertex cv = graph.addVertex( child.getID(), child.getIdentifier());
				Edge edge = graph.addEdge(leaf.getDescriptor(), vertex, cv, null );
			}
		}
		finally{
			graph.commit();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void add( Vertex parent, IModelLeaf<?> child ){
		Vertex vertex = graph.addVertex( child.getID(), child.getIdentifier() );
		cache.add( child.getDescriptor());
		Edge edge = graph.addEdge(null, parent, vertex, "IS_CHILD");
		try{
			if( !( child instanceof IModel ))
				return;
			IModelNode<IDescriptor> model = (IModelNode<IDescriptor>) child;
			for( IModelLeaf<?> gc: model.getChildren() ){
				add( vertex, gc );
			}
		}
		finally{
			graph.commit();
		}
	}
		

	public Collection<IDescriptor> getDescriptors( String id ){
		List<IDescriptor> results = new ArrayList<IDescriptor>();
		for( Vertex vertex: graph.getVerticesOfClass( id ))
			results.add( new ODescriptor( (ODocument) vertex ));		
		return results;
	}

	/**
	 * Add a descriptor to the cache
	 * @param descriptor
	 */
	public void updateDescriptor( IDescriptor descriptor ){
		Vertex vertex = graph.addVertex( descriptor.toString(), descriptor.getName());
		Iterator<String> iterator = descriptor.iterator();
		while( iterator.hasNext() ){
			String field = iterator.next();
			vertex.setProperty( field, descriptor.get( field ));
		}
	}

	@Override
	public void close(){
		graph.shutdown();
		factory.close();
	}

}
