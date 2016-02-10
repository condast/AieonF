package org.aieonf.util.builder;

public interface IBuilder<T extends Object>
{
	/**
	 * A builder extends a filter with a means to build something while the objects are parsed
	 * @throws BuilderException
	 */
	public void build( T object ) throws BuilderException;
}
