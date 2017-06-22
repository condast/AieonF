package org.condast.aieonf.browsersupport.library.firefox;

import java.net.URI;

import org.aieonf.collections.connector.AbstractFileConnector;
import org.aieonf.commons.Utils;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.function.AbstractFunctionProvider;
import org.aieonf.model.provider.IModelProvider;

public class FireFoxModelFunction extends AbstractFunctionProvider<IContextAieon, IModelProvider<IContextAieon, IDescriptor>>
{
	//Default location
	private static final String DEFAULT_FIREFOX_ROOT =
			"\\AppData\\Roaming\\Mozilla\\Firefox\\profiles";

	private static final String DEFAULT_FIREFOX_IDENTIFIER =
			"org.mozilla.firefox.bookmarks";

	private static final String DEFAULT_FIREFOX_PROVIDER_NAME =
			"Firefox";

	private static final String DEFAULT_SQLITE_BOOKMARKS_FILE =
			"places.sqlite";

	public static final String DEFAULT_HTML_BOOKMARKS_FILE =
			"bookmarks.html";

	public enum FireFoxVersion{
		firefox2,
		firefox3
	}
	private FireFoxVersion version;


	public FireFoxModelFunction(IContextAieon context ) {
		super( S_FUNCTION_PROVIDER_ID, context );
		version = FireFoxVersion.firefox3;
	}

	@Override
	public boolean canProvide(IContextAieon leaf) {
		return super.canProvide(S_FUNCTION_PROVIDER_ID);
	}

	/**
	 * Fill the loader with the given details
	 * @param loader
	 * @param identifier
	 * @param folder
	 * @param reference
	 * @throws DatabaseException
	 */
	public static void fillLoader( ILoaderAieon loader ) throws ConceptException{
		fillLoader( loader, DEFAULT_FIREFOX_IDENTIFIER, DEFAULT_FIREFOX_PROVIDER_NAME, DEFAULT_FIREFOX_ROOT, DEFAULT_SQLITE_BOOKMARKS_FILE );
	}

	
	@Override
	protected IModelProvider<IContextAieon, IDescriptor> onCreateFunction(IContextAieon leaf) {
		ILoaderAieon baseLoader = getDefaultLoader(leaf);
		baseLoader.setDescription( DEFAULT_FIREFOX_PROVIDER_NAME );
		IModelLeaf<IDescriptor> model = getModelForLoader(baseLoader, leaf );
		if( Utils.assertNull( model.getIdentifier() ))
			model.setIdentifier( DEFAULT_FIREFOX_IDENTIFIER );
		URI uri = AbstractFileConnector.getDefaultSource( DEFAULT_FIREFOX_ROOT, DEFAULT_SQLITE_BOOKMARKS_FILE );
		baseLoader.set( IConcept.Attributes.SOURCE, uri.getPath());

		IModelProvider<IContextAieon, IDescriptor> provider;
		if( version == FireFoxVersion.firefox3 )
			provider = new FireFoxSQLiteBookmarkProvider( leaf, model );
		else
			provider = new FireFoxHTMLBookmarkProvider( leaf, model );
		return provider;
	}
}
