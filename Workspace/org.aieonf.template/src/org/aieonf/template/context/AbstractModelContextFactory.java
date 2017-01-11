package org.aieonf.template.context;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.commons.Utils;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.model.search.ModelSearch;
import org.aieonf.model.xml.IXMLModelBuilder;
import org.aieonf.model.xml.XMLModelBuilder;
import org.aieonf.template.ITemplateLeaf;
import org.aieonf.template.ITemplateNode;

public abstract class AbstractModelContextFactory<T extends IContextAieon> implements IModelContextFactory<T> {

	private ITemplateLeaf<T> template;

	private Collection<IModelBuilderListener<IModelLeaf<?>>> listeners;

	protected AbstractModelContextFactory() {
		listeners = new ArrayList<IModelBuilderListener<IModelLeaf<?>>>();
	}
	
	/* (non-Javadoc)
	 * @see org.aieonf.template.context.IModelContextFactory#addListener(org.aieonf.model.builder.IModelBuilderListener)
	 */
	@Override
	public void addListener( IModelBuilderListener<IModelLeaf<?>> listener ){
		this.listeners.add( listener );
	}

	/* (non-Javadoc)
	 * @see org.aieonf.template.context.IModelContextFactory#removeListener(org.aieonf.model.builder.IModelBuilderListener)
	 */
	@Override
	public void removeListener( IModelBuilderListener<IModelLeaf<?>>  listener ){
		this.listeners.remove( listener );
	}

	protected final void notifyListeners( ModelBuilderEvent<IModelLeaf<?>>  event ){
		for( IModelBuilderListener<IModelLeaf<?>>  listener: this.listeners )
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
		ModelSearch<T> search = new ModelSearch<T>( this.template );
		IDomainAieon domain = (IDomainAieon) search.getDescriptors( IDomainAieon.Attributes.DOMAIN.toString() )[0];
		return domain;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected final ITemplateLeaf<IContextAieon> createDefaultTemplate( String identifier, IXMLModelBuilder creator ) {
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
		if( Utils.assertNull( identifier ))
			return null;
		if( identifier.equals( leaf.getID())){
			if( Utils.assertNull( leaf.getDescriptor().get( IConcept.Attributes.SOURCE ) )){
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