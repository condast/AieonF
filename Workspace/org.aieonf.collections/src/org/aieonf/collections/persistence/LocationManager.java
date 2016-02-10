package org.aieonf.collections.persistence;

import java.io.File;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.Universe;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.loader.AbstractLocationManager;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.concept.security.AieonFEncryption;
import org.aieonf.util.encryption.IEncryption;
import org.aieonf.util.encryption.IEncryption.Algorithms;

public class LocationManager extends AbstractLocationManager<IDescriptor>
{
	private IEncryption encryption;
	
  /**
   * Create a location manager from the given source
   * @param source
   * @throws NoSuchAlgorithmException 
   * @throws NoSuchPaddingException 
  */
	public LocationManager( ILoaderAieon loader ) throws NoSuchPaddingException, NoSuchAlgorithmException
	{
		super( loader );
  	this.encryption = new AieonFEncryption( loader );
	}

	/**
	 * Locate the given descriptor
	 * @param descriptor IDescriptor
	 * @return
	 * @throws ConceptException
	*/
	@Override
	public String locate( IDescribable<?> describable ) throws ConceptException
	{
    IDescriptor descriptor = describable.getDescriptor();
		String name = descriptor.getName().toLowerCase();
    if( Descriptor.isNull( name ))
      throw new ConceptException( S_ERR_INVALID_NAME + name );

    String firstChar = String.valueOf( name.charAt( 0 ));
    try {
			return getPosition( firstChar, descriptor );
		}
		catch (Exception e) {
			throw new ConceptException( e );
		}
	}

	/**
	 * Get the descriptor corresponding to the given location
	 * @param location
	 * @return
	 * @throws ConceptException
	*/
	@Override
	public IDescriptor getDescribable(String location) throws ConceptException
	{
    File file = new File( location );
    String directory = file.getParentFile().getName();
    if( !super.getLoader().getEncryptionAlgorithm().equals(Algorithms.NONE ))
  		try {
				directory = this.encryption.decryptData( directory );
  		}
			catch (Exception e) {
				throw new ConceptException( e );
			}
	    return this.getDescriptor( directory, this.getRemoveExtension( file.getName()));
	}

  /**
   * Gets the descriptor with the given name and file name (without extension)
   *
   * @param name String
   * @param file String
   * @return IDescriptor
   * @throws ConceptException
  */
  protected IDescriptor getDescriptor( String name, String file )
  	throws ConceptException
  {
    String[] split = file.split("[_]");
    String version = split[ split.length - 1];
    String id;
    if( version.matches("^[\\d]+$"))
    	id = file.substring(0, file.length() - version.length() - 1);
    else{
    	id = split[0];
    	version = "1";
    }
    int provider = 0;
    if( split.length > 3 ){
    	provider = ( split[0] + ":" + split[1] ).hashCode();
    }
  	IDescriptor descriptor = new Descriptor( id, Descriptor.makeValidName(name) );
    descriptor.setDescription(name);
    descriptor.setVersion( Integer.valueOf( version ));
    if( provider != 0 )
    	descriptor.setProvider( String.valueOf( provider));
    return descriptor;
  }

  /**
   * Return the file belonging to a concept, or null if it wasn't found
   * The directory is the full path to a subdirectory:
   * postion/<first char>/<concept name>/<id_version.cpt>
   *
   * @param position String
   * @param descriptor IDescriptor
   * @return String
   * @throws Exception 
  */
  private String getPosition( String position, IDescribable<?> changeable ) throws Exception
  {
    IDescriptor descriptor = changeable.getDescriptor();
  	String directory = descriptor.getName();
  	if( !super.getLoader().getEncryptionAlgorithm().equals(Algorithms.NONE ))
  		directory = this.encryption.encryptData( directory );
    File loc = new File( new File( position ), directory );
    String fullName = descriptor.getID() + "_" + descriptor.getVersion();
    fullName = fullName.replace( '.','_' );
    fullName = fullName.replace( ':','_' );
    return new File( loc, fullName + "." + Universe.CPT ).getPath();
  }
}