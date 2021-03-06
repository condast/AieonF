package org.aieonf.template.parser;

import java.util.List;

import org.aieonf.commons.xml.StoreDocument;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.ModelException;
import org.aieonf.template.core.TemplateNode;
import org.aieonf.template.def.ITemplateAieon;
import org.aieonf.template.def.ITemplateNode;
import org.w3c.dom.Node;

public class XMLTemplateNode<T extends ITemplateAieon> extends TemplateNode<T>
{
	//This interface defines the relationships with other concepts
	public static final String ATTRIBUTES = "Attributes";
	public static final String GROUP = "Group";
	public static final String CHILDREN = "Children";

	//Attributes
	public static final String ID = "id";

	public XMLTemplateNode(T descriptor){
		super(descriptor);
	}


	public void fill( Node node ){
		//Add the attributes
		if(( node == null ) || 
				( node.getNodeName().equals( ATTRIBUTES )) == false )
			return;
		setAttributes( this, node );

	}

	/**
	 * Set the attributes (the children of the given node) into the
	 * descriptor
	 * @param descriptor
	 * @param node
	 * @throws ModelException
	 */
	public static void setAttributes( ITemplateNode<? extends IDescriptor> tNode, Node node )
	{
		if( !node.getNodeName().equals( ATTRIBUTES ))
			return;

		List<Node> children = 
				StoreDocument.getElements( node.getChildNodes(), Node.ELEMENT_NODE );
		String key, value;
		for( Node child: children ){
			key = child.getNodeName();
			value = child.getTextContent();
			if( value != null )
				value = value.trim();
			tNode.getDescriptor().set( key, value );
		} 	
	}

}
