package org.condast.aieonf.browsersupport.library.ie;

import java.io.File;
import java.net.URI;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.model.IModelFunction;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.IModelProvider;
import org.aieonf.model.function.AbstractFunction;
import org.aieonf.util.Utils;

public class IEFavoritesFunction extends AbstractFunction<ILoaderAieon, IModelProvider<ILoaderAieon,IModelLeaf<IDescriptor>>> implements IModelFunction<ILoaderAieon, IModelLeaf<IDescriptor>> 
{
	//Default identifier
	private static final String DEFAULT_IDENTIFIER =
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
	public boolean canProvide(IModelLeaf<ILoaderAieon> leaf) {
		return super.canProvide(S_FUNCTION_PROVIDER_ID, leaf);
	}
	
	@Override
	protected IModelProvider<ILoaderAieon,IModelLeaf<IDescriptor>> onCreateFunction(IModelLeaf<ILoaderAieon> leaf) {
		ILoaderAieon baseLoader = getDefaultLoader(leaf);
		baseLoader.setDescription( DEFAULT_EXPLORER_PROVIDER_NAME );
		IModelLeaf<ILoaderAieon> model = getModelForLoader(baseLoader, leaf);
		baseLoader.set( IConcept.Attributes.SOURCE, getDefaultSource().getPath());

		if( Utils.isNull( model.getIdentifier() ))
			model.setIdentifier( DEFAULT_IDENTIFIER );
		IModelProvider<ILoaderAieon, IModelLeaf<IDescriptor>> provider = new IEFavoritesProvider<ILoaderAieon>( super.getAieon(), model );
		return provider;
	}

}
