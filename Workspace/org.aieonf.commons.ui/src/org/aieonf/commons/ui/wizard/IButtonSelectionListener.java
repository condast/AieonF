package org.aieonf.commons.ui.wizard;

public interface IButtonSelectionListener<T extends Object> {

	public void notifyButtonPressed( ButtonEvent<T> event );

}
