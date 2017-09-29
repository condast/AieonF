package org.aieonf.commons.ui.swt;

public interface IEditListener<T extends Object> {

	public void notifyInputEdited( EditEvent<T> event );
}
