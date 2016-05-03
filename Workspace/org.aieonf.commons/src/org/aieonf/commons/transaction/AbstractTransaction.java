package org.aieonf.commons.transaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractTransaction<T extends Object, U extends Object> implements ITransaction<T,U> {

	private List<T> data;
	private int selection;
	private boolean open = false;
	private U provider;
	
	
	public AbstractTransaction( U provider ) {
		data = new ArrayList<T>();
		this.provider = provider;
		this.selection = 0;
	}

	protected abstract boolean onCreate( U provider );
	
	/**
	 * Perform the steps necessary to create the transaction. Return true if everything went succesfully
	 * @param data
	 * @return
	 */
	@Override
	public void create() {
		open = this.onCreate( provider );
	}

	@Override
	public void addData( T dt ) {
		data.add( dt );
	}
	
	@Override
	public boolean setSelected(T datum) {
		this.selection = data.indexOf( datum );
		if( selection < 0 ){
			selection = 0;
			return false;
		}
		return true;
	}

	@Override
	public T getSelected() {
		return data.get( selection);
	}

	@Override
	public U getProvider() {
		return provider;
	}

	@Override
	public boolean isOpen() {
		return open;
	}

	@Override
	public Collection<T> getData() {
		return data;
	}

	@Override
	public void close() {
		this.open = false;
		this.data = null;
	}

}
