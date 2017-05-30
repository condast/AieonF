package org.aieonf.orientdb.service;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.builder.IFunctionProvider;
import org.aieonf.model.provider.IModelDelegate;
import org.aieonf.model.provider.IModelFunctionProvider;
import org.aieonf.model.provider.IModelProvider;
import org.aieonf.model.provider.IProvider.DefaultModels;
import org.aieonf.orientdb.factory.OrientDBFactory;
import org.aieonf.orientdb.graph.GraphModelFunction;
import org.aieonf.orientdb.tree.TreeModelFunction;
import org.aieonf.osgi.service.AbstractServiceComponent;
import org.aieonf.template.ITemplateLeaf;
import org.aieonf.template.TemplateNodeWrapper;
import org.aieonf.template.provider.AsynchronousProviderDelegate;

public class ServiceComponent extends AbstractServiceComponent implements IModelFunctionProvider<IDescriptor,IModelLeaf<IDescriptor>>
{
	public ServiceComponent() {
		super( OrientDBFactory.getInstance() );
	}

	@Override
	public boolean canProvide(IModelLeaf<IDescriptor> leaf) {
		return DefaultModels.isValid( leaf.getID() );
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public IModelDelegate<IModelLeaf<IDescriptor>> getFunction(IModelLeaf<IDescriptor> leaf) {
		ITemplateLeaf<IContextAieon> template = new TemplateNodeWrapper<IContextAieon>( leaf );
		IModelProvider<IModelLeaf<IDescriptor>> provider = null;
		if(!DefaultModels.isValid( leaf.getID() ))
			return null;
		IFunctionProvider function = null; 
		switch( DefaultModels.getModel( leaf.getID() )){
		case GRAPH:
			function = (IFunctionProvider) new GraphModelFunction<IDescriptor>( template ).getFunction(leaf); 
			break;
		default:
			function = new TreeModelFunction<IDescriptor>( template ); 
			break;
		}
		provider = (IModelProvider<IModelLeaf<IDescriptor>>) function.getFunction(leaf);
		AsynchronousProviderDelegate<IModelLeaf<IDescriptor>> delegate = new AsynchronousProviderDelegate<IModelLeaf<IDescriptor>>();
		delegate.addProvider(provider);
		return delegate;
	}
}
