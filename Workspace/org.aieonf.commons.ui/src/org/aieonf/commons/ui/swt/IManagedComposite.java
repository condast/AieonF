package org.aieonf.commons.ui.swt;

public interface IManagedComposite<T extends Object> extends IManagedProvider<T> {

	boolean checkRequiredFields();

	/**
	 * If update is set, it means that the 
	 * @param update
	 */
	void setUpdate(boolean update);
	
	T createEntry();

	void saveEntry();

	void dispose();
}
