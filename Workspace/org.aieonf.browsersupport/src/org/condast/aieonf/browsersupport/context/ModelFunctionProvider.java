package org.condast.aieonf.browsersupport.context;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.model.function.AbstractFunctionProvider;
import org.aieonf.model.provider.IModelDelegate;
import org.aieonf.model.provider.ModelDelegate;
import org.aieonf.template.provider.AsynchronousProviderDelegate;
import org.condast.aieonf.browsersupport.library.chromium.ChromiumModelFunctionProvider;
import org.condast.aieonf.browsersupport.library.firefox.FireFoxModelFunction;
import org.condast.aieonf.browsersupport.library.ie.IEFavoritesFunction;

public class ModelFunctionProvider extends AbstractFunctionProvider<IContextAieon, IModelDelegate<IContextAieon, IDescriptor>>{

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
	public IModelDelegate<IContextAieon, IDescriptor> getFunction( IContextAieon data) {
		ModelDelegate<IContextAieon, IDescriptor> delegate = new AsynchronousProviderDelegate<IContextAieon, IDescriptor>();
		if( cmf.canProvide(super.getAieon()))
			delegate.addProvider( cmf.getFunction(data));
		if( ffmf.canProvide(data))
			delegate.addProvider( ffmf.getFunction(data));
		if( ieff.canProvide(data))
			delegate.addProvider( ieff.getFunction(data));
		return delegate;
	}


	@Override
	protected IModelDelegate<IContextAieon, IDescriptor> onCreateFunction(IContextAieon describable) {
		// TODO Auto-generated method stub
		return null;
	}
}