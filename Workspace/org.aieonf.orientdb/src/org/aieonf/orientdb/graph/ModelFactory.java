package org.aieonf.orientdb.graph;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.TreeSet;
import java.util.logging.Logger;

import org.aieonf.commons.Utils;
import org.aieonf.commons.filter.FilterException;
import org.aieonf.commons.io.IOUtils;
import org.aieonf.commons.parser.ParseException;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.ConceptBase;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.model.core.Model;
import org.aieonf.model.core.ModelLeaf;
import org.aieonf.model.serialise.SerialisableModel;
import org.aieonf.orientdb.cache.CacheService;
import org.aieonf.orientdb.db.DatabaseService;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class ModelFactory< T extends IDescriptor > {

	public static final String S_CLASS = "class:";
	public static final String S_ERR_NULL_ID = "The model does not have a descriptor: ";

	public enum Attributes{
		P,//Properties of the model
		D,//Descriptor
		C;//Children

		public static boolean isAttribute( String str ) {
			for( Attributes attr: values() ){
				if( attr.name().equals(str.toUpperCase()))
					return true;
			}
			return false;
		}
	}

	private DatabaseService service;
	
	private IDomainAieon domain;
	
	private Stack<Vertex> vertices;
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public ModelFactory( IDomainAieon domain, DatabaseService service ) {
		this.domain = domain;
		this.service = service;
		vertices = new Stack<>();
	}
	
	public Vertex transform( String data ) throws ParseException {
		JsonReader jsonReader = new JsonReader(new StringReader(data));
		logger.info(data);
		this.service.open();
		Vertex result  = null;
		String key = null;
		try {
			key = read( jsonReader, null, key );
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		finally {
			service.close();
			IOUtils.closeQuietly( jsonReader );
		}
		return result;
	}

	protected String read( JsonReader jsonReader, Vertex parent, String key ) throws ParseException, IOException {
		OrientGraph graph = this.service.getGraph();
		Vertex vertex = null;
		while( jsonReader.hasNext() ) {
			JsonToken token = jsonReader.peek();
			logger.info(token.toString());

			switch( token) {
			case BEGIN_ARRAY:
				jsonReader.beginArray();
				break;
			case BEGIN_OBJECT:
				jsonReader.beginObject();
				//transform( graph, parent, jsonReader, key);
				//OrientVertexType vt = graph.getVertexType(type);
				//if( vt == null ) {
				//	vt = graph.createVertexType(type);
				//	vt.addCluster(domain.getShortName());
				//}
				vertex = graph.addVertex(null);
				vertices.add(vertex);
				break;
			case NAME:
				key = jsonReader.nextName().toUpperCase();
				vertex = vertices.peek();
				if( Attributes.isAttribute( key )) {
					switch( Attributes.valueOf( key )) {
					case C:
						jsonReader.beginObject();
						read( jsonReader, vertex, key );
						jsonReader.endObject();
						break;
					case D:
						jsonReader.beginObject();
						Vertex descriptor = graph.addVertex( S_CLASS + IDescriptor.DESCRIPTORS);
						vertex.addEdge(IDescriptor.DESCRIPTOR, descriptor);
						do {
							String name = jsonReader.nextName();
							String value = jsonReader.nextString();
							if( !StringUtils.isEmpty(name) && !StringUtils.isEmpty(value)) {
								name = name.replace(".", "_");
								descriptor.setProperty(name, value);
							}
							token = jsonReader.peek();
						}
						while( JsonToken.NAME.equals( token ));
						jsonReader.endObject();
						break;
					default:
						jsonReader.beginObject();
						do {
							String name = jsonReader.nextName();
							String value = jsonReader.nextString();
							vertex.setProperty(name, value);
							token = jsonReader.peek();
						}
						while( JsonToken.NAME.equals( token ));
						jsonReader.endObject();
						break;
					}
				}else {
					jsonReader.nextString();
					//result.setProperty(key, value);					
				}
				break;
			default:
				break;
			}
		}
		return null;
	}

	public IDescriptor createDescriptor(JsonReader jsonReader, JsonToken nextToken ) throws ParseException, IOException {
		ConceptBase conceptBase = new ConceptBase();
		while( jsonReader.hasNext() ) {
			JsonToken token = jsonReader.peek();
			logger.info(token.toString());
			switch( token) {
			case BEGIN_OBJECT:
				jsonReader.beginObject();
				break;
			case NAME:
				String key = jsonReader.nextName().toUpperCase();
				if(Attributes.isAttribute(key)) {
					jsonReader.beginObject();
					break;
				}
				String value = jsonReader.nextString();
				conceptBase.set(key, value);
				break;
			case BEGIN_ARRAY:
				break;
			case END_ARRAY:
				break;
			case END_OBJECT:
				jsonReader.endObject();
				break;
			default:
				break;
			}
		}
		return new Descriptor( conceptBase );
	}

	@SuppressWarnings("unchecked")
	protected Vertex transform( OrientGraph graph, Vertex parent, IModelLeaf<IDescriptor> leaf, String type ) throws ParseException {

		Vertex result = graph.addVertex(leaf.getIdentifier(), domain.getDomain());
		IDescriptor descriptor = leaf.getDescriptor();
		result.setProperty( IDescriptor.DESCRIPTOR, String.valueOf( descriptor.getID()));
		if( parent != null ) {
			graph.addEdge(null, parent, result, type);
		}
		if( leaf.isLeaf())
			return result;
		IModelNode<IDescriptor> model = (IModelNode<IDescriptor>) leaf;
		Iterator<Map.Entry<IModelLeaf<? extends IDescriptor>, String>> iterator = model.getChildren().entrySet().iterator();
		while( iterator.hasNext() ) {
			Map.Entry<IModelLeaf<? extends IDescriptor>, String> entry = iterator.next();
			transform( graph, result, (IModelLeaf<IDescriptor>) entry.getKey(), entry.getValue() );
		}
		return result;
	}

	protected IModelLeaf<IDescriptor> transform( Vertex vertex ) throws ParseException {		
		return transform( vertex, null );
	}

	protected IModelLeaf<IDescriptor> transform( Vertex vertex, IModelNode<? extends IDescriptor> parent ) throws ParseException {		
		Iterator<Edge> iterator = vertex.getEdges(Direction.OUT).iterator();
		String id = vertex.getId().toString();
		CacheService cache = service.getCache();
		long descid = vertex.getProperty(IDescriptor.DESCRIPTOR);
		IDescriptor descriptors[] = cache.get(descid);
		if( Utils.assertNull(descriptors))
			throw new IllegalArgumentException( S_ERR_NULL_ID );
		if( !iterator.hasNext()) {
			IModelLeaf<IDescriptor> leaf = new ModelLeaf<IDescriptor>( id, parent, descriptors[0] );
			fill( leaf, vertex );
			return leaf;
		}
		IModelNode<IDescriptor> model = new Model<IDescriptor>( id, parent, descriptors[0] );
		fill( model, vertex );
		while( iterator.hasNext()) {
			Edge edge = iterator.next();
			IModelLeaf<IDescriptor> child = transform( edge.getVertex(Direction.OUT), model );
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
		Long id = vertex.getProperty(IDescriptor.DESCRIPTOR);
		ids.add((id == null )?-1: id);
		Iterator<Edge> iterator = vertex.getEdges(Direction.OUT).iterator();
		if( !iterator.hasNext()) {
			return;
		}
		while( iterator.hasNext()) {
			Edge edge = iterator.next();
			getDescriptorIds( edge.getVertex(Direction.OUT), ids );
		}
	}

	protected void fill( IModelLeaf<?> leaf, Vertex vertex ) {
		for( String key: vertex.getPropertyKeys() ) {
			leaf.set(key, vertex.getProperty(key).toString());
		}
	}

	public Collection<IModelLeaf<IDescriptor>> get( IDescriptor descriptor ) throws FilterException {
		this.service.open();
		OrientGraph graph = this.service.getGraph();
		Collection<Long> ids = new TreeSet<>();
		Collection<IModelLeaf<IDescriptor>> results = new ArrayList<>();
		try {
			Iterator<Vertex> iterator = graph.getVertices().iterator();
			while( iterator.hasNext()) {
				Vertex vertex = iterator.next();
				SerialisableNode node = new SerialisableNode(vertex );
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
		return results;
	}

	public Collection<SerialisableModel> get( Collection<Vertex> vertices ) throws FilterException {
		this.service.open();
		Collection<SerialisableModel> results = new ArrayList<>();
		try {
			for( Vertex vertex: vertices ) {
				SerialisableNode node = new SerialisableNode(vertex );
				results.add(node);
			}
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		finally {
			service.close();
		}
		return results;
	}
	
	private class SerialisableNode extends SerialisableModel{

		public SerialisableNode( Vertex descriptor ) {
			super();
			for( String key: descriptor.getPropertyKeys())
				super.putDescriptor( key, descriptor.getProperty(key));
			Iterator<Edge> edges = descriptor.getEdges(Direction.BOTH, IDescriptor.DESCRIPTOR).iterator();
			Vertex root = edges.next().getVertex(Direction.OUT);
			for( String key: root.getPropertyKeys())
				super.put( key, root.getProperty(key));
			edges = root.getEdges(Direction.OUT).iterator();
			while( edges.hasNext() ) {
				Edge edge = edges.next();
				Vertex vertex = edge.getVertex(Direction.IN); 
				if( !vertex.equals(descriptor))
					super.addChild(new SerialisableNode( vertex ), edge.getLabel());
			}
		}
		
	}
}
