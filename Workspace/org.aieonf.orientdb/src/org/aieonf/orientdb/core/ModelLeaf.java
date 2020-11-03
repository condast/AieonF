package org.aieonf.orientdb.core;

import java.util.Iterator;

import org.aieonf.commons.Utils;
import org.aieonf.commons.number.NumberUtils;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IConcept.Scope;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class ModelLeaf extends VertexConceptBase implements IModelLeaf<IDescriptor> {

	public static final String S_ERR_NULL_ID = "The model does not have a descriptor: ";

	public static final String REGEX_NON_NUMERIC = "[^\\d.]";

	private IDescriptor descriptor;
	
	private IModelNode<?> parent;
	
	private transient boolean leaf;
	
	public ModelLeaf( IModelNode<?> parent, Vertex vertex ) {
		super( vertex );
		vertex.setProperty(IDescriptor.Attributes.CLASS.name(), IModelLeaf.class.getCanonicalName());
		this.parent = parent;
		this.leaf = true;
		super.setChanged( false );
		
		//Create the descriptor
		Iterator<Edge> edges = vertex.getEdges(com.tinkerpop.blueprints.Direction.OUT, IDescriptor.DESCRIPTOR).iterator();
		if(edges.hasNext()) {
			Vertex dvertex = edges.next().getVertex(com.tinkerpop.blueprints.Direction.IN);
			this.descriptor = new Descriptor( new VertexConceptBase( dvertex ));
			String idstr = dvertex.getProperty(IDescriptor.Attributes.ID.name());
			if( StringUtils.isEmpty(idstr) || !NumberUtils.isNumeric(idstr) || idstr.trim().startsWith("-")) {
				idstr = dvertex.getId().toString().replaceAll(REGEX_NON_NUMERIC, "");
				this.descriptor.set( IDescriptor.Attributes.ID.name(), idstr);
			}
		}
	}

	@Override
	public IDescriptor getDescriptor() {
		return descriptor;
	}

	@Override
	public int compareTo(IDescribable o) {
		return getDescriptor().compareTo( o.getDescriptor() );
	}

	@Override
	public long getID() {
		Vertex vertex = getVertex();
		String str = vertex.getProperty(IDescriptor.Attributes.ID.name()); 
		return StringUtils.isEmpty(str)?-1: Long.parseLong( str );
	}

	
	@Override
	public String getName() {
		Vertex vertex = getVertex();
		String str = vertex.getProperty(IDescriptor.Attributes.NAME.name()); 
		return str;
	}

	@Override
	public String getVersion() {
		Vertex vertex = getVertex();
		String str = vertex.getProperty(IDescriptor.Attributes.VERSION.name()); 
		return str;
	}

	@Override
	public String getType() {
		Vertex vertex = getVertex();
		return vertex.getProperty(IConcept.Attributes.TYPE.name());
	}

	@Override
	public String getIdentifier() {
		Vertex vertex = getVertex();
		return vertex.getProperty(IModelLeaf.Attributes.IDENTIFIER.name());
	}

	@Override
	public void setIdentifier(String identifier) {
		Vertex vertex = getVertex();
		vertex.setProperty(IModelLeaf.Attributes.IDENTIFIER.name(), identifier);
	}

	@Override
	public Scope getScope() {
		String str = get( IConcept.Attributes.SCOPE.name() ); 
		Scope scope = StringUtils.isEmpty(str)?Scope.PUBLIC: Scope.valueOf(str);
		return scope;	}

	@Override
	public IModelNode<?> getParent() {
		return parent;
	}

	@Override
	public void setParent(IModelNode<?> parent) {
		this.parent = parent;
	}

	@Override
	public void set(Enum<?> attr, String value) {
		Vertex vertex = getVertex();
		vertex.setProperty(attr.name(), value);
	}

	@Override
	public boolean isRoot() {
		return (this.parent == null);
	}

	@Override
	public boolean isLeaf() {
		return leaf;
	}

	@Override
	public void setLeaf(boolean choice) {
		this.leaf = choice;
	}

	@Override
	public boolean contains(IDescriptor descriptor) {
		return descriptor.equals( this.descriptor );
	}

	@Override
	public int getDepth() {
		String str = getValue( IModelLeaf.Attributes.DEPTH);
		if( Utils.assertNull(str))
			str = "0";
		return Integer.parseInt( str );
	}

	@Override
	public void setDepth(int depth) throws ConceptException {
		setValue( IModelLeaf.Attributes.DEPTH, String.valueOf( depth ));
	}

	@Override
	public int implies(IDescriptor descriptor) {
		return 0;
	}

	@Override
	public IDescriptor getData() {
		return descriptor;
	}

	@Override
	public void setData(IDescriptor descriptor) {
		this.descriptor = descriptor;
	}
	
	protected String getValue( IModelLeaf.Attributes attr ) {
		return get(attr.name());
	}

	protected void setValue( IModelLeaf.Attributes attr, String value ) {
		set(attr.name(), value);
	}

	@Override
	public int hashCode() {
		return getVertex().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if( super.equals(obj))
			return true;
		if(!( obj instanceof ModelLeaf))
			return false;
		ModelLeaf leaf = (ModelLeaf) obj;
		return leaf.getVertex().getId().equals(this.getVertex().getId());
	}

	protected static Vertex getParent( Vertex vertex ) {
		Iterator<Edge> edges = vertex.getEdges(Direction.IN, IModelLeaf.IS_PARENT).iterator();
		while( edges.hasNext()) {
			Edge edge = edges.next();
			return edge.getVertex(Direction.OUT);
		}
		return null;
	}

}