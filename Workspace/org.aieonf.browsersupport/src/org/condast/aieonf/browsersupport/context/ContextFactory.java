/**
 * 
 */
package org.condast.aieonf.browsersupport.context;

import java.util.logging.Logger;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.body.BodyFactory;
import org.aieonf.concept.context.*;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.library.ManifestAieon;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.concept.loader.LoaderAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.template.context.AbstractProviderContextFactory;

/**
 * @author Kees Pieters
 *
 */
public class ContextFactory extends AbstractProviderContextFactory<IContextAieon, IModelLeaf<IDescriptor>>
{
	private static final String S_BUNDLE_ID = "org.condast.aieonf.browsersupport";
	public static final String S_ERR_NO_CONNECTION = "The connection could not be made!";

	public static final String S_USER_HOME_PROPERTY = "user.home";

	//Get the logger
	  private Logger logger = Logger.getLogger( this.getClass().getName());

	/**
	 * Create the application
	 * @param aieon
	 * @throws ConceptException
	 */
	public ContextFactory()
	{
		super( S_BUNDLE_ID, ContextFactory.class );
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