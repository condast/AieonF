package org.condast.aieonf.browsersupport.context;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.model.IModelFunction;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.IModelProvider;
import org.aieonf.model.function.AbstractFunction;
import org.condast.aieonf.browsersupport.library.chromium.ChromiumModelFunction;
import org.condast.aieonf.browsersupport.library.firefox.FireFoxModelFunction;
import org.condast.aieonf.browsersupport.library.ie.IEFavoritesFunction;

public class ModelFunction extends AbstractFunction<ILoaderAieon, IModelProvider<ILoaderAieon,IModelLeaf<IConcept>>> implements IModelFunction<ILoaderAieon,IModelLeaf<IConcept>>{

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
	protected IModelProvider<ILoaderAieon,IModelLeaf<IConcept>> onCreateFunction(IModelLeaf<ILoaderAieon> leaf) {
		CombinedProvider<ILoaderAieon> provider = new CombinedProvider<ILoaderAieon>( super.getAieon(), leaf );
		if( cmf.canProvide(leaf))
			provider.addProvider( cmf.getFunction(leaf));
		if( ffmf.canProvide(leaf))
			provider.addProvider( ffmf.getFunction(leaf));
		if( ieff.canProvide(leaf))
			provider.addProvider( ieff.getFunction(leaf));
		return provider;
	}
}