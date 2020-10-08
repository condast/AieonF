package org.aieonf.model.serialise;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Logger;

import org.aieonf.commons.parser.ParseException;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.ConceptBase;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.core.IConceptBase;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public abstract class AbstractModelTypeAdapter<N extends Object, D extends Object> extends TypeAdapter<IModelLeaf<IDescriptor>> {

	public enum Attributes{
		PROPERTIES,//Properties of the model
		DESCRIPTOR,//Descriptor
		CHILDREN,
		ID,
		LEAF,
		REVERSED;

		public static boolean isAttribute( String str ) {
			for( Attributes attr: values() ){
				if( attr.name().equals(str.toUpperCase()))
					return true;
			}
			return false;
		}
	}
	
	private Stack<N> nodes;
	boolean reversed;

	private Logger logger = Logger.getLogger(this.getClass().getName());

	protected AbstractModelTypeAdapter() {
		nodes = new Stack<>();
		reversed = false;
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
			result= onTransform( readNode(reader));
			token = reader.peek();
			if( JsonToken.END_OBJECT.equals(token))
				reader.endObject();
			token = reader.peek();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	protected abstract IModelLeaf<IDescriptor> onTransform( N node );

	protected abstract N onCreateNode( long id, boolean leaf );

	protected abstract boolean onAddChild( N model, N child, boolean reversed, String label );

	protected abstract D onSetDescriptor( N node, IConceptBase properties );

	protected abstract boolean onAddProperty( N node, String key, String value );

	protected N readNode( JsonReader jsonReader ) throws ParseException, IOException {
		String label;
		boolean complete = false;
		String key = null;
		while(!complete && jsonReader.hasNext() ) {
			JsonToken token = jsonReader.peek();
			logger.fine(token.toString());
			N node = null;
			switch( token) {
			case BEGIN_ARRAY:
				jsonReader.beginArray();
				break;
			case BEGIN_OBJECT:
				jsonReader.beginObject();
				if(!StringUtils.isEmpty(key))
					break;
				key = jsonReader.nextName().toUpperCase();
				long id = Descriptor.parseId( jsonReader.nextString());
				key = jsonReader.nextName().toUpperCase();
				boolean leaf = jsonReader.nextBoolean(); 
				node = onCreateNode(id, leaf);
				nodes.push(node);
				break;
			case NAME:
				key = jsonReader.nextName().toUpperCase();
				logger.fine(key);
				if( Attributes.isAttribute( key )) {
					String name = null;
					node = nodes.peek();
					switch( Attributes.valueOf( key )) {
					case REVERSED:
						reversed = jsonReader.nextBoolean();
						break;
					case CHILDREN:
						jsonReader.beginArray();
						while( !JsonToken.END_ARRAY.equals(token )) {
							N child = readNode( jsonReader );
							token = jsonReader.peek();
							jsonReader.endObject();
							token = jsonReader.peek();
							if( JsonToken.NULL.equals(token) ) {
								label = IModelLeaf.IS_CHILD;
								jsonReader.nextNull();
							}else
								label = jsonReader.nextString();
							onAddChild( node, child, reversed, label);
							token = jsonReader.peek();
						}
						jsonReader.endArray();
						jsonReader.endObject();
						complete = true;
						break;
					case DESCRIPTOR:
						jsonReader.beginArray();
						IConceptBase base = new ConceptBase();
						while( !JsonToken.END_ARRAY.equals( token )){
							jsonReader.beginObject();
							name = jsonReader.nextName();
							token = jsonReader.peek();
							if( !JsonToken.NULL.equals(token)) {
								String value = jsonReader.nextString();
								if( !StringUtils.isEmpty(value))
									name = name.replace(".", "_");
									base.set( name, value);
							}else
								jsonReader.nextNull();
							jsonReader.endObject();
							token = jsonReader.peek();
						}
						onSetDescriptor(node, base);
						jsonReader.endArray();
						token = jsonReader.peek();
						break;
					case LEAF:
						jsonReader.nextBoolean();
						break;
					default:
						//The properties of SerialisedModel
						jsonReader.beginArray();
						while( !JsonToken.END_ARRAY.equals( token )){
							jsonReader.beginObject();
							if( jsonReader.peek().equals(JsonToken.NAME)) {
								name = jsonReader.nextName();
								String value = jsonReader.nextString();
								if( !Attributes.ID.name().equals(name))
									onAddProperty(node, name, value);
							}
							jsonReader.endObject();
							token = jsonReader.peek();
						}
						jsonReader.endArray();
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
		return nodes.pop();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void write(JsonWriter arg0, IModelLeaf<IDescriptor> leaf) throws IOException {
		arg0.beginObject();
		arg0.name(Attributes.ID.name());
		arg0.value(leaf.getID());
		arg0.name(Attributes.LEAF.name());
		arg0.value(leaf.isLeaf());
		arg0.name(Attributes.PROPERTIES.name());
		Iterator<Map.Entry<String, String>> iterator = leaf.iterator();
		arg0.beginArray();
		while(iterator.hasNext() ) {
			Map.Entry<String, String> entry = iterator.next();
			if( Attributes.ID.name().equals(entry.getKey()))
				continue;
			arg0.beginObject();
			arg0.name(entry.getKey());
			arg0.value(entry.getValue());
			arg0.endObject();
		}
		arg0.endArray();
		if( leaf.getData() != null ) {
			arg0.name(Attributes.DESCRIPTOR.name());
			iterator = leaf.getData().iterator();
			arg0.beginArray();
			while(iterator.hasNext() ) {
				Map.Entry<String, String> entry = iterator.next();
				arg0.beginObject();
				arg0.name(entry.getKey());
				arg0.value(entry.getValue());
				arg0.endObject();
			}
			arg0.endArray();
		}
		if( !leaf.isLeaf()) {
			IModelNode<IDescriptor> model = (IModelNode<IDescriptor>) leaf;
			arg0.name(Attributes.REVERSED.name());
			arg0.value(model.isReverse());
			arg0.name(Attributes.CHILDREN.name());
			arg0.beginArray();
			for( Map.Entry<IModelLeaf<? extends IDescriptor>, String> child: model.getChildren().entrySet() ) {
				write( arg0, (IModelLeaf<IDescriptor>) child.getKey());
				if( child.getValue() == null)
					arg0.value( IModelLeaf.IS_CHILD);
				else
					arg0.value(child.getValue());
			}
			arg0.endArray();
		}
		arg0.endObject();
	}
}
