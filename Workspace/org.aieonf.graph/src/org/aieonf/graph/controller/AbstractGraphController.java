package org.aieonf.graph.controller;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.template.context.IModelContextFactory;
import org.aieonf.template.controller.AbstractModelController;
import org.aieonf.template.graph.IGraphModelProvider;

public abstract class AbstractGraphController<T extends IContextAieon> extends AbstractModelController<T>
{
	private IGraphModelProvider<T,IConcept> provider;
	
	protected AbstractGraphController( IModelContextFactory<T> factory ) {
		super( factory );
	}
	
	protected IGraphModelProvider<T,IConcept> getProvider() {
		return provider;
	}

	protected void setProvider(IGraphModelProvider<T,IConcept> provider) {
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
			provider.create( super.getTemplate() );
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
			provider.create( super.getTemplate() );
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
			provider.create( super.getTemplate() );
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
}
