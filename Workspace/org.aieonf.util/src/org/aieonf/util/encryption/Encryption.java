package org.aieonf.util.encryption;

//J2SE imports
import java.io.*;
import java.security.*;

import javax.crypto.*;
import javax.crypto.spec.*;

import org.aieonf.util.hex.HexConvertor;
import org.aieonf.util.logger.*;

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
public class Encryption implements IEncryption
{
	//Error messages
	public static final String S_ERR_NO_ALGORITHM = 
		"No algorithm is provided with the encryption";
	public static final String S_ERR_ALGORITHM_NOT_SUPPORTED = 
		"This algorithm is not supported for the requested operation: ";
	public static final String S_ERR_NO_KEY = 
		"No key is provided with the encryption";
	
  //The algorithm that is used
  private Algorithms algorithm;

  //The cipher manages encryption and decryption
  private Cipher cipher;
  private SecretKeySpec skeySpec;

  /**
   * Create a default encryption using Rijndael (AES)
   * @param key String
   * @throws NoSuchPaddingException, NoSuchAlgorithmException
  */
  public Encryption( String key ) 
  	throws NoSuchPaddingException, NoSuchAlgorithmException
  {
  	this( Algorithms.AES, key );
  }

  /**
   * Encrypt data using a predefined algorithm
   * @param algorithm String
   * @param key String
   * @throws NoSuchPaddingException, NoSuchAlgorithmException
  */
  public Encryption( Algorithms algorithm, String key ) 
  	throws NoSuchPaddingException, NoSuchAlgorithmException
  {
    if( algorithm == null )
    	throw new NullPointerException( S_ERR_NO_ALGORITHM );
    this.algorithm = algorithm;
    if(( !algorithm.equals( Algorithms.NONE )) && ( key == null ))
    	throw new NullPointerException( S_ERR_NO_KEY );
    this.skeySpec = new SecretKeySpec( key.getBytes(), this.algorithm.name() );
    if( !this.algorithm.equals( IEncryption.Algorithms.NONE ))
      this.cipher = Cipher.getInstance( this.algorithm.name() );
  }
  
  /**
   * Get the algorithm used for this encryption
   * @return String
  */
  @Override
	public Algorithms getAlgorithm()
  {
    return this.algorithm;
  }

  /**
   * Encrypt the String
   * @param input String
   * @return String
   * @throws Exception
  */
  @Override
	public String encryptData( String input ) throws Exception
  {
  	if( this.algorithm.equals( IEncryption.Algorithms.NONE ))
  		return HexConvertor.convertHex( input.getBytes() );
  	return HexConvertor.convertHex( this.encryptData( input.getBytes() ));
  }

  /**
   * Encrypt the given byte array
   *
   * @param inputBytes byte[]
   * @return byte[]
   * @throws Exception
  */
  @Override
	public byte[] encryptData( byte[] inputBytes ) throws Exception
  {
   	if( this.algorithm.equals( IEncryption.Algorithms.NONE ))
   		return inputBytes;
  	cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    return cipher.doFinal( inputBytes );
  }

  /**
   * Decrypt the given byte array
   * @param cypherText byte[]
   * @return byte[]
   * @throws Exception
  */
  @Override
	public byte[] decryptData( byte[] cypherText ) throws Exception
  {
   	if( this.algorithm.equals( IEncryption.Algorithms.NONE ))
   		return cypherText;
    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
    return cipher.doFinal( cypherText );
  }

  /**
   * Decrypt the given string
   * @param cypherData String
   * @return String
   * @throws Exception
   * @todo Implement this org.condast.concept.database.security.IEncryption
   *   method
  */
  @Override
	public String decryptData( String cypherData ) throws Exception
  {
  	byte[] convert = HexConvertor.convertBytes( cypherData );
  	return new String( this.decryptData( convert ));
  }

  /**
   * Get an input stream that encrypts or decrypts the data from the given
   * inputstream using the initialised form of encryption
   *
   * @param mode int (Cipher.DECRYPT_MODE, Cipher.ENCRYPT_MODE)
   * @param is InputStream
   * @return InputStream
   * @throws InvalidKeyException
  */
  @Override
	public InputStream getInputStream( int mode, InputStream is )
    throws InvalidKeyException
  {
   	if( this.algorithm.equals( IEncryption.Algorithms.NONE ))
   		return is;
  	cipher.init( mode, skeySpec);
    return new CipherInputStream( is, this.cipher );
  }


  /**
   * Get an outputstream that encrypts or descrypts the data in the given
   * outputstream using the form of encryption of this manner of encryption
   * @param mode int
   * @param os OutputStream
   * @return OutputStream
   * @throws InvalidKeyException
   */
  @Override
	public OutputStream getOutputStream( int mode, OutputStream os )
  throws InvalidKeyException
  {
   	if( this.algorithm.equals( IEncryption.Algorithms.NONE ))
   		return os;
    cipher.init( mode, skeySpec);
    return new CipherOutputStream( os, this.cipher );
  }

  /**
   * Create the key for this encryption using 128-bits AES encryption
   *
   * @return String
   * @throws NoSuchAlgorithmException
  */
  public static String getKey() throws NoSuchAlgorithmException
  {
     return Encryption.getKey( Algorithms.AES, 128 );
  }

  /**
   * Create the key for this encryption using the given encryption
   *
   * @param algorithm String
   * @param length int
   * @return String
   * @throws NoSuchAlgorithmException
  */
  public static String getKey( Algorithms algorithm, int length ) throws NoSuchAlgorithmException
  {
    if( algorithm.equals( IEncryption.Algorithms.NONE ))
    	throw new NoSuchAlgorithmException( S_ERR_ALGORITHM_NOT_SUPPORTED + algorithm.name() );
  	Logger logger = Logger.getLogger( Encryption.class );

    // Get the KeyGenerator
    KeyGenerator kgen = KeyGenerator.getInstance( algorithm.name() );
    kgen.init( length ); // 192 and 256 bits may not be available

    // Generate the secret key specs.
    SecretKey skey = kgen.generateKey();
    byte[] bytes = skey.getEncoded();
    String str = "[";
    for( int i = 0; i < bytes.length; i++ ){
      str += bytes[ i ] + ", ";
    }
    str += "]";
    logger.trace( "Key created: " + str );
    return HexConvertor.convertHex( skey.getEncoded() );
  }

  /**
   * Get the secret key for this application, using the given key string
   *
   * @return String
  */
  public static String getRandomSecretKeyString( Algorithms algorithm )
  	throws Exception
  {
    if( algorithm.equals( IEncryption.Algorithms.NONE ))
    	throw new NoSuchAlgorithmException( S_ERR_ALGORITHM_NOT_SUPPORTED + algorithm.name() );

    byte[] keyString = null;
    if( algorithm.equals( Algorithms.AES ))
    	return Encryption.getKey();
    if( algorithm.equals( Algorithms.ARCFOUR ))
    	keyString = new byte[ BitLengths.Int8.toInteger() ];
    if( algorithm.equals( Algorithms.TEAV ))
    	keyString = new byte[ BitLengths.Int32.toInteger() ];
    for( int i = 0; i < keyString.length; i++ )
      keyString[ i ] = ( byte )( Math.random() * Byte.MAX_VALUE );
    return HexConvertor.convertHex( keyString );
  }
}
