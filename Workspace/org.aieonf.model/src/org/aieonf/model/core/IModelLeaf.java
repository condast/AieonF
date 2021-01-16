package org.aieonf.model.core;

import org.aieonf.commons.strings.StringStyler;
import org.aieonf.concept.*;
import org.aieonf.concept.core.ConceptException;

public interface IModelLeaf<D extends Object> extends IDescribable, Comparable<IDescribable>
{
	//Supported attributes
	public static final String S_MODEL = "Model";
	public static final String CHILDREN = "Children";
	public static final String IS_CHILD = "isChild";
	public static final String IS_PARENT = "isParent";

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
		DESCRIPTOR,
		TYPE,
		ROOT,
		LEAF,
		CHILD,
		DEPTH;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}	
	}

	/**
	 * Get the id of the model. 
	 * @return
	 */
	public long getID();

	/**
	 * Get the name of the model. 
	 * @return
	 */
	public String getName();

	/**
	 * Get the version of the model. 
	 * @return
	 */
	public String getVersion();

	/**
	 * The type is mandatory, and is used to assign a model to a cluster. 
	 * @return
	 */
	public String getType();

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
	 * Get the scope of the model
	 * @return
	 */
	public IConcept.Scope getScope();
	
	/**
	 * Get the parent of this leaf, or null if the leaf is a root
	 * @return
	 */
	public IModelNode<?> getParent();

	/**
	 * Set the parent
	 * @param parent
	 */
	void setParent( IModelNode<?> parent );

	/**
	 * Get the attribute
	 * @param attr
	 * @param value
	 */
	public String get( Enum<?> attr ); 

	/**
	 * Get the attribute
	 * @param attr
	 * @param value
	 */
	public String get( String attr ); 

	/**
	 * Set a value
	 * @param attr
	 * @param value
	 * @return
	 */
	public void set( Enum<?> attr, String value );

	/**
	 * Set a value
	 * @param attr
	 * @param value
	 * @return
	 */
	public void set( String attr, String value );

	/**
	 * Returns true @Override
	if the node is a root
	 * @return
	 */
	public boolean isRoot();

	/**
	 * Returns true if the model is a leaf ( has no children )
	 * @return
	 */
	public boolean isLeaf();
	
	/**
	 * If the boolean value is set, the node is always considered to be a leaf. 
	 * @param choice
	 */
	public void setLeaf( boolean choice );

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
	
	/**
	 * Get a data object
	 * @return
	 */
	public D getData();
	
	/**
	 * Set a data object
	 * @param descriptor
	 */
	void setData(D descriptor);

	boolean isReadOnly();

	void setReadOnly(boolean choice);

	/**
	 * Get the (optional) identifier of the model. 
	 * @return
	 */
	String getDescriptorId();

	/**
	 * Set the identifier for this leaf
	 * @param descriptor
	 */
	void setDescriptorId(String descriptor);

}
