package org.aieonf.commons.ui.swt;

public interface ISelectionProvider<D extends Object> {

	/**
	 * Add a selection listener. This propgates a selection event from an SWT widget
	 * @param listener
	 */
	public void addEditListener( IEditListener<D> listener );

	/**
	 * remove the selection listener
	 * @param listener
	 */
	public void removeEditListener( IEditListener<D> listener );
}
