package org.aieonf.model;

import org.aieonf.concept.*;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.util.StringStyler;

public interface IModelLeaf<T extends IDescriptor> extends IDescribable<T>
{
	//Supported attributes
	public static final String S_MODEL = "Model";
	public static final String CHILDREN = "Children";

	//Error messages
	public static final String S_ERR_NO_DESCRIPTOR = 
			"No descriptor was added to this model";
	public static final String S_ERR_MODELS_CAN_NOT_BE_MERGED = 
			"The two models can not be merged as the descriptors do not match: ";
	public static final String S_ERR_NOT_FULL_CONCEPT =
			"Can not conceptualise this concept, as it is not a full concept";
	public static final String S_ERR_ROOT_NOT_FOUND =
			"Cannot check for cycles in this model as the root is not available";
	public static final String S_ERR_RELATIONSHIP_EXISTS =
			"Can not add relationship as it exists in the model. ";
	public static final String S_ERR_CYCLE_FOUND_IN_MODEL =
			"Attempting to add a concept that will cause a cycle in the model. ";

	public enum Attributes{
		ID,
		NAME,
		IDENTIFIER,
		ROOT,
		LEAF,
		DEPTH,
		DIRECTION;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}	
	}

	/**
	 * Get the possible directions for the model
	 * @author Kees Pieters
	 */
	public enum Direction
	{
		UNI_DIRECTIONAL,
		BI_DIRECTIONAL;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
	}

	/**
	 * Get the (optional) id of the model. 
	 * @return
	 */
	public String getID();

	/**
	 * Get the identifier of the changeable object
	 * @return
	 */
	public String getIdentifier();
	
	/**
	 * Set the identifier for this leaf
	 * @param identifier
	 */
	public void setIdentifier( String identifier );
	

	/**
	 * Get the parent of this leaf, or null if the leaf is a root
	 * @return
	 */
	public IModelLeaf<? extends IDescriptor> getParent();

	/**
	 * Set the parent
	 * @param parent
	 */
	void setParent( IModelLeaf<? extends IDescriptor> parent );
	
	/**
	 * Set the leaf with the given value
	 * @param attr
	 * @param value
	 */
	public void set( Attributes attr, String value ); 

	/**
	 * Returns true if the node is a root
	 * @return
	 */
	public boolean isRoot();

	/**
	 * Get the direction of this model with 
	 * respect to its children
	 * @return
	 */
	public Direction getDirection();

	/**
	 * Returns true if the model is a leaf ( has no children )
	 * @return
	 */
	public boolean isLeaf();

	/**
	 * Returns true if the leaf contains the given descriptor
	 * @param descriptor
	 * @return
	 */
	public boolean contains( IDescriptor descriptor );

	/**
	 * Get the depth of the model. This is the maximum amount of the
	 * root to the farthest ancestor in the tree
	 * @return
	 */
	public int getDepth();

	/**
	 * This support method sets the depth of a model
	 * @param depth
	 * @throws ConceptException
	 */
	void setDepth( int depth ) throws ConceptException;
	
	/**
	 * If the given descriptor is considered the same as that of the leaf, this method returns '0'.
	 * Otherwise it returns a comparable result, depending on the provided implies operator 
	 * @return
	 */
	public int implies( IDescriptor descriptor );
}
