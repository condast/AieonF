/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Condast</p>
 * @author Kees Pieters
 * @version 1.0
 */
package org.aieonf.concept.xml;

//J2SE imports
import java.util.*;

import org.w3c.dom.*;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.IRelationship;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.ConceptInstance;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.util.xml.StoreDocument;
//Concept imports

/**
 *
 * <p>Title: Conceptual Network Database</p>
 *
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 *  This method maintains all the general settings of this program
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: Condast</p>
 *
 * @author Kees Pieters
 * @version 1.0
 */
public class ConceptParser
{
	private Map<String, NamedNodeMap> directives;


	public ConceptParser()
	{
		super();
		this.directives = new HashMap<String, NamedNodeMap>();
	}

	public NamedNodeMap getDirective( String attribute ){
		return this.directives.get(attribute);
	}

	/**
	 * Get the keys and values from the given attribute root.
	 * This can either be the values of the root itself, or a map
	 * of key-value pairs of an underlying structure 
	 * @param root
	 * @return
	 */
	protected void fillElements( Document doc, Element attributes, Node offset, String keyString )
	{
		List<Node> children = StoreDocument.getElements( offset.getChildNodes(), Node.ELEMENT_NODE );
		this.directives.put(keyString, offset.getAttributes());
		if( children.size() == 0 ){
			Element element = ( Element )offset;
			if( keyString.endsWith( element.getNodeName() )){
				element = doc.createElement( keyString );
				element.setTextContent( offset.getTextContent() );
			}
			attributes.appendChild( element );
			return;
		}

		//There were children, so parse them
		String key;
		for( Node node: children ){
			key = keyString + "." + node.getNodeName();
			fillElements( doc, attributes, node, key );
		}
	}

	/**
	 * Parses a concept from a document
	 *
	 * @param doc Document
	 * @return IConcept
	 * @throws ConceptException
	 */
	public IConcept parse( Document doc ) throws ConceptException
	{
		Element attributes = null;
		Node relationships = null;
		String str;
		List<Node> children = StoreDocument.getElements( doc.getChildNodes(), Node.ELEMENT_NODE );
		List<Node> attributeList;
		if( children.size() != 1 )
			throw new ConceptException( StoreDocument.S_ERR_INVALID_FORMAT );

		Node root = children.get( 0 );
		children = StoreDocument.getElements( root.getChildNodes(), Node.ELEMENT_NODE );
		for( Node child: children ){
			str = child.getNodeName();
			if( str.toLowerCase().equals( IConcept.ATTRIBUTES.toLowerCase() )){
				attributes = doc.createElement( child.getNodeName() );
				attributeList = StoreDocument.getElements( child.getChildNodes(), Node.ELEMENT_NODE );
				for( Node attr: attributeList )
					fillElements( doc, attributes, attr, attr.getNodeName() );
			}
			if( str.toLowerCase().equals( IRelationship.RELATIONSHIPS.toLowerCase() ))
				relationships = child;
		}
		ConceptDbInstance concept = new ConceptDbInstance();
		concept.fill( doc, attributes, relationships );
		return concept;
	}

	/**
	 * Parses a concept from a root element
	 *
	 * @param doc Document
	 * @return IConcept
	 * @throws ConceptException
	 */
	public IConcept parse( Node element ) throws ConceptException
	{
		List<Node> children = StoreDocument.getElements( element.getChildNodes(), Node.ELEMENT_NODE );
		if(( children == null ) || ( children.size() != 1 ))
			throw new ConceptException( StoreDocument.S_ERR_INVALID_FORMAT );
		Node root = children.get( 0 );
		return parseConceptNode( root );
	}

	/**
	 * Parses a concept from a root element
	 *
	 * @param doc Document
	 * @return IConcept
	 * @throws ConceptException
	 */
	public IConcept parseConceptNode( Node element ) throws ConceptException
	{
		Element attributes = null;
		Node relationships = null;
		String str;
		List<Node> children = StoreDocument.getElements( element.getChildNodes(), Node.ELEMENT_NODE );
		List<Node> attributeList;

		Document doc = element.getOwnerDocument();
		for( Node child: children ){
			str = child.getNodeName();
			if( Descriptor.isNull( str ))
				continue;
			str = str.trim().toLowerCase();
			if( str.equals( IConcept.ATTRIBUTES.toLowerCase() )){
				attributes = doc.createElement( child.getNodeName() );
				attributeList = StoreDocument.getElements( child.getChildNodes(), Node.ELEMENT_NODE );
				for( Node attr: attributeList )
					fillElements( doc, attributes, attr, attr.getNodeName() );
			}
			if( str.toLowerCase().equals( IRelationship.RELATIONSHIPS.toLowerCase() ))
				relationships = child;
		}
		ConceptDbInstance concept = new ConceptDbInstance();
		concept.fill( doc, attributes, relationships );
		return concept;
	}

	/**
	 * Set the attributes (the children of the given node) into the
	 * descriptor
	 * @param descriptor
	 * @param node
	 * @throws ConceptException
	 */
	public static void setAttributes( IDescriptor descriptor, Node node )
			throws ConceptException
	{
		if( !node.getNodeName().equals( IConcept.ATTRIBUTES ))
			return;

		List<Node> children = 
				StoreDocument.getElements( node.getChildNodes(), Node.ELEMENT_NODE );
		String key, value;
		for( Node child: children ){
			key = child.getNodeName();
			value = child.getTextContent();
			if( value != null )
				value = value.trim();
			descriptor.set( key, value );
		} 	
	}

	/**
	 *
	 * <p>Title: Conceptual Network Database</p>
	 *
	 * <p>Description: Stores concepts; XML pages that describe functions</p>
	 * Create a concept, using a properties file
	 * <p>Copyright: Copyright (c) 2006</p>
	 *
	 * <p>Company: Condast</p>
	 *
	 * @author Kees Pieters
	 * @version 1.0
	*/
	private static class ConceptDbInstance extends ConceptInstance
	{
		/**
		 * For serialization purposes
		*/
		private static final long serialVersionUID = -418987229311718695L;

	/**
	   * Create a concept that can be stored
	   *
	   * @throws ConceptException
	  */
	  public ConceptDbInstance() throws ConceptException
	  {
	    super();
	  }


	  public void fill( Document doc, Node attributes, Node relationships ) throws ConceptException{
	    super.clear();
	  	this.getAttributes( attributes );
	    this.getRelations( doc, relationships );
	  	
	  }
	  /**
	   * Parses a concept from a document
	   *
	   * @param root Node
	   * @throws ConceptException
	  */
	  protected void getAttributes( Node root ) throws ConceptException
	  {
	    //Add the attributes
	    if(( root == null ) || 
	    		( root.getNodeName().equals( IConcept.ATTRIBUTES )) == false )
	    	return;
	    ConceptParser.setAttributes( this, root );
	  }

	  /**
	   * Get the relationships corresponding to the element for the given concept
	   *
	   * @param root Node
	  */
	  protected void getRelations( Document doc, Node root )
	  {
	    //Add the properties of the relationship
	  	if(( root == null ) ||
	       ( root.getNodeName().equals( IRelationship.RELATIONSHIPS ) == false))
	  		return;
	    
	    this.clearRelationships();
	  	List<Node> relationships = StoreDocument.getElements( root.getChildNodes(), Node.ELEMENT_NODE );
	    for( Node element: relationships ){
	      try{
	        IRelationship relationship = new RelationshipDbInstance( doc, element );
	        this.addRelationship( relationship );
	      }
	      catch( ConceptException ce ){
	        ce.printStackTrace();
	      }
	    }
	  }
	}

}
