package org.aieonf.model.core;

import java.util.*;

import org.aieonf.concept.*;

public interface IModelNode<D extends Object> extends IModelLeaf<D>
{	
	/**
	 * Add a child model to the model
	 * @param child IModelNode<? extends IDescriptor>
	 * @returns the created model
	 */
	public boolean addChild( IModelLeaf<? extends IDescriptor> child );

	/**
	 * Add a child model to the model of the given type
	 * @param child IModelNode<? extends IDescriptor>
	 * @returns the created model
	 */
	public boolean addChild( IModelLeaf<? extends IDescriptor> child, String type );

	/**
	 * Remove a child model from the parent
	 * @param child IModelNode<? extends IDescriptor>
	*/
	public boolean removeChild( IModelLeaf<? extends IDescriptor> child );

	/**
	 * Remove a child model from the parent
	 * @return Collection<IModelNode<? extends IDescriptor>>
	*/
	public Map<IModelLeaf<? extends IDescriptor>, String> getChildren();

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
	 * Get the identifier of the given child. can be null
	 * @param name
	 * @return IModelNode<? extends IDescriptor>
	*/
	public String getChildIdentifier( IModelLeaf<? extends IDescriptor> child );

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
}
