package org.aieonf.browsersupport.service;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.browsersupport.context.ContextFactory;
import org.aieonf.browsersupport.core.Dispatcher;
import org.aieonf.browsersupport.library.chromium.ChromiumModelFunctionProvider;
import org.aieonf.browsersupport.library.firefox.FireFoxModelFunction;
import org.aieonf.browsersupport.library.ie.IEFavoritesFunction;
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
import org.aieonf.concept.request.DataProcessedEvent;
import org.aieonf.concept.request.IKeyEventDataProvider;
import org.aieonf.concept.request.KeyEvent;
import org.aieonf.model.builder.IFunctionProvider;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.model.core.ModelEvent;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.model.filter.ModelFilter;
import org.aieonf.model.provider.IModelProvider;
import org.aieonf.template.def.ITemplateLeaf;
import org.aieonf.template.provider.AbstractModelProvider;
import org.osgi.service.component.annotations.Component;

@Component( name=KeyEventDataProvider.S_COMPONENT_NAME, immediate=true)
public class KeyEventDataProvider extends AbstractKeyEventDataProvider<IDatabaseConnection.Requests, IModelLeaf<IDescriptor>[]> implements IKeyEventDataProvider<IDatabaseConnection.Requests, IModelLeaf<IDescriptor>[]> {

	public static final String S_COMPONENT_NAME = "org.aieonf.browsersupport.key.data.service";

	private ModelProvider provider;
	
	private IDomainAieon domain;
	
	private KeyEvent<IDatabaseConnection.Requests> request;
	
	public KeyEventDataProvider() {
		super( S_COMPONENT_NAME, ProviderTypes.LINKS );
		ContextFactory factory = ContextFactory.getInstance();
		ITemplateLeaf<IContextAieon> template = factory.createTemplate();
		domain = factory.getDomain();
		provider = new ModelProvider( IFunctionProvider.S_FUNCTION_PROVIDER_ID, new ContextAieon( template.getData()));
	}

	@SuppressWarnings("unchecked")
	@Override
	protected IModelLeaf<IDescriptor>[] onProcesskeyEvent(KeyEvent<Requests> event) {
		Collection<IModelLeaf<IDescriptor>> results = new ArrayList<>();
		try {
			request = event;
			provider.open(domain);
			switch( event.getRequest()) {
			case SEARCH:
				IModelFilter<IDescriptor, IModelLeaf<IDescriptor>> filter = 
				new ModelFilter<IDescriptor, IModelLeaf<IDescriptor>>( new AttributeFilter<IDescriptor>( AttributeFilter.Rules.WILDCARD, 
						IDescriptor.Attributes.NAME.name(), CategoryAieon.Attributes.CATEGORY.name()));
				results = provider.search( filter );
				break;
			default:
				break;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			provider.close();
		}
		return results.toArray( new IModelLeaf[ results.size()]);
	}

	@SuppressWarnings("unchecked")
	protected void notifyResultsFound( ModelEvent<IModelLeaf<IDescriptor>> event ) {
		IModelLeaf<IDescriptor>[] results = event.getModel().toArray( new IModelLeaf[ event.getModel().size()]);
		DataProcessedEvent<Requests, IModelLeaf<IDescriptor>[]> devent = new DataProcessedEvent<IDatabaseConnection.Requests, IModelLeaf<IDescriptor>[]>( this, request, results );
		super.notifyDataProcessed( devent);
	}
	
	private class ModelProvider extends AbstractModelProvider<IDomainAieon, IDescriptor, IModelLeaf<IDescriptor>>{

		private IModelProvider<IDomainAieon, IDescriptor, IModelLeaf<IDescriptor>> cmf;
		private IModelProvider<IDomainAieon, IDescriptor, IModelLeaf<IDescriptor>> ffmf;
		private IModelProvider<IDomainAieon, IDescriptor, IModelLeaf<IDescriptor>> ieff;

		protected ModelProvider(String identifier, IContextAieon context) {
			super(identifier, context);
			cmf = new ChromiumModelFunctionProvider(context).getFunction(identifier);
			cmf.addListener(e->notifyResultsFound( e ));
			ffmf = new FireFoxModelFunction( context ).getFunction(identifier);
			ffmf.addListener(e->notifyResultsFound( e ));
			ieff = new IEFavoritesFunction(context).getFunction(identifier);
			ieff.addListener(e->notifyResultsFound( e ));
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
		protected Collection<IModelLeaf<IDescriptor>> onSearch(IModelFilter<IDescriptor, IModelLeaf<IDescriptor>> filter) throws ParseException {
			Collection<IModelLeaf<IDescriptor>> results = new ArrayList<IModelLeaf<IDescriptor>>();
			results.addAll( cmf.search( filter )); //VERY SLOW
			results.addAll( ffmf.search( filter ));
			results.addAll( ieff.search( filter ));
			Dispatcher dispatcher = Dispatcher.getInstance();
			for( IModelLeaf<IDescriptor> leaf: results)
				dispatcher.complete( (IModelNode<IDescriptor>) leaf );
			return results;
		}

	}
}