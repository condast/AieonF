package org.aieonf.orientdb.service;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.provider.IModelDatabase;
import org.aieonf.model.provider.IProvider.DefaultModels;
import org.aieonf.orientdb.factory.OrientDBFactory;
import org.aieonf.orientdb.graph.GraphModel;
import org.aieonf.orientdb.tree.TreeModel;
import org.aieonf.osgi.service.AbstractServiceComponent;
import org.aieonf.template.ITemplateLeaf;
import org.aieonf.template.TemplateNodeWrapper;

public class ServiceComponent extends AbstractServiceComponent<IContextAieon, IDomainAieon, String, IModelLeaf<IDescriptor>>
{
	public ServiceComponent() {
		super( OrientDBFactory.getInstance() );
	}
	
	
	@Override
	public boolean canProvide(String key) {
		return DefaultModels.isValid(key);
	}

	@Override
	public IModelDatabase<IDomainAieon, IModelLeaf<IDescriptor>> getFunction( String functionName ) {
		ITemplateLeaf<IContextAieon> template = new TemplateNodeWrapper<IContextAieon>( super.getFactory().getTemplate() );
		IModelDatabase<IDomainAieon, IModelLeaf<IDescriptor>> database = null;
		if(!DefaultModels.isValid( functionName ))
			return null;
		switch( DefaultModels.getModel( functionName )){
		case GRAPH:
			database = new GraphModel( template ); 
			break;
		default:
			database = new TreeModel( template ); 
			break;
		}
		//AsynchronousProviderDelegate<IDomainAieon, IDescriptor> delegate = new AsynchronousProviderDelegate<IDomainAieon, IDescriptor>();
		//delegate.addProvider(provider);
		return database;
	}
}
