package test.aieonf.serialise.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Logger;

import org.aieonf.commons.parser.ParseException;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.core.IConceptBase;
import org.aieonf.model.core.IModelLeaf;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class ModelTypeAdapter extends TypeAdapter<TestObject> {

	public static final String S_ERR_NULL_CHILD = "The child is null: ";
	public static final String S_ERR_CYCLE_DETECTED = "A cycle has been detected ";

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
	
	private Stack<TestObject> nodes;
	
	private Map<String, String> base;
	
	//The creation of nodes isd delayed until the descriptor is read, so the rest is temporarily 
	//stored as a concept base
	private Store store; 
	boolean reversed;
	
	private Logger logger = Logger.getLogger(this.getClass().getName());

	public ModelTypeAdapter() {
		nodes = new Stack<>();
		reversed = false;
	}

	@Override
	public TestObject read(JsonReader reader) throws IOException {
		TestObject result = null;
		try {
			result= readNode(reader );
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	protected void handleCycle( TestObject parent, TestObject child, String label ) {
		logger.warning( S_ERR_CYCLE_DETECTED + "(" + label + "):" + parent.getID() + "->" + child.getID());
	}

	protected TestObject readNode( JsonReader jsonReader ) throws ParseException, IOException {
		String label;
		String key = null;
		TestObject node = null;
		String name = null;
		JsonToken token = null;
		boolean completed = false;
		while(!completed && jsonReader.hasNext() ) {
			token = jsonReader.peek();
			logger.info(token.toString());
			switch( token) {
			case BEGIN_OBJECT:
				jsonReader.beginObject();
				token = jsonReader.peek();
				if( JsonToken.END_OBJECT == token ) {
					jsonReader.endObject();
					continue;
				}
				key = jsonReader.nextName().toUpperCase();
				long id = Descriptor.parseId( jsonReader.nextString());
				key = jsonReader.nextName().toUpperCase();
				boolean leaf = jsonReader.nextBoolean(); 
				name = jsonReader.nextName();
				if( name.equals(IConceptBase.S_PROPERTIES)) {
					base = createBase(jsonReader, token);
				}
				else
					base = new HashMap<>();
				store = new Store( id, name, leaf, base );
				break;
			case NAME:
				key = jsonReader.nextName().toUpperCase();
				logger.fine(key);
				if( Attributes.isAttribute( key )) {
					switch( Attributes.valueOf( key )) {
					case REVERSED:
						reversed = jsonReader.nextBoolean();
						break;
					case CHILDREN:
						node = nodes.peek();
						jsonReader.beginArray();
						token = jsonReader.peek();
						while( !JsonToken.END_ARRAY.equals(token )) {
							jsonReader.beginArray();
							TestObject child = readNode( jsonReader );
							token = jsonReader.peek();
							logger.fine( token.name());
							jsonReader.endObject();
							token = jsonReader.peek();
							logger.fine( token.name());
							label = IModelLeaf.IS_CHILD;
							if( JsonToken.STRING.equals(token))
								label = jsonReader.nextString();
							else if( JsonToken.NULL.equals(token))
								jsonReader.nextNull();
							if( child != null ) {
								if( child.getID() == node.getID())
									handleCycle(node, child, label);
								else
									node.addChild(child, label);		
							}
							jsonReader.endArray();
							token = jsonReader.peek();
						}
						jsonReader.endArray();
						jsonReader.endObject();
						completed = true;
						break;
					case DESCRIPTOR:
						base = createBase( jsonReader, token);
						node = new TestObject( store.id, store.name, store.leaf, store.base, base);
						nodes.push(node);
						break;
					case LEAF:
						jsonReader.nextBoolean();
						break;
					default:
						break;
					}
				}
				break;
			default:
				break;
			}
		}
		return ( node == null )? node: ( nodes.isEmpty()? node: nodes.pop());
	}

	protected Map<String, String> createBase( JsonReader jsonReader, JsonToken token) throws IOException {
		Map<String, String> base = new HashMap<String, String>();
		jsonReader.beginArray();
		token = jsonReader.peek();
		while( !JsonToken.END_ARRAY.equals( token )){
			jsonReader.beginObject();
			if( jsonReader.peek().equals(JsonToken.NAME)) {
				String name = jsonReader.nextName();
				token = jsonReader.peek();
				if( !JsonToken.NULL.equals(token)) {
					String value = jsonReader.nextString();
					if( !StringUtils.isEmpty(value))
						name = name.replace(".", "_");
					base.put( name, value);
				}else
					jsonReader.nextNull();
			}
			jsonReader.endObject();
			token = jsonReader.peek();
		}
		jsonReader.endArray();
		token = jsonReader.peek();
		return base;
	}

	@Override
	public void write(JsonWriter arg0, TestObject testObject) throws IOException {
		arg0.beginObject();
		if( testObject == null ) {
			arg0.endObject();
			return;
		}
		arg0.name(Attributes.ID.name());
		arg0.value(testObject.getID());
		arg0.name(Attributes.LEAF.name());
		arg0.value(testObject.isLeaf());
		arg0.name(Attributes.PROPERTIES.name());
		Iterator<Map.Entry<String, String>> iterator = testObject.getBase().entrySet().iterator();
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
		if( testObject.getData() != null ) {
			arg0.name(Attributes.DESCRIPTOR.name());
			iterator = testObject.getData().entrySet().iterator();
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
		if( !testObject.isLeaf()) {
			arg0.name(Attributes.REVERSED.name());
			arg0.value(testObject.isReverse());
			arg0.name(Attributes.CHILDREN.name());
			arg0.beginArray();
			for( Map.Entry<TestObject, String> entry: testObject.getChildren().entrySet() ) {
				TestObject child = entry.getKey();
				if( child.equals(testObject)) {
					logger.warning( S_ERR_CYCLE_DETECTED + testObject.getID() + "->" + child.getID());
					continue;
				}
				arg0.beginArray();
				write( arg0, child);
				if( entry.getValue() == null)
					arg0.value( IModelLeaf.IS_CHILD);
				else
					arg0.value(entry.getValue());
				arg0.endArray();
			}
			arg0.endArray();
		}
		arg0.endObject();
	}
	
	private class Store{
		
		private long id;
		private String name;
		private boolean leaf;
		private Map<String, String> base;
		
		protected Store(long id, String name, boolean leaf, Map<String, String> base) {
			super();
			this.id = id;
			this.leaf = leaf;
			this.name = name;
			this.base = base;
		}
	}
}
