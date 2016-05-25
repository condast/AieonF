package org.aieonf.orientdb.service;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.graph.IGraphModel;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.provider.IModelDelegate;
import org.aieonf.model.provider.IModelFunctionProvider;
import org.aieonf.model.provider.IModelProvider;
import org.aieonf.orientdb.graph.GraphModelFunction;
import org.aieonf.orientdb.tree.TreeModelFunction;
import org.aieonf.template.ITemplateLeaf;
import org.aieonf.template.TemplateNodeWrapper;
import org.aieonf.template.provider.AsynchronousProviderDelegate;

public class ServiceComponent implements IModelFunctionProvider<IDescriptor,Object>
{
	public void activate(){
		
	}
	
	public void deactivate(){
	}

	@Override
	public boolean canProvide(IModelLeaf<IDescriptor> leaf) {
		if(IGraphModel.S_GRAPH_MODEL_PROVIDER_ID.equals( leaf.getID()))
			return true;
		if(IModelProvider.S_MODEL_PROVIDER_ID.equals( leaf.getID()))
			return true;
		return ( TreeModelFunction.S_DATABASE_ID.equals( leaf.getID()));
	}

	@SuppressWarnings("unchecked")
	@Override
	public IModelDelegate<Object> getFunction(IModelLeaf<IDescriptor> leaf) {
		ITemplateLeaf<IContextAieon> template = new TemplateNodeWrapper<IContextAieon>( leaf );
		IModelProvider<Object> provider = null;
		if(IGraphModel.S_GRAPH_MODEL_PROVIDER_ID.equals( leaf.getID())){
			provider = (IModelProvider<Object>) new GraphModelFunction<IDescriptor>( template ); 
		}
		if(IModelProvider.S_MODEL_PROVIDER_ID.equals( leaf.getID())){
			provider = (IModelProvider<Object>) new TreeModelFunction<IDescriptor>( template );
		}
		if( TreeModelFunction.S_DATABASE_ID.equals( leaf.getID())){
			provider = (IModelProvider<Object>) new TreeModelFunction<IDescriptor>( template );
		}
		if( provider == null )
			return null;
		AsynchronousProviderDelegate<Object> delegate = new AsynchronousProviderDelegate<Object>();
		delegate.addProvider(provider);
		return delegate;
	}

	/*
	@Override
	protected void initialise() {
		modelProvider = ModelFunctionProvider.getInstance();	
		super.addAttendee( modelProvider );
	}

	@Override
	protected void finalise() {
		this.removeAttendee( modelProvider );
		this.modelProvider = null;
		super.finalise();
	}
*/
}
