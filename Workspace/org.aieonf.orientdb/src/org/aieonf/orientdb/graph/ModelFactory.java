package org.aieonf.orientdb.graph;

import java.util.Iterator;
import java.util.Map;

import org.aieonf.commons.Utils;
import org.aieonf.commons.parser.ParseException;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.core.IConceptBase;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.orientdb.cache.CacheService;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class ModelFactory {

	private OrientGraph graph;
	
	private CacheService cache;
	
	public ModelFactory( CacheService cache, OrientGraph graph ) {
		this.graph = graph;
		this.cache = cache;
	}
	
	public IModelLeaf<IDescriptor> transform( IModelLeaf<? extends IDescriptor> model ) throws ParseException {
		return transform( null, model);
	}

	protected IModelLeaf<IDescriptor> transform( OrientDBModel parent, IModelLeaf<? extends IDescriptor> model ) throws ParseException {
		IDescriptor[] descriptors = cache.get( model.getDescriptor());
		IDescriptor descriptor = Utils.assertNull(descriptors)? cache.add(model.getDescriptor()): descriptors[0];		
		IModelLeaf<IDescriptor> result = null;
		if( model.isLeaf()) {
			result = new OrientDBModelLeaf( parent, model.getID(), graph, descriptor);
		}else {
			OrientDBModel oModel = new OrientDBModel( parent, model.getID(), graph, descriptor);	
			result = oModel;
			IModelNode<? extends IDescriptor> node = (IModelNode<? extends IDescriptor>) model;
			for( IModelLeaf<? extends IDescriptor> child: node.getChildren())
				transform( oModel, child );
		}
		return result;
	}

	protected VertexConceptBase fill( IDescriptor descriptor ) {
		Vertex vertex = graph.addVertex(descriptor.getID());
		Iterator<Map.Entry<String,String>> iterator = descriptor.iterator();
		while( iterator.hasNext()) {
			Map.Entry<String,String> entry = iterator.next();
			vertex.setProperty(entry.getKey(), entry.getValue());
		}
		return new VertexConceptBase(vertex);
	}

	public IDescriptor transform( IDescriptor descriptor ) {
		return new OrientDBDescriptor( fill( descriptor ));
	}

	private class OrientDBDescriptor extends Descriptor{
		private static final long serialVersionUID = 1L;

		protected OrientDBDescriptor(IConceptBase base) {
			super(base);
		}	
	}
}
