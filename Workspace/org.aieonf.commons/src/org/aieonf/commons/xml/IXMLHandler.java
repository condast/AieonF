package org.aieonf.commons.xml;

public interface IXMLHandler<T extends Object> {

	void addListener(IBuildListener<T> listener);

	void removeListener(IBuildListener<T> listener);

	T getUnit(String id);

	T[] getUnits();
}