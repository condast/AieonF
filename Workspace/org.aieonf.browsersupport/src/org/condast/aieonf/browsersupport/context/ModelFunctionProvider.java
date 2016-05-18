package org.condast.aieonf.browsersupport.context;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.model.IModelFunctionProvider;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.IModelProvider;
import org.aieonf.model.function.AbstractFunctionProvider;
import org.aieonf.template.provider.CombinedProvider;
import org.condast.aieonf.browsersupport.library.chromium.ChromiumModelFunctionProvider;
import org.condast.aieonf.browsersupport.library.firefox.FireFoxModelFunction;
import org.condast.aieonf.browsersupport.library.ie.IEFavoritesFunction;

public class ModelFunctionProvider extends AbstractFunctionProvider<IDescriptor, IModelProvider<IModelLeaf<IDescriptor>>> implements IModelFunctionProvider<IDescriptor,IModelLeaf<IDescriptor>>{

	private ChromiumModelFunctionProvider cmf;
	private FireFoxModelFunction ffmf;
	private IEFavoritesFunction ieff;
	
	public ModelFunctionProvider( IContextAieon context ) {
		super( S_FUNCTION_PROVIDER_ID, context );
		cmf = new ChromiumModelFunctionProvider(context);
		ffmf = new FireFoxModelFunction( context );
		ieff = new IEFavoritesFunction(context);
	}

	
	@Override
	public boolean canProvide(IModelLeaf<IDescriptor> leaf) {
		if( !super.canProvide(S_FUNCTION_PROVIDER_ID, leaf))
			return false;
		return cmf.canProvide(leaf) || ffmf.canProvide(leaf) || ieff.canProvide(leaf);
	}

	@Override
	protected IModelProvider<IModelLeaf<IDescriptor>> onCreateFunction(IModelLeaf<IDescriptor> leaf) {
		CombinedProvider<IModelLeaf<IDescriptor>> provider = new CombinedProvider<IModelLeaf<IDescriptor>>();
		if( cmf.canProvide(leaf))
			provider.addProvider( cmf.getFunction(leaf));
		if( ffmf.canProvide(leaf))
			provider.addProvider( ffmf.getFunction(leaf));
		if( ieff.canProvide(leaf))
			provider.addProvider( ieff.getFunction(leaf));
		return provider;
	}
}