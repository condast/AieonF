package org.aieonf.graph.controller;

import org.aieonf.commons.graph.IVertex;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.graph.IGraphModelProvider;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.template.context.IModelContextFactory;
import org.aieonf.template.controller.AbstractModelController;

public abstract class AbstractGraphController<D extends IDomainAieon, U extends IDescriptor> 
extends AbstractModelController<IContextAieon,D,U>
{
	private IGraphModelProvider<D, U> provider;
	
	protected AbstractGraphController( IModelContextFactory<IContextAieon, D> factory ) {
		super( factory );
	}
	
	protected IGraphModelProvider<D, U> getProvider() {
		return provider;
	}

	protected void setProvider(IGraphModelProvider<D, U> provider) {
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
			IModelBuilderListener<U> listener = new IModelBuilderListener<U>(){

				@Override
				public void notifyChange(ModelBuilderEvent<U> event) {
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
			IModelBuilderListener<U> listener = new IModelBuilderListener<U>(){

				@Override
				public void notifyChange(ModelBuilderEvent<U> event) {
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
			IModelBuilderListener<U> listener = new IModelBuilderListener<U>(){

				@Override
				public void notifyChange(ModelBuilderEvent<U> event) {
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

	protected IVertex<U> convertFrom( IModelLeaf<U> model ){
		return null;
	}
}