package org.aieonf.template.parser.attr;

import org.aieonf.commons.validation.AbstractValidator;
import org.aieonf.commons.validation.ValidationEvent;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.template.property.TemplateProperty;

public class TemplateAttributeValidator<T extends Enum<?>> extends AbstractValidator<T>
{
	public static final String S_ERR_REGEX_FAILURE = "The String is not correct: ";
	public static final String S_ERR_USE_FAILURE_MANDATORY = "The mandatory field is not filled in correctly: ";

	private TemplateProperty<IDescriptor, String> attr;
	
	public TemplateAttributeValidator( TemplateProperty<IDescriptor, String> attr )
	{
		super( attr.getKey() );		
		this.attr = attr;
	}
	
	protected TemplateProperty<IDescriptor, String> getAttr() {
		return attr;
	}

	/**
	 * Initialises the attribute with the given string. Returns a String if this is allowed or possible
	 * @param str
	 * @return
	 */
	public String initValue( String str ){
		if( Descriptor.isNull(str))
			return null;
		if( str.substring(0, 1).equals("{"))
			str = str.substring(1);
		if( str.substring(str.length()-1, str.length()).equals("}"))
			str = str.substring(0, str.length()-1);
/*
		if( attr.getInit() == null ) 
			return str;
		
		switch( attr.getInit() ){
			case CREATE:
				if( attr.getKey().equals( IDescriptor.Attributes.VERSION ))
					return String.valueOf( 1 );
				break;
			case DEFAULT:
				return str;
			case CLEAR:
				return null;
			default:
				return attr.getValue();
		}
		*/
		return str;
	}

	/**
	 * Validates the given String against the attributes. Returns a String if this is allowed or possible
	 * @param str
	 * @return
	 */
	@Override
	protected ValidationEvent<String, T> performValidation(T str)
	{
/*
		if( Descriptor.isNull(str))
			return new ValidationEvent<String, String>( this, super.getReference(), str, S_ERR_USE_FAILURE_MANDATORY, attr.getUsage().equals(Usage.OPTIONAL ));

		if( attr.getRegex() != null )
			if( !str.matches(attr.getRegex() ))
				new ValidationEvent<String, String>( this, super.getReference(), str, S_ERR_REGEX_FAILURE + attr);
		return new ValidationEvent<String, String>( this, super.getReference(), str, true );
*/
		return null;
	}
}
