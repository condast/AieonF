package org.aieonf.concept.processor;

import org.aieonf.concept.*;
import org.aieonf.concept.core.*;
import org.aieonf.util.encryption.Encryption;
import org.aieonf.util.encryption.IEncryption;
import org.aieonf.util.hex.HexConvertor;
import org.aieonf.util.logger.*;



//java2 imports
import java.util.*;

/**
 * <p>Title: Conceptual Network Database</p>
 *
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: Condast BV</p>
 *
 * @author Kees Pieters
 * @version 1.0
 */
public class StringProcessor
{

  //private Logger logger;

  /**
   * Supportive methods to create string objects from concepts or lists of
   * concepts
   */
  public StringProcessor()
  {
    //this.logger = Logger.getLogger( this.getClass() );
  }

  /**
   * Transforms a list of concepts into a string array that can be used by,
   * for instance java script. The format is
   * [[name,id,version],[name,id,version], ..., ]]
   *
   * @param descriptors List
   * @return String
  */
  public static String getStringArray( List<IDescriptor> descriptors )
  {
    IDescriptor descriptor;
    String str = "[";
    for( int i=0; i < descriptors.size(); i++ ){
      descriptor = descriptors.get( i );
      str += descriptor.toStringArray();
      if( i < descriptors.size() - 1 )
        str += ",";
    }
    str +="]";
    return str;
  }

  /**
   * Create a descriptor from a string array.
   *
   * @param stringArray String
   * @return List
   * @throws ConceptException
  */
  public static List<IDescriptor> getDescriptors( String stringArray )
    throws ConceptException
  {
    Logger logger = Logger.getLogger( StringProcessor.class );

    List<IDescriptor> descriptors = new ArrayList<IDescriptor>();
    if( stringArray.replace( " ", "" ).equals( "[]" ))
      return descriptors;

    //remove leading' and trailing brackets
    stringArray = stringArray.replaceAll( "\\[\\[","" );
    stringArray = stringArray.replaceAll( "\\]\\]","" );
    logger.trace( "String array is: " + stringArray);
    stringArray = stringArray.trim();
    //Remove in-between brackets
    String[] split = stringArray.split( "\\]\\,\\[" );
    IDescriptor descriptor;
    logger.trace( "Split length is: " + split.length );
    for( int i = 0; i < split.length; i++ ){
      logger.trace( "Split is: " + split [ i ] );
      descriptor = Descriptor.getDescriptor( split[ i ] );
      descriptors.add( descriptor );
    }
    return descriptors;
  }

  /**
   * Create a descriptor from an encrypted descriptor.
   *
   * @param applicationKey String
   * @param stringArray String
   * @return List
   * @throws Exception
  */
  public static List<IDescriptor> getDescriptorsFromEncrytion( String applicationKey,
                                                  String stringArray )
    throws Exception
  {
    Logger logger = Logger.getLogger( StringProcessor.class );

    List<IDescriptor> descriptors = new ArrayList<IDescriptor>();
    if( stringArray.replace( " ", "" ).equals( "[]" ))
      return descriptors;

    //remove leading' and trailing brackets
    stringArray = stringArray.replaceAll( "\\[","" );
    stringArray = stringArray.replaceAll( "\\]" ,"" );
    logger.trace( "String array is: " + stringArray);
    stringArray = stringArray.trim();
    //Remove in-between brackets
    String[] split = stringArray.split( "[,]" );
    IDescriptor descriptor;
    logger.trace( "Split length is: " + split.length );
    for( int i = 0; i < split.length; i++ ){
      logger.trace( "Split is: " + split [ i ] );
      descriptor = StringProcessor.getDescriptor( applicationKey, split[ i ] );
      descriptors.add( descriptor );
    }
    return descriptors;
  }

  /**
   * Transforms a list of concepts into a string array that can be used by,
   * for instance java script. The additional attribute allows extending the descriptor
   * with one additional property. The format is
   * [[name,id,version,attributeValue],[name,id,version,attributeValue], ..., ]]
   *
   * @param concepts List
   * @param attribute String
   * @return String
  */
  public static String getStringArray( List<IConcept> concepts, String attribute )
  {
    IConcept concept;
    String str = "[";
    String attrVal;
    for( int i=0; i<concepts.size(); i++ ){
      concept = concepts.get( i );
      str += concept.toStringArray();

      attrVal = concept.get( attribute );
      if( attrVal == null )
        attrVal = "";

      str = str.substring( 0, str.length() - 2) + attrVal + "]";
      if( i < concept.size() - 1 )
        str += ",";
    }
    str +="]";
    return str;
  }

  /**
   * Transforms a list of concepts into a string array that can be used by,
   * for instance javascript. The additional attribute allows extending the descriptor
   * with one additional property. The format is
   * [[name,id,version,attributeValue],[name,id,version,attributeValue], ..., ]]
   *
   * @param concepts List
   * @param attribute String
   * @return String
  */
  public static String getStringArray( List<IConcept> concepts, String[] attributes )
  {
    IConcept concept;
    String str = "[";
    String attrVal;
    for( int i=0; i< concepts.size(); i++ ){
      concept = concepts.get( i );
      str += concept.toStringArray();
      if(( attributes == null ) || ( attributes.length == 0 ))
      	continue;
      str = str.substring( 0, str.length() - 2);
      for( int j =0; j < attributes.length; j++ ){
      	attrVal = concept.get( attributes[j] );
      	if( attrVal == null )
      		attrVal = "";
      	str += attrVal;
      	if( j < attributes.length - 1)
      		str += ",";
      }
      str +="]";
      if( i < concepts.size() - 1)
        str += ",";
    }
    str +="]";
    return str;
  }

  /**
   * Transforms a list of concepts into a string array that can be used by,
   * for instance javascript. The additional attribute allows extending the descriptor
   * with one additional property. The format is
   * [[name,id,version,attributeValue],[name,id,version,attributeValue], ..., ]]
   *
   * @param concepts List
   * @return String
   * @throws Exception
  */
  public static String getXMLArray( List<IConcept> concepts ) throws Exception
  {
    String str = "<concepts>";
    IConcept concept;
    for( int i=0; i < concepts.size(); i++ ){
      concept = concepts.get( i );
      str += concept.toXML();
    }
    str +="</concepts>";
    return str;
  }

  /**
   * Get an encrypted String containing the concept descriptor
   *
   * @param applicationKey String
   * @param descriptor IDescriptor
   * @return String
   * @throws Exception
  */
  public static String getEncryptedDescriptor( String applicationKey,
                                               IDescriptor descriptor ) throws Exception
  {
    IEncryption encryption = new Encryption( IEncryption.Algorithms.ARCFOUR, applicationKey );
    byte[] encrypt = encryption.encryptData( descriptor.toStringArray().getBytes() );
    return HexConvertor.convertHex( encrypt );
  }

  /**
   * Convert the signature to a descriptor
   *
   * @param applicationKey String
   * @param signature String
   * @return IDescriptor
   * @throws Exception
  */
  public static IDescriptor getDescriptor( String applicationKey, String signature ) throws Exception
  {
    IEncryption encryption = new Encryption( IEncryption.Algorithms.ARCFOUR, applicationKey );
    String desc = new String( encryption.decryptData( HexConvertor.convertBytes( signature )));
    //Logger logger = Logger.getLogger( StringProcessor.class );
    //logger.debug( desc );
    return Descriptor.getDescriptor( desc );
  }

}
