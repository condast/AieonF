/**
 * 
 */
package org.aieonf.browsersupport.context;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.template.builder.TemplateInterpreter;
import org.aieonf.template.context.AbstractProviderContextFactory;

/**
 * @author Kees Pieters
 *
 */
public class ContextFactory extends AbstractProviderContextFactory<IDescriptor, IModelLeaf<IDescriptor>>
{
	private static final String S_BUNDLE_ID = "org.condast.aieonf.browsersupport";
	public static final String S_ERR_NO_CONNECTION = "The connection could not be made!";

	public static final String S_USER_HOME_PROPERTY = "user.home";

	private static ContextFactory factory = new ContextFactory();
	
	/**
	 * Create the application
	 * @param aieon
	 * @throws ConceptException
	 */
	private ContextFactory(){
		super( S_BUNDLE_ID, new TemplateInterpreter( ContextFactory.class )  );
	}
	
	public static ContextFactory getInstance() {
		return factory;
	}
}