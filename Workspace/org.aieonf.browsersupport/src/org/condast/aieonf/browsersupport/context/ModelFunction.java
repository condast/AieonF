package org.condast.aieonf.browsersupport.context;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.IModelProvider;
import org.aieonf.model.function.AbstractFunction;
import org.aieonf.template.provider.CombinedProvider;
import org.condast.aieonf.browsersupport.library.chromium.ChromiumModelFunction;
import org.condast.aieonf.browsersupport.library.firefox.FireFoxModelFunction;
import org.condast.aieonf.browsersupport.library.ie.IEFavoritesFunction;

public class ModelFunction extends AbstractFunction<ILoaderAieon, IModelProvider<ILoaderAieon,IModelLeaf<IDescriptor>>>{

	private ChromiumModelFunction cmf;
	private FireFoxModelFunction ffmf;
	private IEFavoritesFunction ieff;
	
	public ModelFunction( IContextAieon context ) {
		super( S_FUNCTION_PROVIDER_ID, context );
		cmf = new ChromiumModelFunction(context);
		ffmf = new FireFoxModelFunction( context );
		ieff = new IEFavoritesFunction(context);
	}

	
	@Override
	public boolean canProvide(IModelLeaf<ILoaderAieon> leaf) {
		if( !super.canProvide(S_FUNCTION_PROVIDER_ID, leaf))
			return false;
		return cmf.canProvide(leaf) || ffmf.canProvide(leaf) || ieff.canProvide(leaf);
	}

	@Override
	protected IModelProvider<ILoaderAieon,IModelLeaf<IDescriptor>> onCreateFunction(IModelLeaf<ILoaderAieon> leaf) {
		CombinedProvider<ILoaderAieon,IModelLeaf<IDescriptor>> provider = new CombinedProvider<ILoaderAieon,IModelLeaf<IDescriptor>>();
		if( cmf.canProvide(leaf))
			provider.addProvider( cmf.getFunction(leaf));
		if( ffmf.canProvide(leaf))
			provider.addProvider( ffmf.getFunction(leaf));
		if( ieff.canProvide(leaf))
			provider.addProvider( ieff.getFunction(leaf));
		return provider;
	}
}