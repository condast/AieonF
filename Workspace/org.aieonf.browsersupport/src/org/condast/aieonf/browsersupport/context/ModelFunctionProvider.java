package org.condast.aieonf.browsersupport.context;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.function.AbstractFunctionProvider;
import org.aieonf.model.provider.IModelDelegate;
import org.aieonf.model.provider.IModelFunctionProvider;
import org.aieonf.model.provider.ModelProviderDelegate;
import org.aieonf.template.provider.AsynchronousProviderDelegate;
import org.condast.aieonf.browsersupport.library.chromium.ChromiumModelFunctionProvider;
import org.condast.aieonf.browsersupport.library.firefox.FireFoxModelFunction;
import org.condast.aieonf.browsersupport.library.ie.IEFavoritesFunction;

public class ModelFunctionProvider extends AbstractFunctionProvider<IDescriptor, IModelDelegate<IModelLeaf<IDescriptor>>> implements IModelFunctionProvider<IDescriptor,IModelLeaf<IDescriptor>>{

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
	protected IModelDelegate<IModelLeaf<IDescriptor>> onCreateFunction(IModelLeaf<IDescriptor> leaf) {
		ModelProviderDelegate<IModelLeaf<IDescriptor>> delegate = new AsynchronousProviderDelegate<IModelLeaf<IDescriptor>>();
		if( cmf.canProvide(leaf))
			delegate.addProvider( cmf.getFunction(leaf));
		if( ffmf.canProvide(leaf))
			delegate.addProvider( ffmf.getFunction(leaf));
		if( ieff.canProvide(leaf))
			delegate.addProvider( ieff.getFunction(leaf));
		return delegate;
	}
}