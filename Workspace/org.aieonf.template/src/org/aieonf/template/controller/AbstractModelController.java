package org.aieonf.template.controller;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.IModelNode;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.template.ITemplateLeaf;
import org.aieonf.template.builder.IModelBuilder;
import org.aieonf.template.context.IModelContextFactory;

public abstract class AbstractModelController<T extends IContextAieon, U extends IDescriptor> implements IModelController<U> {

	private boolean initialised;
	private IModelLeaf<U> model;
	private ITemplateLeaf<T> template;
	private boolean open;
	
	private IModelContextFactory<T> factory;	

	private Collection<IModelBuilderListener> listeners;
	
	protected AbstractModelController( IModelContextFactory<T> factory ) {
		super();
		this.open = false;
		this.factory = factory;
		this.initialised = false;
		listeners = new ArrayList<IModelBuilderListener>();
	}
	
	public void open(){
		this.open = (this.factory != null );
	}
	
	@Override
	public boolean isOpen() {
		return open;
	}


	public void addBuilderListener( IModelBuilderListener listener ){
		this.listeners.add( listener );
	}

	public void removeBuilderListener( IModelBuilderListener listener ){
		this.listeners.remove( listener );
	}

	/**
	 * Notfiy the listeners of changes
	 * @param event
	 */
	protected void notifyModelChanged(ModelBuilderEvent event) {
		for( IModelBuilderListener listener: getListeners() )
			listener.notifyChange(event);					
	}

	protected Collection<IModelBuilderListener> getListeners() {
		return listeners;
	}

	protected IModelContextFactory<T> getFactory() {
		return factory;
	}

	public ITemplateLeaf<T> getTemplate() {
		return factory.getTemplate();
	}

	@Override
	public void initialise() {
		this.initialised = ( this.factory != null );
		template = factory.createTemplate();
		this.initialised = ( template != null );
	}

	@Override
	public void close() {
		this.model = null;
		this.template = null;
		this.initialised = false;
	}

	@Override
	public boolean isInitialised() {
		return initialised;
	}

	/**
	 * add the required model builder to build the model
	 * @return
	 */
	protected abstract IModelBuilder<U> getModelBuilder();
	
	/**
	 * Verify the model with the given string for categories
	 * @param categories
	 * @return
	 */
	public abstract boolean verifyModel();
	
	@Override
	public IModelLeaf<U> createModel(){
		IModelBuilder<U> builder = this.getModelBuilder();
		IModelBuilderListener listener = new IModelBuilderListener(){

			@Override
			public void notifyChange(ModelBuilderEvent event) {
				for( IModelBuilderListener listener: listeners)
					listener.notifyChange(event);
			}
			
		};
		builder.addListener(listener);
		model = builder.createModel(template);
		builder.removeListener(listener);
		return null;
	}

	@Override
	public IModelLeaf<U> getModel() {
		return model;
	}

	/**
	 * It is possible that a certain model is created externally. In that case it can be included here
	 * @param factory
	 * @param root
	 */
	public void setModel( IModelLeaf<U> root ) {
		this.model = root;
	}

	@Override
	public IModelLeaf<IDescriptor> getSubModel(IDescriptor descriptor) {
		return this.getSubModel( model, descriptor);
	}

	@SuppressWarnings("unchecked")
	protected final IModelLeaf<IDescriptor> getSubModel( IModelLeaf<? extends IDescriptor> model, IDescriptor descriptor ){
		if( model.getDescriptor().equals( descriptor ))
			return (IModelLeaf<IDescriptor>) model;
		if( model.isLeaf() )
			return null;
		IModelNode<IDescriptor> node = (IModelNode<IDescriptor>) model;
		for( IModelLeaf<? extends IDescriptor>child: node.getChildren() ){
			IModelLeaf<IDescriptor>result = this.getSubModel(child, descriptor );
			if( result != null )
				return result;
		}
		return null;
	}

	/**
	 * Get the concept
	 * @param concept
	 * @return
	 */
	public ITemplateLeaf<IDescriptor> getTemplate( String identifier ){
		return (ITemplateLeaf<IDescriptor>) getModel( this.template, identifier );
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
