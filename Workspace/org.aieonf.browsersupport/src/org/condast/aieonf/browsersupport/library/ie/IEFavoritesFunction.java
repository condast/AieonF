package org.condast.aieonf.browsersupport.library.ie;

import java.io.File;
import java.net.URI;

import org.aieonf.commons.Utils;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.function.AbstractFunctionProvider;
import org.aieonf.model.provider.IModelProvider;

public class IEFavoritesFunction extends AbstractFunctionProvider<IContextAieon, String, 
IModelProvider<IDomainAieon, IModelLeaf<IDescriptor>>>
{
	//Default identifier
	private static final String DEFAULT_IE_IDENTIFIER =
			"com.microsoft.explorer.favorites";
	
	private static final String DEFAULT_IE_ROOT = "\\Favorites";

	private static final String DEFAULT_EXPLORER_PROVIDER_NAME ="Explorer";

	public IEFavoritesFunction( IContextAieon context ) {
		super( S_FUNCTION_PROVIDER_ID, context );
	}

	/**
	 * Get the default location for the personal database
	 * @return String
	 */
	public static URI getDefaultSource()
	{
		if( System.getProperty( "os.name").toLowerCase().startsWith( "windows" )){
			File file = new File( System.getProperty( "user.home" ) + DEFAULT_IE_ROOT );
			return file.toURI();
		}
		return null;
	}

	@Override
	protected IModelProvider<IDomainAieon, IModelLeaf<IDescriptor>> onCreateFunction( String aieon ) {
		ILoaderAieon baseLoader = getDefaultLoader(super.getAieon());
		baseLoader.setDescription( DEFAULT_EXPLORER_PROVIDER_NAME );
		IModelLeaf<IDescriptor> model = getModelForLoader(baseLoader, super.getAieon() );
		if( Utils.assertNull( model.getIdentifier() ))
			model.setIdentifier( DEFAULT_IE_IDENTIFIER );
		baseLoader.set( IConcept.Attributes.SOURCE, getDefaultSource().getPath());

		return  new IEFavoritesProvider( super.getAieon());
	}
}
