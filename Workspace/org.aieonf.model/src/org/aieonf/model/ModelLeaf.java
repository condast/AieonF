package org.aieonf.model;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.concept.*;
import org.aieonf.concept.core.ConceptBase;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.implicit.AbstractImplies;
import org.aieonf.concept.implicit.Implies;
import org.aieonf.util.Utils;

public class ModelLeaf<T extends IDescriptor> extends ConceptBase implements IModelLeaf<T>, Comparable<IModelLeaf<T>>
{	
	//The concept that is modelled
	private T descriptor;
	private boolean leaf;
	private Implies<T,IDescriptor> implies;
	
	private IModelLeaf<? extends IDescriptor> parent;
	
	/**
	 * Create the model
	 * @param concept
	 */
	public ModelLeaf( T descriptor )
	{
		this( descriptor, new DefaultImplies<T>( descriptor ));
 	}

	/**
	 * Create the model
	 * @param concept
	 */
	public ModelLeaf( T descriptor, Implies<T,IDescriptor> implies )
	{
		this.descriptor = descriptor;
		this.implies = implies;
		this.leaf = true;
	}

	/**
	 * Set the leaf with the given value
	 * @param attr
	 * @param value
	 */
	@Override
	public void set( Enum<?> attr, String value ){
		super.set( attr, value);
	}

	/**
	 * Fill the model with the given descriptor
	 */
	public void fill(){
		super.set( IModelLeaf.Attributes.DIRECTION, Direction.UNI_DIRECTIONAL.toString() );
		super.set( IModelLeaf.Attributes.DEPTH, String.valueOf( 0 ));
	}

	/**
	 * Get the (optional) id of the model. 
	 * @return
	 */
	@Override
	public String getID(){
		return super.get( Attributes.ID );
	}

	/**
	 * Get the (optional) identifier of the model. 
	 * @return
	 */
	@Override
	public String getIdentifier(){
		return super.get( Attributes.IDENTIFIER );
	}

	/**
	 * Set the identifier for this leaf
	 * @param identifier
	 */
	@Override
	public void setIdentifier( String identifier ){
		super.set( Attributes.IDENTIFIER , identifier );
	}

	/**
	 * @return the parent
	 */
	@Override
	public IModelLeaf<? extends IDescriptor> getParent()
	{
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	@Override
	public void setParent(IModelLeaf<? extends IDescriptor> parent)
	{
		this.parent = parent;
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
  public boolean hasChanged()
  {
  	if( descriptor.hasChanged() )
  		return true;
  	return super.hasChanged();
  }
  
 	/**
	 * Get the descriptor that this tree node represents
	 * @return
	 */
	@Override
	public T getDescriptor()
	{
		return ( T )this.descriptor;
	}

	/**
	 * Get the direction of this model with 
	 * respect to its children
	 * @return
	*/
	@Override
	public Direction getDirection()
	{
		return Direction.valueOf( super.get( IModelLeaf.Attributes.DIRECTION ));
	}

	/**
	 * Returns true if the model is a leaf ( has no children )
	 * @return
	*/
	@Override
	public boolean isLeaf(){
		return leaf;
	}

	
	protected void setLeaf(boolean leaf) {
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
		String str = super.get( IModelLeaf.Attributes.DEPTH);
		if( Utils.isNull(str))
			str = "0";
		return Integer.parseInt( str );
	}

	/**
	 * This support method sets the depth of a model
	 * @param depth
	*/
	@Override
	public void setDepth( int depth ) throws ConceptException{
		super.set( IModelLeaf.Attributes.DEPTH, String.valueOf( depth ));
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
	public int compareTo( IModelLeaf<T> node ) 
	{
		if( this.descriptor == null )
			return -1;
		return this.descriptor.compareTo( node.getDescriptor() );
	}

	
	@Override
	public int implies(IDescriptor descriptor) {
		return implies.compareTo( descriptor);
	}

	private static class DefaultImplies<T extends IDescriptor> extends AbstractImplies<T,IDescriptor>{

		DefaultImplies(T reference) {
			super(reference, Conditions.ID);
		}

		@Override
		protected int compareOnAttribute(IDescriptor obj) {
			return 0;
		}
		
	}
	
	/**
	 * Get the children of the given model leaf if they exist
	 * @param model
	 * @return
	 */
	public static Collection<? extends IModelLeaf<? extends IDescriptor>> getChildren( IModelLeaf<? extends IDescriptor> model ){
		Collection<IModelLeaf<? extends IDescriptor>> children = new ArrayList<IModelLeaf<? extends IDescriptor>>();
		if(!( model instanceof IModelNode ))
			return children;
		IModelNode<? extends IDescriptor> md = 
			(IModelNode<? extends IDescriptor>) model;
		return md.getChildren();
	}

	/**
	 * Get the first child with the given descriptor, or null if it wasn't found
	 * @param model
	 * @return
	 */
	public static IModelLeaf<? extends IDescriptor> getChild( IModelLeaf<? extends IDescriptor> model, IDescriptor descriptor ){
		if(!( model instanceof IModelNode ))
			return null;
		IModelNode<? extends IDescriptor> md = 
			(IModelNode<? extends IDescriptor>) model;
		return md.getChild( descriptor );
	}

	public static boolean hasChildren( IModelLeaf<? extends IDescriptor> model ){
		if(!( model instanceof IModelNode ))
			return false;
		IModelNode<? extends IDescriptor> md = 
			(IModelNode<? extends IDescriptor>) model;
		return md.hasChildren();		
	}
	
	/**
	 * Get the model with the given id
	 */
	@SuppressWarnings("unchecked")
	public static IModelLeaf<IDescriptor> getModel( IModelLeaf<? extends IDescriptor> root, String id ){
		if( Utils.isNull( id ))
			return null;
		if( id.equals( root.getIdentifier() ))
			return (IModelLeaf<IDescriptor>) root;
		if(!( root instanceof IModelNode))
			return null;
		IModelNode<?> node = (IModelNode<?>) root;
		for( IModelLeaf<?> child: node.getChildren() ){
			IModelLeaf<IDescriptor> result = getModel( child, id ); 
			if( result != null )
				return result;
		}
		return null;
	}
}