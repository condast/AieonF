package org.aieonf.util.transaction;

/**
 * A transaction is assigned to a data object, and is open after it is created, until the close method is called
 * @author Kees
 *
 * @param <T>
 */
public interface ITransaction<T extends Object> {

	public void create( T data);
	
	public T getData();
	
	public boolean isOpen();
	
	public void close();
}
