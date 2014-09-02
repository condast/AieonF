package org.condast.aieonf.browsersupport.library.chromium;

import java.net.URI;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.model.IModelFunction;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.IModelProvider;
import org.aieonf.model.function.AbstractFunction;
import org.aieonf.util.Utils;
import org.condast.aieonf.osgi.utils.ProjectFolderUtils;

public class ChromiumModelFunction extends AbstractFunction<ILoaderAieon, IModelProvider<ILoaderAieon,IModelLeaf<IConcept>>> implements IModelFunction<ILoaderAieon, IModelLeaf<IConcept>> {

	//Default identifier
	public static final String DEFAULT_IDENTIFIER =
			"com.google.chromium.browser.bookmarks";

	//Default location
	private static final String DEFAULT_CHROMIUM_ROOT =
			"\\AppData\\Local\\Google\\Chrome\\User Data\\Default";

	private static final String DEFAULT_CHROMIUM_IDENTIFIER =
			"org.google.chrome.bookmarks";

	private static final String DEFAULT_CHROMIUM_PROVIDER_NAME ="Chrome";

	public static final String DEFAULT_BOOKMARKS_FILE ="Bookmarks";

	public ChromiumModelFunction( IContextAieon context ) {
		super( S_FUNCTION_PROVIDER_ID, context );
	}

	@Override
	public boolean canProvide(IModelLeaf<ILoaderAieon> leaf) {
		if( DEFAULT_CHROMIUM_IDENTIFIER.equals( leaf.getIdentifier() ))
			return true;
		return super.canProvide(S_FUNCTION_PROVIDER_ID, leaf);
	}

	@Override
	protected IModelProvider<ILoaderAieon,IModelLeaf<IConcept>> onCreateFunction(IModelLeaf<ILoaderAieon> leaf) {
		ILoaderAieon baseLoader = getDefaultLoader(leaf);
		URI uri = ProjectFolderUtils.appendToUserDir(DEFAULT_CHROMIUM_ROOT + "//" + DEFAULT_BOOKMARKS_FILE, false );
		baseLoader.set( IConcept.Attributes.SOURCE, uri.toString() );
		baseLoader.setURI( uri );
		baseLoader.setDescription( DEFAULT_CHROMIUM_PROVIDER_NAME );
		IModelLeaf<ILoaderAieon> model = getModelForLoader(baseLoader, leaf);
		if( Utils.isNull( model.getIdentifier() ))
			model.setIdentifier( DEFAULT_CHROMIUM_IDENTIFIER );
		IModelProvider<ILoaderAieon,IModelLeaf<IConcept>> gdb = new ChromiumBookmarkProvider<ILoaderAieon>( super.getAieon(), model );
		return gdb;
	}
}