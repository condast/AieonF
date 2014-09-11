package org.aieonf.graph.controller;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.IModelNode;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.template.ITemplateLeaf;
import org.aieonf.template.context.IModelContextFactory;
import org.aieonf.template.graph.IGraphModelProvider;

public abstract class AbstractController<T extends IContextAieon>
{
	private IModelContextFactory<T> factory;
	private IModelLeaf<IDescriptor> model;
	
	private IGraphModelProvider<T,IConcept> provider;
	
	private Collection<IModelBuilderListener> listeners;
	private boolean initialised;
		
	protected AbstractController( IModelContextFactory<T> factory ) {
		this.factory = factory;
		listeners = new ArrayList<IModelBuilderListener>();
		this.initialised = false;
	}

	protected IModelContextFactory<T> getFactory() {
		return factory;
	}

	public ITemplateLeaf<T> getTemplate() {
		return factory.getTemplate();
	}

	public IModelLeaf<IDescriptor> getModel() {
		return model;
	}

	public void addListener( IModelBuilderListener listener ){
		this.listeners.add( listener );
	}

	public void removeListener( IModelBuilderListener listener ){
		this.listeners.remove( listener );
	}

	public boolean isInitialised() {
		return initialised;
	}

	/**
	 * Initialise the controller
	 * @param listener
	 */
	protected synchronized void init(){
		IModelBuilderListener listener = new IModelBuilderListener(){

			@Override
			public void notifyChange(ModelBuilderEvent event) {
				for( IModelBuilderListener listener: listeners)
					listener.notifyChange(event);
			}
			
		};
		factory.addListener(listener);
		factory.createTemplate();
		factory.removeListener(listener);
		this.initialised = true;
	}

	/**
	 * Create a model 
	 * @return
	 */
	public IModelLeaf<IDescriptor> createModel(){
		IModelBuilderListener listener = new IModelBuilderListener(){

			@Override
			public void notifyChange(ModelBuilderEvent event) {
				for( IModelBuilderListener listener: listeners)
					listener.notifyChange(event);
			}
			
		};
		factory.addListener(listener);
		this.model = factory.createModel();
		factory.removeListener(listener);
		return model;
	}
	
	protected IGraphModelProvider<T,IConcept> getProvider() {
		return provider;
	}

	protected void setProvider(IGraphModelProvider<T,IConcept> provider) {
		this.provider = provider;
		for( IModelBuilderListener listener: this.listeners )
			this.provider.addListener(listener);
	}

	public void shutdown(){
		for( IModelBuilderListener listener: this.listeners )
			this.provider.removeListener(listener);
		provider.close();
	}

	/**
	 * Verify the model with the given string for categories
	 * @param categories
	 * @return
	 */
	public abstract boolean verifyModel();

	/**
	 * Add a model to the database
	 */
	public void addModel(){
		try {
			IModelBuilderListener listener = new IModelBuilderListener(){

				@Override
				public void notifyChange(ModelBuilderEvent event) {
					for( IModelBuilderListener listener: listeners )
						listener.notifyChange(event);					
				}
			};
			provider.addListener(listener);
			provider.open();
			provider.create( this.factory.getTemplate() );
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
	
	/**
	 * Get the concept
	 * @param concept
	 * @return
	 */
	public ITemplateLeaf<IDescriptor> getTemplate( String identifier ){
		return (ITemplateLeaf<IDescriptor>) getModel( this.factory.getTemplate(), identifier );
	}

	@SuppressWarnings("unchecked")
	public static IModelLeaf<IDescriptor> getModel( IModelLeaf<? extends IDescriptor> leaf, String identifier ){
		if( identifier.equals( leaf.getIdentifier() ))
			return (IModelLeaf<IDescriptor>) leaf;
		if( leaf.isLeaf())
			return null;
		IModelNode<IDescriptor> node = (IModelNode<IDescriptor>) leaf;
		for( IModelLeaf<? extends IDescriptor> child: node.getChildren()){
			IModelLeaf<IDescriptor> result = getModel( child, identifier );
			if( result != null )
				return result;
		}
		return null;
	}

}
