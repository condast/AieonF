package org.aieonf.model;

import java.util.Collection;
import java.util.Vector;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.ConceptException;

public class ModelWrapper<T extends IDescriptor> implements IModelNode<T>
{
	//The model that is wrapped
	private IModelLeaf<T> model;
	
	private Vector<IModelLeaf<? extends IDescriptor>> children;
	
	/**
	 * Create a wrapper for the given model
	 * @param model
	 */
	public ModelWrapper( IModelLeaf<T> model ){
		this.model = model;
		this.children = new Vector<IModelLeaf<? extends IDescriptor>>();
	}
	
	@Override
	public String get(Enum<?> attr) {
		return this.model.get(attr);
	}


	@Override
	public void set(Enum<?> attr, String value) {
		this.model.set(attr, value);
	}

	@Override
	public String getID() {
		return this.model.getID();
	}
	
	@Override
	public String getIdentifier() {
		return this.model.getIdentifier();
	}

	/**
	 * Set the identifier for this leaf
	 * @param identifier
	 */
	@Override
	public void setIdentifier( String identifier ){
		this.model.setIdentifier(identifier);
	}

	/**
	 * Returns true if the node is a root
	 * @return
	 */
	@Override
	public boolean isRoot(){
		return this.model.isRoot();
	}
	
  /**
   * If true, the values have changed
   * @return
  */
  @Override
	public boolean hasChanged()
  {
  	return this.model.hasChanged();
  }
  
	/**
	 * Get the descriptor that this tree node represents
	 * @return
	*/
	@Override
	public T getDescriptor()
	{
		return (T)this.model.getDescriptor();
	}

	/**
	 * Get the direction of this model with 
	 * respect to its children
	 * @return
	*/
	@Override
	public Direction getDirection()
	{
		return this.model.getDirection();
	}

	/**
	 * Add a child model to the model
	 * @param model
	 * @returns the created model
	 */
	@Override
	public boolean addChild(IModelLeaf<? extends IDescriptor> child)
	{
		if( this.model instanceof IModelNode ){
			IModelNode<T> node = ( IModelNode<T> )this.model;
			return node.addChild( child );
		}
		return children.add(child );
	}	

	/**
	 * Remove a child model from the parent
	 * @param concept
	*/
	@Override
	public boolean removeChild(IModelLeaf<? extends IDescriptor> child)
	{
		if( this.model instanceof IModelNode ){
			IModelNode<T> node = ( IModelNode<T> )this.model;
			return node.removeChild( child );
		}
		return children.remove(child );
	}

	/**
	 * Remove a child model from the parent
	 * @param concept
	*/
	@Override
	public Collection<IModelLeaf<? extends IDescriptor>> getChildren()
	{
		if( this.model instanceof IModelNode ){
			IModelNode<T> node = ( IModelNode<T> )this.model;
			return node.getChildren();
		}
		return children;
	}

	/**
	 * Get the child with the given descriptor
	 * @param descriptor
	 * @return
	*/
	@Override
	public IModelLeaf<? extends IDescriptor> getChild( IDescriptor descriptor)
	{
		if( this.model instanceof IModelNode ){
			IModelNode<T> node = ( IModelNode<T> )this.model;
			return node.getChild( descriptor );
		}
		for( IModelLeaf<? extends IDescriptor> child: children ){
			if( child.getDescriptor().equals( descriptor ))
				return child;
		}
		return null;
	}

	/**
	 * Returns true if the model is a leaf ( has no children )
	 * @return
	*/
	@Override
	public boolean isLeaf(){
		return this.model.isLeaf();
	}

	/**
	 * Returns true if the model has children
	 * @return
	 */
	@Override
	public boolean hasChildren()
	{
		if( this.model instanceof IModelNode ){
			IModelNode<T> node = ( IModelNode<T> )this.model;
			return node.hasChildren();
		}
		return ( children.size() > 0 );
	}

	/**
	 * Get the number of children
	 * @return
	*/
	@Override
	public int nrOfchildren()
	{
		if( this.model instanceof IModelNode ){
			IModelNode<T> node = ( IModelNode<T> )this.model;
			return node.nrOfchildren();
		}
		return children.size();
	}

	/**
	 * Returns true if the model, or one of its descendants contains
	 * the given descriptor
	 * @param descriptor
	 * @return
	*/
	@Override
	public boolean contains(IDescriptor descriptor)
	{
		return this.model.contains( descriptor );
	}

	/**
	 * Get the depth of the model. This is the maximum amount of the
	 * root to the farthest ancestor in the tree
	 * @return
	*/
	@Override
	public int getDepth()
	{
		return this.model.getDepth();
	}

	@Override
	public void setDepth(int depth) throws ConceptException
	{
		this.model.setDepth( depth );
	}

	@Override
	public IModelLeaf<? extends IDescriptor> getParent()
	{
		return this.model.getParent();
	}

	@Override
	public void setParent(IModelLeaf<? extends IDescriptor> parent)
	{
		this.model.setParent( parent );
	}

	@Override
	public int implies(IDescriptor descriptor) {
		return this.model.implies(descriptor);
	}
}