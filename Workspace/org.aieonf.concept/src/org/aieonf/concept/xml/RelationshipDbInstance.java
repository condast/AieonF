/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Condast</p>
 * @author Kees Pieters
 * @version 1.0
*/
package org.aieonf.concept.xml;

import java.util.List;

import org.w3c.dom.*;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.*;
import org.aieonf.util.xml.StoreDocument;

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
class RelationshipDbInstance extends RelationshipInstance
{
	public static final String S_ERR_INVALID_CONCEPT_DESCRIPTOR = 
		"The relationship is corrupt. There shoulds be exactly one concept descriptor: ";
	/**
	 * For serialization purposes
	*/
	private static final long serialVersionUID = -8466413400147403313L;

	//The descriptor to which the relationship points
	private IDescriptor descriptor;
	
	/**
   * Create a relationship from the given element
   *
   * @param element Element
   * @throws ConceptException
  */
  public RelationshipDbInstance( Document doc, Node element ) throws ConceptException
  {
    super.clear();
  	this.getRelationData( doc, element );
  }

  /**
   * Get the relationships corresponding to the element for the given concept
   *
   * @param root Element
   * @throws ConceptException
  */
  protected void getRelationData( Document doc, Node root ) throws ConceptException
  {
    if( root == null )
      return;

    //Add the properties of the relationship
    List<Node> children = StoreDocument.getElements( root.getChildNodes(), Node.ELEMENT_NODE );
    //if( children.size() != 2 )
    //	throw new ConceptException( S_ERR_INVALID_CONCEPT_DESCRIPTOR + this.toString() );

    for( Node child: children ){
    	if( child.getNodeName().equals( IDescriptor.DESCRIPTOR )){
    		this.getConceptDescriptor( doc, child );
    		continue;
    	}
    	ConceptParser.setAttributes( this, child );
    }
  }

  /* (non-Javadoc)
	 * @see org.condast.concept.core.RelationshipInstance#getConceptDescriptor()
	 */
	@Override
	public IDescriptor getConceptDescriptor()
	{
		return descriptor;
	}

	/**
   * Get the concept descriptor of this relationship
   *
   * @param root Element
  */
  protected void getConceptDescriptor( Document doc, Node root ) throws ConceptException
  {
  	if(( root == null ) ||
        ( root.getNodeName().equals( IDescriptor.DESCRIPTOR )== false ))
   		return;

    //Add the properties of the relationship
    this.descriptor = new Descriptor();
  	List<Node> children = StoreDocument.getElements( root.getChildNodes(), Node.ELEMENT_NODE );
    if( children.size() != 1 )
    	throw new ConceptException( S_ERR_INVALID_CONCEPT_DESCRIPTOR + this.toString() );
    ConceptParser.setAttributes( descriptor, children.get( 0 ));
  }

  /**
   * Set the attribute of the relationship
   * @param attribute
   */
  protected void set( IDescriptor descriptor, Node attribute )
  {
  	String key = attribute.getNodeName();
  	String value = attribute.getTextContent();
  	if( value != null )
  		value = value.trim();
  	descriptor.set( key, value );  	
  }
}
