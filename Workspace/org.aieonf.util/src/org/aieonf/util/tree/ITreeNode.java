package org.aieonf.util.tree;

import java.util.List;

public interface ITreeNode<T> 
{
	/**
	 * Get the value that is contained in the tree node
	 * @return
	*/
	public T getValue();
	
	/**
	 * Get the depth of the tree node from the root
	 * @return
	*/
	public int getDepth();
	
	/**
	 * Set the depth of the tree node
	 * @param depth
	*/
	public void setDepth( int depth );
	
	/**
	 * Add a child
	 * @param child
	*/
	public void addChild( ITreeNode<?> child );
	
	/**
	 * Get the children of the tree node
	 * @return
	*/
	public List<ITreeNode<?>> getChildren();
	
	/**
	 * The amount of children in the tree node
	 * @return
	 */
	public int nrOfChildren();
}
