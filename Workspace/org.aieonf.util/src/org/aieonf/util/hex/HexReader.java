package org.aieonf.util.hex;

import java.io.*;

import org.aieonf.util.hex.HexConvertor.Convert;

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
public class HexReader extends Reader
{
	//The mode hex <=>string
	private Convert mode;

	public HexReader( Convert mode, String str )
	{
		super();
		this.mode = mode;
	}

	public HexReader( Convert mode, String str, Object object )
	{
		super( object );
		this.mode = mode;
	}

	/**
	 * close
	 *
	 * @throws IOException
	 */
	@Override
	public void close() throws IOException
	{
	}

	/**
	 * read
	 *
	 * @param charArray char[]
	 * @param offset int
	 * @param length int
	 * @return int
	 * @throws IOException
	 */
	@Override
	public int read( char[] charArray, int offset, int length ) throws IOException
	{
		String str = new String( charArray );
		StringReader reader;
		if( Convert.CONVERT_TO_HEX.equals( this.mode ))
			reader = new StringReader( HexConvertor.convertHex( str.getBytes( )));
		else
			reader = new StringReader( HexConvertor.getString( str.getBytes( )));
		return reader.read( charArray, offset, length );
	}
}
