package org.aieonf.orientdb.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.concept.security.IPasswordAieon;
import org.aieonf.concept.security.PasswordAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.db.IAieonFDbService;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.model.template.ITemplateLeaf;
import org.aieonf.model.template.ITemplateNode;
import org.aieonf.model.xml.IXMLModelInterpreter;
import org.aieonf.orientdb.core.OModelLeaf;
import org.aieonf.orientdb.core.OModelNode;
import org.aieonf.orientdb.core.TemplateInterpreter;
import org.aieonf.orientdb.core.TemplateParser;
import org.aieonf.template.context.AbstractModelContextFactory;
import org.aieonf.template.xml.XMLTemplateBuilder;
import org.osgi.service.component.annotations.Component;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;

@Component( name="org.aieonf.orientdb.db.service", immediate=true)
public class AieonFDbService implements IAieonFDbService<IDescriptor>{

	private static final String S_PATH = "plocal:/tmp/test";
	private static final int DEFAULT_MAX_POOL = 10;
	
	private OrientGraph graph;
	private int poolSize;
	
	public AieonFDbService() {
		this( DEFAULT_MAX_POOL);
	}

	public AieonFDbService(int poolSize) {
		super();
		this.poolSize = poolSize;
	}

	@Override	
	public boolean connect( ILoaderAieon loader ) {
		IPasswordAieon pwd = new PasswordAieon( loader );
		this.graph = new OrientGraphFactory( loader.getURIPath(), pwd.getUserName(), pwd.getPassword()).setupPool(1, poolSize).getTx();
		return true;
	}
	
	@Override
	public void disconnect( ILoaderAieon domain ) {
		graph.shutdown();
	}
	
	@Override
	public IModelLeaf<IContextAieon> create(String identifier, Class<?> clss, InputStream in){
		TemplateFactory factory = new TemplateFactory( identifier, clss, in );
		return factory.createTemplate();
	}

	@Override
	public Collection<IModelLeaf<IDescriptor>> getModels(IDescriptor descriptor, boolean checkVersion) {
	       Iterable<Vertex> vertices = graph.getVertices( descriptor.get( OModelLeaf.OAttributes.DESCRIPTOR_ID.name()), descriptor.getID());
	       Collection<IModelLeaf<IDescriptor>> models = new ArrayList<>();
	       for( Vertex vertex: vertices ) {
	    	   if( !vertex.getProperty( OModelLeaf.OAttributes.DESCRIPTOR_NAME.name()).equals(descriptor.getName()))
	    		continue;
	    	   if( checkVersion && !vertex.getProperty( OModelLeaf.OAttributes.DESCRIPTOR_VERSION.name()).equals(descriptor.getVersion()))
    			   continue;
	    	   models.add( new OModelNode<IDescriptor>( graph, vertex, descriptor ));
	       }
	       return models;
	}

	@Override
	public Collection<IModelLeaf<IDescriptor>> search(String key, String value) {
	       Iterable<Vertex> vertices = graph.getVertices( key, value );
	       Collection<IModelLeaf<IDescriptor>> models = new ArrayList<>();
	       //for( Vertex vertex: vertices ) {
	    	//   models.add( new OModelNode<IDescriptor>( graph, vertex, descriptor ));
	       //z
	       //}
	       return models;
	}

	@Override
	public Collection<IModelLeaf<IDescriptor>> search(IModelFilter<IDescriptor, IDescribable<IDescriptor>> filter) {
		///graph.getv// TODO Auto-generated method stub
		return null;
	}

	@Override
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

	@Override
	public boolean update(IModelLeaf<IDescriptor> model) {
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

	private class TemplateFactory extends AbstractModelContextFactory<IContextAieon> {

		private String identifier;
		private IXMLModelInterpreter<IDescriptor, IDescriptor> interpreter;
		
		private TemplateFactory( String identifier, Class<?> clss, InputStream in ){
			super();
			this.identifier = identifier;
			this.interpreter = new TemplateInterpreter( clss, in);
		}

		@Override
		protected ITemplateLeaf<IContextAieon> onCreateTemplate() {
			return createTemplate(identifier, interpreter);
		}
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		protected ITemplateLeaf<IContextAieon> createTemplate( String identifier, IXMLModelInterpreter interpreter ) {
			TemplateParser parser = new TemplateParser(interpreter );
			XMLTemplateBuilder<IContextAieon,ITemplateNode<IContextAieon>> builder = 
					new XMLTemplateBuilder<IContextAieon, ITemplateNode<IContextAieon>>( identifier, parser );
			builder.build();
			ITemplateLeaf<IContextAieon> root = (ITemplateLeaf<IContextAieon>) builder.getModel();
			root.getDescriptor().set( IConcept.Attributes.SOURCE, identifier );
			return root;	
		}

	}
}
