package org.aieonf.osgi.controller;

public interface IEditListener<T extends Object> {

	public void notifyInputEdited( EditEvent<T> event );
}
