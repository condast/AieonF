package org.aieonf.commons.ui.widgets;

public interface IStoreWithDelete<T extends Object> {

	String getText(int index);

	void addText(String text);

	int getIndex();

	int getCount();

	boolean isDelete();

	T getStore();

}