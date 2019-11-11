package org.aieonf.collections.persistence;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.NoSuchPaddingException;

import org.aieonf.commons.encryption.Encryption;
import org.aieonf.commons.encryption.IEncryption;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.Universe;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.concept.security.AieonFEncryption;

public class EncryptedLocationManager extends LocationManager
{
	//Error messages
	public static final String S_ERR_FAILED_TO_ENCRYPT = "Failed to encrypt";
  public static final String S_ERR_CONCEPT_INVALID_MANIFEST =
      "Could not instantiate the collection because the manifest aieon is invalid";
		
	//The loader contains encryption info
	private ILoaderAieon loader;
	
	public EncryptedLocationManager( ILoaderAieon loader ) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException
	{
		super( loader );
		this.loader = loader;
	}

	/**
	 * Set the loader aieon
	 * @param loader
	 */
	public void setLoader( ILoaderAieon loader )
	{
		this.loader = loader;
	}

	/**
	 * Get the descriptor corresponding to the given location
	 * @param location
	 * @return
	 * @throws ConceptException
	*/
	@Override
	public IDescriptor getDescribable( String location ) throws ConceptException
	{
    try{
    	IDescriptor descriptor = super.getDescribable( location );
      String name = getDecryptedFileName( loader, descriptor.getName() );
      long id = descriptor.getID();
    	IDescriptor newDescriptor = new Descriptor( id, name );
      newDescriptor.setVersion( descriptor.getVersion() );
      return newDescriptor;
    }
    catch( Exception ex ){
      throw new ConceptException( ex.getMessage(), ex );
    }
	}

	
	/**
	 * Locate the given descriptor
	 * @param descriptor IDescriptor
	 * @return
	 * @throws ConceptException
	*/
	@Override
	public String locate(IDescribable<?> changeable) throws ConceptException
	{
    IDescriptor descriptor = changeable.getDescriptor();
    try
    {
    	String firstChar = String.valueOf( descriptor.getName().charAt( 0 ));
      return getEncryptedFile( this.loader, firstChar, descriptor );
    }
    catch( Exception e ){
      throw new ConceptException( descriptor.toString(), S_ERR_FAILED_TO_ENCRYPT, e );
    }
	}

	/**
   * Returns true if the file is consistent with the given name
   *
   * @param name String
   * @param ePosition String
   * @return boolean
   * @throws ConceptException
  */
	/*
	@Override
  public boolean isCorrectPosition( IDescriptor descriptor, String ePosition )
  	throws ConceptException
  {
    try{
    	this.logger.trace( "Loader: " + loader.getSource() + " = " + loader.getEncryptionKey() );
    	String encryptName = EncryptedLocationManager.getEncryptedFileName( loader, name );
      this.logger.trace( "\n\t Comparing name: " + name + "=\t stored: " +
                               encryptName + "==>\t" + ePosition );
      return ePosition.endsWith( encryptName );
    }
    catch( Exception ex ){
      throw new ConceptException( ex.getMessage(), ex );
    }
  }
*/
	
  /**
   * Returns the encrypted file name belonging to a concept
   *
   * @param key SecretKey
   * @param name String
   * @return String
   * @throws Exception
  */
  public static String getEncryptedFileName( ILoaderAieon manifest, String name )
    throws Exception
  {
    name = name.toLowerCase();
    if( manifest == null )
    	throw new NullPointerException( S_ERR_CONCEPT_INVALID_MANIFEST );
    if( manifest.getEncryptionKey() == null )
    	throw new NullPointerException( Encryption.S_ERR_NO_KEY );
    if( manifest.getEncryptionAlgorithm() == null )
    	throw new NullPointerException( Encryption.S_ERR_NO_ALGORITHM );
    IEncryption encryption = new AieonFEncryption( manifest );
    String str = name.charAt( 0 ) + encryption.encryptData( name );
    return str;
  }

  /**
   * Returns the decrypted file name belonging to a concept
   *
   * @param key SecretKey
   * @param name String
   * @return String
   * @throws Exception
  */
  public static String getDecryptedFileName( ILoaderAieon loader, String name )
    throws Exception
  {
    name = name.toLowerCase();
    IEncryption encryption = new AieonFEncryption( loader );
    String encr = name.substring( 1 );
    return encryption.decryptData( encr );
  }

  /**
   * Return the file belonging to a concept, or null if it wasn't found
   *
   * @param key SecretKey
   * @param position String
   * @param descriptor IDescriptor
   * @return String
   * @throws Exception
  */
  public static String getEncryptedFile( ILoaderAieon loader, String position, IDescribable<?> changeable )
    throws Exception
  {
    IDescriptor descriptor = changeable.getDescriptor();
  	File loc =
      new File( position, getEncryptedFileName( loader, descriptor.getName().toLowerCase() ));
    String fullName = descriptor.getID() + "_" + descriptor.getVersion();
    fullName = fullName.replace( '.','_' );
    fullName = fullName.replace( ':','_' );
    return new File( loc, fullName + "." + Universe.CPT ).getPath();
  }

}
