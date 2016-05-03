package org.aieonf.commons.hex;

import java.io.*;

import org.aieonf.commons.hex.HexConvertor.Convert;

/**
 * <p>Title: Util</p>
 *
 * <p>Description: This package consists of classes that are used~nto implement
 * a run time environment, objetc ids and so on.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Condast</p>
 *
 * @author Kees Pieters
 * @version 1.0
 */
public class HexWriter extends Writer
{
	//The mode hex <=>string
	private Convert mode;

	//Use a string writer for the actual work
	private StringWriter writer;

	/**
	 * Create a writer that converts Strings to hex strings
	 * @param mode int
	 */
	public HexWriter( Convert mode )
	{
		super();
		this.mode = mode;
		this.writer = new StringWriter();
	}

	/**
	 * Create a writer that converts Strings to hex strings.
	 * Use the given lock to synchronise access
	 *
	 * @param mode int
	 * @param lock Object
	 */
	public HexWriter( Convert mode, Object lock )
	{
		super( lock );
		this.mode = mode;
		this.writer = new StringWriter();
	}

	/**
	 * close the writer
	 *
	 * @throws IOException
	 */
	@Override
	public void close() throws IOException
	{
		this.writer.close();
	}

	/**
	 * flush the writer
	 *
	 * @throws IOException
	 */
	@Override
	public void flush() throws IOException
	{
		this.writer.flush();
	}

	/**
	 * write the given char array from the given offset and the given length
	 *
	 * @param charArray char[]
	 * @param offset int
	 * @param length int
	 * @throws IOException
	 */
	@Override
	public void write( char[] charArray, int offset, int length ) throws IOException
	{
		String hex;
		String str = new String( charArray );
		if( Convert.CONVERT_TO_HEX.equals( this.mode ))
			hex = HexConvertor.convertHex( str.getBytes() );
		else
			hex = String.valueOf( HexConvertor.convertBytes( str ));

		this.writer.write( hex, offset, length );
		return;
	}
}