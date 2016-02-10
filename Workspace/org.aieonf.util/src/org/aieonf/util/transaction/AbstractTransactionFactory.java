package org.aieonf.util.transaction;

import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractTransactionFactory<T extends Object, U extends Object> {

	private Collection<ITransaction<T,U>> transactions;

	protected AbstractTransactionFactory() {
		this.transactions = new ArrayList<ITransaction<T,U>>();
	}
	
	protected abstract ITransaction<T,U> onCreateTransaction( T data );

	/**
	 * Create a new transaction
	 * @param data
	 * @return
	 */
	public ITransaction<T,U> createTransaction( T data ){
		ITransaction<T,U> transaction = this.onCreateTransaction(data);
		this.transactions.add( transaction );
		return transaction;
	}
	
	/**
	 * Close the transaction
	 * @param transaction
	 */
	public void close( ITransaction<T,U> transaction ){
		transactions.remove( transaction );
		transaction.close();
	}
}
