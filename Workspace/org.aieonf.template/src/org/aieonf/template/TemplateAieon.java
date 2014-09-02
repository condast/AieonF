package org.aieonf.template;

import java.net.URI;
import java.net.URISyntaxException;

import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.locator.ILocatorAieon;

public class TemplateAieon extends Descriptor implements ITemplateAieon
{
	/**
	 * For serialisation
	*/
	private static final long serialVersionUID = 3258757643369246204L;

	public static final String TEMPLATE = "Template";
	
	/**
	 * Create a default template aieon
	 */
	public TemplateAieon()
	{
		super();
	}

	/**
	 * Create a default template aieon
	 */
	public TemplateAieon( ITemplateLeaf<?> parent )
	{
		super( parent.getDescriptor() );
	}

	/**
	 * Get the template name
	 * @return
	*/
	public String getTemplate()
	{
		return this.get( TEMPLATE );
	}

	/**
	 * Get the (optional) id of the model. 
	 * @return
	 */
	@Override
	public String getIdentifier(){
		return super.get( ILocatorAieon.Attributes.IDENTIFIER.name() );
	}

	/**
	 * Set the identifier
	 * @param identifier
	 */
	@Override
	public void setIdentifier( String identifier ){
		this.set( ILocatorAieon.Attributes.IDENTIFIER.name(), identifier );
	}

	
	@Override
	public String getSource() {
		return this.getIdentifier();
	}

	@Override
	public URI getURI()
	{
		try {
			return new URI( this.getIdentifier());
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void setURI(URI source)
	{
		this.setIdentifier( source.getPath() );
	}
	
}