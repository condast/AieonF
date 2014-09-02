package org.aieonf.concept.context;

import org.aieonf.concept.context.IContextAieonFactory;
import org.aieonf.concept.core.ConceptException;

/**
 * The application configurator is defined by an
 * application aieon that contains all the necessary
 * info to create the application aieon
 * @author Kees Pieters
*/
public class ContextAieonFactory<T extends IContextAieon> implements IContextAieonFactory<T> 
{
	//Error messages
	public static final String S_ERR_AIEON_NOT_FOUND = 
		"The aieon could not be loaded. Terminating plugin: ";
	
	private T contextAieon;

	@Override
	public IContextAieon getContextAieon() throws ConceptException
	{
		return this.contextAieon;
	}

	/**
	 * Set the context aieon
	 * @param contextAieon
	 */
	protected void setContextAieon( T contextAieon ){
		this.contextAieon = contextAieon;
	}
}