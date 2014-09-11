package org.aieonf.template.context;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.IModelNode;
import org.aieonf.model.Model;
import org.aieonf.model.ModelLeaf;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.model.xml.IModelCreator;
import org.aieonf.model.xml.XMLModelBuilder;
import org.aieonf.template.ITemplateLeaf;
import org.aieonf.template.ITemplateNode;
import org.aieonf.template.builder.TemplateModelBuilderEvent;
import org.aieonf.template.property.BindingDescriptor;
import org.aieonf.util.Utils;

public abstract class AbstractModelContextFactory<T extends IContextAieon> implements IModelContextFactory<T> {

	private ITemplateLeaf<T> template;

	private Collection<IModelBuilderListener> listeners;

	protected AbstractModelContextFactory() {
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
	 * Create the model
	 * @return
	 */
	protected abstract ITemplateLeaf<T> onCreateTemplate();

	/**
	 * Create the model
	 * @return
	 */
	@Override
	public ITemplateLeaf<T> createTemplate(){
		this.template = onCreateTemplate();
		return template;
	}
	
	/* (non-Javadoc)
	 * @see org.aieonf.template.context.IModelContextFactory#getModel()
	 */
	@Override
	public ITemplateLeaf<T> getTemplate() {
		return template;
	}

	/* (non-Javadoc)
	 * @see org.aieonf.template.context.IModelContextFactory#getDomain()
	 */
	@Override
	public IDomainAieon getDomain(){
		return this.template.getDescriptor().getDomain();
	}
	
	@Override
	public IModelLeaf<IDescriptor> createModel() {
		return this.createModel( this.template );
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected final ITemplateLeaf<IContextAieon> createDefaultTemplate( String identifier, IModelCreator creator ) {
		IModelBuilderListener listener = new IModelBuilderListener(){

			@Override
			public void notifyChange(ModelBuilderEvent event) {
				notifyListeners( event );
			}	
		};
		XMLModelBuilder<IContextAieon> builder = new XMLModelBuilder<IContextAieon>( identifier, creator );
		builder.addListener(listener);
		ITemplateLeaf<IContextAieon> root = (ITemplateLeaf<IContextAieon>) builder.build();
		builder.removeListener(listener);
		root.getDescriptor().set( IConcept.Attributes.SOURCE, identifier );
		return root;	
	}

	/**
	 * Get the template with the given identifier
	 * @param identifier
	 * @return
	 */
	protected ITemplateLeaf<IDescriptor> getTemplate( String identifier ){
		return getTemplate( this.template, identifier );
	}

	@SuppressWarnings("unchecked")
	protected static ITemplateLeaf<IDescriptor> getTemplate( ITemplateLeaf<? extends IDescriptor> leaf, String identifier ){
		if( Utils.isNull( identifier ))
			return null;
		if( identifier.equals( leaf.getID()))
			return (ITemplateLeaf<IDescriptor>) leaf;
		if( leaf.isLeaf())
			return null;
		ITemplateNode<IDescriptor> node = (ITemplateNode<IDescriptor>) leaf;
		for( IModelLeaf<? extends IDescriptor> child: node.getChildren() ){
			ITemplateLeaf<IDescriptor> result = getTemplate( (ITemplateLeaf<? extends IDescriptor>) child, identifier );
			if( result != null )
				return result;
		}
		return null;
	}


	/**
	 * Get the template with the given identifier
	 * @param identifier
	 * @return
	 */
	public IModelLeaf<IDescriptor> createModel( ITemplateLeaf<? extends IDescriptor> template ){
		IModelLeaf<IDescriptor> model = new Model<IDescriptor>( new BindingDescriptor());
		createModel( template, model );
		return model;
	}

	@SuppressWarnings("unchecked")
	protected void createModel( ITemplateLeaf<? extends IDescriptor> template, IModelLeaf<? extends IDescriptor> model ){
		model.setIdentifier( template.getIdentifier() );
		this.notifyListeners( new TemplateModelBuilderEvent( this, template, model ));
		if( template.isLeaf())
			return;
		ITemplateNode<IDescriptor> node = (ITemplateNode<IDescriptor>) template;
		IModelNode<IDescriptor> parent = (IModelNode<IDescriptor>) model;
		for( IModelLeaf<? extends IDescriptor> child: node.getChildren() ){
			IModelLeaf<IDescriptor> newModel = ( child.isLeaf()) ? new ModelLeaf<IDescriptor>( new BindingDescriptor() ): new Model<IDescriptor>( new Descriptor() );
			parent.addChild( newModel );
			createModel( (ITemplateLeaf<? extends IDescriptor>) child, newModel );
		}
	}

}