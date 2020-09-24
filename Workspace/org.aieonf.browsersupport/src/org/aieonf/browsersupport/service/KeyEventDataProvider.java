package org.aieonf.browsersupport.service;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.browsersupport.context.ContextFactory;
import org.aieonf.browsersupport.library.chromium.ChromiumModelFunctionProvider;
import org.aieonf.browsersupport.library.firefox.FireFoxModelFunction;
import org.aieonf.browsersupport.library.ie.IEFavoritesFunction;
import org.aieonf.commons.Utils;
import org.aieonf.commons.db.IDatabaseConnection;
import org.aieonf.commons.db.IDatabaseConnection.Requests;
import org.aieonf.commons.parser.ParseException;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.ContextAieon;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.filter.AttributeFilter;
import org.aieonf.concept.library.CategoryAieon;
import org.aieonf.concept.library.ManifestAieon;
import org.aieonf.concept.request.AbstractKeyEventDataProvider;
import org.aieonf.concept.request.IKeyEventDataProvider;
import org.aieonf.concept.request.KeyEvent;
import org.aieonf.model.builder.IFunctionProvider;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.model.filter.ModelFilter;
import org.aieonf.model.provider.IModelProvider;
import org.aieonf.template.def.ITemplateLeaf;
import org.aieonf.template.provider.AbstractModelProvider;
import org.osgi.service.component.annotations.Component;

@Component( name=KeyEventDataProvider.S_COMPONENT_NAME, immediate=true)
public class KeyEventDataProvider extends AbstractKeyEventDataProvider<IDatabaseConnection.Requests, IModelLeaf<IDescriptor>> implements IKeyEventDataProvider<IDatabaseConnection.Requests, IModelLeaf<IDescriptor>> {

	public static final String S_COMPONENT_NAME = "org.aieonf.browsersupport.key.data.service";

	private ModelProvider provider;
	
	private IDomainAieon domain;
	
	public KeyEventDataProvider() {
		ContextFactory factory = ContextFactory.getInstance();
		ITemplateLeaf<IContextAieon> template = factory.createTemplate();
		domain = factory.getDomain();
		provider = new ModelProvider( IFunctionProvider.S_FUNCTION_PROVIDER_ID, new ContextAieon( template.getData()));
	}

	@Override
	protected IModelLeaf<IDescriptor> onProcesskeyEvent(KeyEvent<Requests> event) {
		try {
			provider.open(domain);
			IModelFilter<IModelLeaf<IDescriptor>> filter = 
					new ModelFilter<IModelLeaf<IDescriptor>>( new AttributeFilter<IDescriptor>( AttributeFilter.Rules.WILDCARD, 
							IDescriptor.Attributes.NAME.name(), CategoryAieon.Attributes.CATEGORY.name()));
			Collection<IModelLeaf<IDescriptor>> results = provider.search( filter );
			if( Utils.assertNull(results))
				return null;
			return results.iterator().next();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			provider.close();
		}
		return null;
	}
	
	private class ModelProvider extends AbstractModelProvider<IDomainAieon, IDescriptor, IModelLeaf<IDescriptor>>{

		private IModelProvider<IDomainAieon, IModelLeaf<IDescriptor>> cmf;
		private IModelProvider<IDomainAieon, IModelLeaf<IDescriptor>> ffmf;
		private IModelProvider<IDomainAieon, IModelLeaf<IDescriptor>> ieff;

		protected ModelProvider(String identifier, IContextAieon context) {
			super(identifier, context);
			cmf = new ChromiumModelFunctionProvider(context).getFunction(identifier);
			ffmf = new FireFoxModelFunction( context ).getFunction(identifier);
			ieff = new IEFavoritesFunction(context).getFunction(identifier);
		}

		@Override
		protected boolean onOpen( IDomainAieon key) {
			cmf.open(domain);
			ffmf.open(domain);
			ieff.open(domain);
			return true;
		}

		@Override
		public void close() {
			cmf.close();
			ffmf.close();
			ieff.close();
			super.close();
		}

		@Override
		protected void onSetup(ManifestAieon manifest) { /* NOTHING */}
		
		@Override
		public boolean hasFunction(String functionName) {
			return cmf.hasFunction(functionName) || ffmf.hasFunction(functionName) || ieff.hasFunction(functionName);
		}
	
		@Override
		protected Collection<IModelLeaf<IDescriptor>> onSearch(IModelFilter<IModelLeaf<IDescriptor>> filter) throws ParseException {
			Collection<IModelLeaf<IDescriptor>> results = new ArrayList<IModelLeaf<IDescriptor>>();
			//results.addAll( cmf.search( filter ));
			results.addAll( ffmf.search( filter ));
			results.addAll( ieff.search( filter ));
			return results;
		}

	}
}