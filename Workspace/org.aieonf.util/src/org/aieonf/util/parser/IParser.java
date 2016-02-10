package org.aieonf.util.parser;

import java.io.IOException;
import java.io.InputStream;

import org.w3c.dom.Document;

public interface IParser<T extends Object>
{
	/**
	 * Parse an object from the given input stream
	 * @param in
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public T parse( InputStream in ) throws IOException, ParseException;
	
  /**
   * Creates a document from the given object
   *
   * @return Document
   * @throws ParseException 
  */
  public Document createDocument( T object ) throws ParseException;
  
	/**
	 * @param listeners the listener to add
	 */
	public void addListener( IParserListener<T> listener);

	/**
	 * @param listeners the listener to add
	 */
	public void removeListener( IParserListener<T> listener);

}
