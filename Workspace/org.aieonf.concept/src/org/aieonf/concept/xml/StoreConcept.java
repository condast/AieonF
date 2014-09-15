/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Condast</p>
 * @author Kees Pieters
 * @version 1.0
*/
package org.aieonf.concept.xml;

import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;

import org.w3c.dom.*;

import javax.xml.parsers.*;

import org.aieonf.concept.*;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.core.Concept;
import org.aieonf.concept.persist.ConceptPersistException;

/**
 *
 * <p>Title: Conceptual Network Database</p>
 *
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * Create an element that can be used to store  concept, using a properties file
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: Condast</p>
 *
 * @author Kees Pieters
 * @version 1.0
*/
public class StoreConcept
{
	//Supported error messages
	public static final String S_ERR_NULL_ATTRIBUTE = 
		"The attribute is null";
	
  //The concept that will be transferred into a document
  private IDescriptor descriptor;

  /**
   * Create a relationship from the given element
   *
   * @param descriptor IDescriptor
  */
  public StoreConcept( IDescriptor descriptor )
  {
    this.descriptor = descriptor;
  }

  /**
   * Creates a document from a descriptor
   *
   * @return Document
   * @throws ConceptPersistException
  */
  public Document createDocument() throws ConceptPersistException
  {
    try{
    	DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();
      Element root = doc.createElement( IConcept.CONCEPT );
      doc.appendChild( root );
      createElement( doc, root, this.descriptor );
      return doc;
    }
    catch( Exception ce ){
      throw new ConceptPersistException( ce.getMessage(), ce );
    }
  }

  /**
   * Creates a document from a descriptor
   *
   * @return StructuredTextDocument
   * @throws ConceptPersistException
  */
  public Document createDescriptorXML() throws ConceptPersistException
  {
    try{
      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();
      doc.setTextContent( IConcept.CONCEPT );

      Element attr  = doc.createElement( IConcept.ATTRIBUTES );
      doc.appendChild( attr );
      Element el = doc.createElement( IDescriptor.Attributes.ID.toString() );
      el.setTextContent( descriptor.getID() );
      attr.appendChild( el );
      el = doc.createElement( IDescriptor.Attributes.NAME.toString() );
      el.setTextContent( descriptor.getName() );
      attr.appendChild( el );
      el = doc.createElement( IDescriptor.Attributes.VERSION.toString() );
      el.setTextContent( String.valueOf( descriptor.getVersion() ));
      attr.appendChild( el );
      return doc;
    }
    catch( Exception ce ){
      throw new ConceptPersistException( ce );
    }
  }
  
	/**
	 * Print the given descriptor
	 * @param IDescriptor descriptor
	 * @param includeAttrs
	 * @return
	 */
	public static final String print( IDescriptor descriptor, boolean includeAttrs )
	{
		StringBuffer buffer = new StringBuffer();
		print( "", buffer, descriptor, 0, includeAttrs );
		return "\nDescriptor:\n" + buffer.toString();
	}
	
	/**
	 * quick print of the structure of the given model
	 * @param descriptor IDescriptor
	 * @return
	*/
	public static final void print( String leadingText, StringBuffer buffer, IDescriptor descriptor, int depth, boolean includeAttrs )
	{
		for( int i =0; i< depth; i++ )
			buffer.append( "\t" );
		
		String str = descriptor.toString() + "\n";
		if( leadingText != null )
			str = leadingText + ": " + str;
		buffer.append( str  );
		if( includeAttrs ){
			Iterator<String> iterator = descriptor.iterator();
			String key;
			while( iterator.hasNext() ){
				for( int i =0; i< depth+1; i++ )
					buffer.append( "\t" );
				key = iterator.next();
				buffer.append("[" + key + ": " + descriptor.get( key ) + "]\n" );
			}
		}
	}
	
  /**
   * Set the attributes for this descriptor
   * @param doc
   * @param root
   * @param descriptor
   * @throws ConceptPersistException
   * @throws ConceptException
   */
  protected static void setAttributes( Document doc, Element root, IDescriptor descriptor )
  throws ConceptPersistException, ConceptException
  {
    Element attributesRoot  =
      doc.createElement( IConcept.ATTRIBUTES );
    root.appendChild( attributesRoot );

    Iterator<String> iterator = descriptor.iterator();
    String key;
    Element element;
    Element appendRoot;
    String[] split;
    String value;
    String name;
    while( iterator.hasNext() == true ){
      key = iterator.next();
      value = descriptor.get( key );
      if( value == null )
      	continue;
      value = value.replaceAll("'", "`" ); //Use unicode for quotes to prevent javascript problems 
      appendRoot = org.aieonf.util.xml.StoreDocument.getAttributeRoot( doc, attributesRoot, key);
      split = key.split("[\\.]");
      name = split[ split.length - 1 ];
      element = doc.createElement( name );
      element.setTextContent( value );
      appendRoot.appendChild( element );
    }	
  }
  
  /**
   * Creates an element under the given root element for the given document
   * from a concept. If the root is null, the element is appended as a root
   * for the document
   *
   * @param doc Document
   * @param root Element
   * @param concept IConcept
   * @throws ConceptPersistException
   * @throws ConceptException
  */
  public static void createElement( Document doc, Element root, IDescriptor descriptor )
    throws ConceptPersistException,
    ConceptException
  {
  	//Add the attributes and the relationships (if they are present)
  	setAttributes( doc, root, descriptor );
  }

  /**
   * Sets the create and update info of the concept
   *
   * @param concept IConcept
   * @throws ConceptException
  */
  protected void setTime( IConcept concept ) throws ConceptException
  {
    Calendar calendar = Calendar.getInstance();
    if( Descriptor.getCreateDate( concept ) == null )
      Descriptor.setCreateDate( concept, calendar.getTime() );
    else
      Descriptor.setUpdateDate( concept, calendar.getTime() );
  }

  /**
   * Save the given descriptor to the outputstream as styled XML, that is
   * including tabs, breaks etc.
   *
   * @param descriptor IDescriptor
   * @return String
   * @throws IOException
  */
  public final static String styledXML( IDescriptor descriptor ) throws IOException
  {
    try {
      StoreConcept store = new StoreConcept( descriptor );
      Document doc = store.createDocument();
      return org.aieonf.util.xml.StoreDocument.styledXML( doc );
    }
    catch( ConceptPersistException ex ) {
      throw new IOException( ex.getMessage(), ex );
    }
  }

  /**
   * Save the given descriptor to the outputstream as styled XML, that is
   * including tabs, breaks etc.
   *
   * @param descriptor IDescriptor
   * @param attrs String[]
   * @return String
   * @throws IOException
  */
  public final static String styledXML( IDescriptor descriptor, String[] attrs ) throws Exception
  {
    try {
      StoreConcept store = new StoreConcept( createMinimalDescriptor( descriptor, attrs ));
      Document doc = store.createDocument();
      return org.aieonf.util.xml.StoreDocument.styledXML( doc );
    }
    catch( ConceptPersistException ex ) {
      throw new IOException( ex.getMessage() );
    }
  }

  /**
   * Create a concept with only the given attributes and the descriptor values
   * @param descriptor
   * @param attrs
   * @throws ConceptException
   */
  protected static IDescriptor createMinimalDescriptor( IDescriptor descriptor, String[] attrs ) throws ConceptException
  {
  	IConcept concept = new Concept();
  	setValues( descriptor, concept, attrs );
 	return concept;
  }
  
  /**
   * Create a minimal concept with only the given attributes
   * @param concept
   * @param descriptor
   * @param attrs
   * @throws ConceptException
   */
  protected static void setValues( IDescriptor source, IDescriptor target, String[] attrs ) throws ConceptException
  {
  	String value;
  	for( String attr: attrs ){
  		value = source.get( attr );
  		if(( value != null ) && ( value.trim().length() != 0 ))
  			target.set( attr, value);
  	}
  }
	
}