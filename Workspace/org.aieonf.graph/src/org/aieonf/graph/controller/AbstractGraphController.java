package org.aieonf.graph.controller;

import org.aieonf.commons.graph.IVertex;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.graph.IGraphModelProvider;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.template.context.IModelContextFactory;
import org.aieonf.template.controller.AbstractModelController;

public abstract class AbstractGraphController<D extends IDescriptor, M extends IDescribable> 
extends AbstractModelController<D,M>
{
	private IGraphModelProvider<D, M> provider;
	
	protected AbstractGraphController( IModelContextFactory<M> factory ) {
		super( factory );
	}
	
	protected IGraphModelProvider<D, M> getProvider() {
		return provider;
	}

	protected void setProvider(IGraphModelProvider<D, M> provider) {
		this.provider = provider;
	}

	public void shutdown(){
	}

	/**
	 * Add a model to the database
	 */
	@Override
	public boolean addModel(){
		try {
			IModelBuilderListener<M> listener = new IModelBuilderListener<M>(){

				@Override
				public void notifyChange(ModelBuilderEvent<M> event) {
					notifyModelChanged(event);					
				}
			};
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
		}
		return false;
	}

	@Override
	public boolean removeModel() {
		try {
			IModelBuilderListener<M> listener = new IModelBuilderListener<M>(){

				@Override
				public void notifyChange(ModelBuilderEvent<M> event) {
					notifyModelChanged(event);			
				}
			};
			//Collection<IVertex<U>> vertices = provider.get( super.getModel().getDescriptor() );
			//for( IVertex<U> vertex: vertices )
			//	provider.delete( vertex );
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
		}
		return false;
	}

	@Override
	public void updateModel() {
		try {
			IModelBuilderListener<M> listener = new IModelBuilderListener<M>(){

				@Override
				public void notifyChange(ModelBuilderEvent<M> event) {
					notifyModelChanged(event);					
				}
			};
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
		}
	}

	protected IVertex<M> convertFrom( IModelLeaf<M> model ){
		return null;
	}
}