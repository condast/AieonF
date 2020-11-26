package org.aieonf.browsersupport.library.chromium;

import java.net.URI;

import org.aieonf.commons.Utils;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.file.ProjectFolderUtils;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.function.AbstractFunctionProvider;
import org.aieonf.model.provider.IModelProvider;

public class ChromiumModelFunctionProvider extends AbstractFunctionProvider<String, IModelProvider<IDomainAieon, IDescriptor, IModelLeaf<IDescriptor>>> {

	//Default location
	private static final String DEFAULT_CHROMIUM_ROOT =
			"\\AppData\\Local\\Google\\Chrome\\User Data\\Default";

	private static final String DEFAULT_CHROMIUM_IDENTIFIER =
			"org.google.chrome.bookmarks";

	private static final String DEFAULT_CHROMIUM_PROVIDER_NAME ="Chrome";

	private static final String DEFAULT_BOOKMARKS_FILE ="Bookmarks";

	private IContextAieon context;
	
	public ChromiumModelFunctionProvider( IContextAieon context ) {
		super( S_FUNCTION_PROVIDER_ID );
		this.context = context;
	}

	@Override
	public boolean canProvide( String functionName) {
		if( DEFAULT_CHROMIUM_IDENTIFIER.equals( functionName ))
			return true;
		return super.canProvide( functionName );
	}

	
	@Override
	protected IModelProvider<IDomainAieon, IDescriptor, IModelLeaf<IDescriptor>> onCreateFunction( String functionName) {
		ILoaderAieon baseLoader = getDefaultLoader( context);
		URI uri = ProjectFolderUtils.appendToUserDir(DEFAULT_CHROMIUM_ROOT + "//" + DEFAULT_BOOKMARKS_FILE, false );
		baseLoader.set( IConcept.Attributes.SOURCE.name(), uri.toString() );
		baseLoader.setURI( uri );
		baseLoader.setDescription( DEFAULT_CHROMIUM_PROVIDER_NAME );
		IModelLeaf<IDescriptor> model = getModelForLoader(baseLoader, context);
		if( Utils.assertNull( model.getIdentifier() ))
			model.setIdentifier( DEFAULT_CHROMIUM_IDENTIFIER );
		IModelProvider<IDomainAieon, IDescriptor, IModelLeaf<IDescriptor>> gdb = new ChromiumBookmarkProvider( baseLoader );
		return gdb;
	}
}