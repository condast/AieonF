package org.aieonf.util.transaction;

import java.util.Collection;

/**
 * A transaction is assigned to a data object, and is open after it is created, until the close method is called
 * @author Kees
 *
 * @param <T>
 */
public interface ITransaction<T extends Object, U extends Object> {

	public void create();
	
	/**
	 * A transaction usually starts with an operation on the provider, e.g search or add. 
	 * The data of the operation is added for further processing
	 * @return
	 */
	public Collection<T> getData();
	
	public void addData( T data );
	
	/**
	 * Set a selected data item. Returns false if the data item does not exist in the collection
	 * @param data
	 * @return
	 */
	public boolean setSelected( T data );
	
	/**
	 * Get the selected data item
	 * @return
	 */
	public T getSelected();
	
	public boolean isOpen();
	
	public U getProvider();
	
	public void close();
}
