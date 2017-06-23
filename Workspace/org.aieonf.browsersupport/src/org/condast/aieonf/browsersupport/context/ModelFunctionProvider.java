package org.condast.aieonf.browsersupport.context;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.function.AbstractFunctionProvider;
import org.aieonf.model.provider.IModelProvider;
import org.condast.aieonf.browsersupport.library.chromium.ChromiumModelFunctionProvider;
import org.condast.aieonf.browsersupport.library.firefox.FireFoxModelFunction;
import org.condast.aieonf.browsersupport.library.ie.IEFavoritesFunction;

public class ModelFunctionProvider extends AbstractFunctionProvider<IContextAieon, 
	IModelProvider<IContextAieon, IModelLeaf<IDescriptor>>>{

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
	public boolean canProvide(IContextAieon data) {
		if( !super.canProvide(S_FUNCTION_PROVIDER_ID ))
			return false;
		return cmf.canProvide(data) || ffmf.canProvide(data) || ieff.canProvide(data);
	}


	@Override
	public IModelProvider<IContextAieon, IModelLeaf<IDescriptor>> getFunction( IContextAieon data) {
		IModelProvider<IContextAieon, IModelLeaf<IDescriptor>> provider = null;
		if( cmf.canProvide(super.getAieon()))
			provider = cmf.getFunction(data);
		if( ffmf.canProvide(data))
			provider = ffmf.getFunction(data);
		if( ieff.canProvide(data))
			provider = ieff.getFunction(data);
		return provider;
	}


	@Override
	protected IModelProvider<IContextAieon, IModelLeaf<IDescriptor>> onCreateFunction(IContextAieon describable) {
		// TODO Auto-generated method stub
		return null;
	}
}