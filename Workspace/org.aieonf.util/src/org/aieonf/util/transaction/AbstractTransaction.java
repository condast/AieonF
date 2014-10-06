package org.aieonf.util.transaction;

public abstract class AbstractTransaction<T extends Object> implements ITransaction<T> {

	private T data;
	private boolean open = false;
	
	/**
	 * Perform the steps necessary to create the transaction. Return true if everything went succesfully
	 * @param data
	 * @return
	 */
	protected abstract boolean onCreated( T data );
	
	@Override
	public void create(T data) {
		this.data = data;
		open = this.onCreated(data);
	}

	@Override
	public T getData() {
		return data;
	}

	@Override
	public boolean isOpen() {
		return open;
	}

	@Override
	public void close() {
		this.open = false;
		this.data = null;
	}

}
