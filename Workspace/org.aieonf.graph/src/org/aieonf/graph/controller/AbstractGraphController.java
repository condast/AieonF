package org.aieonf.graph.controller;

import java.util.Collection;

import org.aieonf.commons.graph.IVertex;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.graph.IGraphModel;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.template.context.IModelContextFactory;
import org.aieonf.template.controller.AbstractModelController;

public abstract class AbstractGraphController<T extends IContextAieon, U extends IDescriptor> extends AbstractModelController<T,U>
{
	private IGraphModel<IVertex<U>> provider;
	
	protected AbstractGraphController( IModelContextFactory<T> factory ) {
		super( factory );
	}
	
	protected IGraphModel<IVertex<U>> getProvider() {
		return provider;
	}

	protected void setProvider(IGraphModel<IVertex<U>> provider) {
		this.provider = provider;
	}

	public void shutdown(){
		for( IModelBuilderListener listener: super.getListeners() )
			this.provider.removeListener(listener);
		provider.close();
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
			provider.addListener(listener);
			provider.open();
			provider.add( this.convertFrom( super.getModel()));
			provider.removeListener(listener);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			provider.close();
			provider.deactivate();
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
			provider.addListener(listener);
			provider.open();
			Collection<IVertex<U>> vertices = provider.get( super.getModel().getDescriptor() );
			for( IVertex<U> vertex: vertices )
				provider.delete( vertex );
			provider.removeListener(listener);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			provider.close();
			provider.deactivate();
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
			provider.addListener(listener);
			provider.open();
			provider.removeListener(listener);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			provider.close();
			provider.deactivate();
		}
	}

	protected IVertex<U> convertFrom( IModelLeaf<U> model ){
		return null;
	}
}