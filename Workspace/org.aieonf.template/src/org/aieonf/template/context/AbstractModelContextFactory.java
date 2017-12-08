package org.aieonf.template.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import org.aieonf.commons.Utils;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.search.ModelScanner;
import org.aieonf.model.xml.IXMLModelInterpreter;
import org.aieonf.template.def.ITemplateLeaf;
import org.aieonf.template.def.ITemplateNode;
import org.aieonf.template.xml.XMLTemplateBuilder;

public abstract class AbstractModelContextFactory<C extends IContextAieon> implements IModelContextFactory<C,IDomainAieon> {

	private ITemplateLeaf<C> template;

	private Collection<IModelBuilderListener<IDescribable<?>>> listeners;

	private Logger logger = Logger.getLogger( this.getClass().getName() );
	
	protected AbstractModelContextFactory() {
		listeners = new ArrayList<IModelBuilderListener<IDescribable<?>>>();
	}
	
	protected final void notifyListeners( ModelBuilderEvent<IDescribable<?>>  event ){
		for( IModelBuilderListener<IDescribable<?>>  listener: this.listeners )
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
		logger.info("Parsing model: " + identifier );
		XMLTemplateBuilder<C,ITemplateLeaf<C>> builder = new XMLTemplateBuilder<C, ITemplateLeaf<C>>( identifier, interpreter );
		builder.build();
		ITemplateLeaf<C> root = (ITemplateLeaf<C>) builder.getModel();
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