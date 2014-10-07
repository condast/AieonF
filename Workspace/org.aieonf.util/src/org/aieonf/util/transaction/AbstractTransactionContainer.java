package org.aieonf.util.transaction;

import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractTransactionContainer<T extends Object> {

	private Collection<ITransaction<T>> transactions;

	protected AbstractTransactionContainer() {
		this.transactions = new ArrayList<ITransaction<T>>();
	}
	
	protected abstract ITransaction<T> onCreateTransaction( T data );

	/**
	 * Create a new transaction
	 * @param data
	 * @return
	 */
	public ITransaction<T> createTransaction( T data ){
		ITransaction<T> transaction = this.onCreateTransaction(data);
		this.transactions.add( transaction );
		return transaction;
	}
	
	/**
	 * Close the transaction
	 * @param transaction
	 */
	public void close( ITransaction<T> transaction ){
		transactions.remove( transaction );
		transaction.close();
	}
}
