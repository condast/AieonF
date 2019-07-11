package org.aieonf.orientdb.core;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

import org.aieonf.commons.Utils;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.IRelationship;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;

public class OModelLeaf<D extends IDescriptor> extends VertexConceptBase implements IModelLeaf<D> {

	public enum OAttributes{
		DESCRIPTOR_ID,
		DESCRIPTOR_NAME,
		DESCRIPTOR_VERSION
	}

	private D descriptor;
	private IModelNode<? extends IDescriptor> parent;
	
	private OrientGraph graph;
	private Edge parentEdge;
	private boolean leaf;

	public OModelLeaf( OrientGraph graph, D descriptor) {
		this( null, graph, descriptor );
	}

	public OModelLeaf( IModelNode<D> parent, OrientGraph graph, D descriptor) {
		this( parent, graph, graph.addVertex(null), descriptor );
	}
	
	protected OModelLeaf( IModelNode<D> parent, OrientGraph graph, Vertex vertex, D descriptor) {
		super( vertex );
		this.graph = graph;
		setParent( parent);
	}

	protected OrientGraph getGraph() {
		return graph;
	}

	@Override
	public D getDescriptor() {
		return descriptor;
	}

	@Override
	public int compareTo(IDescribable<?> node) {
		if( this.descriptor == null )
			return -1;
		return this.descriptor.compareTo( node.getDescriptor() );
	}

	@Override
	public void init(D descriptor) {
		this.descriptor = descriptor;
		set( OAttributes.DESCRIPTOR_ID, descriptor.getID());
		set( OAttributes.DESCRIPTOR_NAME, descriptor.getName());
		set( OAttributes.DESCRIPTOR_VERSION, String.valueOf( descriptor.getVersion()));
	}

	public Edge getParentEdge() {
		return parentEdge;
	}

	/**
	 * Set the leaf with the given value
	 * @param attr
	 * @param value
	 */
	@Override
	public String get( Enum<?> attr ){
		return super.get( attr.name());
	}

	/**
	 * Set the leaf with the given value
	 * @param attr
	 * @param value
	 */
	@Override
	public void set( Enum<?> attr, String value ){
		super.set( attr.name(), value);
	}

	/**
	 * Get the (optional) id of the model. 
	 * @return
	 */
	@Override
	public String getID(){
		return get( Attributes.ID );
	}

	
	@Override
	public String getType() {
		return get( Attributes.TYPE );
	}

	/**
	 * Get the (optional) identifier of the model. 
	 * @return
	 */
	@Override
	public String getIdentifier(){
		return get( Attributes.IDENTIFIER );
	}

	/**
	 * Set the identifier for this leaf
	 * @param identifier
	 */
	@Override
	public void setIdentifier( String identifier ){
		set( Attributes.IDENTIFIER , identifier );
	}

	@Override
	public IModelNode<? extends IDescriptor> getParent() {
		return parent;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void setParent(IModelNode<? extends IDescriptor> parent) {
		this.parent = parent;
		if( parent == null ) {
			graph.removeEdge(parentEdge);
			return;
		}else {
			OModelNode<D> node = (OModelNode<D>) parent;
			parentEdge = graph.addEdge( null, node.getVertex(), getVertex(), IRelationship.Attributes.CHILD_OF.toString() );
		}
	}

	/**
	 * Returns true if the node is a root
	 * @return
	 */
	@Override
	public boolean isRoot(){
		return (( this.parent == null ) && ( this.getDepth() ) == 0 );
	}
	
  /**
   * If true, the values have changed
   * @return
  */
	@Override
  public boolean hasChanged(){
  	if( descriptor.hasChanged() )
  		return true;
  	return super.hasChanged();
  }
  
	/**
	 * Get the direction of this model with 
	 * respect to its children
	 * @return
	*/
	@Override
	public Direction getDirection()
	{
		return Direction.valueOf( get( IModelLeaf.Attributes.DIRECTION ));
	}

	/**
	 * Returns true if the model is a leaf ( has no children )
	 * @return
	*/
	@Override
	public boolean isLeaf(){
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	/**
	 * Get the depth of the model. This is the maximum amount of the
	 * root to the farthest ancestor in the tree
	 * @return
	*/
	@Override
	public int getDepth()
	{
		String str = get( IModelLeaf.Attributes.DEPTH);
		if( Utils.assertNull(str))
			str = "0";
		return Integer.parseInt( str );
	}

	/**
	 * This support method sets the depth of a model
	 * @param depth
	*/
	@Override
	public void setDepth( int depth ) throws ConceptException{
		set( IModelLeaf.Attributes.DEPTH, String.valueOf( depth ));
	}

	/**
	 * Returns true if the model, or one of its descendants contains
	 * the given descriptor
	 * @param descriptor
	 * @return
	 */
	@Override
	public boolean contains( IDescriptor descr )
	{
		return descr.equals( this.descriptor );
	}
	
	@Override
	public int implies(IDescriptor descriptor) {
		return 0;//implies.compareTo( descriptor);
	}
}
