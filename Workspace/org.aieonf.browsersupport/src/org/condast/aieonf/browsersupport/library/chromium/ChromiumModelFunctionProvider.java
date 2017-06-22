package org.condast.aieonf.browsersupport.library.chromium;

import java.net.URI;

import org.aieonf.commons.Utils;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.file.ProjectFolderUtils;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.function.AbstractFunctionProvider;
import org.aieonf.model.provider.IModelProvider;

public class ChromiumModelFunctionProvider extends AbstractFunctionProvider<IContextAieon,  
IModelProvider<IContextAieon, IDescriptor>> {

	//Default location
	private static final String DEFAULT_CHROMIUM_ROOT =
			"\\AppData\\Local\\Google\\Chrome\\User Data\\Default";

	private static final String DEFAULT_CHROMIUM_IDENTIFIER =
			"org.google.chrome.bookmarks";

	private static final String DEFAULT_CHROMIUM_PROVIDER_NAME ="Chrome";

	private static final String DEFAULT_BOOKMARKS_FILE ="Bookmarks";

	public ChromiumModelFunctionProvider( IContextAieon context ) {
		super( S_FUNCTION_PROVIDER_ID, context );
	}

	@Override
	public boolean canProvide( IContextAieon leaf) {
		if( DEFAULT_CHROMIUM_IDENTIFIER.equals( leaf ))
			return true;
		return super.canProvide(S_FUNCTION_PROVIDER_ID );
	}

	@Override
	protected IModelProvider<IContextAieon, IDescriptor> onCreateFunction( IContextAieon leaf) {
		ILoaderAieon baseLoader = getDefaultLoader(leaf);
		URI uri = ProjectFolderUtils.appendToUserDir(DEFAULT_CHROMIUM_ROOT + "//" + DEFAULT_BOOKMARKS_FILE, false );
		baseLoader.set( IConcept.Attributes.SOURCE, uri.toString() );
		baseLoader.setURI( uri );
		baseLoader.setDescription( DEFAULT_CHROMIUM_PROVIDER_NAME );
		IModelLeaf<IDescriptor> model = getModelForLoader(baseLoader, leaf);
		if( Utils.assertNull( model.getIdentifier() ))
			model.setIdentifier( DEFAULT_CHROMIUM_IDENTIFIER );
		IModelProvider<IContextAieon, IDescriptor> gdb = new ChromiumBookmarkProvider( leaf, model );
		return gdb;
	}
}