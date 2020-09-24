package org.aieonf.orientdb.core;

import java.util.Iterator;

import org.aieonf.commons.Utils;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IConcept.Scope;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class ModelLeaf extends VertexConceptBase implements IModelLeaf<IDescriptor> {

	public static final String S_ERR_NULL_ID = "The model does not have a descriptor: ";
	
	private IDescriptor descriptor;
	
	private IModelNode<?> parent;
	
	private transient boolean leaf;
	private transient boolean changed;

	public ModelLeaf( OrientGraph graph, IDomainAieon domain, Vertex vertex ) {
		this( graph, domain, null, vertex );
		Iterator<Edge> edges = vertex.getEdges(com.tinkerpop.blueprints.Direction.OUT).iterator();
		parent = null;
		while(edges.hasNext()) {
			Edge edge = edges.next();
			parent = new ModelNode( graph, domain, edge.getVertex(com.tinkerpop.blueprints.Direction.OUT)); 
		}
	}
	
	public ModelLeaf( OrientGraph graph, IDomainAieon domain, IModelNode<?> parent, Vertex vertex ) {
		super( vertex );
		this.parent = parent;
		this.leaf = true;
		this.changed = false;
		//Fill the properties;
		for( String key: vertex.getPropertyKeys())
			super.set( key, (String)vertex.getProperty(key));
		
		Iterator<Edge> edges = vertex.getEdges(com.tinkerpop.blueprints.Direction.IN, IDescriptor.DESCRIPTOR).iterator();
		if(edges.hasNext()) {
			//throw new NullPointerException( S_ERR_NULL_ID );

			//Create the descriptor
			Vertex dvertex = edges.next().getVertex(com.tinkerpop.blueprints.Direction.OUT);
			this.descriptor = new Descriptor();
			for( String key: dvertex.getPropertyKeys())
				descriptor.set( key, (String)dvertex.getProperty(key));
		}
	}

	@Override
	public IDescriptor getDescriptor() {
		return descriptor;
	}

	@Override
	public boolean hasChanged() {
		return changed;
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
		String str = get( IConcept.Attributes.SCOPE ); 
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
	public boolean isReverse() {
		String str = super.get(IModelLeaf.Attributes.REVERSE.name());
		return StringUtils.isEmpty(str)? false: Boolean.parseBoolean(str);
	}

	@Override
	public void setReverse( boolean choice ) {
		super.set( IModelLeaf.Attributes.REVERSE.name(), String.valueOf(choice));
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
		String str = get( IModelLeaf.Attributes.DEPTH);
		if( Utils.assertNull(str))
			str = "0";
		return Integer.parseInt( str );
	}

	@Override
	public void setDepth(int depth) throws ConceptException {
		set( IModelLeaf.Attributes.DEPTH, String.valueOf( depth ));
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

}