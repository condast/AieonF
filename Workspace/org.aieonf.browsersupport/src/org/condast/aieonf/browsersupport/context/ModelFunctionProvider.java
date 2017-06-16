package org.condast.aieonf.browsersupport.context;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.function.AbstractFunctionProvider;
import org.aieonf.model.provider.IModelDelegate;
import org.aieonf.model.provider.IModelFunctionProvider;
import org.aieonf.model.provider.ModelDelegate;
import org.aieonf.template.provider.AsynchronousProviderDelegate;
import org.condast.aieonf.browsersupport.library.chromium.ChromiumModelFunctionProvider;
import org.condast.aieonf.browsersupport.library.firefox.FireFoxModelFunction;
import org.condast.aieonf.browsersupport.library.ie.IEFavoritesFunction;

public class ModelFunctionProvider extends AbstractFunctionProvider<IContextAieon, IModelDelegate<IContextAieon, IDescriptor>> implements IModelFunctionProvider<IContextAieon,IDescriptor>{

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
	public boolean canProvide( IContextAieon data ) {
		if( !super.canProvide(S_FUNCTION_PROVIDER_ID, data))
			return false;
		return cmf.canProvide(data) || ffmf.canProvide(data) || ieff.canProvide(data);
	}


	@Override
	public IModelDelegate<IContextAieon, IDescriptor> getFunction(IContextAieon data) {
		ModelDelegate<IDomainAieon, IDescriptor> delegate = new AsynchronousProviderDelegate<IDescriptor>();
		if( cmf.canProvide(leaf))
			delegate.addProvider( cmf.getFunction(leaf));
		if( ffmf.canProvide(leaf))
			delegate.addProvider( ffmf.getFunction(leaf));
		if( ieff.canProvide(leaf))
			delegate.addProvider( ieff.getFunction(leaf));
		return delegate;
	}
}