package org.aieonf.model;

import java.util.List;

import org.aieonf.concept.*;

public interface IRelationModelNode<T extends IDescriptor> extends IModelNode<T>
{
	//Error messages
	public static final String S_ERR_NO_CONCEPT_ADDED =
		"A null concept was added to the model";
	public static final String S_ERR_CANNOT_ADD_RELATIONSHIP =
		"The relationships can be added to this concept: ";

	/**
	 * Add child models to the model that connects to the given relationship
	 * @param concept
	 * @returns the created models
	 * @throws ModelException
	 */
	public List<IModelNode<? extends IDescriptor>> addChildren( IFixedConcept concept ) throws ModelException;

	/**
	 * Get the model corresponding with the given relationship.
	 * returns null if no such relationship exists
	 * @param child
	 * @return
	*/
	public IModelNode<? extends IDescriptor> getChild( IRelationship relationship );
}
