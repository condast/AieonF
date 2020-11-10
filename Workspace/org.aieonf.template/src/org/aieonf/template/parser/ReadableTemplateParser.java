/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Condast</p>
 * @author Kees Pieters
 * @version 1.0
 */
package org.aieonf.template.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.aieonf.commons.parser.IParser;
import org.aieonf.commons.parser.IParserListener;
import org.aieonf.commons.parser.ParseException;
import org.aieonf.commons.xml.StoreDocument;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.body.BodyFactory;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.persist.ConceptPersistException;
import org.aieonf.concept.xml.ConceptParser;
import org.aieonf.concept.xml.StoreConcept;
import org.aieonf.model.constraints.IAspect;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.ModelException;
import org.aieonf.template.core.TemplateAieon;
import org.aieonf.template.core.TemplateNode;
import org.aieonf.template.def.ITemplate;
import org.aieonf.template.def.ITemplateAieon;
import org.aieonf.template.def.ITemplateLeaf;
import org.aieonf.template.def.ITemplateNode;
import org.aieonf.template.parser.attr.TemplateAttributeValidator;
import org.aieonf.template.property.TemplateProperty;

/**
 *
 * <p>Title: AieonF</p>
 *
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 *  This method maintains all the general settings of this program
 * <p>Copyright: Copyright (c) 2012</p>
 *
 * <p>Company: Condast</p>
 *
 * @author Kees Pieters
 * @version 1.0
 */
public class ReadableTemplateParser implements IParser<ITemplateLeaf<? extends IDescriptor>>
{

	//Supported error messages
	public static final String S_PARENT = "Parent";
	public static final String S_CHILDREN = "Children";

	public static final String S_ERR_INVALID_FORM = "The model has an invalid form. A Template aieon with an identifier was not recognised";

	private Collection<IParserListener<ITemplateLeaf<? extends IDescriptor>>> listeners;

	public enum Specify{
		ALL,
		ASPECT,
		SKIPROOT
	}
	private Specify specify;

	private ITemplateAieon aieon;

	protected ReadableTemplateParser( Specify specify )
	{
		super();
		this.specify = specify;
		listeners = new ArrayList<IParserListener<ITemplateLeaf<? extends IDescriptor>>>();
	}

	public ReadableTemplateParser()
	{
		this( Specify.ALL );
	}

	public ReadableTemplateParser( boolean skipRoot )
	{
		this( Specify.ALL );
		if( skipRoot )
			specify = Specify.SKIPROOT;
	}

	public ReadableTemplateParser( IAspect aspect )
	{
		this( Specify.ASPECT );
	}

	/**
	 * @return the specify
	 */
	public final Specify getSpecify()
	{
		return specify;
	}

	/**
	 * Read an object from the given location
	 * @param location String
	 * @return T
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ITemplate parse( InputStream in ) throws IOException
	{
		try{
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse ( in );
			ITemplate template = parse( doc );
			switch( specify ){
			case SKIPROOT:
				return new TemplateWrapper<IDescriptor>( (ITemplateLeaf<IDescriptor>) template.getChildren().keySet().iterator().next() );
			case ASPECT:
				return new TemplateWrapper<IDescriptor>( (ITemplateLeaf<IDescriptor>) template.getChildren().keySet().iterator().next() );
			default:
				return template;
			}
		} 
		catch( Exception ex ){
			throw new IOException( ex );
		}
	}

	/**
	 * Parses a concept from a document
	 *
	 * @param doc Document
	 * @return IConcept
	 * @throws ModelException
	 */
	protected ITemplate parse( Document doc ) throws ModelException
	{
		List<Node> children = StoreDocument.getElements( doc.getChildNodes(), Node.ELEMENT_NODE );
		if( children.size() != 1 )
			throw new ModelException( StoreDocument.S_ERR_INVALID_FORMAT );

		Node root = children.get( 0 );
		return parse( root );
	}

	/**
	 * Parses a concept from a root element
	 *
	 * @param doc Document
	 * @return IConcept
	 * @throws ModelException
	 */
	public ITemplate parse( Node element ) throws ModelException
	{
		List<Node> children = StoreDocument.getElements( element.getChildNodes(), Node.ELEMENT_NODE );
		if(( children == null ) || ( children.size() != 1 ))
			throw new ModelException( StoreDocument.S_ERR_INVALID_FORMAT );
		Node root = children.get( 0 );
		ITemplateAieon aieon = new TemplateAieon();
		ITemplateNode<IDescriptor> tRoot = new TemplateNode<IDescriptor>( aieon );
		//parseTemplate( tRoot, root );
		return new TemplateWrapper<IDescriptor>( tRoot );
	}

	/**
	 * Creates a document from a descriptor
	 *
	 * @return Document
	 * @throws ConceptPersistException
	 * @throws ParserConfigurationException 
	 */
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
		ITemplateNode<? extends IDescriptor> tn = (org.aieonf.template.def.ITemplateNode<? extends IDescriptor> )template;
		if( tn.hasChildren() == false )
			return doc;

		Element childNode = doc.createElement( S_CHILDREN );
		Element conceptNode = null;
		Collection<? extends IModelLeaf<? extends IDescriptor>>children = tn.getChildren().keySet();
		for( IModelLeaf<? extends IDescriptor> child: children ){
			try{
				createDocument( child, doc, conceptNode );
			}
			catch( Exception ex ){
				ex.printStackTrace();
			}
			childNode.appendChild( conceptNode );
		}
		parentNode.appendChild( childNode );
		return doc;
	}

	/**
	 * Parses a concept from a root element
	 *
	 * @param doc Document
	 * @return IConcept
	 * @throws ModelException
	 */
	protected void parseTemplate( ITemplateNode<IDescriptor> tNode, Node element ) throws ModelException
	{
		String str;
		Node child;
		NodeList children = element.getChildNodes();
		for( int i=0; i< children.getLength(); i++ ){
			child = children.item(i);
			if( child.getNodeType() != Node.ELEMENT_NODE )
				continue;
			str = child.getNodeName().toLowerCase();
			if( str.equals( XMLTemplateNode.ATTRIBUTES.toLowerCase() )){
				//tNode.fill(child);
				continue;
			}
			if( str.equals( IConcept.CONCEPT.toLowerCase() )){
				try {
					IDescriptor descriptor;
					if( tNode.getDescriptor() == null ){
						descriptor = new Descriptor();
					}
					else
						descriptor = tNode.getDescriptor();
					validateNode( descriptor, child );
					if( tNode.getDescriptor() instanceof ITemplateAieon ){
						this.aieon = ( ITemplateAieon)tNode.getDescriptor();
					}else{
						if( this.aieon == null )
							throw new ModelException( S_ERR_INVALID_FORM );
						BodyFactory.IDFactory( tNode.getDescriptor() );
					}
					continue;
				}
				catch (Exception e) {
					throw new ModelException( e );
				}

			}
			if( str.equals( XMLTemplateNode.CHILDREN.toLowerCase() )){
				NodeList chldren = child.getChildNodes();
				Node chld;
				for( int j=0; j < chldren.getLength(); j++ ){
					chld = chldren.item( j );
					if( chld.getNodeType() != Node.ELEMENT_NODE )
						continue;
					ITemplateNode<IDescriptor> tChild = new TemplateNode<IDescriptor>( null, null );
					tNode.addChild( tChild );
					parseTemplate( tChild, chld );
				}
				continue;
			}
		}
		this.notifyParseListeners(tNode);
	}

	/**
	 * notify the listeners
	 * @param node
	 */
	protected void notifyParseListeners( ITemplateLeaf<? extends IDescriptor> node ){
		for( IParserListener<ITemplateLeaf<? extends IDescriptor>> listener: listeners )
			listener.notifyParsed( node );
	}

	/**
	 * Validate the node based on its attribute list.
	 * @param tNode
	 * @param node
	 * @throws DOMException
	 * @throws ParseException
	 * @throws ConceptException
	 */
	@SuppressWarnings("null")
	protected static final void validateNode( ITemplateLeaf<? extends IDescriptor> tNode, Node node ) throws DOMException, ParseException, ConceptException{
		TemplateProperty<?,?> tda = null;//new TemplateProperty( tNode.getDescriptor(), node );
		tda.fill(node.getAttributes() );
		TemplateAttributeValidator<?> validator = new TemplateAttributeValidator( tda );
		String result = validator.initValue( node.getTextContent() );
		if( !Descriptor.assertNull( result )){
			tNode.getDescriptor().set( node.getNodeName(), result );
		};
	}

	/**
	 * Validate the node based on its attribute list.
	 * @param target
	 * @param node
	 * @throws DOMException
	 * @throws ParseException
	 * @throws ConceptException
	 */
	protected static final void validateNode( IDescriptor target, Node node ) throws DOMException, ParseException, ConceptException{
		ConceptParser parser = new ConceptParser();
		IDescriptor source = parser.parseConceptNode( node );
		Iterator<String> iterator = source.keySet();
		String key, value;
		while( iterator.hasNext() ){
			key = iterator.next();
			value = source.get( key );
			TemplateProperty<?,?> ta = null;//new TemplateProperty( target, key, source.get( key ));
			ta.fill( parser.getDirective(key));
			//target.addAttribute( ta );
			TemplateAttributeValidator<?> validator = new TemplateAttributeValidator( ta );
			String result = validator.initValue( value );
			if( !Descriptor.assertNull( result ))
				target.set( key,  result);
		}
	}

	@Override
	public Document createDocument(
			ITemplateLeaf<? extends IDescriptor> object)
					throws ParseException
	{
		return null;
	}

	@Override
	public void addListener( IParserListener<ITemplateLeaf<? extends IDescriptor>> listener)
	{
		this.listeners.add( listener );
	}

	@Override
	public void removeListener( IParserListener<ITemplateLeaf<? extends IDescriptor>> listener)
	{
		this.listeners.remove( listener );
	}
}
