package org.aieonf.orientdb.cache;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.concept.security.IPasswordAieon;
import org.aieonf.concept.security.PasswordAieon;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.orientdb.core.OModelLeaf;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;

public class CacheService{

	private static final String S_PATH = "plocal:/cache/test";
	private static final int DEFAULT_MAX_POOL = 10;
	
	private OrientGraph graph;
	private int poolSize;
	
	public CacheService() {
		this( DEFAULT_MAX_POOL);
	}

	public CacheService(int poolSize) {
		super();
		this.poolSize = poolSize;
	}

	public boolean connect( ILoaderAieon loader ) {
		IPasswordAieon pwd = new PasswordAieon( loader );
		this.graph = new OrientGraphFactory( loader.getURIPath(), pwd.getUserName(), pwd.getPassword()).setupPool(1, poolSize).getTx();
		return true;
	}
	
	public void disconnect( ILoaderAieon domain ) {
		graph.shutdown();
	}
	
	public Collection<IDescriptor> getDescriptors(IDescriptor descriptor, boolean checkVersion) {
	       Iterable<Vertex> vertices = graph.getVertices( descriptor.get( OModelLeaf.OAttributes.DESCRIPTOR_ID.name()), descriptor.getID());
	       Collection<IDescriptor> models = new ArrayList<>();
	       for( Vertex vertex: vertices ) {
	    	   if( !vertex.getProperty( OModelLeaf.OAttributes.DESCRIPTOR_NAME.name()).equals(descriptor.getName()))
	    		continue;
	    	   if( checkVersion && !vertex.getProperty( OModelLeaf.OAttributes.DESCRIPTOR_VERSION.name()).equals(descriptor.getVersion()))
    			   continue;
	    	   models.add( descriptor );
	       }
	       return models;
	}

	public Collection<IDescriptor> search(String key, String value) {
	       Iterable<Vertex> vertices = graph.getVertices( key, value );
	       Collection<IDescriptor> models = new ArrayList<>();
	       //for( Vertex vertex: vertices ) {
	    	//   models.add( new OModelNode<IDescriptor>( graph, vertex, descriptor ));
	       //z
	       //}
	       return models;
	}

	public Collection<IDescriptor> search(IModelFilter<IDescriptor, IDescribable<IDescriptor>> filter) {
		///graph.getv// TODO Auto-generated method stub
		return null;
	}

	public boolean remove(IDescriptor descriptor) {
	       Iterable<Vertex> vertices = graph.getVertices( descriptor.get( OModelLeaf.OAttributes.DESCRIPTOR_ID.name()), descriptor.getID());
	       boolean found = false; 
	       for( Vertex vertex: vertices ) {
	    	   if( !vertex.getProperty( OModelLeaf.OAttributes.DESCRIPTOR_NAME.name()).equals(descriptor.getName()))
	    		continue;
	    	   if( !vertex.getProperty( OModelLeaf.OAttributes.DESCRIPTOR_VERSION.name()).equals(descriptor.getVersion()))
 			   continue;
	    	   graph.removeVertex(vertex);
	    	   found = true;
	       }
	       return found;
	}

	public boolean update(IDescriptor model) {
	    boolean result = false; 
		try{
			graph.commit();
			result = true;
		}
		catch( Exception ex ) {
			graph.rollback();
		}
		return result;
	}
}