package org.aieonf.template.builder;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.template.ITemplateLeaf;
import org.aieonf.template.ITemplateNode;

public abstract class AbstractModelBuilder<T extends IDescriptor> implements IModelBuilder<T> {

	private Collection<IModelBuilderListener> listeners;

	
	public AbstractModelBuilder() {
		listeners = new ArrayList<IModelBuilderListener>();
	}

	/* (non-Javadoc)
	 * @see org.aieonf.template.context.IModelContextFactory#addListener(org.aieonf.model.builder.IModelBuilderListener)
	 */
	@Override
	public void addListener( IModelBuilderListener listener ){
		this.listeners.add( listener );
	}

	/* (non-Javadoc)
	 * @see org.aieonf.template.context.IModelContextFactory#removeListener(org.aieonf.model.builder.IModelBuilderListener)
	 */
	@Override
	public void removeListener( IModelBuilderListener listener ){
		this.listeners.remove( listener );
	}

	protected final void notifyListeners( ModelBuilderEvent event ){
		for( IModelBuilderListener listener: this.listeners )
			listener.notifyChange(event);
	}

	/**
	 * Create the correct descriptor for the model
	 * @param descriptor
	 * @return
	 */
	protected abstract IModelLeaf<T> createRoot( IDescriptor descriptor );

	/**
	 * Create the correct descriptor for the model
	 * @param descriptor
	 * @return
	 */
	protected abstract IModelLeaf<IDescriptor> createNode( ITemplateLeaf<? extends IDescriptor> template );

	/**
	 * Get the template with the given identifier
	 * @param identifier
	 * @return
	 */
	@Override
	public IModelLeaf<T> createModel( ITemplateLeaf<? extends IDescriptor> template ){
		IModelLeaf<T> model = this.createRoot( new Descriptor() );
		createModel( template, model );
		return model;
	}

	@SuppressWarnings("unchecked")
	protected void createModel( ITemplateLeaf<? extends IDescriptor> template, IModelLeaf<? extends IDescriptor> model ){
		model.setIdentifier( template.getIdentifier() );
		IModelLeaf<? extends IDescriptor>[] models = new IModelLeaf[1];
		models[0] = model;
		this.notifyListeners( new TemplateModelBuilderEvent( this, template, models ));
		if( template.isLeaf())
			return;
		ITemplateNode<IDescriptor> node = (ITemplateNode<IDescriptor>) template;
		IModelNode<IDescriptor> parent = (IModelNode<IDescriptor>) model;
		for( IModelLeaf<? extends IDescriptor> child: node.getChildren() ){
			IModelLeaf<IDescriptor> newModel = createNode( (ITemplateLeaf<? extends IDescriptor>) child );
			parent.addChild( newModel );
			createModel( (ITemplateLeaf<? extends IDescriptor>) child, newModel );
		}
	}

}
