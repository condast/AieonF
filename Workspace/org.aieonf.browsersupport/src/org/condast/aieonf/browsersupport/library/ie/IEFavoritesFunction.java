package org.condast.aieonf.browsersupport.library.ie;

import java.io.File;
import java.net.URI;

import org.aieonf.commons.Utils;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.IModelProvider;
import org.aieonf.model.function.AbstractFunctionProvider;

public class IEFavoritesFunction extends AbstractFunctionProvider<IDescriptor, IModelProvider<IModelLeaf<IDescriptor>>>
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
	public boolean canProvide(IModelLeaf<IDescriptor> leaf) {
		return super.canProvide(S_FUNCTION_PROVIDER_ID, leaf);
	}
	
	@Override
	protected IModelProvider<IModelLeaf<IDescriptor>> onCreateFunction(IModelLeaf<IDescriptor> leaf) {
		IDescriptor baseLoader = getDefaultLoader(leaf);
		baseLoader.setDescription( DEFAULT_EXPLORER_PROVIDER_NAME );
		//TODO IModelLeaf<IDescriptor> model = getModelForLoader(baseLoader, leaf);
		baseLoader.set( IConcept.Attributes.SOURCE, getDefaultSource().getPath());

		if( Utils.isNull( leaf.getIdentifier() ))
			leaf.setIdentifier( DEFAULT_IDENTIFIER );
		IModelProvider<IModelLeaf<IDescriptor>> provider = new IEFavoritesProvider( super.getAieon(), leaf );
		return provider;
	}

}
