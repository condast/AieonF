package org.aieonf.template.parser.attr;

import org.aieonf.commons.constraints.Cardinality;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.model.constraints.Aspect;
import org.aieonf.model.constraints.IAspect;
import org.aieonf.template.def.ITemplateLeaf;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TemplateAttributes
{
	public static final String S_ATTRIBUTES = "Attributes";
	
	public enum Attributes{
		ASPECT,
		CARDINALITY,
		DIRECTION;

		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString()
		{
			return prettyString( super.toString() );
		}

		/**
		 * Returns true if the given string is a valid attribute
		 * @param str
		 * @return
		 */
		public static boolean contains( String str ){
			if( Descriptor.assertNull(str))
				return false;
			Attributes[] attrs = Attributes.values();
			for( Attributes attr: attrs ){
				if( attr.name().equals( str.toUpperCase() ))
					return true;
			}
			return false;
		}
	}
	
	public enum Direction{
		TOP_DOWN,
		BOTTOM_UP,
		BI;
			
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString()
		{
			return prettyString( super.toString() );
		}		
		
	}

	private Cardinality cardinality;	
	private Direction direction;
	private Aspect aspect;
	
	private ITemplateLeaf<?> parent;
		
	public TemplateAttributes( ITemplateLeaf<?> parent )
	{
		this.parent = parent;
		this.cardinality = Cardinality.ONEORMORE;
		this.direction = Direction.BI;
	}

	
	public ITemplateLeaf<?> getParent() {
		return parent;
	}

	/**
	 * Fill the attributes with the given list
	 * @param list
	 * @throws ConceptException 
	 */
	public void fill( NodeList list ) throws ConceptException{
		
		Node node;
		for( int i=0; i<list.getLength(); i++ ){
  		node = list.item( i );
  		if( node.getNodeType() != Node.ELEMENT_NODE )
  			continue;
 			String key = node.getNodeName();
  		if( !Attributes.contains( key ))
  			continue;	
  		String text = node.getTextContent();
  		if( Descriptor.assertNull( text ))
  			return;
  		
  		Attributes attr = Attributes.valueOf( key.toUpperCase() );
  		switch( attr ){
  			case CARDINALITY:
  				this.cardinality = Cardinality.toCardinality( text );
  				break;
  			case DIRECTION:
  				this.direction = Direction.valueOf( text );
  				break;
  			case ASPECT:
  				if( this.aspect == null ){
  					this.aspect = new Aspect( text );
  					this.aspect.fill( node.getAttributes());
  				}
  				else
  				  this.aspect.setAspect( text );
				default:
					break;
  		}
		}
	}
	
	/**
	 * @return the cardinality
	 */
	public final Cardinality getCardinality()
	{
		return cardinality;
	}

	/**
	 * @return the direction
	 */
	public final Direction getDirection()
	{
		return direction;
	}

	/**
	 * @return the aspect
	 */
	public final IAspect getAspect()
	{
		return aspect;
	}

	/**
	 * Create a pretty string from the given one
	 * @param strng
	 * @return
	 */
	public static String prettyString( String strng ){
		char chr = strng.charAt(0);
		String str = strng.toString().toLowerCase().substring(1);
		return String.valueOf(chr) + str;		
	}
}

