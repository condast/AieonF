package org.aieonf.orientdb.graph;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Logger;

import org.aieonf.commons.filter.FilterException;
import org.aieonf.commons.io.IOUtils;
import org.aieonf.commons.parser.ParseException;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.ConceptBase;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.model.core.Model;
import org.aieonf.model.core.ModelLeaf;
import org.aieonf.orientdb.db.DatabaseService;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class ModelFactory< T extends IDescriptor > {

	public static final String S_ERR_NULL_ID = "The model does not have a descriptor: ";

	public enum Attributes{
		BASE,
		CHILDREN,
		DESCRIPTOR,
		PROPERTIES;

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
	
	private Map<Long, IDescriptor> descriptors;

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public ModelFactory( IDomainAieon domain, DatabaseService service ) {
		this.domain = domain;
		this.service = service;
		descriptors = new HashMap<>();
	}
	
	public Map<Long, IDescriptor> getDescriptors() {
		return descriptors;
	}

	public Vertex transform( String data ) throws ParseException {
		JsonReader jsonReader = new JsonReader(new StringReader(data));
		this.service.open();
		OrientGraph graph = this.service.getGraph();
		Vertex result  = null;
		try {
		    while(jsonReader.hasNext()){
		        JsonToken nextToken = jsonReader.peek();
		        switch( nextToken) {
		        case BEGIN_OBJECT:
		        	//OrientVertexType vt = graph.getVertexType(type);
		    		//if( vt == null ) {
		    		//	vt = graph.createVertexType(type);
		    		//	vt.addCluster(domain.getShortName());
		    		//}
		        	Vertex vertex = graph.addVertex(null);
		        	transform ( vertex, jsonReader, nextToken);
		        	//String label  =  jsonReader.nextString();
		        	//Edge edge = graph.addEdge(null, result, vertex, label );
		        	//jsonReader.beginObject();
		        	result = vertex;
		        	break;
		        case NAME:
		        	String key = jsonReader.nextName().toUpperCase();
		        	switch( Attributes.valueOf( key )) {
		        	case CHILDREN:
		        		break;
		        	case DESCRIPTOR:
						IDescriptor descriptor = createDescriptor(jsonReader, nextToken);
						result.setProperty(IDescriptor.DESCRIPTOR, descriptor.getID());
						descriptors.put(descriptor.getID(), descriptor);
		        		break;
		        	default:
		        		String value = jsonReader.nextString();
		        		result.setProperty(key, value);
		        		break;
		        	}
		        	break;
		        	default:
		        		jsonReader.skipValue();
		        	break;
		        }
		    }
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		finally {
			service.close();
			IOUtils.closeQiuetly( jsonReader );
		}
		return result;
	}

	public void transform( Vertex parent, JsonReader jsonReader, JsonToken token ) throws ParseException, IOException {
		logger.info(token.toString());
		switch( token) {
		case BEGIN_OBJECT:
			jsonReader.beginObject();
			transform( parent, jsonReader, jsonReader.peek());
			//OrientVertexType vt = graph.getVertexType(type);
			//if( vt == null ) {
			//	vt = graph.createVertexType(type);
			//	vt.addCluster(domain.getShortName());
			//}
			//Vertex vertex = graph.addVertex(null);
			//String label  =  jsonReader.nextString();
			//Edge edge = graph.addEdge(null, result, vertex, label );
			//jsonReader.beginObject();
			//result = vertex;
			//jsonReader.endObject();
			break;
		case NAME:
			String key = jsonReader.nextName().toUpperCase();
			switch( Attributes.valueOf( key )) {
			case CHILDREN:
				transform( parent, jsonReader, jsonReader.peek());
				break;
			case DESCRIPTOR:
				break;
			default:
				jsonReader.skipValue();
				//String value = jsonReader.nextString();
				//result.setProperty(key, value);
				break;
			}
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
		if( !iterator.hasNext()) {
			IModelLeaf<IDescriptor> leaf = new ModelLeaf<IDescriptor>( parent );
			fill( leaf, vertex );
			return leaf;
		}
		IModelNode<IDescriptor> model = new Model<IDescriptor>( parent );
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
		String idstr = vertex.getProperty(IDescriptor.DESCRIPTOR);
		long id=  StringUtils.isEmpty(idstr)?-1:Long.parseLong(idstr);
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
