/**
 * 
 */
package org.condast.aieonf.browsersupport.context;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.body.BodyFactory;
import org.aieonf.concept.context.*;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.library.ManifestAieon;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.concept.loader.LoaderAieon;
import org.aieonf.model.IModelProvider;
import org.aieonf.template.ITemplateLeaf;
import org.aieonf.template.context.AbstractSimpleContextFactory;
import org.aieonf.template.context.IContext;
import org.aieonf.util.logger.Logger;

/**
 * @author Kees Pieters
 *
 */
public class BrowserSupportContextFactory extends AbstractSimpleContextFactory<IModelProvider<IContextAieon, IConcept>>
{
	private static final String S_BUNDLE_ID = "org.condast.aieonf.browsersupport";
	public static final String S_ERR_NO_CONNECTION = "The connection could not be made!";

	public static final String S_USER_HOME_PROPERTY = "user.home";

	//Get the logger
	private Logger logger;

	/**
	 * Create the application
	 * @param aieon
	 * @throws ConceptException
	 */
	public BrowserSupportContextFactory()
	{
		super( S_BUNDLE_ID );
		logger = Logger.getLogger( this.getClass() );
	}
	
	@Override
	protected IContext<IContextAieon> onCreateContext(
			ITemplateLeaf<IContextAieon> model) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Create a loader for this context
	 * @param context
	 * @return
	 * @throws ConceptException
	 */
	protected ILoaderAieon createLoader( IContextAieon context ) throws ConceptException{
		LoaderAieon loader = new LoaderAieon();
		BodyFactory.transfer( loader, context, false );
		loader.setAieonCreatorClass( ILoaderAieon.class );
		loader.setReadOnly( false );
		loader.setStoreInternal( false );
		loader.setCreatable(false);
		logger.info( "Getting source " + loader.getSource() );	
		return new ManifestAieon( loader );
	}
}