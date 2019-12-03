/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Condast</p>
 * @author Kees Pieters
 * @version 1.0
 */
package org.aieonf.model.xml;

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.*;

import javax.xml.parsers.*;

import org.aieonf.commons.xml.StoreDocument;
import org.aieonf.concept.*;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.persist.ConceptPersistException;
import org.aieonf.concept.xml.ConceptParser;
import org.aieonf.concept.xml.StoreConcept;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.model.core.Model;

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
public class StoreModel<T extends IDescriptor>
{
	//Supported error messages
	public static final String S_PARENT = "Parent";
	public static final String S_CHILDREN = "Children";

	private Document doc;
	private Element root;

	private Logger logger = Logger.getLogger( this.getClass().getName());
	  
	/**
	 * Create a relationship from the given element
	 *
	 * @param descriptor IDescriptor
	 */
	public StoreModel() throws ParserConfigurationException
	{
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		this.doc = docBuilder.newDocument();
		this.root = doc.createElement( IConcept.CONCEPT );
		this.doc.appendChild( root );
	}

	/**
	 * Creates a document from a descriptor
	 *
	 * @return Document
	 * @throws ConceptPersistException
	 */
	public Document createDocument( IModelLeaf<? extends IDescriptor> model ) 
			throws ConceptException, ConceptPersistException
			{
		this.createDocument( model, this.root );
		return doc;
			}

	/**
	 * Creates a document from a descriptor
	 *
	 * @return Document
	 * @throws ConceptPersistException
	 */
	protected Document createDocument( IModelLeaf<? extends IDescriptor> model, Element parentNode ) 
			throws ConceptException, ConceptPersistException
			{
		IDescriptor parent = model.getDescriptor();
		StoreConcept.createElement( doc, parentNode, parent );
		if(!( model instanceof IModelNode ))
			return doc;
		IModelNode<? extends IDescriptor> md = 
				(org.aieonf.model.core.IModelNode<? extends IDescriptor> )model;
		if( md.hasChildren() == false )
			return doc;

		Element childNode = doc.createElement( S_CHILDREN );
		Element conceptNode;
		Collection<? extends IModelLeaf<? extends IDescriptor>>children = md.getChildren().keySet();
		for( IModelLeaf<? extends IDescriptor> child: children ){
			logger.log( Level.FINE, "Adding child: " + child.getDescriptor().toString() );
			conceptNode = doc.createElement( IConcept.CONCEPT );
			try{
				createDocument( child, conceptNode );
			}
			catch( Exception ex ){
				logger.log( Level.SEVERE, ex.getMessage(), ex );
			}
			childNode.appendChild( conceptNode );
		}
		parentNode.appendChild( childNode );
		return doc;
			}

	/**
	 * Parse a tree node model from the given document
	 * @param doc
	 * @return
	 * @throws ConceptException
	 * @throws ConceptException
	 */
	public IModelNode<IDescriptor> parseDocument( Document doc ) throws ConceptException, ConceptException
	{
		List<Node> elements = StoreDocument.getElements( doc.getChildNodes(), Node.ELEMENT_NODE );
		if( elements.size() != 1 )
			throw new ConceptException( StoreDocument.S_ERR_INVALID_FORMAT );

		Node root = elements.get( 0 );
		return parseElement( root );  	
	}

	/**
	 * Parse a tree node model from the given document
	 * @param doc
	 * @return
	 * @throws ConceptException
	 * @throws ConceptException
	 */
	public IModelNode<IDescriptor> parseElement( Node node ) throws ConceptException, ConceptException
	{
		String str;
		ConceptParser parser = new ConceptParser();
		IConcept parent = parser.parseConceptNode( node );
		IModelNode<IDescriptor> treeNode = new Model<IDescriptor>(parent );
		Node childNode = null;
		List<Node> children = StoreDocument.getElements( node.getChildNodes(), Node.ELEMENT_NODE );
		for( Node child: children ){
			str = child.getNodeName();
			if( str.equals( S_CHILDREN )){
				childNode = child;
				break;
			}
		}
		if( childNode == null )
			return treeNode;
		children = StoreDocument.getElements( childNode.getChildNodes(), Node.ELEMENT_NODE );
		for( Node child: children ){
			treeNode.addChild( parseElement( child ));
		}
		return treeNode;  	
	}  

	/**
	 * Print the given concept model
	 * @param model IModelNode<? extends IDescriptor>
	 * @param boolean includeattrs: if true the attributes 
	 * @return
	 */
	public static final String printModel( IModelLeaf<? extends IDescriptor> model, boolean includeAttrs )
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("{ Created: " + Descriptor.getDate( model, IDescriptor.Attributes.CREATE_DATE ) + "}\n" );
		buffer.append("{ Updated: " + Descriptor.getDate( model, IDescriptor.Attributes.UPDATE_DATE ) + "}\n" );
		if( includeAttrs )
			printExtendedModel( buffer, model, 0 );
		else
			printModel( buffer, model, 0 );
		return "\nModel:\n" + buffer.toString();
	}

	/**
	 * quick print of the structure of the given model
	 * @param model IModelNode<? extends IDescriptor>
	 * @return
	 */
	protected static final void printModel( StringBuffer buffer, IModelLeaf<? extends IDescriptor> model, int depth )
	{
		for( int i =0; i< depth; i++ )
			buffer.append( "\t" );
		buffer.append( model.getDescriptor().toString());
		buffer.append( "(" + model.getDepth() + ")\n" );
		if(!( model instanceof IModelNode ))
			return;
		IModelNode<? extends IDescriptor> md = 
				(org.aieonf.model.core.IModelNode<? extends IDescriptor> )model;
		for( IModelLeaf<? extends IDescriptor> child: md.getChildren().keySet() )
			printModel( buffer, child, depth + 1);
	}  

	/**
	 * quick print of the structure of the given model
	 * @param model IModelNode<? extends IDescriptor>
	 * @return
	 */
	protected static final void printExtendedModel( StringBuffer buffer, IModelLeaf<? extends IDescriptor> model, int depth )
	{
		for( int i =0; i< depth; i++ )
			buffer.append( "\t" );
		buffer.append( model.getDescriptor().toString());
		buffer.append( "(" + model.getDepth() + ")\n" );
		buffer.append( "\t" );

		StoreConcept.print(null, buffer, model.getDescriptor(), depth, true );
		if(!( model instanceof IModelNode ))
			return;

		IModelNode<? extends IDescriptor> md = 
				(org.aieonf.model.core.IModelNode<? extends IDescriptor> )model;
		for( IModelLeaf<? extends IDescriptor> child: md.getChildren().keySet() ){
			printExtendedModel( buffer, child, depth + 1);
		}
	}  

	/**
	 * Clone the given source
	 * @param source
	 * @return
	 */
	public static IModelNode<IDescriptor> clone( IModelLeaf<? extends IDescriptor> source )
			throws ConceptException, ParserConfigurationException, ConceptPersistException, ConceptException
	{
		StoreModel<IDescriptor> model = new StoreModel<IDescriptor>();
		Document doc = model.createDocument( source );
		return model.parseDocument( doc );
	}
}