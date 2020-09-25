package org.aieonf.commons.ui.controller;

public interface IEditListener<T extends Object> {

	public void notifyInputEdited( EditEvent<T> event );
}
