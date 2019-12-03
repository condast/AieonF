package org.aieonf.orientdb.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import org.aieonf.commons.filter.FilterException;
import org.aieonf.commons.parser.ParseException;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.model.core.Model;
import org.aieonf.model.core.ModelLeaf;
import org.aieonf.orientdb.cache.CacheService;
import org.aieonf.orientdb.db.DatabaseService;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class ModelFactory< T extends IDescriptor > {

	public static final String S_ERR_NULL_ID = "The model does not have a descriptor: ";
	
	private DatabaseService service;
	
	private CacheService cache;
	
	private IDomainAieon domain;
	
	private Map<Long, IDescriptor> descriptors;
	
	public ModelFactory( IDomainAieon domain, CacheService cache, DatabaseService service ) {
		this.domain = domain;
		this.service = service;
		this.cache = cache;
		descriptors = new HashMap<>();
	}
	
	public Vertex transform( IModelLeaf<T> model ) throws ParseException {
		this.service.open();
		OrientGraph graph = this.service.getGraph();
		Vertex result  = null;
		try {
			result =  transform( graph, null, model, null);
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		finally {
			service.close();
		}
		return result;
	}

	protected Vertex transform( OrientGraph graph, Vertex parent, IModelLeaf<? extends IDescriptor> model, String type ) throws ParseException {
		OrientVertexType vt = graph.getVertexType(type);
		if( vt == null ) {
			vt = graph.createVertexType(type);
			vt.addCluster(domain.getShortName());
		}
		
		Vertex result = graph.addVertex(model.getIdentifier(), domain.getDomain());
		IDescriptor descriptor = model.getDescriptor();	
		result.setProperty( IDescriptor.DESCRIPTOR, descriptor.getID());
		if( parent != null ) {
			graph.addEdge(null, parent, result, type);
		}
		if( model.isLeaf())
			return result;
		
		IModelNode<? extends IDescriptor> node = (IModelNode<? extends IDescriptor>) model;
		Iterator<Map.Entry<IModelLeaf<? extends IDescriptor>, String>> iterator = node.getChildren().entrySet().iterator();
		while( iterator.hasNext() ) {
			Map.Entry<IModelLeaf<? extends IDescriptor>, String> entry = iterator.next();
			transform( graph, result, entry.getKey(), entry.getValue() );
		}
		return result;
	}

	protected IModelLeaf<IDescriptor> transform( Vertex vertex ) throws ParseException {		
		return transform( vertex, null );
	}
	
	protected IModelLeaf<IDescriptor> transform( Vertex vertex, IModelNode<? extends IDescriptor> parent ) throws ParseException {		
		Iterator<Edge> iterator = vertex.getEdges(Direction.OUT).iterator();
		if( !iterator.hasNext()) {
			IModelLeaf<IDescriptor> result = new ModelLeaf<IDescriptor>( parent );
			fill( result, vertex );
			return result;
		}
		IModelNode<IDescriptor> model = new Model<IDescriptor>( parent );
		fill( model, vertex );
		while( iterator.hasNext()) {
			Edge edge = iterator.next();
			IModelLeaf<? extends IDescriptor> child = transform( edge.getVertex(Direction.OUT), model );
			model.addChild(child, edge.getLabel());
		}
		return model;
	}

	protected Long[] getDescriptorIds( Vertex vertex ) throws ParseException {		
		Collection<Long> ids = new TreeSet<>();
		getDescriptorIds(vertex, ids);
		return ids.toArray( new Long[ ids.size()]);
	}
	
	protected void getDescriptorIds( Vertex vertex, Collection<Long> ids ) throws ParseException {		
		String idstr = vertex.getProperty(IDescriptor.DESCRIPTOR);
		long id=  Long.parseLong(idstr);
		ids.add(id);
		Iterator<Edge> iterator = vertex.getEdges(Direction.OUT).iterator();
		if( !iterator.hasNext()) {
			return;
		}
		while( iterator.hasNext()) {
			Edge edge = iterator.next();
			getDescriptorIds( edge.getVertex(Direction.OUT), ids );
		}
	}

	protected void fill( IDescribable<?> describable, Vertex vertex ) {
		IDescriptor descriptor = describable.getDescriptor();
		for( String key: vertex.getPropertyKeys() ) {
			descriptor.set(key, (String) vertex.getProperty(key));
		}
	}

	public Collection<IModelLeaf<IDescriptor>> get( IDescriptor descriptor ) throws FilterException {
		this.service.open();
		this.descriptors.clear();
		OrientGraph graph = this.service.getGraph();
		Collection<Long> ids = new TreeSet<>();
		Collection<IModelLeaf<IDescriptor>> results = new ArrayList<>();
		try {
			Iterator<Vertex> iterator = graph.getVertices().iterator();

			while( iterator.hasNext()) {
				Vertex vertex = iterator.next();
				getDescriptorIds(vertex, ids);
				IModelLeaf<IDescriptor> result = transform( vertex );
				results.add(result);
			}
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		finally {
			service.close();
		}
		cache.open();
		Map<Long, IDescriptor> descriptors = new HashMap<>();
		try {
			descriptors = cache.get(ids);
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		finally {
			cache.close();
		}	
		for( IModelLeaf<IDescriptor> model: results )
			fillIds( (ModelLeaf<IDescriptor>)model, descriptors );
		return results;
	}
	
	@SuppressWarnings("unchecked")
	protected void fillIds( ModelLeaf<IDescriptor> leaf,  Map<Long, IDescriptor> ids ) {
		String idstr = leaf.getDescriptor().get(IDescriptor.DESCRIPTOR);
		long id = Long.parseLong(idstr);
		leaf.setDescriptor(ids.get(id));

		if( !ModelLeaf.hasChildren(leaf))
			return;
		Model<IDescriptor> model = (Model<IDescriptor>) leaf;
		for( IModelLeaf<? extends IDescriptor> child: model.getChildren().keySet()) {
			fillIds( (ModelLeaf<IDescriptor>) child, ids );
		}
	}
}
