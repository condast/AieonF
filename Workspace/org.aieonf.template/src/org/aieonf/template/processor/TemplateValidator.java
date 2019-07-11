package org.aieonf.template.processor;

import org.aieonf.commons.validation.AbstractValidator;
import org.aieonf.commons.validation.IValidator;
import org.aieonf.commons.validation.ValidationEvent;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.template.core.ITemplate;
import org.aieonf.template.parser.attr.TemplateAttributes;

public class TemplateValidator extends AbstractValidator<String> implements IValidator<String,String>
{
	private ITemplate template;
	
	public TemplateValidator( String attribute, ITemplate template )
	{
		super( attribute );
		this.template = template;
	}

	@Override
	protected ValidationEvent<String, String> performValidation( String value)
	{
		TemplateAttributes ta = null;//this.template.getTemplateAttributes();
		if( !ta.getAspect().getAspect().equals( super.getReference() ))
			return new ValidationEvent<String, String>( this, false);
		return new ValidationEvent<String, String>( this, (( Descriptor.isNull( value )) || ( value.length() <= 25)));
	}

	protected boolean performValidation(String aspect, String attribute, String value)
	{
		TemplateAttributes ta = null;//this.template.getTemplateAttributes();
		if( !ta.getAspect().getAspect().equals( super.getReference() ))
			return false;
		return (( Descriptor.isNull( value )) || ( value.length() > 25));
	}

}
