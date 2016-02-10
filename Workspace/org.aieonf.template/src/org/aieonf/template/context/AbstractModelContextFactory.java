package org.aieonf.template.context;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.model.xml.IModelCreator;
import org.aieonf.model.xml.XMLModelBuilder;
import org.aieonf.template.ITemplateLeaf;
import org.aieonf.template.ITemplateNode;
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
	protected ITemplateLeaf<IDescriptor> getTemplate( ITemplateLeaf<? extends IDescriptor> leaf, String identifier ){
		if( Utils.isNull( identifier ))
			return null;
		if( identifier.equals( leaf.getID())){
			if( Utils.isNull( leaf.getDescriptor().get( IConcept.Attributes.SOURCE ) )){
				leaf.getDescriptor().set( IConcept.Attributes.SOURCE, template.getID() );
			}
			return (ITemplateLeaf<IDescriptor>) leaf;
		}
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
}