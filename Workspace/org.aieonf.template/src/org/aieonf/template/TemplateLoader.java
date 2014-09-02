package org.aieonf.template;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;

import org.aieonf.concept.context.ContextAieon;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.loader.LoaderAieon;

public class TemplateLoader extends LoaderAieon
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1617364546377576693L;

	public static final String S_TEMPLATES = "Templates";
	
	public TemplateLoader(String identifier, File file) throws ConceptException, MalformedURLException
	{
		super(identifier, file.toURI() );
	}

	public TemplateLoader( ContextAieon aieon )
		throws ConceptException, MalformedURLException
	{
		super( S_TEMPLATES, getDefaultTemplatesDir( aieon ));
	}

	public TemplateLoader(String identifier, URI uri) throws ConceptException, MalformedURLException
	{
		super(identifier, uri);
	}
	
	/**
	 * Get the templates directory
	 * @return
	 */
	public URI getTemplateDirectory(){
		return super.getURI();
	}
	
	/**
	 * Get the default application directory. This is '.\config\<organisation>\'
	 * @param aieon
	 * @return
	 */
	public static URI getDefaultTemplatesDir( ContextAieon aieon )
	{
		File file = new File( "." + File.separator +	S_TEMPLATES + File.separator + 
			aieon.getApplicationName() + File.separator +
			aieon.getSource() + File.separator );
		return file.toURI();
	}
	

}
