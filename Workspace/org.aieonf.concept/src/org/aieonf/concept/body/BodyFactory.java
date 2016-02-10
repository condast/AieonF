package org.aieonf.concept.body;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;

import javax.crypto.Cipher;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.core.Concept;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.concept.security.AieonFEncryption;
import org.aieonf.concept.util.DescribableSet;
import org.aieonf.concept.util.IDescribableSet;
import org.aieonf.concept.xml.ConceptParser;
import org.aieonf.concept.xml.StoreConcept;
import org.aieonf.util.encryption.IEncryption;
import org.aieonf.util.hex.HexConvertor;
import org.aieonf.util.xml.StoreDocument;
import org.w3c.dom.Document;

public class BodyFactory<T extends Object>
{
	//Supported error messages
	public static final String S_ERR_INVALID_RELATIONSHIP = 
		"The concept contains an invalid relationship: ";
	public static final String S_ERR_INVALID_DESCRIPTOR = 
		"The descriptor does not have a name: ";
	public static final String S_ERR_INVALID_ENCRYPTION_LENGTH = 
			"The length of the string is too short for the encryption: ";

  /**
   * Conceive the body from the given body aieon
   * @param bodyAieon IBodyAieon
   * @return T
   * @throws BodyFactoryException
  */
  @SuppressWarnings("unchecked")
	public T conceive( IBodyAieon bodyAieon ) throws BodyFactoryException
  {
    Class<?> bodyClass;
    Constructor<?> bodyArgsConstructor;
    try{
      Class<?>[] bodyArgsClass = new Class[] { bodyAieon.getAieonCreatorClass() };
      bodyClass = bodyAieon.getBodyClass();
      bodyArgsConstructor =
          bodyClass.getConstructor( bodyArgsClass );
			T body = ( T )bodyArgsConstructor.newInstance( bodyAieon );
      return body;
    }
    catch( NoSuchMethodException ex){
      throw new BodyFactoryException( ex.getMessage() , ex );
    }
    catch (Exception e) {
      throw new BodyFactoryException( e.getMessage() , e );
    }
  }

  /**
   * Create an id for the given concept
   *
   * @param describable IConcept
   * @param collection IConceptCollection
   * @return String
  */
  public static String IDFactory( IDescribableSet<? extends ILoaderAieon> collection )
  {
    String hexStr;
    do{
      hexStr = IDFactory();
    }
    while( collection.containsID( hexStr ) );
    return hexStr;
  }

  /**
   * Create an id for the given concept
   *
   * @param describable IConcept
   * @param collection IConceptCollection
   * @return String
  */
  public static String IDFactory( IDescribable<? extends IDescriptor> describable, IDescribableSet<? extends ILoaderAieon> collection )
  {
    StringBuffer buffer;
    if( Descriptor.isNull(describable.getDescriptor().getID() ))
    	buffer = new StringBuffer();
    else
    	buffer = new StringBuffer( describable.getDescriptor().getID() + ":" );
    buffer.append( collection.getLoader().getID() + ":" );
    buffer.append( IDFactory( collection ));
    return buffer.toString();
  }

  /**
   * Create an id for the given concept
   *
   * @param describable IConcept
   * @param collection IConceptCollection
   * @return String
  */
  public static void IDFactory( IDescribable<? extends IDescriptor> describable ){
  	String id = IDFactory( describable, new DescribableSet<Object>( new ArrayList<Object>()));
  	describable.getDescriptor().set( IDescriptor.Attributes.ID, id );
  }

  /**
   * Create an id for the given concept
   *
  */
  public static String IDFactory()
  {
    long newId = ( long )( Math.random() * Long.MAX_VALUE );
    return Long.toHexString( newId );

  }

  /**
   * Load a concept from the inputstream
   *
   * @param in InputStream
   * @return IDescriptor
   * @throws Exception
  */
  public final static IDescriptor loadFromXML( InputStream in ) throws Exception
  {
    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
    Document doc = docBuilder.parse ( in );
    ConceptParser parser = new ConceptParser();
    return parser.parse( doc );
  }

  /**
   * Save the given concept to the outputstream
   *
   * @param descriptor IDescriptor
   * @param out OutputStream
   * @throws IOException
  */
  public final static void saveToXML( IDescriptor descriptor, OutputStream out ) throws IOException
  {
    try {
    	if( descriptor instanceof Descriptor ){
    		Descriptor desc = ( Descriptor )descriptor;
    		if(( desc.getID() == null ) && ( Descriptor.isValid( desc ) == false ))
    			desc.set( IDescriptor.Attributes.ID, BodyFactory.IDFactory() );
    	}
    	StoreConcept store = new StoreConcept( descriptor );
      Document doc = store.createDocument();
      StoreDocument.sendToStream( doc, out );
    }
    catch( Exception ex ) {
      throw new IOException( ex.getMessage(), ex );
    }
  }

  /**
   * Set the correct defaults for the descriptor
   * @param descriptor
   * @throws ConceptException
   */
  protected static void setDescriptor( IDescriptor descriptor ) throws ConceptException
  {
		if( Descriptor.isNull( descriptor.getName() ))
			throw new ConceptException( S_ERR_INVALID_DESCRIPTOR + descriptor.toString() );
		if( Descriptor.isNull( descriptor.getID() ))
			descriptor.set( IDescriptor.Attributes.ID, BodyFactory.IDFactory() );
		if( descriptor.getVersion() < 0 )
			descriptor.setVersion( 0 );
  	
  }
  
  /**
   * Transfer the attributes of the given concept to this concept,
   * while clearing the current values if this is required
   * 
   * @param target IConcept
   * @param source IDescriptor
   * @param clear boolean
   * @throws ConceptException
  */
  public final static void transfer( IDescriptor target, IDescriptor source, 
  		boolean clear ) throws ConceptException
  {
    if( clear == true)
    	target.clear();
    Iterator<String> keys = source.iterator();
    String key;
    while( keys.hasNext() == true ){
      key = keys.next();
      target.set( key, source.get( key ));
    }
  }

  /**
   * Transfer the attributes of the given enumeration from source
   * to target concept. Existing attributes are overridden
   * while clearing the current values if this is required.
   * If sourceRoot is true, then the names of the key will be used
   * in stead of the full canonical name
   * 
   * @param enumClass
   * @param sourceRoot
   * @param target
   * @param source
   * @throws ConceptException
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
	public final static void transfer( Class<? extends Enum> enumClass, boolean sourceRoot, IConcept target, IDescriptor source ) throws ConceptException
  {
    EnumSet<? extends Enum<?>> set = EnumSet.allOf( enumClass );
    String attr;
    for( Enum<?> key: set ){
    	attr = ConceptBody.getKeyName( key );
      if( sourceRoot)
      	target.set( attr, source.get( key.name() ));
      else
      	target.set( attr, source.get( attr ));
    }
  }

  /**
   * Casts the given concept to the given class type
   *
   * @param clazz Class
   * @param concept IConcept
   * @return IConcept
   * @throws ConceptException
  */
  public final static IConcept transform(Class<? extends IConcept> clazz, IDescriptor descriptor ) throws ConceptException
  {
    if( IConcept.class.isAssignableFrom( clazz ) == false )
      throw new ConceptException( Concept.S_ERR_INVALID_CLASS );

    IConcept result = null;
    try{
      result = clazz.newInstance();
    }
    catch( Exception ex ){
      throw new ConceptException( ex.getMessage(), ex );
    }
    BodyFactory.transfer( result, descriptor, true );
    result.set( IDescriptor.Attributes.CLASS.toString(), result.getClass().getName() );
    return result;
  }
  
  /**
   * Casts the given concept to the given class type
   *
   * @param concept IConcept
   * @return IConcept
   * @throws ConceptException
  */
  @SuppressWarnings({ "unchecked", "rawtypes" })
	public final static IConcept transform( IConcept concept ) throws ConceptException
  {
  	Class clzz = null;
  	try{
      clzz = Class.forName( concept.getClassName() );
    }
    catch( Exception ex ){
      throw new ConceptException( ex.getMessage(), ex );
    }
    return transform( clzz, concept );
  }

  /**
   * Sign a concept. This is mainly used to verify the integrity of a
   * concept when it is shared throughout the network. It uses manifest info
   * from the database to which it belongs
   *
   * @param manifest ManifestAieon
   * @param descriptor IDescriptor
   * @throws IOException
  */
  public static int hashCode( ILoaderAieon loader, IDescriptor descriptor )
  	throws IOException
  {
    ByteArrayOutputStream bStream = null;
    OutputStream eStream = null;
    try{
      IEncryption encryption = new AieonFEncryption( loader );
    	bStream = new ByteArrayOutputStream();
    	eStream = encryption.getOutputStream( Cipher.ENCRYPT_MODE, bStream );
    	saveToXML( descriptor, eStream );
    	String encrypt = HexConvertor.convertHex( bStream.toByteArray() );
    	return encrypt.hashCode();
    }
    catch( Exception ex ){
    	throw new IOException( ex.getMessage(), ex );
    }
    finally{
    	if( eStream != null ){
    		eStream.flush();
    		eStream.close();
    	}
    	if( bStream != null ){
    		bStream.flush();
    		bStream.close();
    	}
    }
  }

  /**
   * Create a hash code for the given string. 
   *
   * @param loader LoaderAieon
   * @param str String
   * @throws IOException
  */
  public static int hashCode( ILoaderAieon loader, String str )
  	throws IOException
  {
    ByteArrayOutputStream bStream = null;
    OutputStream eStream = null;
    try{
      IEncryption encryption = new AieonFEncryption( loader );
    	bStream = new ByteArrayOutputStream();
    	eStream = encryption.getOutputStream( Cipher.ENCRYPT_MODE, bStream );
    	String encrypt = HexConvertor.convertHex( str.getBytes() );
    	return encrypt.hashCode();
    }
    catch( Exception ex ){
    	throw new IOException( ex.getMessage(), ex );
    }
    finally{
    	if( eStream != null ){
    		eStream.flush();
    		eStream.close();
    	}
    	if( bStream != null ){
    		bStream.flush();
    		bStream.close();
    	}
    }
  }

  /**
   * Create a key of the given length for the given string. 
   *
   * @param loader LoaderAieon
   * @param str String
   * @param length int
   * @throws IOException
  */
  public static String GetEncryptionKey( ILoaderAieon loader, String str, int length )
  	throws IOException
  {
    ByteArrayOutputStream bStream = null;
    OutputStream eStream = null;
    try{
      IEncryption encryption = new AieonFEncryption( loader );
    	bStream = new ByteArrayOutputStream();
    	eStream = encryption.getOutputStream( Cipher.ENCRYPT_MODE, bStream );
    	String encrypt = HexConvertor.convertHex( str.getBytes() );
    	if( encrypt.length() < length )
    		throw new SecurityException( S_ERR_INVALID_ENCRYPTION_LENGTH + str);
    	return encrypt.substring(0 , length );
    }
    catch( Exception ex ){
    	throw new IOException( ex.getMessage(), ex );
    }
    finally{
    	if( eStream != null ){
    		eStream.flush();
    		eStream.close();
    	}
    	if( bStream != null ){
    		bStream.flush();
    		bStream.close();
    	}
    }
  }
  
  /**
   * Sign a concept. This is mainly used to verify the integrity of a
   * concept when it is shared throughout the network. It uses manifest info
   * from the database to which it belongs
   *
   * @param manifest ManifestAieon
   * @param descriptor IDescriptor
   * @throws ConceptException, IOException
  */
  public static void sign( ILoaderAieon loader, IDescribable<? extends IDescriptor> descriptor ) 
  	throws ConceptException, IOException
  {
    int hashCode = BodyFactory.hashCode( loader, loader );
    long signature = ( long )hashCode << 32;
    signature += BodyFactory.hashCode( loader, descriptor.getDescriptor() );
    descriptor.getDescriptor().set( IDescriptor.Attributes.SIGNATURE, String.valueOf( signature ));
  }
  
  /**
   * Verify a concept. Returns true if the hash code matches
   *
   * @param manifest ManifestAieon
   * @param descriptor IDescriptor
   * @throws ConceptException, IOException
  */
  public static boolean verify( ILoaderAieon loader, IDescriptor descriptor ) 
  	throws ConceptException, IOException
  {
    if( descriptor.getSignature() == null )
    	return false;
  	long hashCode = Long.valueOf( descriptor.getSignature() );
    int check = BodyFactory.hashCode( loader, loader );
    long signature = ( long )check << 32;
    signature += BodyFactory.hashCode( loader, descriptor );
    return ( signature == hashCode );
  }
  
}
