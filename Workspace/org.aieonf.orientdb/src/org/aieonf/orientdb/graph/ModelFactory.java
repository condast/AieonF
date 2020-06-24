package org.aieonf.orientdb.graph;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;
import java.util.logging.Logger;

import org.aieonf.commons.filter.FilterException;
import org.aieonf.commons.io.IOUtils;
import org.aieonf.commons.parser.ParseException;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.serialise.SerialisableModel;
import org.aieonf.orientdb.db.DatabaseService;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class ModelFactory< T extends IDescriptor > {

	public static final String S_CLASS = "class:";
	public static final String IS_CHILD = "isChild";
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
	
	private IDescriptor domain;
	
	private Stack<Vertex> vertices;
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public ModelFactory( IDescriptor domain, DatabaseService service ) {
		this.domain = domain;
		this.service = service;
		vertices = new Stack<>();
	}
	
	public long transform( String data ) throws ParseException {
		JsonReader jsonReader = new JsonReader(new StringReader(data));
		logger.info(data);
		this.service.open();
		Vertex result  = null;
		try {
			result = read( jsonReader );
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		finally {
			service.close();
			IOUtils.closeQuietly( jsonReader );
		}
		String str = result.getProperty( IDescriptor.Attributes.ID.name());
		return Long.parseLong( str );
	}

	protected Vertex read( JsonReader jsonReader ) throws ParseException, IOException {
		OrientGraph graph = this.service.getGraph();
		String label;
		while( jsonReader.hasNext() ) {
			JsonToken token = jsonReader.peek();
			logger.fine(token.toString());
			Vertex vertex = null;
			switch( token) {
			case BEGIN_ARRAY:
				jsonReader.beginArray();
				jsonReader.beginObject();
				vertex = graph.addVertex(null);
				vertex.setProperty(IDescriptor.Attributes.ID.name(), String.valueOf(graph.countVertices()));
				vertices.push(vertex);
				//OrientVertexType vt = graph.getVertexType(type);
				//if( vt == null ) {
				//	vt = graph.createVertexType(type);
				//	vt.addCluster(domain.getShortName());
				//}
				break;
			case BEGIN_OBJECT:
				jsonReader.beginObject();
				break;
			case NAME:
				String key = jsonReader.nextName().toUpperCase();
				if( Attributes.isAttribute( key )) {
					vertex = vertices.peek();
					switch( Attributes.valueOf( key )) {
					case C:
						jsonReader.beginArray();
						Vertex child = read( jsonReader );
						jsonReader.endObject();
						token = jsonReader.peek();
						label = ( JsonToken.NULL.equals(token) )? IS_CHILD: jsonReader.nextString();
						graph.addEdge(null, vertex, child, label);
						if( JsonToken.NULL.equals(token))
							jsonReader.nextNull();
						else
							jsonReader.nextString();
						jsonReader.endArray();
						jsonReader.endArray();
						//jsonReader.nextName();
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
						jsonReader.endObject();//descriptor
						token = jsonReader.peek();
						break;
					default:
						//The properties of SerialisedModel
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
				}
				break;
			case END_OBJECT:
				break;
			default:
				break;
			}
		}
		return vertices.pop();
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
			if(!edges.hasNext())
				return;
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
