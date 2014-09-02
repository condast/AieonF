package org.aieonf.util.encryption;

import java.security.InvalidKeyException;
import java.io.InputStream;
import java.io.OutputStream;

import org.aieonf.util.StringStyler;

/**
 * <p>Title: Conceptual Network Database</p>
 *
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Condast</p>
 *
 * @author Kees Pieters
 * @version 1.0
 */
public interface IEncryption
{
	//This enum is a service to aieon that want to pass encrypted
	//information
	public enum Attributes
	{
		ENCRYPTION_KEY,
		ALTERNATIVE_ENCRYPTION_KEY,
		ALGORITHM;

	  	@Override
	  	public String toString()
	  	{
	  		return StringStyler.prettyString( super.toString() );
	  	}

	}
	
	//Supported algorithms
  public enum Algorithms
  {
  	NONE,
  	AES,
  	ARCFOUR,
  	TEAV;
  	
  	@Override
  	public String toString()
  	{
  		return StringStyler.prettyString( super.toString() );
  	}
  }

  //Possible bit lengths
  public enum BitLengths
  {
  	Int8,
  	Int16,
  	Int32, 
  	Int64,
  	Int128,
  	Int192,
  	Int256;
  	
  	@Override
		public String toString()
  	{
  		return this.name().substring( 3 );
  	}
  	
  	public int toInteger()
  	{
  		return Integer.parseInt( this.toString() );
  	}
  }

  /**
   * Get the algorithm used for this encryption
   * @return String
  */
  public Algorithms getAlgorithm();

  /**
   * Encrypt the given input data , using the key
   *
   * @param inputByte byte[]
   * @return byte[]
   * @throws Exception
  */
  public byte[] encryptData( byte[] inputByte ) throws Exception;

  /**
   * Encrypt the given input data , using the key
   *
   * @param input String
   * @return String
   * @throws Exception
  */
  public String encryptData( String input ) throws Exception;

  /**
   * Encrypt the given input data , using the key
   *
   * @param cypherText byte[]
   * @return byte[]
   * @throws Exception
  */
  public byte[] decryptData( byte[] cypherText ) throws Exception;

  /**
   * Encrypt the given input data , using the key
   *
   * @param cypherData String
   * @return String
   * @throws Exception
  */
  public String decryptData( String cypherData ) throws Exception;

  /**
   * Get an input stream that encrypts or decrypts the data from the given
   * inputstreaM using the initialised form of encryption
   *
   * @param mode int
   * @param is InputStream
   * @return InputStream
   * @throws InvalidKeyException
  */
  public InputStream getInputStream( int mode, InputStream is )
    throws InvalidKeyException;

  /**
   * Get an outputstream that encryptos or descryptos the data in the given
   * outputstream using the form of encryption of this manner of encryption
   * @param mode int
   * @param os OutputStream
   * @return OutputStream
   * @throws InvalidKeyException
   */
  public OutputStream getOutputStream( int mode, OutputStream os )
  throws InvalidKeyException;
}
