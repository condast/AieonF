package org.aieonf.template.property;

import org.aieonf.commons.strings.StringStyler;
import org.aieonf.concept.core.Descriptor;

public interface ITemplateProperty<T extends Object, U extends Object, V extends Object> {

	public enum Attributes{
		INIT,
		TYPE,
		REGEX,
		USE, 
		CLASS,
		CREATE,
		DEFAULT;
	
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString()
		{
			return StringStyler.xmlStyleString( super.toString() );
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
				if( attr.name().toLowerCase().equals( str ))
					return true;
			}
			return false;
		}
	}

	public enum Types{
		STRING,
		INT,
		LONG,
		BOOLEAN,
		URL,
		ENUM;
	
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString()
		{
			return StringStyler.xmlStyleString( super.toString() );
		}		
	
	}

	public enum Usage{
		OPTIONAL,
		MANDATORY;
	
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString()
		{
			return StringStyler.xmlStyleString( super.toString() );
		}			
	}

	/**
	 * The initial values fo an attribute, when they are loaded. 
	 * DEFAULT: The template value is a default
	 * CLEAR: The attribute should be empty
	 * FIXED: The template attribute must be used and may not change (default setting)
	 * @author Kees
	 *
	 */
	public enum Init{
		CREATE,
		DEFAULT,
		CLEAR,
		FIXED;
	
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString()
		{
			return StringStyler.xmlStyleString( super.toString() );
		}			
	}

	/**
	 * The key of the property
	 * @return
	 */
	public T getKey();
	
	/**
	 * Set the value of the property
	 * @param value
	 */
	public void setValue( V value );
	
	/**
	 * Verify the given value with the internal constraints 
	 * @param value
	 * @return
	 */
	public boolean verify( V value );
	
	/**
	 * Get the attribute with the given name
	 * @param attr
	 * @return
	 */
	public String getAttribute( U attr ); 

	/**
	 * Set the attribute value with the given name
	 * @param attr
	 * @param value
	 */
	public boolean setAttribute( U attr, String value ); 

	/**
	 * Remove the attribute
	 * @param attr
	 * @return
	 */
	public Object removeAttribute( U attr ); 

	/**
	 * Get a list of attributes
	 * @return
	 */
	public U[] attributes();
}
