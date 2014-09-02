package org.condast.aieonf.browsersupport.library.firefox;

import java.net.URI;

import org.aieonf.collections.connector.AbstractFileConnector;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.model.IModelFunction;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.IModelProvider;
import org.aieonf.model.function.AbstractFunction;
import org.aieonf.util.Utils;

public class FireFoxModelFunction extends AbstractFunction<ILoaderAieon, IModelProvider<ILoaderAieon,IModelLeaf<IConcept>>> implements IModelFunction<ILoaderAieon, IModelLeaf<IConcept>>
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
	public boolean canProvide(IModelLeaf<ILoaderAieon> leaf) {
		return super.canProvide(S_FUNCTION_PROVIDER_ID, leaf);
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
	protected IModelProvider<ILoaderAieon,IModelLeaf<IConcept>> onCreateFunction(IModelLeaf<ILoaderAieon> leaf) {
		ILoaderAieon baseLoader = getDefaultLoader(leaf);
		URI uri = AbstractFileConnector.getDefaultSource( DEFAULT_FIREFOX_ROOT, DEFAULT_SQLITE_BOOKMARKS_FILE );
		baseLoader.set( IConcept.Attributes.SOURCE, uri.getPath());

		baseLoader.setDescription( DEFAULT_FIREFOX_PROVIDER_NAME );
		IModelLeaf<ILoaderAieon> model = getModelForLoader(baseLoader, leaf);
		if( Utils.isNull( model.getIdentifier() ))
			model.setIdentifier( DEFAULT_FIREFOX_IDENTIFIER );
		IModelProvider<ILoaderAieon, IModelLeaf<IConcept>> provider;
		if( version == FireFoxVersion.firefox3 )
			provider = new FireFoxSQLiteBookmarkProvider<ILoaderAieon>( super.getAieon(), model );
		else
			provider = new FireFoxHTMLBookmarkProvider<ILoaderAieon>( super.getAieon(), model );
		return provider;
	}
}
