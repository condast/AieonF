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

public class ChromiumModelFunctionProvider extends AbstractFunctionProvider<IDescriptor, IModelProvider<IModelLeaf<IDescriptor>>> {

	

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
	public boolean canProvide(IModelLeaf<IDescriptor> leaf) {
		if( DEFAULT_CHROMIUM_IDENTIFIER.equals( leaf.getIdentifier() ))
			return true;
		return super.canProvide(S_FUNCTION_PROVIDER_ID, leaf);
	}

	@Override
	protected IModelProvider<IModelLeaf<IDescriptor>> onCreateFunction(IModelLeaf<IDescriptor> leaf) {
		ILoaderAieon baseLoader = getDefaultLoader(leaf);
		URI uri = ProjectFolderUtils.appendToUserDir(DEFAULT_CHROMIUM_ROOT + "//" + DEFAULT_BOOKMARKS_FILE, false );
		baseLoader.set( IConcept.Attributes.SOURCE, uri.toString() );
		baseLoader.setURI( uri );
		baseLoader.setDescription( DEFAULT_CHROMIUM_PROVIDER_NAME );
		IModelLeaf<IDescriptor> model = getModelForLoader(baseLoader, leaf);
		if( Utils.isNull( model.getIdentifier() ))
			model.setIdentifier( DEFAULT_CHROMIUM_IDENTIFIER );
		IModelProvider<IModelLeaf<IDescriptor>> gdb = new ChromiumBookmarkProvider( super.getAieon(), model );
		return gdb;
	}
}