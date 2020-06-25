package org.aieonf.orientdb.serialisable;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Logger;

import org.aieonf.commons.parser.ParseException;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.orientdb.core.ModelLeaf;
import org.aieonf.orientdb.core.ModelNode;
import org.aieonf.orientdb.graph.ModelFactory;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class ModelLeafTypeAdapter extends TypeAdapter<IModelLeaf<IDescriptor>> {

	public enum Attributes{
		PROPERTIES,//Properties of the model
		DESCRIPTOR,//Descriptor
		CHILDREN,
		LEAF;//Children

		public static boolean isAttribute( String str ) {
			for( Attributes attr: values() ){
				if( attr.name().equals(str.toUpperCase()))
					return true;
			}
			return false;
		}
	}

	private OrientGraph graph;
	
	private Stack<Vertex> vertices;

	private IDomainAieon domain;
	
	private Logger logger = Logger.getLogger(this.getClass().getName());

	public ModelLeafTypeAdapter( IDomainAieon domain, OrientGraph graph) {
		this.graph = graph;
		this.domain = domain;
		vertices = new Stack<>();
	}

	@Override
	public IModelLeaf<IDescriptor> read(JsonReader reader) throws IOException {
		JsonToken token = reader.peek();
		if (token == JsonToken.NULL) {
			reader.nextNull();
			return null;
		}
		IModelLeaf<IDescriptor>  result = null;
		try {
			Vertex vertex = readVertex(reader);
			result = new ModelNode( this.graph, this.domain, vertex );
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	protected Vertex readVertex( JsonReader jsonReader ) throws ParseException, IOException {
		String label;
		boolean complete = false;
		String key = null;
		while(!complete && jsonReader.hasNext() ) {
			JsonToken token = jsonReader.peek();
			logger.info(token.toString());
			Vertex vertex = null;
			switch( token) {
			case BEGIN_ARRAY:
				jsonReader.beginArray();
				break;
			case BEGIN_OBJECT:
				jsonReader.beginObject();
				if(!StringUtils.isEmpty(key))
					break;
				vertex = graph.addVertex( domain.getDomain());
				vertex.setProperty(IDescriptor.Attributes.ID.name(), String.valueOf(graph.countVertices()));
				vertices.push(vertex);
				break;
			case NAME:
				key = jsonReader.nextName().toUpperCase();
				logger.info(key);
				if( Attributes.isAttribute( key )) {
					vertex = vertices.peek();
					switch( Attributes.valueOf( key )) {
					case CHILDREN:
						jsonReader.beginArray();
						while( !JsonToken.END_ARRAY.equals(token )) {
							Vertex child = readVertex( jsonReader );
							token = jsonReader.peek();
							label = ( JsonToken.NULL.equals(token) )? ModelLeaf.IS_CHILD: jsonReader.nextString();
							graph.addEdge(domain.getDomain(), vertex, child, label);
							token = jsonReader.peek();
						}
						jsonReader.endArray();
						jsonReader.endObject();
						jsonReader.endArray();
						complete = true;
						break;
					case DESCRIPTOR:
						jsonReader.beginArray();
						Vertex descriptor = graph.addVertex( ModelFactory.S_CLASS + IDescriptor.DESCRIPTORS);
						vertex.addEdge(IDescriptor.DESCRIPTOR, descriptor);
						while( !JsonToken.END_ARRAY.equals( token )){
							jsonReader.beginObject();
							String name = jsonReader.nextName().replace(".", "_");
							token = jsonReader.peek();
							if( !JsonToken.NULL.equals(token)) {
								String value = jsonReader.nextString();
								if( !StringUtils.isEmpty(value))
									descriptor.setProperty(name, value);
							}
							jsonReader.endObject();
							token = jsonReader.peek();
						}
						jsonReader.endArray();
						jsonReader.endObject();
						break;
					case LEAF:
						jsonReader.nextBoolean();
						break;
					default:
						//The properties of SerialisedModel
						jsonReader.beginArray();
						while( !JsonToken.END_ARRAY.equals( token )){
							jsonReader.beginObject();
							String name = jsonReader.nextName();
							String value = jsonReader.nextString();
							vertex.setProperty(name, value);
							jsonReader.endObject();
							token = jsonReader.peek();
						}
						jsonReader.endArray();
						jsonReader.endObject();
						break;
					}
				}
				break;
			case STRING:
				complete = true;//Label of the child
				break;
			case END_OBJECT:
				break;
			case END_DOCUMENT:
				complete = true;
				break;
			default:
				break;
			}
		}
		return vertices.pop();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void write(JsonWriter arg0, IModelLeaf<IDescriptor> arg1) throws IOException {
		arg0.beginObject();
		arg0.name(Attributes.PROPERTIES.name());
		Iterator<Map.Entry<String, String>> iterator = arg1.iterator();
		arg0.beginArray();
		while(iterator.hasNext() ) {
			Map.Entry<String, String> entry = iterator.next();
			arg0.beginObject();
			arg0.name(entry.getKey());
			arg0.value(entry.getValue());
			arg0.endObject();
		}
		arg0.endArray();
		arg0.name(Attributes.DESCRIPTOR.name());
		iterator = arg1.getData().iterator();
		arg0.beginArray();
		while(iterator.hasNext() ) {
			Map.Entry<String, String> entry = iterator.next();
			arg0.beginObject();
			arg0.name(entry.getKey());
			arg0.value(entry.getValue());
			arg0.endObject();
		}
		arg0.endArray();
		if( !arg1.isLeaf()) {
			arg0.name(Attributes.CHILDREN.name());
			arg0.beginArray();
			IModelNode<IDescriptor> model = (IModelNode<IDescriptor>) arg1;
			for( Map.Entry<IModelLeaf<? extends IDescriptor>, String> child: model.getChildren().entrySet() ) {
				arg0.beginObject();
				write( arg0, (IModelLeaf<IDescriptor>) child.getKey());
				arg0.value(child.getValue());
				arg0.endObject();
			}
			arg0.endArray();
		}
		arg0.endObject();
	}
}
