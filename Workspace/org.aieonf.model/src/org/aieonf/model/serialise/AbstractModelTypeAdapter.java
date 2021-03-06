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

	public static final String S_ERR_NULL_CHILD = "The child is null: ";
	public static final String S_ERR_CYCLE_DETECTED = "A cycle has been detected ";
	public static final String S_ERR_NO_DESCRIPTOR = "The descriptor is null!";

	public enum Attributes{
		PROPERTIES,//Properties of the model
		DESCRIPTOR,//Descriptor
		CHILDREN,
		ID,
		LEAF,
		PARENT,
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
	
	//The creation of nodes isd delayed until the descriptor is read, so the rest is temporarily 
	//stored as a concept base
	private Store store; 
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
			result= onTransform( readNode(reader, true ));
			token = reader.peek();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	protected abstract IModelLeaf<IDescriptor> onTransform( N node );

	protected abstract N onCreateNode( long id, IConceptBase base, boolean leaf, D descriptor );

	protected abstract boolean onAddParent( N model, N parent );

	protected abstract boolean onAddChild( N model, N child, boolean reversed, String label );

	protected abstract D onCreateDescriptor( IConceptBase properties );

	/**
	 * Handle cycles. Returns true if a cycle was detected
	 * @param parent
	 * @param child
	 * @param label
	 * @return
	 */
	protected boolean handleCycle( N parent, N child, String label ) {
		long parentId = Long.parseLong( getID( parent ));
		long childId = Long.parseLong( getID( child ));
		if(( parentId < 0 ) && ( childId < 0 ))
			return false;
		if( parentId != childId)
			return false;
		logger.warning( S_ERR_CYCLE_DETECTED + "(" + label + "):" + parentId + "->" + getID( child ));
		return true;
	}

	/**
	 * Get the id of the given node
	 * @param node
	 * @return
	 */
	protected abstract String getID( N node );

	protected Store readLeaf( JsonReader jsonReader) throws IOException {
		JsonToken token = jsonReader.peek();
		if( JsonToken.END_OBJECT == token ) {
			return null;
		}
		jsonReader.nextName().toUpperCase();
		long id = Descriptor.parseId( jsonReader.nextString());
		jsonReader.nextName().toUpperCase();
		boolean leaf = jsonReader.nextBoolean(); 
		String name = jsonReader.nextName();
		IConceptBase base = null;
		if( name.equals(IConceptBase.S_PROPERTIES)) {
			base = createBase(jsonReader, token);
		}
		else
			base = new ConceptBase();
		store = new Store( id, leaf, base );
		return store;
	}
	
	protected N readNode( JsonReader jsonReader, boolean root ) throws ParseException, IOException {
		String label;
		String key = null;
		N node = null;
		IConceptBase base;//stores intermediate key-value pairs
		JsonToken token = null;
		boolean completed = false;
		while(!completed && jsonReader.hasNext() ) {
			token = jsonReader.peek();
			logger.fine(token.toString());
			switch( token) {
			case BEGIN_OBJECT:
				jsonReader.beginObject();
				store = readLeaf( jsonReader);
				token = jsonReader.peek();
				if( JsonToken.END_OBJECT.equals(token)) {
					logger.warning(S_ERR_NO_DESCRIPTOR );
					node = onCreateNode( store.id, store.base, store.leaf, null);
					nodes.push(node);
					jsonReader.endObject();
				}

				if( node != null )
					completed = true;
				token = jsonReader.peek();
				break;
			case NAME:
				key = jsonReader.nextName().toUpperCase();
				logger.fine(key);
				D descriptor = null;
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
							N child = readNode( jsonReader, false );
							token = jsonReader.peek();
							logger.fine( token.name());
							label = IModelLeaf.IS_CHILD;
							if( JsonToken.STRING.equals(token))
								label = jsonReader.nextString();
							else if( JsonToken.NULL.equals(token))
								jsonReader.nextNull();
							if( child != null ) {
								boolean cycle = handleCycle(node, child, label);
								if(!cycle )			
									onAddChild( node, child, reversed, label);
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
						descriptor = onCreateDescriptor( base);
						node = onCreateNode( store.id, store.base, store.leaf, descriptor);
						nodes.push(node);
						//Close if it is a leaf
						token = jsonReader.peek();	
						if( JsonToken.END_OBJECT.equals(token)) {
							jsonReader.endObject();
							completed = true;
						}
						break;
					case PARENT:
						if( !root ) {
							break;
						}
						jsonReader.beginObject();
						token = jsonReader.peek();	
						Store pstore = readLeaf( jsonReader);
						jsonReader.nextName();
						base = createBase( jsonReader, token);
						descriptor = onCreateDescriptor( base);
						N pnode = onCreateNode( pstore.id, pstore.base, pstore.leaf, descriptor);
						onAddParent( node, pnode);
						jsonReader.endObject();
						token = jsonReader.peek();	
						if( !JsonToken.END_OBJECT.equals(token))
							break;
						jsonReader.endObject();	
						token = jsonReader.peek();
						break;
					case LEAF:
						jsonReader.nextBoolean();
						break;
					default:
						break;
					}
				}
				break;
			case END_ARRAY:
				jsonReader.endArray();
				break;
			case END_OBJECT:
				jsonReader.endObject();
				break;
			default:
				break;
			}
		}
		return ( node == null )? node: ( nodes.isEmpty()? node: nodes.pop());
	}

	protected IConceptBase createBase( JsonReader jsonReader, JsonToken token) throws IOException {
		IConceptBase base = new ConceptBase();
		jsonReader.beginArray();
		while( !JsonToken.END_ARRAY.equals( token )){
			jsonReader.beginObject();
			if( jsonReader.peek().equals(JsonToken.NAME)) {
				String name = jsonReader.nextName();
				token = jsonReader.peek();
				if( !JsonToken.NULL.equals(token)) {
					String value = jsonReader.nextString();
					if( !StringUtils.isEmpty(value))
						name = name.replace(".", "_");
					base.set( name, value);
				}else
					jsonReader.nextNull();
			}
			jsonReader.endObject();
			token = jsonReader.peek();
		}
		jsonReader.endArray();
		return base;
	}

	@Override
	public void write(JsonWriter arg0, IModelLeaf<IDescriptor> leaf) throws IOException {
		write( arg0, leaf, true );
	}

	protected void writeLeaf(JsonWriter arg0, IModelLeaf<IDescriptor> leaf) throws IOException {
		if( leaf == null ) {
			arg0.endObject();
			return;
		}
		arg0.name(Attributes.ID.name());
		arg0.value(leaf.getID());
		arg0.name(Attributes.LEAF.name());
		arg0.value(leaf.isLeaf());
		arg0.name(Attributes.PROPERTIES.name());
		Iterator<Map.Entry<String, String>> iterator = leaf.entrySet().iterator();
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
			iterator = leaf.getData().entrySet().iterator();
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
	}
	
	@SuppressWarnings("unchecked")
	protected void write(JsonWriter arg0, IModelLeaf<IDescriptor> leaf, boolean root ) throws IOException {
		arg0.beginObject();
		writeLeaf( arg0, leaf );
		if( root && leaf.getParent() != null ) {
			arg0.name(Attributes.PARENT.name());
			arg0.beginObject();
			writeLeaf( arg0, (IModelLeaf<IDescriptor>) leaf.getParent() );
			arg0.endObject();
		}
		if( !leaf.isLeaf()) {
			IModelNode<IDescriptor> model = (IModelNode<IDescriptor>) leaf;
			arg0.name(Attributes.REVERSED.name());
			arg0.value(model.isReverse());
			arg0.name(Attributes.CHILDREN.name());
			arg0.beginArray();
			for( Map.Entry<IModelLeaf<? extends IDescriptor>, String> entry: model.getChildren().entrySet() ) {
				IModelLeaf<? extends IDescriptor> child = entry.getKey();
				if( child.equals(model)) {
					logger.warning( S_ERR_CYCLE_DETECTED + model.getID() + "->" + child.getID());
					continue;
				}
				arg0.beginArray();
				write( arg0, (IModelLeaf<IDescriptor>) child, false);
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
		private boolean leaf;
		private IConceptBase base;
		
		protected Store(long id, boolean leaf, IConceptBase base) {
			super();
			this.id = id;
			this.leaf = leaf;
			this.base = base;
		}
	}
}
