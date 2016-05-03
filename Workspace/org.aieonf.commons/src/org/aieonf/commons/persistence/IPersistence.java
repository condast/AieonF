package org.aieonf.commons.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Default interface for persistence of an object
 * @author Kees
 *
 */
public interface IPersistence<T extends Object>
{
	/**
	 * open the persistence 
	*/
	public void open();
	
	/**
	 * returns true if persistence is open
	 * @return
	*/
	public boolean isOpen();
	
	/**
	 * Close the persistence 
	*/
	public void close();
	
	/**
	 * Read an object from the given location
	 * @param location String
	 * @return T
	 * @throws IOException
	*/
	public T read( InputStream in ) throws IOException;
	
	/**
	 * Persist an object 
	 * @param object T
	 * @param out OutputStream
	 * @throws IOException
	*/
	public void write( T object, OutputStream out ) throws IOException;	
}
