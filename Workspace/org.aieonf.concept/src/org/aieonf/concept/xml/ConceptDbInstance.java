/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Condast BV</p>
 * @author Kees Pieters
 * @version 1.0
*/
package org.aieonf.concept.xml;

//J2SE imports
import java.util.List;

import org.w3c.dom.*;
import org.aieonf.concept.*;
import org.aieonf.concept.core.*;
import org.aieonf.util.xml.StoreDocument;
//Concept imports

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
class ConceptDbInstance extends ConceptInstance
  implements IFullConcept
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
