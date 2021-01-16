package org.aieonf.template.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import org.aieonf.commons.Utils;
import org.aieonf.commons.filter.IAttributeFilter;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.filter.AttributeFilter;
import org.aieonf.concept.filter.DescribableFilter;
import org.aieonf.concept.filter.IDescribableFilter;
import org.aieonf.model.builder.IModelBuilder;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.search.ModelScanner;
import org.aieonf.model.xml.IModelInterpreterFactory;
import org.aieonf.template.def.ITemplateLeaf;
import org.aieonf.template.def.ITemplateNode;
import org.aieonf.template.xml.XMLTemplateBuilder;

public abstract class AbstractModelContextFactory<D extends IDescriptor, M extends IModelLeaf<D>> implements ITemplateContextFactory<M> {

	public static final String S_MODEL_ID = "org.aieonf.model";
	public static final String S_DEFAULT_VERSION = "0.1";
	
	private ITemplateLeaf<IContextAieon> template;

	private Collection<IModelBuilderListener<IDescribable>> listeners;

	private Logger logger = Logger.getLogger( this.getClass().getName() );
	
	protected AbstractModelContextFactory() {
		listeners = new ArrayList<IModelBuilderListener<IDescribable>>();
	}
	
	protected final void notifyListeners( ModelBuilderEvent<IDescribable>  event ){
		for( IModelBuilderListener<IDescribable>  listener: this.listeners )
			listener.notifyChange(event);
	}

	/**
	 * Create the model
	 * @return
	 */
	protected abstract ITemplateLeaf<IContextAieon> onCreateTemplate();

	/**
	 * Create the model
	 * @return
	 */
	@Override
	public ITemplateLeaf<IContextAieon> createTemplate(){
		this.template = onCreateTemplate();
		return template;
	}
	
	/* (non-Javadoc)
	 * @see org.aieonf.template.context.IModelContextFactory#getModel()
	 */
	@Override
	public ITemplateLeaf<IContextAieon> getTemplate() {
		return template;
	}

	/* (non-Javadoc)
	 * @see org.aieonf.template.context.IModelContextFactory#getDomain()
	 */
	@Override
	public IDomainAieon getDomain(){
		ModelScanner<IContextAieon> search = new ModelScanner<IContextAieon>( this.template );
		IDescriptor[] domains = search.getDescriptors( IDomainAieon.Attributes.DOMAIN.toString() );
		if( Utils.assertNull(domains))
			return null;
		IDomainAieon domain = (IDomainAieon) domains[0];
		String str = domain.getDomain();
		domain.set( IDescriptor.Attributes.ID.name(), String.valueOf( str.hashCode() ) );
		domain.set( IDescriptor.Attributes.VERSION.name(), S_DEFAULT_VERSION );
		return domain;
	}

	protected final ITemplateLeaf<IContextAieon> createDefaultTemplate( String identifier, IModelInterpreterFactory<IContextAieon> factory ) {
		logger.info("Parsing model: " + identifier );
		XMLTemplateBuilder<IContextAieon,ITemplateLeaf<IContextAieon>> builder = 
				new XMLTemplateBuilder<IContextAieon, ITemplateLeaf<IContextAieon>>( identifier, factory, IModelBuilder.S_DEFAULT_LOCATION);
		builder.build();
		ITemplateLeaf<IContextAieon> root = (ITemplateLeaf<IContextAieon>) builder.getModel();
		long id = StringUtils.isEmpty(identifier)?-1:identifier.hashCode();
		root.getDescriptor().set( IDescriptor.Attributes.ID.name(), String.valueOf(id ));
		root.getDescriptor().set( IDescriptor.Attributes.VERSION.name(), S_DEFAULT_VERSION );
		root.getDescriptor().set( IConcept.Attributes.SOURCE.name(), identifier );
		return root;	
	}

	/**
	 * Get the template with the given identifier
	 * @param identifier
	 * @return
	 */
	protected ITemplateLeaf<IContextAieon> getTemplate( String identifier ){
		return getTemplate( this.template, identifier );
	}

	@SuppressWarnings("unchecked")
	protected ITemplateLeaf<IContextAieon> getTemplate( ITemplateLeaf<? extends IDescriptor> leaf, String identifier ){
		if( StringUtils.isEmpty( identifier ))
			return null;
		if( identifier.equals( String.valueOf( leaf.getID()))){
			if( StringUtils.isEmpty( leaf.getDescriptor().get( IConcept.Attributes.SOURCE.name() ) )){
				leaf.getDescriptor().set( IConcept.Attributes.SOURCE.name(), String.valueOf( template.getID() ));
			}
			return (ITemplateLeaf<IContextAieon>) leaf;
		}
		if( leaf.isLeaf())
			return null;
		ITemplateNode<IDescriptor> node = (ITemplateNode<IDescriptor>) leaf;
		for( IModelLeaf<? extends IDescriptor> child: node.getChildren().keySet() ){
			ITemplateLeaf<IContextAieon> result = getTemplate( (ITemplateLeaf<? extends IDescriptor>) child, identifier );
			if( result != null )
				return result;
		}
		return null;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public M createModel() {
		ModelScanner<IDescriptor> scanner = new ModelScanner<IDescriptor>( this.template );
		IAttributeFilter<IDescriptor> filter = new AttributeFilter<>( AttributeFilter.Rules.EQUALS, IConcept.Attributes.IDENTIFIER.name(), S_MODEL_ID);
		IDescribableFilter<IModelLeaf<IDescriptor>> modelFilter = new DescribableFilter<>( filter);
		M model = null;
		Collection<IModelLeaf<IDescriptor>> models = scanner.search(modelFilter);
		if( !Utils.assertNull(models))
			model = (M) models.iterator().next();
		return model;
	}

}