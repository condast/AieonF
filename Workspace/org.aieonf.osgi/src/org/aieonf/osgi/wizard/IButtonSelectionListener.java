package org.aieonf.osgi.wizard;

public interface IButtonSelectionListener<T extends Object> {

	public void notifyButtonPressed( ButtonEvent<T> event );

}
