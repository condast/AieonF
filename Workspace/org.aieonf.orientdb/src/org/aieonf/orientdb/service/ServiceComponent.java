package org.aieonf.orientdb.service;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.DomainAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.builder.IFunctionProvider;
import org.aieonf.model.provider.IModelDatabase;
import org.aieonf.model.provider.IProvider.DefaultModels;
import org.aieonf.orientdb.factory.OrientDBFactory;
import org.aieonf.orientdb.graph.GraphModelFunction;
import org.aieonf.orientdb.tree.TreeModelFunction;
import org.aieonf.osgi.service.AbstractServiceComponent;
import org.aieonf.template.ITemplateLeaf;
import org.aieonf.template.TemplateNodeWrapper;

public class ServiceComponent extends AbstractServiceComponent<IContextAieon, IDomainAieon, String, IModelLeaf<IDescriptor>>
{
	public ServiceComponent() {
		super( OrientDBFactory.getInstance() );
	}

	
	@Override
	public void activate() {
		OrientDBFactory factory = (OrientDBFactory) super.getFactory();
		ITemplateLeaf<IContextAieon> template = super.getFactory().createTemplate();
		factory.addProvider(new GraphModelFunction( template ));
		factory.addProvider(new TreeModelFunction( template ));
		super.activate();
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public IModelDatabase<String, IModelLeaf<IDescriptor>> getFunction( String functionName ) {
		ITemplateLeaf<IContextAieon> template = new TemplateNodeWrapper<IContextAieon>( super.getFactory().getTemplate() );
		IModelDatabase<String, IModelLeaf<IDescriptor>> provider = null;
		if(!DefaultModels.isValid( functionName ))
			return null;
		IFunctionProvider function = null; 
		IDomainAieon domain = new DomainAieon( functionName );
		switch( DefaultModels.getModel( functionName )){
		case GRAPH:
			function = (IFunctionProvider) new GraphModelFunction( template ); 
			break;
		default:
			function = new TreeModelFunction( template ); 
			break;
		}
		provider = (IModelDatabase<String, IModelLeaf<IDescriptor>>) function.getFunction(domain);
		//AsynchronousProviderDelegate<IDomainAieon, IDescriptor> delegate = new AsynchronousProviderDelegate<IDomainAieon, IDescriptor>();
		//delegate.addProvider(provider);
		return provider;
	}
}
