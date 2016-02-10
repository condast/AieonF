package org.aieonf.util.hex;

import org.aieonf.util.StringStyler;

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
public class HexConvertor
{

	public enum Convert{
		CONVERT_TO_HEX, 
		CONVERT_FROM_HEX;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}

	/**
	 * Converts a byte string to a hexadecimal representation as String format
	 *
	 * @param bytes byte[]
	 * @return String
	 */
	public static String convertHex( byte[] bytes )
	{
		StringBuffer str = new StringBuffer();
		byte hi, low;
		for( int i = 0; i < bytes.length; i++ ){
			hi = ( byte )(( bytes[ i ] & 0x00F0 ) >>> 4 );
			low = ( byte )( bytes[ i ] & 0x000F );
			str.append( Integer.toHexString( hi ) + Integer.toHexString( low ));
		}
		return str.toString();
	}

	/**
	 * Returns true if the given char is a hex char
	 *
	 * @param chr char
	 * @return boolean
	 */
	protected static boolean isHexChar( char chr )
	{
		if(( chr >= '0' ) && ( chr <= '9' ))
			return true;

		if(( chr >= 'A' ) && ( chr <= 'F' ))
			return true;

		return (( chr >= 'a' ) && ( chr <= 'f' ));
	}

	/**
	 * Converts a char corresponding to hexadecimal numbers into a byte
	 *
	 * @param chr char
	 * @return byte
	 * @throws NumberFormatException
	 */
	protected static byte convertHexChar( char chr ) throws NumberFormatException
	{
		if(( chr >= '0' ) && ( chr <= '9' )){
			return Byte.decode( String.valueOf( chr )).byteValue();
		}
		if(( chr >= 'A' ) && ( chr <= 'F' )){
			return ( byte )( chr + 10 - 'A' );
		}
		if(( chr >= 'a' ) && ( chr <= 'f' )){
			return ( byte )( chr + 10 - 'a' );
		}
		else
			throw new NumberFormatException( "Number is not a Hexadecimal" );
	}

	/**
	 * converts a hex string into bytes. Throws an exception if non-hex
	 * characters has been detected
	 *
	 * @param str String
	 * @return byte[]
	 * @throws NumberFormatException
	 */
	public static byte[] convertBytes( String str ) throws NumberFormatException
	{
		str = str.toUpperCase();
		byte[] bytes = new byte[ str.length() / 2 ];
		byte b = 0;
		boolean firstChar = true;
		for( int i = 0; i < str.length(); i++ ){
			if( firstChar ){
				b = convertHexChar( str.charAt( i ) );
				b = ( byte )( b << 4 );
			}else{
				b += convertHexChar( str.charAt( i ) );
				bytes[ i/2 ] = b;
			}
			firstChar = !firstChar;
		}
		return bytes;
	}

	/**
	 * Convert a source string to or from hex, depending on the selected mode
	 * @param mode int
	 * @param source String
	 * @return String
	 */
	public static String convert( Convert mode, String source )
	{
		switch( mode ){
		case CONVERT_TO_HEX:
			return HexConvertor.convertHex( source.getBytes() );
		default:
			byte[] bytes = HexConvertor.convertBytes( source );
			StringBuffer buf = new StringBuffer();
			for( byte bt: bytes )
				buf.append(( char )bt );
			return buf.toString();
		}
	}

	/**
	 * Returns true if the given String is a hex string
	 *
	 * @param str String
	 * @return byte[]
	 * @throws NumberFormatException
	 */
	public static boolean isHexString( String str )
	{
		str = str.toUpperCase();
		for( int i = 0; i < str.length(); i++ ){
			if( HexConvertor.isHexChar( str.charAt( i )) == false )
				return false;
		}
		return true;
	}

	/**
	 * Gets an integer representation of the hex string.
	 *
	 * @param hexString String
	 * @return int
	 */
	public static int toInteger( String hexString )
	{
		return Integer.decode( "0x" + hexString );
	}

	/**
	 * Gets an long representation of the hex string.
	 *
	 * @param hexString String
	 * @return long
	 * @throws NumberFormatException
	 */
	public static long toLong( String hexString )
	{
		return Long.decode( "0x" + hexString );
	}

	/**
	 * Print the bytes in an array
	 * @param bytes byte[]
	 * @return String
	 */
	public static String printBytes( byte[] bytes )
	{
		String str = "[";
		for( int i = 0; i < bytes.length; i++ ){
			str += bytes[ i ];
			if( i < bytes.length - 1 )
				str += ", ";
		}
		return str + "]";
	}

	/**
	 * Print the bytes in an array
	 * @param bytes byte[]
	 * @return String
	 */
	public static String getString( byte[] bytes )
	{
		String str = "";
		for( int i = 0; i < bytes.length; i++ )
			str += ( char )bytes[ i ];
		return str;
	}
}