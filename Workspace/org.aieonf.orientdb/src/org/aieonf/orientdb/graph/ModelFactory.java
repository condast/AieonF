package org.aieonf.orientdb.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.aieonf.commons.Utils;
import org.aieonf.commons.filter.FilterException;
import org.aieonf.commons.parser.ParseException;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.core.IConceptBase;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.orientdb.cache.CacheService;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class ModelFactory {

	public static final String S_ERR_NULL_ID = "The model does not have a descriptor: ";
	
	private OrientGraph graph;
	
	private CacheService cache;
	
	private IDomainAieon domain;
	
	public ModelFactory( IDomainAieon domain, CacheService cache, OrientGraph graph ) {
		this.domain = domain;
		this.graph = graph;
		this.cache = cache;
	}
	
	public IModelLeaf<IDescriptor> transform( IModelLeaf<? extends IDescriptor> model ) throws ParseException {
		OrientDBModelLeaf result =  transform( null, model);
		return result;
	}

	protected OrientDBModelLeaf transform( OrientDBModel parent, IModelLeaf<? extends IDescriptor> model ) throws ParseException {
		String type = model.getType();
		OrientVertexType vt = graph.getVertexType(type);
		if( vt == null ) {
			vt = graph.createVertexType(type);
			vt.addCluster(domain.getShortName());
		}
		
		IDescriptor[] descriptors = cache.get( model.getDescriptor());
		IDescriptor descriptor = Utils.assertNull(descriptors)? cache.add(model.getDescriptor()): descriptors[0];		
		OrientDBModelLeaf result = null;
		if( model.isLeaf()) {
			result = new OrientDBModelLeaf( parent, null, graph, descriptor);
		}else {
			OrientDBModel oModel = new OrientDBModel( parent, null, graph, descriptor);	
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

	public Collection<IModelLeaf<IDescriptor>> get( IDescriptor descriptor ) throws FilterException {
		Collection<IModelLeaf<IDescriptor>> results = new ArrayList<>();
		Iterator<Vertex> iterator = graph.getVertices().iterator();
		while( iterator.hasNext()) {
			Vertex vertex = iterator.next();
			String id = vertex.getProperty(OrientDBModelLeaf.S_DESCRIPTOR);
			if( StringUtils.isEmpty( id))
				throw new IllegalArgumentException( S_ERR_NULL_ID + vertex.getId());
			IDescriptor[] descriptors = null;
			try {
				descriptors = cache.get( Long.parseLong( id ));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if( Utils.assertNull(descriptors ))
				continue;
			IModelLeaf<IDescriptor> model = new OrientDBModel(vertex, descriptors[0]);
			results.add( model );
		}
		return results;
	}

	private class OrientDBDescriptor extends Descriptor{
		private static final long serialVersionUID = 1L;

		protected OrientDBDescriptor(IConceptBase base) {
			super(base);
		}	
	}
}
