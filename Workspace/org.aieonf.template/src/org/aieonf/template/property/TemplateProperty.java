package org.aieonf.template.property;

import org.aieonf.concept.IDescriptor;
import org.aieonf.template.ITemplateLeaf;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class TemplateProperty<T extends IDescriptor, U extends Enum<?>, V extends Object> extends AbstractTemplateProperty<T, U, ITemplateProperty.Attributes, V> implements Comparable<TemplateProperty<?,?,?>>
{
	public static final String S_DEFAULT_REGEX = ".+";

	public TemplateProperty( ITemplateLeaf<T> leaf, U key )
	{
		this( leaf, key, null );
	}
	
	public TemplateProperty( ITemplateLeaf<T> leaf, U key, V defaultValue )
	{
		super( leaf, key, defaultValue );
		super.setAttribute( ITemplateProperty.Attributes.TYPE, ITemplateProperty.Types.STRING.toString() );
		super.setAttribute( ITemplateProperty.Attributes.USE, ITemplateProperty.Usage.OPTIONAL.toString() );
		super.setAttribute( ITemplateProperty.Attributes.INIT, ITemplateProperty.Init.FIXED.toString() );
		super.setAttribute( ITemplateProperty.Attributes.REGEX, S_DEFAULT_REGEX );
	}

	/**
	 * Fill the attributes with the given list
	 * @param list
	 */
	public void fill( NamedNodeMap list ){

		Node node;
		for( int i=0; i<list.getLength(); i++ ){
			node = list.item( i );
			String key = node.getNodeName();
			if( !ITemplateProperty.Attributes.contains( key ))
				continue;

			ITemplateProperty.Attributes attr = ITemplateProperty.Attributes.valueOf( key.toUpperCase() );
			String value = node.getTextContent();
			super.setAttribute( attr, value );
		}
	}

	@Override
	public int compareTo(TemplateProperty<?,?,?> arg0)
	{
		return this.getKey().toString().compareTo( arg0.getKey().toString());
	}
}

