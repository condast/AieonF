package org.aieonf.template.context;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.commons.Utils;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.concept.security.IPasswordAieon;
import org.aieonf.concept.security.PasswordAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.IModelNode;
import org.aieonf.model.builder.IFunctionProvider;
import org.aieonf.model.provider.IModelProvider;
import org.aieonf.model.xml.IXMLModelBuilder;
import org.aieonf.template.ITemplateLeaf;
import org.aieonf.template.context.AbstractModelContextFactory;

/**
 * The simple context factory creates a default context and model
 * @author Kees Pieters
 */
public abstract class AbstractProviderContextFactory<C extends IContextAieon, U extends IDescribable<IDescriptor>> extends AbstractModelContextFactory<C> implements IProviderContextFactory<C, U>
{
	public static final String S_DATABASE_ID = "org.aieonf.database";

	private static final String S_MODEL = "Model";
	private String bundle_id;
	private String provider_id;
	
	private Collection<IModelProvider<C, U>> functions;
	
	private IXMLModelBuilder<IDescriptor,ITemplateLeaf<IDescriptor>> creator;
	
	protected AbstractProviderContextFactory( String bundle_id, 
			IXMLModelBuilder<IDescriptor,ITemplateLeaf<IDescriptor>> creator ) {
		this( bundle_id, S_DATABASE_ID, creator );
	}
	
	protected AbstractProviderContextFactory( String bundle_id, String provider_id, 
			IXMLModelBuilder<IDescriptor,ITemplateLeaf<IDescriptor>> creator ) {
		this.bundle_id = bundle_id;
		this.provider_id = provider_id;
		functions = new ArrayList<IModelProvider<C, U>>();
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
		for( IModelProvider<C, U>  delegate: functions ){
			if( delegate.hasFunction(function))
				return true;
		}
		return false;
	}
	
	@Override
	public IModelProvider<C, U> getFunction( String functionName ) {
		for( IModelProvider<C, U> function: functions ){
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
	
	@SuppressWarnings("unchecked")
	@Override
	public void addProvider(IFunctionProvider<C, IModelProvider<C, U>> function) {
		if( super.getTemplate() == null )
			createTemplate();
		IModelNode<C> leaf = (IModelNode<C>) super.getTemplate( provider_id );
		IModelLeaf<IDescriptor> child = (IModelLeaf<IDescriptor>) leaf.getChildren( ILoaderAieon.Attributes.LOADER.toString() )[0];
		IPasswordAieon pwd = new PasswordAieon( child.getDescriptor());
		pwd.setUserName("keesp");
		pwd.setPassword("test");
		if( function.canProvide( leaf.getDescriptor() )){
			IModelProvider<C, U> delegate = function.getFunction( leaf.getDescriptor() );
			this.functions.add( delegate );
		}
	}
	

	/* (non-Javadoc)
	 * @see org.aieonf.template.context.IProviderContextFactory#removeProvider(org.aieonf.model.builder.IFunctionProvider)
	 */
	@Override
	public void removeProvider( IFunctionProvider<C,IModelProvider<C, U>> function ){
		IModelLeaf<C> leaf = super.getTemplate( provider_id );
		if( function.canProvide(super.getTemplate( provider_id ).getDescriptor())){
			IModelProvider<C, U> delegate = function.getFunction( leaf.getDescriptor() );
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