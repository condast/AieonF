package org.aieonf.commons.tree;

import java.util.ArrayList;
import java.util.List;

public class TreeNode<T> implements ITreeNode<T> 
{
	// The value contained by this tree node
	private T value;
	
	//The depth of the tree from the root
	private int depth;
	
	private List<ITreeNode<?>> children;

	/**
	 * Create the tree node;
	 * @param value
	*/
	public TreeNode()
	{
		this.children = new ArrayList<ITreeNode<?>>();
	}

	/**
	 * Create the tree node;
	 * @param value
	*/
	public TreeNode( T value )
	{
		this();
		this.value = value;
	}

	/**
	 * Get the value of this node
	*/
	@Override
	public T getValue() 
	{
		return this.value;
	}

	/**
	 * Get the depth of the tree node from the root
	 * @return
	*/
	@Override
	public int getDepth()
	{
		return this.depth;
	}

	/**
	 * Set the depth of the tree node
	 * @param depth
	*/
	@Override
	public void setDepth( int depth )
	{
		this.depth = depth;
	}

	/**
	 * Set the value for this node
	 * @param value
	*/
	public void setValue( T value )
	{
		this.value = value;
	}
	
	/**
	 * Add a child to the tree node and update the depth
	 * @param child ITreeNode
	*/
	@Override
	public void addChild( ITreeNode<?> child) 
	{
		this.children.add( child );
		child.setDepth( this.depth + 1 );
	}

	/**
	 * Get the children of the tree node.
	 */
	@Override
	public List<ITreeNode<?>> getChildren() 
	{
		return new ArrayList<ITreeNode<?>>( this.children );
	}

	/**
	 * Get the number of children for this node
	*/
	@Override
	public int nrOfChildren() 
	{
		return this.children.size();
	}
}
