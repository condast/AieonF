/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Condast</p>
 * @author Kees Pieters
 * @version 1.0
*/
package org.aieonf.util.xml;

//Concept imports
//J2SE imports
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

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
public class StoreDocument
{
	//Default document header
	public static final String S_DEFAULT_HEADER = 
		"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>";

	//The Regular expression for a document header
	public static final String DOC_HEADER_REGEXP = "<\\?xml.*\\?>"; 

	//additional attributes
	public static final String S_JSON_STRING = "toJSONString";
	
	//Supported error messages
	public static final String S_ERR_NULL_ATTRIBUTE = 
		"The attribute is null";
  public static final String S_ERR_INVALID_FORMAT =
    "The concept source has an invalid format";
  
  /**
   * Create a relationship from the given element
   *
   * @param descriptor IDescriptor
  */
  public StoreDocument()
  {
  }

  /**
   * Key names that are split up in a number of '.' separated names
   * are parsed separately;
   * @param attributeRoot
   * @param key
   * @return
  */
  public static Element getAttributeRoot( Document doc, Element attributeRoot, String key )
  {
  	key = key.trim();
  	if( key.contains( ".") == false )
  		return attributeRoot;
  	String[] split = key.split("[\\.]");
  	if(( split.length == 1 ) || ( split[ split.length - 1].equals( "" )))
  		return attributeRoot;
  	Element child = getAttributeRoot( doc, attributeRoot, split, 0 );
  	if( child != null )
  		return child;
		throw new NullPointerException( S_ERR_NULL_ATTRIBUTE );
  }

  /**
   * Key names that are split up in a number of '.' separated names
   * are parsed separately;
   * @param attributeRoot
   * @param key
   * @return
  */
  protected static Element getAttributeRoot( Document doc, Element attributeRoot, String[] keySplit, int index )
  {
  	//always ignore the last as this is the leaf
  	int last = keySplit.length - 1;
  	if( index >= last  )
  		return attributeRoot;
  	
  	NodeList children = attributeRoot.getChildNodes();
  	Element child = null;
  	Node node;
  	Node root = attributeRoot;
  	for( int i = 0; i < children.getLength(); i++ ){
 	  	node = children.item( i );
 	  	if( node.getNodeType() != Node.ELEMENT_NODE )
 	  		continue;
  		if( node.getNodeName().equals( keySplit[ index ])){
    		child = getAttributeRoot( doc, ( Element )node, keySplit, index + 1 );
    		if( child != null )
    			return child;
  		}
  	}
  	
  	//No children were found, so create them
		for( int i = index ; i < last; i++ ){
			child = doc.createElement( keySplit[ i ]);
			root.appendChild( child );
			root = child;
		}
		return ( Element )root;
  }



  /**
   * Get the elements that correspond to the given int node type
   * @param elements
   * @return
   */
  public static List<Node> getElements( NodeList nodes, int nodeType )
  {
  	List<Node> results = new ArrayList<Node>();
  	Node node;
  	for( int i = 0; i < nodes.getLength(); i++ ){
  		node = nodes.item( i );
  		if( node.getNodeType() == nodeType )
  			results.add( node );
  	}
  	return results;
  }

  /**
   * If true, the given node contains elements of the given type
   * @param elements
   * @return
   */
  public static boolean containsChildren( Node node, int nodeType )
  {
  	Node child;
  	NodeList nodes = node.getChildNodes();
  	for( int i = 0; i < nodes.getLength(); i++ ){
  		child = nodes.item( i );
  		if( child.getNodeType() == nodeType )
  			return true;
  	}
  	return false;
  }

  /**
   * Make a String from the given document. If hex is true,
   * the the name and attributes are converted to hex values
   * @param node StringNode
   * @return
   * @throws TransformerFactoryConfigurationError
   * @throws TransformerException
   * @throws IOException
   * @throws ParserConfigurationException 
   */
	public static String getString( Document doc, boolean removeDocHeader ) 
		throws TransformerFactoryConfigurationError, 
			TransformerException, IOException, 
			ParserConfigurationException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		sendToStream( doc, out );
		out.flush();
		out.close();
		String str = new String( out.toByteArray() );
    if( removeDocHeader )
    	str = str.replaceFirst( org.aieonf.util.xml.StoreDocument.DOC_HEADER_REGEXP, "" );
    return str;
	}

  /**
   * Send the document to the given outputstream
   * @param doc
   * @param out
   * @throws TransformerFactoryConfigurationError 
   * @throws TransformerException 
  */
  public static void sendToStream( Document doc, OutputStream out ) 
  throws TransformerFactoryConfigurationError, TransformerException
  {
    Source source = new DOMSource(doc);
    StreamResult result = new StreamResult( out );
    Transformer xformer = TransformerFactory.newInstance().newTransformer();
    xformer.transform(source, result);
  }

  /**
   * Get the the given descriptor as styled XML, that is
   * including tabs, breaks etc.
   *
   * @param descriptor IDescriptor
   * @return String
   * @throws IOException
  */
  public final static String styledXML( Document doc ) throws IOException
  {
    StringBuffer buf = new StringBuffer();
    if( doc.getDoctype() != null )
    	buf.append( doc.getDoctype().toString() + "\n");
    StoreDocument.setStyledXMLNode( 0, buf, doc );
    return buf.toString();
  }

  /**
   * Create  styled XML string for this node
   *
   * @param depth int
   * @param buf StringBuffer
   * @param node Node
  */
  protected static void setStyledXMLNode( int depth, StringBuffer buf, Node node )
  {
		if( node.getNodeName().equals( S_JSON_STRING ))
			return;

		//First add the amount of tabs for this node
  	if( node.hasChildNodes() == true ){
    	buf.append( "\n" );
    	for( int i = 0; i < depth; i++ )
    		buf.append( "  " );
    }
    String nodeStr;
    if( node.hasChildNodes() == false )
    	nodeStr = node.getTextContent();
    else{
      nodeStr = "<" + node.getNodeName();
      NamedNodeMap attributes = node.getAttributes();
      if((attributes != null ) && ( attributes.getLength() > 0 )){
      	nodeStr += " ";
      	Node attribute;
      	for( int i = 0; i < attributes.getLength(); i++ ){
      		attribute = attributes.item( i );
      		nodeStr += attribute.getNodeName() + "=" + attribute.getTextContent();
      		if( i < attributes.getLength() - 1 )
      			nodeStr += " ";
      	}
      }
      nodeStr += ">";
    }
    buf.append( nodeStr );
  	
    Node child;
    NodeList children = node.getChildNodes();
    int nrOfParentChildren = 0;
    for( int i = 0; i < children.getLength(); i++ ){
      child = children.item( i );
      setStyledXMLNode(depth+1, buf, child);
      if( child.getNodeType() == 3 )
      	buf.append( "<" + node.getNodeName() + "/>" );
      else
      	nrOfParentChildren++;
    }
    if( nrOfParentChildren > 0 ){
    	buf.append( "\n" );
    	for( int i = 0; i < depth; i++ )
    		buf.append( "  " );
    	buf.append( "<" + node.getNodeName() + "/>" );
    }
  }  
}
