package org.aieonf.commons.ui.swt;

public interface ISelectionProvider {

	/**
	 * Add a selection listener. This propgates a selection event from an SWT widget
	 * @param listener
	 */
	public void addEditListener( IEditListener<?> listener );

	/**
	 * remove the selection listener
	 * @param listener
	 */
	public void removeEditListener( IEditListener<?> listener );
}
