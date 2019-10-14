package org.aieonf.orientdb.model;

import org.aieonf.commons.Utils;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.*;
import org.aieonf.concept.IConcept.Scope;
import org.aieonf.concept.core.ConceptBase;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;

public class ModelLeaf extends ConceptBase implements IModelLeaf<IDescriptor>
{	
	//The concept that is modeled
	private Descriptor descriptor;
	private boolean leaf;
	
	private Model parent;

	public ModelLeaf(){
	}
	
	@Override
	public void init(IDescriptor descriptor) {
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
	public Scope getScope() {
		String str = get( IConcept.Attributes.SCOPE.name());
		return StringUtils.isEmpty(str)?Scope.PUBLIC: Scope.valueOf(str);
	}

	/**
	 * @return the parent
	 */
	@Override
	public IModelNode<? extends IDescriptor> getParent()
	{
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	@Override
	public void setParent(IModelNode<? extends IDescriptor> parent)
	{
		this.parent = (Model) parent;
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
	 * Get the descriptor that this tree node represents
	 * @return
	 */
	@Override
	public Descriptor getDescriptor()
	{
		return this.descriptor;
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

	/**
	 * Implement the compare to
	 */
	@Override
	public int compareTo( IDescribable<?> node ) 
	{
		if( this.descriptor == null )
			return -1;
		return this.descriptor.compareTo( node.getDescriptor() );
	}

	
	@Override
	public int implies(IDescriptor descriptor) {
		return 0;//implies.compareTo( descriptor);
	}
}