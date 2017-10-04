package org.aieonf.template.context;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.commons.Utils;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.search.ModelScanner;
import org.aieonf.model.xml.IXMLModelInterpreter;
import org.aieonf.model.xml.XMLModelBuilder;
import org.aieonf.template.ITemplateLeaf;
import org.aieonf.template.ITemplateNode;

public abstract class AbstractModelContextFactory<C extends IContextAieon> implements IModelContextFactory<C,IDomainAieon> {

	private ITemplateLeaf<C> template;

	private Collection<IModelBuilderListener<C>> listeners;

	protected AbstractModelContextFactory() {
		listeners = new ArrayList<IModelBuilderListener<C>>();
	}
	
	/* (non-Javadoc)
	 * @see org.aieonf.template.context.IModelContextFactory#addListener(org.aieonf.model.builder.IModelBuilderListener)
	 */
	@Override
	public void addListener( IModelBuilderListener<C> listener ){
		this.listeners.add( listener );
	}

	/* (non-Javadoc)
	 * @see org.aieonf.template.context.IModelContextFactory#removeListener(org.aieonf.model.builder.IModelBuilderListener)
	 */
	@Override
	public void removeListener( IModelBuilderListener<C>  listener ){
		this.listeners.remove( listener );
	}

	protected final void notifyListeners( ModelBuilderEvent<C>  event ){
		for( IModelBuilderListener<C>  listener: this.listeners )
			listener.notifyChange(event);
	}

	/**
	 * Create the model
	 * @return
	 */
	protected abstract ITemplateLeaf<C> onCreateTemplate();

	/**
	 * Create the model
	 * @return
	 */
	@Override
	public ITemplateLeaf<C> createTemplate(){
		this.template = onCreateTemplate();
		return template;
	}
	
	/* (non-Javadoc)
	 * @see org.aieonf.template.context.IModelContextFactory#getModel()
	 */
	@Override
	public ITemplateLeaf<C> getTemplate() {
		return template;
	}

	/* (non-Javadoc)
	 * @see org.aieonf.template.context.IModelContextFactory#getDomain()
	 */
	@Override
	public IDomainAieon getDomain(){
		ModelScanner<C> search = new ModelScanner<C>( this.template );
		IDomainAieon domain = (IDomainAieon) search.getDescriptors( IDomainAieon.Attributes.DOMAIN.toString() )[0];
		return domain;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected final ITemplateLeaf<C> createDefaultTemplate( String identifier, IXMLModelInterpreter interpreter ) {
		IModelBuilderListener listener = new IModelBuilderListener(){

			@Override
			public void notifyChange(ModelBuilderEvent event) {
				notifyListeners( event );
			}	
		};
		XMLModelBuilder<C,ITemplateLeaf<C>> builder = new XMLModelBuilder<C, ITemplateLeaf<C>>( identifier, interpreter );
		builder.addListener(listener);
		ITemplateLeaf<C> root = (ITemplateLeaf<C>) builder.build();
		builder.removeListener(listener);
		root.getDescriptor().set( IConcept.Attributes.SOURCE, identifier );
		return root;	
	}

	/**
	 * Get the template with the given identifier
	 * @param identifier
	 * @return
	 */
	protected ITemplateLeaf<C> getTemplate( String identifier ){
		return getTemplate( this.template, identifier );
	}

	@SuppressWarnings("unchecked")
	protected ITemplateLeaf<C> getTemplate( ITemplateLeaf<? extends IDescriptor> leaf, String identifier ){
		if( Utils.assertNull( identifier ))
			return null;
		if( identifier.equals( leaf.getID())){
			if( Utils.assertNull( leaf.getDescriptor().get( IConcept.Attributes.SOURCE ) )){
				leaf.getDescriptor().set( IConcept.Attributes.SOURCE, template.getID() );
			}
			return (ITemplateLeaf<C>) leaf;
		}
		if( leaf.isLeaf())
			return null;
		ITemplateNode<IDescriptor> node = (ITemplateNode<IDescriptor>) leaf;
		for( IModelLeaf<? extends IDescriptor> child: node.getChildren() ){
			ITemplateLeaf<C> result = getTemplate( (ITemplateLeaf<? extends IDescriptor>) child, identifier );
			if( result != null )
				return result;
		}
		return null;
	}
}