package org.aieonf.template.context;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.commons.Utils;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.function.DescribableCollectionPredicate;
import org.aieonf.concept.function.IDescribablePredicate;
import org.aieonf.model.builder.IFunctionProvider;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.provider.IModelProvider;
import org.aieonf.model.xml.IModelInterpreterFactory;
import org.aieonf.template.builder.TemplateInterpreterFactory;
import org.aieonf.template.def.ITemplateLeaf;

/**
 * The simple context factory creates a default context and model
 * @author Kees Pieters
 */
public abstract class AbstractProviderContextFactory<D extends IDescriptor, M extends IModelLeaf<D>> 
extends AbstractModelContextFactory<D,M> implements IProviderContextFactory<String, D, M>
{
	private static final String S_MODEL = "Model";
	private String bundle_id;
	
	private Collection<IFunctionProvider<String, IModelProvider<String, D, M>>> functions;
	
	private IModelInterpreterFactory<IContextAieon> factory;
	
	private DescribableCollectionPredicate<IDescriptor> predicates;

	protected AbstractProviderContextFactory( String bundle_id, Class<?> clss ) {
		this( bundle_id, new TemplateInterpreterFactory<IContextAieon>(clss));
	}
	
	protected AbstractProviderContextFactory( String bundle_id, IModelInterpreterFactory<IContextAieon> factory ) {
		this.bundle_id = bundle_id;
		functions = new ArrayList<>();
		this.factory = factory;
		predicates = new DescribableCollectionPredicate<>(); 
	}

	protected String getBundleId() {
		return bundle_id;
	}

	/* (non-Javadoc)
	 * @see org.aieonf.template.context.IProviderContextFactory#hasFunction(java.lang.String)
	 */
	@Override
	public boolean hasFunction( String name ){
		if( Utils.assertNull( name ))
			return false;
		for( IFunctionProvider<String, IModelProvider<String, D, M>> function: functions ){
			if( function.canProvide( name ))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.aieonf.template.context.IProviderContextFactory#removeProvider(org.aieonf.model.builder.IFunctionProvider)
	 */
	@Override
	public void addProvider(IFunctionProvider<String, IModelProvider<String, D, M>> function) {
		IDomainAieon domain = super.getDomain();
		if( function.supportsDomain( domain )){
			this.functions.add( function );
		}
	}

	@Override
	public IModelProvider<String, D, M> getFunction(String key) {
		for( IFunctionProvider<String, IModelProvider<String, D, M>> function: functions ){
			if( function.canProvide( key ))
				return function.getFunction(key);
		}
		return null;
	}

	@Override
	public void removeProvider( IFunctionProvider<String,IModelProvider<String, D, M>> function ){
		this.functions.remove( function );
	}

	@Override
	public ITemplateLeaf<IContextAieon> onCreateTemplate() {
		ITemplateLeaf<IContextAieon> template  = this.createDefaultTemplate( bundle_id, factory );	
		IDescriptor descriptor = template.getDescriptor();
		String source = descriptor.get( IConcept.Attributes.SOURCE.name() );
		if( Utils.assertNull( source ))
			descriptor.set( IConcept.Attributes.SOURCE.name(), template.getID()+ File.separator + S_MODEL );
		return template;
	}

	public void addPredicate( IDescribablePredicate<IDescriptor> predicate ) {
		this.predicates.addPredicate(predicate);
	}

	public void removePredicate( IDescribablePredicate<IDescriptor> predicate ) {
		this.predicates.removePredicate(predicate);
	}

	@Override
	public IDescribablePredicate<IDescriptor> createPredicates(){
		return predicates;
	}
}