package org.aieonf.util.xml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.aieonf.util.hex.HexConvertor;
import org.w3c.dom.*;

/**
 * A string node is the base of a tree of strings
 * that can be used to create simple xml documents
 * @author Kees Pieters
 *
 */
public class StringNode
{	
	//The name of the string node
	private String name;
	
	//The supported attributes
	private Properties attributes;
	
	private List<StringNode> children;
	
	public StringNode( String name )
	{
		this.name = name;
		this.attributes = new Properties();
		this.children = new ArrayList<StringNode>();
	}
	
	/**
	 * Get the name of the node
	 * @return
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * Add an attribute
	 * @param key
	 * @param value
	 */
	public void addAttribute( String key, String value )
	{
		this.attributes.setProperty( key, value );
	}

	/**
	 * Get the attribute size
	 * @return
	 */
	public int attributeSize()
	{
		return this.attributes.size();
	}

	public String getAttributeValue( String key )
	{
		return this.attributes.getProperty( key ); 
	}
	
	/**
	 * Get a set of attribute keys
	 * @return
	 */
	public Set<String> attributeKeys()
	{
		return this.attributes.stringPropertyNames();
	}
	
	/**
	 * Add a child node
	 * @param key
	 * @param value
	 */
	public void addChild( StringNode child )
	{
		this.children.add( child );
	}

	/**
	 * Get the number of children
	 * @return
	 */
	public int childrenSize()
	{
		return this.children.size();
	}

	/**
	 * Get an iterator over the children
	 * @return
	 */
	public Iterator<StringNode> iterator()
	{
		return this.children.iterator();
	}
	
	/**
	 * Create a document from the given String node root.
	 * If hex is true, then the name and attribute values of tdocument
	 * are converted to corresponding hex values
	 * @param root
	 * @return
	 * @throws ParserConfigurationException
	 */
	public static Document createDocument( StringNode root )
		throws ParserConfigurationException
	{
  	DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
    Document doc = docBuilder.newDocument();
    String name = root.getName();
    Element element = doc.createElement( name );
    doc.appendChild( element );
    addElement( doc, element, root );
    return doc;
	}
	
	/**
	 * Fill the given element with data from the string node
	 * @param doc
	 * @param element
	 * @param node
	 */
	protected static void addElement( Document doc, Element element, StringNode node )
	{
		Set<String>keys = node.attributeKeys();
		for( String key: keys )
			element.setAttribute( key, node.getAttributeValue( key ));
		Iterator<StringNode> iterator = node.iterator();
		Element childElement;
		StringNode child;
		while( iterator.hasNext() ){
			child = iterator.next();
			childElement = doc.createElement( child.getName() );
			element.appendChild( childElement );
			addElement( doc, childElement, child );
		}
	}

	/**
	 * Convert the given base string to a corresponding hex string
	 * @param base
	 * @return
	 */
	protected static String convertHex( String base )
	{
		return "H" + HexConvertor.convertHex( base.getBytes() ); 
	}
	
	/**
	 * Create a document from the given String node root
	 * @param root
	 * @return
	 * @throws ParserConfigurationException
	 */
	public static Document createHexDocument( StringNode root )
		throws ParserConfigurationException
	{
  	DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
    Document doc = docBuilder.newDocument();
    Element element = doc.createElement( convertHex( root.getName() ));
    doc.appendChild( element );
    addHexElement( doc, element, root );
    return doc;
	}
	
	/**
	 * Fill the given element with data from the string node
	 * @param doc
	 * @param element
	 * @param node
	 */
	protected static void addHexElement( Document doc, Element element, StringNode node )
	{
		Set<String>keys = node.attributeKeys();
		for( String key: keys )
			element.setAttribute( key, convertHex( node.getAttributeValue( key )));
		Iterator<StringNode> iterator = node.iterator();
		Element childElement;
		StringNode child;
		while( iterator.hasNext() ){
			child = iterator.next();
			childElement = doc.createElement( convertHex( child.getName() ));
			element.appendChild( childElement );
			addHexElement( doc, childElement, child );
		}
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
   * Make a String from the given document. If hex is true,
   * the the name and attributes are converted to hex values
   * @param node StringNode
   * @return
   * @throws TransformerFactoryConfigurationError
   * @throws TransformerException
   * @throws IOException
   * @throws ParserConfigurationException 
   */
	public static String getString( StringNode node, boolean hex ) 
		throws TransformerFactoryConfigurationError, 
			TransformerException, IOException, 
			ParserConfigurationException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Document doc;
		if( hex )
			doc = createHexDocument( node );
		else
			doc = createDocument( node );
		sendToStream( doc, out );
		out.flush();
		out.close();
		String str = new String( out.toByteArray() );
    str = str.replaceFirst( StoreDocument.DOC_HEADER_REGEXP, "" );
    return str;
	}
}
