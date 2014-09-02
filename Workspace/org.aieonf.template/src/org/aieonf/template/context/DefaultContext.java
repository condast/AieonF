/**
 * 
 */
package org.aieonf.template.context;

import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.template.ITemplateLeaf;

/**
 * @author Kees Pieters
 *
 */
public class DefaultContext extends AbstractModelContext<IContextAieon>
{	
	/**
	 * Create the application
	 * @param aieon
	 * @throws ConceptException
	 */
	public DefaultContext( ITemplateLeaf<IContextAieon> model )
	{
		super( model );
	}
}