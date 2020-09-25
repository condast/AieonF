package org.aieonf.commons.ui.controller;

import org.eclipse.swt.events.VerifyListener;

public interface IManagedProvider<T extends Object> extends ISelectionProvider{

	/**
	 * Add a verification listener, that verifies widget input
	 * @param listener
	 */
	public void addVerifyListener(VerifyListener listener);

	public void removeVerifyListener(VerifyListener listener);
		
	/**
	 * Get the input of the composite
	 * @return
	 */
	public T getInput();
	
	/**
	 * Set the input of the composite. If overwrite is true, the widgets 
	 * will get the values of the input
	 * @param input
	 * @param overwrite
	 */
	public void setInput( T input, boolean overwrite );

	/**
	 * Notify listeners that the input has been edited
	 * @param event
	 */
	void notifyInputEdited(EditEvent<T> event);
}
