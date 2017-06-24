package org.aieonf.template.context;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.commons.Utils;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.DomainAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.builder.IFunctionProvider;
import org.aieonf.model.provider.IModelProvider;
import org.aieonf.model.xml.IXMLModelBuilder;
import org.aieonf.template.ITemplateLeaf;
import org.aieonf.template.context.AbstractModelContextFactory;

/**
 * The simple context factory creates a default context and model
 * @author Kees Pieters
 */
public abstract class AbstractProviderContextFactory<C extends IContextAieon, U extends IDescribable<? extends IDescriptor>> 
extends AbstractModelContextFactory<C> implements IProviderContextFactory<C, IDomainAieon, U>
{
	public static final String S_DATABASE_ID = "org.aieonf.database";

	private static final String S_MODEL = "Model";
	private String bundle_id;
	private String provider_id;
	
	private Collection<IModelProvider<IDomainAieon, U>> functions;
	
	private IXMLModelBuilder<IDescriptor,ITemplateLeaf<IDescriptor>> creator;
	
	protected AbstractProviderContextFactory( String bundle_id, 
			IXMLModelBuilder<IDescriptor,ITemplateLeaf<IDescriptor>> creator ) {
		this( bundle_id, S_DATABASE_ID, creator );
	}
	
	protected AbstractProviderContextFactory( String bundle_id, String provider_id, 
			IXMLModelBuilder<IDescriptor,ITemplateLeaf<IDescriptor>> creator ) {
		this.bundle_id = bundle_id;
		this.provider_id = provider_id;
		functions = new ArrayList<IModelProvider<IDomainAieon, U>>();
		this.creator = creator;
	}

	protected String getBundleId() {
		return bundle_id;
	}

	/* (non-Javadoc)
	 * @see org.aieonf.template.context.IProviderContextFactory#hasFunction(java.lang.String)
	 */
	@Override
	public boolean hasFunction( String function ){
		for( IModelProvider<IDomainAieon, U>  delegate: functions ){
			if( delegate.hasFunction(function))
				return true;
		}
		return false;
	}
	
	@Override
	public IModelProvider<IDomainAieon,U> getFunction( String functionName ) {
		for( IModelProvider<IDomainAieon, U> function: functions ){
			if( function.hasFunction( functionName ))
					return function;
		}
		return null;
	}


	/**
	 * Return a function that supports the given function
	 * @param function
	 * @return
	 */
/*
	public IModelProvider<IContextAieon, T> getDelegate( String function ){
		for( IModelProvider<IContextAieon, T> delegate: delegates ){
			if( delegate.hasFunction(function))
			return provider;
		}
		return null;
	}
*/
	
	@Override
	public void addProvider(IFunctionProvider<IDomainAieon, IModelProvider<IDomainAieon, U>> function) {
		IDomainAieon domain = new DomainAieon( provider_id );
		if( function.canProvide( domain )){
			IModelProvider<IDomainAieon, U> delegate = function.getFunction( domain );
			this.functions.add( delegate );
		}
	}
	

	/* (non-Javadoc)
	 * @see org.aieonf.template.context.IProviderContextFactory#removeProvider(org.aieonf.model.builder.IFunctionProvider)
	 */
	@Override
	public void removeProvider( IFunctionProvider<IDomainAieon,IModelProvider<IDomainAieon, U>> function ){
		IDomainAieon domain = new DomainAieon( provider_id );
		if( function.canProvide( domain )){
			IModelProvider<IDomainAieon, U> delegate = function.getFunction( domain );
			this.functions.remove( delegate );
		}
	}

	@Override
	public ITemplateLeaf<C> onCreateTemplate() {
		ITemplateLeaf<C> template  = this.createDefaultTemplate( bundle_id, this.creator );	
		IDescriptor descriptor = template.getDescriptor();
		String source = descriptor.get( IConcept.Attributes.SOURCE );
		if( Utils.assertNull( source ))
			descriptor.set( IConcept.Attributes.SOURCE, this.bundle_id + File.separator + S_MODEL );
		return template;
	}
	
	/*
	public void get(IDescriptor descriptor) throws ParseException {
		IModelLeaf<IDescriptor> leaf = super.getTemplate( provider_id );
		IDomainAieon domain = (IDomainAieon) leaf.getDescriptor();
		for( IModelFunctionProvider<IModelLeaf<T>> delegate: providers ){
			delegate.open( domain);
			delegate.get(descriptor);
			delegate.close( domain);
		}
	}

	public void search(IModelFilter<IDescriptor> filter) throws ParseException {
		IModelLeaf<IDescriptor> leaf = super.getTemplate( provider_id );
		IDomainAieon domain = (IDomainAieon) leaf.getDescriptor();
		for( IModelFunctionProvider<IModelLeaf<T>> delegate: providers ){
			delegate.open( domain );
			delegate.search( filter );
			delegate.close( domain );
		}
	}
	*/

}