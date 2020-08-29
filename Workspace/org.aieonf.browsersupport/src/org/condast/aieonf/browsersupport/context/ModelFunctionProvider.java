package org.condast.aieonf.browsersupport.context;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.commons.parser.ParseException;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.library.ManifestAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.model.function.AbstractFunctionProvider;
import org.aieonf.model.provider.IModelProvider;
import org.aieonf.template.provider.AbstractModelProvider;
import org.condast.aieonf.browsersupport.library.chromium.ChromiumModelFunctionProvider;
import org.condast.aieonf.browsersupport.library.firefox.FireFoxModelFunction;
import org.condast.aieonf.browsersupport.library.ie.IEFavoritesFunction;

public class ModelFunctionProvider extends AbstractFunctionProvider<String, IModelProvider<IDomainAieon, IModelLeaf<IDescriptor>>>{
	
	private ModelProvider provider;
	
	public ModelFunctionProvider( IContextAieon context ) {
		super( S_FUNCTION_PROVIDER_ID );
		provider = new ModelProvider( S_FUNCTION_PROVIDER_ID, context );
	}

	@Override
	public boolean canProvide( String domain) {
		return provider.hasFunction( domain );
	}

	@Override
	public IModelProvider<IDomainAieon, IModelLeaf<IDescriptor>> getFunction( String domain) {
		return provider;
	}

	@Override
	protected IModelProvider<IDomainAieon, IModelLeaf<IDescriptor>> onCreateFunction( String describable) {
		return null;
	}
	
	private class ModelProvider extends AbstractModelProvider<IDomainAieon, IDescriptor, IModelLeaf<IDescriptor>>{

		private ChromiumModelFunctionProvider cmf;
		private FireFoxModelFunction ffmf;
		private IEFavoritesFunction ieff;

		protected ModelProvider(String identifier, IContextAieon context) {
			super(identifier, context);
			cmf = new ChromiumModelFunctionProvider(context);
			ffmf = new FireFoxModelFunction( context );
			ieff = new IEFavoritesFunction(context);
		}

		@Override
		protected void onSetup(ManifestAieon manifest) { /* NOTHING */}
		
		@Override
		public boolean hasFunction(String functionName) {
			return cmf.canProvide(functionName) || ffmf.canProvide(functionName) || ieff.canProvide(functionName);
		}
	
		@Override
		protected Collection<IModelLeaf<IDescriptor>> onSearch(IModelFilter<IModelLeaf<IDescriptor>> filter) throws ParseException {
			Collection<IModelLeaf<IDescriptor>> results = new ArrayList<IModelLeaf<IDescriptor>>();
			results.addAll( cmf.getFunction( super.getIdentifier()).search( filter ));
			results.addAll( ffmf.getFunction( super.getIdentifier()).search( filter ));
			results.addAll( cmf.getFunction( super.getIdentifier()).search( filter ));
			return results;
		}

		@Override
		protected boolean onOpen( IDomainAieon key) {
			return true;
		}
	}
}