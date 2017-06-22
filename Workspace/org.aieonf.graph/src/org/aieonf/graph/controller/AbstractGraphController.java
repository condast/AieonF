package org.aieonf.graph.controller;

import org.aieonf.commons.graph.IVertex;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.graph.IGraphModel;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.template.context.IModelContextFactory;
import org.aieonf.template.controller.AbstractModelController;

public abstract class AbstractGraphController<D extends IDescribable<IContextAieon>, U extends IDescriptor> 
extends AbstractModelController<IContextAieon,U>
{
	private IGraphModel<D, U> provider;
	
	protected AbstractGraphController( IModelContextFactory<IContextAieon> factory ) {
		super( factory );
	}
	
	protected IGraphModel<D, U> getProvider() {
		return provider;
	}

	protected void setProvider(IGraphModel<D, U> provider) {
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
			IModelBuilderListener listener = new IModelBuilderListener(){

				@Override
				public void notifyChange(ModelBuilderEvent event) {
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
			IModelBuilderListener listener = new IModelBuilderListener(){

				@Override
				public void notifyChange(ModelBuilderEvent event) {
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
			IModelBuilderListener listener = new IModelBuilderListener(){

				@Override
				public void notifyChange(ModelBuilderEvent event) {
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