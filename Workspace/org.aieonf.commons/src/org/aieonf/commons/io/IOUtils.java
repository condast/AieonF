package org.aieonf.commons.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IOUtils
{

	/**
	 * Convert the content of the inputstream to a byte array
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArray( InputStream in ) throws IOException{
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[16384];

		while ((nRead = in.read(data, 0, data.length)) != -1) {
		  buffer.write(data, 0, nRead);
		}

		buffer.flush();

		return buffer.toByteArray();
	}

	/**
	 * Convert the content of the inputstream to a String, using the given charset
	 * @param in
	 * @param charsetName
	 * @return
	 * @throws IOException
	 */
	public static String toString( InputStream in, String charsetName ) throws IOException{
		return new String( toByteArray( in ), charsetName );
	}

	/**
	 * Close an input  stream
	 *
	 * @param stream OutputStream
	*/
	public static void closeQuietly( Closeable closeable )
	{
	  Logger logger = Logger.getLogger( IOUtils.class.getName() );
	  if( closeable == null )
	    return;
	
	  try{
		  closeable.close();
	  }
	  catch( IOException ioex ){
	    logger.log( Level.SEVERE, ioex.getMessage(), ioex );
	  }
	}

	/**
	 * Close a jar file
	 * @param jar
	 */
	public static void closeJarFile( JarFile jar )
	{
	  Logger logger = Logger.getLogger( IOUtils.class.getName() );
	  if( jar == null )
	    return;
	
	  try{
	    jar.close();
	  }
	  catch( IOException ioex ){
	    logger.log( Level.SEVERE, ioex.getMessage(), ioex );
	  }
	}

	/**
	 * Close an input  stream
	 * @Use closeQuietly
	 * @param stream OutputStream
	*/
	@Deprecated
	public static void closeInputStream( InputStream stream )
	{
	  Logger logger = Logger.getLogger( IOUtils.class.getName() );
	  if( stream == null )
	    return;
	
	  try{
	    stream.close();
	  }
	  catch( IOException ioex ){
	    logger.log( Level.SEVERE, ioex.getMessage(), ioex );
	  }
	}

	/**
	 * Close an output  stream
	 * @Use closeQuietly
	 *
	 * @param stream OutputStream
	*/
	@Deprecated
	public static void closeOutputStream( OutputStream stream )
	{
	  Logger logger = Logger.getLogger( IOUtils.class.getName() );
	  if( stream == null )
	    return;
	
	  try{
	    stream.flush();
	    stream.close();
	  }
	  catch( IOException ioex ){
	    logger.log( Level.SEVERE, ioex.getMessage(), ioex );
	  }
	}

	/**
	 * Close a reader
	 * @Use closeQuietly
	 *
	 * @param stream Reader
	*/
	@Deprecated
	public static void closeReader( Reader reader )
	{
	  Logger logger = Logger.getLogger( IOUtils.class.getName() );
	  if( reader == null )
	    return;
	
	  try{
	    reader.close();
	  }
	  catch( IOException ioex ){
	    logger.log( Level.SEVERE, ioex.getMessage(), ioex );
	  }
	}

	/**
	 * Close a writer
	 * @Use closeQuietly
	 *
	 * @param stream Writer
	*/
	@Deprecated
	public static void closeWriter( Writer writer )
	{
	  Logger logger = Logger.getLogger( IOUtils.class.getName() );
	  if( writer == null )
	    return;
	
	  try{
	    writer.flush();
	    writer.close();
	  }
	  catch( IOException ioex ){
	    logger.log( Level.SEVERE, ioex.getMessage(), ioex );
	  }
	}

	/**
	 * Prints the contents of an input stream, and closes it if requested.
	 * 
	 * @param in
	 */
	public static InputStream printStreamContents( InputStream in ){
		Logger logger = Logger.getLogger(IOUtils.class.getName());
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int bytesRead;
    try {
			while ((bytesRead = in.read(buffer)) != -1)
			{
			   bout.write(buffer, 0, bytesRead);
			}
		}
		catch (IOException e) {
			logger.severe( e.getMessage() );
		}
    finally{
    	IOUtils.closeQuietly(in);
    	IOUtils.closeQuietly(bout);
    }
    if( bout == null )
    	return in;
    
		String str = new String( bout.toByteArray() );
		str = str.replaceAll("><", ">\n<");
    logger.info( "CONTENTS INPUTSTREAM: \n" + str );
		return new ByteArrayInputStream( bout.toByteArray());
	}
}
