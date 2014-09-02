package org.aieonf.util.parser;

public interface IParserListener<T extends Object>
{

	/**
	 * notification that an object was parsed
	 * @param templateNode
	 */
	public void notifyParsed( T obj );
}
