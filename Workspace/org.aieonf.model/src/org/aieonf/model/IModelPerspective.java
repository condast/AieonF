package org.aieonf.model;

import org.aieonf.concept.IDescriptor;

/**
 * Defines a perspective on models
 * @see IReadPerspective, IWritePerspective
 * @author Kees Pieters
 *
 */
public interface IModelPerspective<T extends IModelLeaf<U>, U extends IDescriptor> 
{
	//Error messages
	public static final String S_ERR_ADD_CONTAINS_DESCENDANT = 
		"could not add the model, as the database contains a descendant";
	public static final String S_ERR_UPDATE_DOES_NOT_CONTAIN_DESCENDANT = 
		"could not update the model, as the database does not contain a descendant";
	
	/**
	 * The policy for write operations. 
	 * Strict: every concept in the model needs to exist in an update,
	 * and be non-existent in an add. 
	 * Soft: the above applies only for the root  
	 * @author Kees Pieters
	 *
	*/
	public enum Policy
	{
		Strict,
		Soft
	}

	/**
	 * state the policy for embedded children. 
	 * EmbedOnly means that the child does not need to be stored separately
	 *   
	 * @author Kees Pieters
	 *
	*/
	public enum EmbeddedPolicy
	{
		EmbedOnly,
		AddToDatabase
	}

	/**
	 * Get the policy for this perspective.A policy is strict when all the
	 * concepts in a model should exist (update) or not (add). A soft policy
	 * only requires this for the root concept 
	 * @return Policy
	*/
	public Policy getPolicy();
	
	/**
	 * The model that is being processed
	 * @return
	*/
	public T getModel();
	
	/**
	 * Get the model for the given descriptor. If more models
	 * are found, they are added under the root descriptor
	 * @param descriptor
	 * @return
	 * @throws ModelException
	*/
	public IModelNode<? extends IDescriptor> getModel( IDescriptor descriptor )
		throws ModelException;
	
	/**
	 * Returns true if the model is contained in this perspective
	 * @param model IModelNode
	 * @return boolean
	 * @throws ModelException
	*/
	public boolean containsModel( IModelNode<? extends IDescriptor> model) throws ModelException;
	
	/**
	 * Add a model to the perspective
	 * 
	 * @param model IModelNode
	 * @return boolean
	 * @throws ModelException
	*/
	public boolean addModel( IModelLeaf<? extends IDescriptor> model ) throws ModelException;

	/**
	 * Update a model in the perspective
	 * 
	 * @param model IModelNode 
	 * @throws ModelException
	*/
	public void updateModel( T model ) throws ModelException;

	/**
	 * Remove a model from the perspective.
	 * Returns any model that was found, or null if it wasn't
	 * contained in the perspective
	 * 
	 * @param model IModelNode
	 * @return IModelNode
	 * @throws ModelException
	*/
	public IModelNode<? extends IDescriptor> removeModel( T model ) throws ModelException;

	/**
	 * Returns true if a child with the given descriptor is contained 
	 * in this perspective
	 * @return
	*/
	public boolean containsChild( IDescriptor descriptor );

	/**
	 * Get the child model for the given descriptor. If more models
	 * are found, they are added under the root descriptor.
	 * Returns null if the child is not found
	 * @param descriptor
	 * @return
	 * @throws ModelException
	*/
	public IModelLeaf<? extends IDescriptor> getChild( IDescriptor descriptor )
		throws ModelException;

	/**
	 * Add a child to the model.
	 * Returns true if successful 
	 * @param child
	 * @return boolean
	 * @throws ModelException
	*/
	public boolean addChild( T child ) throws ModelException;
	
	/**
	 * Update a child of the model 
	 * @param child
	 * @throws ModelException
	*/
	public void updateChild( T child ) throws ModelException;

	/**
	 * Remove a child of the model. Returns the models that were removed,
	 * or null if the child does not exist in the model 
	 * @param child
	 * @throws ModelException
	*/
	public T removeChild( T child ) throws ModelException;
}
