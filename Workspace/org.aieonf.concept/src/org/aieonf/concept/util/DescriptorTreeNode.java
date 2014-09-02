package org.aieonf.concept.util;

import java.util.*;

import org.aieonf.concept.*;

public class DescriptorTreeNode 
{
	//The root descriptor of this tree node
	private IDescriptor descriptor;
	
	//The relationship that determines the pinpoints the node as a node
	private IRelationship relationship;
	
	//The list if children
	private List<DescriptorTreeNode> children;
	
	/**
	 * Create a concpet tree node for the given descriptor.
	 * Note that this structure does NOT follow the default structure
	 * of a concept, in order to allow other parent-child relationships.
	 * The provided static function creates the default structure 
	 * @param concept
	*/
	public DescriptorTreeNode( IDescriptor descriptor )
	{
		this.descriptor = descriptor;
		this.relationship = null;
		this.children = new ArrayList<DescriptorTreeNode>();
	}
	
	/**
	 * Get the descriptor
	 * @return
	*/
	public IDescriptor getDescriptor()
	{
		return this.descriptor;
	}
	
	/**
	 * Get the relationship of the descriptor, or null if there aren't any
	 * @return IRelationship
	*/
	public IRelationship getRelationship()
	{
		return this.relationship;
	}

	/**
	 * Set the relationships of the concept
	 * @param relationships
	 */
	public void setRelationship( IRelationship relationship )
	{
		this.relationship = relationship;
	}
	
	/**
	 * Get the children of this descriptor node 
	 * @return
	*/
	public List<DescriptorTreeNode> getChildren()
	{
		return this.children;
	}
	
	/**
	 * Set the children of this descriptor node
	 * @param concepts
	 */
	public void setChildren( List<? extends IDescriptor> descriptors )
	{
		this.children.clear();
		for( IDescriptor descriptor: descriptors )
			this.children.add( new DescriptorTreeNode( descriptor ));
	}
}
