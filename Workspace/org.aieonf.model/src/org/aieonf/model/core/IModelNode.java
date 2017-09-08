package org.aieonf.model.core;

import java.util.*;

import org.aieonf.concept.*;

public interface IModelNode<T extends IDescriptor> extends IModelLeaf<T>
{	
	/**
	 * Add a child model to the model
	 * @param child IModelNode<? extends IDescriptor>
	 * @returns the created model
	 */
	public boolean addChild( IModelLeaf<? extends IDescriptor> child );

	/**
	 * Remove a child model from the parent
	 * @param child IModelNode<? extends IDescriptor>
	*/
	public boolean removeChild( IModelLeaf<? extends IDescriptor> child );

	/**
	 * Remove a child model from the parent
	 * @return Collection<IModelNode<? extends IDescriptor>>
	*/
	public Collection<IModelLeaf<? extends IDescriptor>> getChildren();

	/**
	 * Get the child with the given descriptor
	 * @param descriptor IDescriptor
	 * @return IModelNode<? extends IDescriptor>
	*/
	public IModelLeaf<? extends IDescriptor> getChild( IDescriptor descriptor );

	/**
	 * Get the children with the given name
	 * @param name
	 * @return IModelNode<? extends IDescriptor>
	*/
	public IModelLeaf<? extends IDescriptor>[] getChildren( String name );

	/**
	 * Returns true if the model is a leaf ( has no children )
	 * @return
	*/
	@Override
	public boolean isLeaf();

	/**
	 * Returns true if the model is has children
	 * @return
	*/
	public boolean hasChildren();

	/**
	 * Get the number of children
	 * @return
	*/
	public int nrOfchildren();

	/**
	 * Returns true if the model, or one of its descendants contains
	 * the given descriptor
	 * @param descriptor
	 * @return
	*/
	@Override
	public boolean contains( IDescriptor descriptor );
}
