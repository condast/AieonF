/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Condast</p>
 * @author Kees Pieters
 * @version 1.0
 */
package org.aieonf.template.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import org.w3c.dom.*;

import javax.xml.parsers.*;

import org.aieonf.concept.*;
import org.aieonf.concept.body.BodyFactory;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.persist.ConceptPersistException;
import org.aieonf.concept.xml.ConceptParser;
import org.aieonf.concept.xml.StoreConcept;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.ModelException;
import org.aieonf.model.constraints.IAspect;
import org.aieonf.template.ITemplate;
import org.aieonf.template.ITemplateAieon;
import org.aieonf.template.ITemplateNode;
import org.aieonf.template.TemplateAieon;
import org.aieonf.template.TemplateNode;
import org.aieonf.template.TemplateWrapper;
import org.aieonf.util.logger.Logger;
import org.aieonf.util.parser.IParser;
import org.aieonf.util.parser.IParserListener;
import org.aieonf.util.parser.ParseException;
import org.aieonf.util.xml.StoreDocument;
//Concept

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
public class StoreTemplate implements IParser<ITemplate>
{
	//Supported error messages
	public static final String S_PARENT = "Parent";
	public static final String S_CHILDREN = "Children";

	private IAspect aspect;

	private Logger logger;

	/**
	 * Create a relationship from the given element
	 *
	 * @param descriptor IDescriptor
	 */
	public StoreTemplate() throws ParserConfigurationException
	{
		this.logger = Logger.getLogger( this.getClass() );
	}

	/**
	 * Create a relationship from the given element
	 *
	 * @param descriptor IDescriptor
	 */
	public StoreTemplate( IAspect aspect ) throws ParserConfigurationException
	{
		this.aspect = aspect;
		this.logger = Logger.getLogger( this.getClass() );
	}

	/**
	 * Read an object from the given location
	 * @param location String
	 * @return T
	 * @throws IOException
	 */
	@Override
	public ITemplate parse( InputStream in ) throws IOException
	{
		try{
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse ( in );
			if( this.aspect == null )
				return parseDocument( doc );
			else
				return new TemplateWrapper( parseDocument( doc, aspect ));
		} 
		catch( Exception ex ){
			throw new IOException( ex );
		}
	}

	/**
	 * Creates a document from a descriptor
	 *
	 * @return Document
	 * @throws ConceptPersistException
	 * @throws ParserConfigurationException 
	 */
	@Override
	public Document createDocument( ITemplate model ) 
			throws ParseException
	{
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element root = doc.createElement( IConcept.CONCEPT );
			doc.appendChild( root );
			this.createDocument( model, doc, root );
			return doc;
		}
		catch (ParserConfigurationException e) {
			throw new ParseException( e );
		}
		catch (ConceptPersistException e) {
			throw new ParseException( e );
		}
		catch (ConceptException e) {
			throw new ParseException( e );
		}
	}

	/**
	 * Creates a document from a descriptor
	 *
	 * @return Document
	 * @throws ConceptPersistException
	 */
	protected Document createDocument( IModelLeaf<? extends IDescriptor> template, Document doc, Element parentNode ) 
			throws ConceptException, ConceptPersistException
	{
		ITemplateAieon parent = ( TemplateAieon )template.getDescriptor();
		StoreConcept.createElement( doc, parentNode, parent );
		if(!( template instanceof ITemplateNode ))
			return doc;
		ITemplateNode<? extends IDescriptor> tn = ( ITemplateNode<? extends IDescriptor> )template;
		if( tn.hasChildren() == false )
			return doc;

		Element childNode = doc.createElement( S_CHILDREN );
		Element conceptNode;
		Collection<? extends IModelLeaf<? extends IDescriptor>>children = tn.getChildren();
		for( IModelLeaf<? extends IDescriptor> child: children ){
			logger.trace( "Adding child: " + child.getDescriptor().toString() );
			conceptNode = doc.createElement( IConcept.CONCEPT );
			try{
				createDocument( child, doc, conceptNode );
			}
			catch( Exception ex ){
				logger.error( ex.getMessage(), ex );
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
	 * @throws ModelException
	 * @throws ConceptException
	 */
	protected ITemplate parseDocument( Document doc ) throws ModelException, ConceptException
	{
		List<Node> elements = StoreDocument.getElements( doc.getChildNodes(), Node.ELEMENT_NODE );
		if( elements.size() != 1 )
			throw new ConceptException( StoreDocument.S_ERR_INVALID_FORMAT );

		Node root = elements.get( 0 );
		return new TemplateWrapper( parseElement( root ));  	
	}

	/**
	 * Parse a tree node model from the given document, and generating the given aspect
	 * @param doc
	 * @return
	 * @throws ConceptException
	 * @throws ConceptException
	 */
	protected ITemplateNode<IDescriptor> parseDocument( Document doc, IAspect aspect ) throws ConceptException, ConceptException
	{
		List<Node> elements = StoreDocument.getElements( doc.getChildNodes(), Node.ELEMENT_NODE );
		if( elements.size() != 1 )
			throw new ConceptException( StoreDocument.S_ERR_INVALID_FORMAT );

		Node root = elements.get( 0 );
		return parseElement( root, aspect, -1 );  	
	}

	/**
	 * Parse a tree node model from the given document
	 * @param doc
	 * @return
	 * @throws ConceptException
	 * @throws ConceptException
	 */
	protected ITemplateNode<IDescriptor> parseElement( Node node ) throws ConceptException, ConceptException
	{
		String str;
		ConceptParser parser = new ConceptParser();
		IDescriptor parent = parser.parseConceptNode( node );
		IDescriptor td = new Descriptor();
		BodyFactory.transfer(td, parent, true );

		ITemplateNode<IDescriptor> treeNode = new TemplateNode<IDescriptor>( td );
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
	 * Parse a tree node model from the given document
	 * @param doc
	 * @return
	 * @throws ConceptException
	 * @throws ConceptException
	 */
	protected ITemplateNode<IDescriptor> parseElement( Node node, IAspect aspect, int depth ) throws ConceptException, ConceptException
	{
		int aspectDepth = aspect.getDepth();
		if(( aspectDepth >=0 ) && ( depth > aspectDepth ))
			return null;

		String str;

		//TODO change
		ConceptParser parser = new ConceptParser();
		IDescriptor parent = parser.parseConceptNode( node );
		IDescriptor td = new Descriptor();
		BodyFactory.transfer(td, parent, true );
		ITemplateNode<IDescriptor> treeNode = 
				new TemplateNode<IDescriptor>( new Descriptor());
		ITemplateNode<IDescriptor> root = null;
		//IAspect pa = node.getAspect( aspect );
		//if((( pa != null ) && pa.getAspect().equals( aspect.getAspect() ))){
		//	root = treeNode;
		//	if(( aspectDepth < 0 ) || ( depth < aspectDepth )){
		//		depth = 0;
		//	}
		//}
		Node childNode = null;
		List<Node> children = StoreDocument.getElements( node.getChildNodes(), Node.ELEMENT_NODE );
		for( Node child: children ){
			str = child.getNodeName();
			if( str.equals( S_CHILDREN )){
				childNode = child;
				break;
			}
		}
		if(( childNode == null ) || ( depth >= aspectDepth )){
			return root;
		}

		children = StoreDocument.getElements( childNode.getChildNodes(), Node.ELEMENT_NODE );
		ITemplateNode<IDescriptor> tn;
		if(( root != null ) || ( depth > 0))
			depth +=1;
		for( Node child: children ){
			tn = parseElement( child, aspect, depth );
			if( tn == null )
				continue;
			if( root == null )
				return tn;
			//treeNode.addChild( tn );
		}
		return root;
	}

	@Override
	public void addListener(IParserListener<ITemplate> listener)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void removeListener(IParserListener<ITemplate> listener)
	{
		// TODO Auto-generated method stub

	}  

	/**
	 * Print the given concept model
	 * @param model IModelNode<? extends IDescriptor>
	 * @param boolean includeattrs: if true the attributes 
	 * @return
	 */
	public static final String print( ITemplate template, boolean includeAttrs )
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("{ Created: " + Descriptor.getCreateDate( template.getDescriptor()) + "}\n" );
		buffer.append("{ Updated: " + Descriptor.getUpdateDate( template.getDescriptor()) + "}\n" );
		if( includeAttrs )
			printExtendedTemplate( buffer, template, 0 );
		else
			printTemplate( buffer, template, 0 );
		return "\nTemplate:\n" + buffer.toString();
	}

	/**
	 * quick print of the structure of the given template
	 * @param leaf IModelNode<? extends IDescriptor>
	 * @return
	 */
	protected static final void printTemplate( StringBuffer buffer, IModelLeaf<? extends IDescriptor> leaf, int depth )
	{
		for( int i =0; i< depth; i++ )
			buffer.append( "\t" );
		buffer.append( leaf.getDescriptor().toString());
		buffer.append( "(" + leaf.getDepth() + ")\n" );
		if(!( leaf instanceof ITemplateNode ))
			return;
		ITemplateNode<? extends IDescriptor> md = 
				( ITemplateNode<? extends IDescriptor> )leaf;
		for( IModelLeaf<? extends IDescriptor> child: md.getChildren() )
			printTemplate( buffer, child, depth + 1);
	}  

	/**
	 * quick print of the structure of the given model
	 * @param leaf IModelNode<? extends IDescriptor>
	 * @return
	 */
	protected static final void printExtendedTemplate( StringBuffer buffer, IModelLeaf<? extends IDescriptor> leaf, int depth )
	{
		for( int i =0; i< depth; i++ )
			buffer.append( "\t" );
		buffer.append( leaf.getDescriptor().toString());
		buffer.append( "(" + leaf.getDepth() + ")\n" );
		buffer.append( "\t" );

		StoreConcept.print(null, buffer, leaf.getDescriptor(), depth, true );
		if(!( leaf instanceof ITemplateNode ))
			return;

		ITemplateNode<? extends IDescriptor> md = 
				( ITemplateNode<? extends IDescriptor> )leaf;
		for( IModelLeaf<? extends IDescriptor> child: md.getChildren() ){
			printExtendedTemplate( buffer, child, depth + 1);
		}
	}  

}