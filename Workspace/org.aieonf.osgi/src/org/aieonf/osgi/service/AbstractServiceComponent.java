package org.aieonf.osgi.service;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.builder.IFunctionProvider;
import org.aieonf.model.provider.IModelFunctionProvider;
import org.aieonf.model.provider.IModelProvider;
import org.aieonf.template.ITemplateLeaf;
import org.aieonf.template.context.IProviderContextFactory;

public abstract class AbstractServiceComponent<C extends IContextAieon, D extends IDomainAieon, T extends Object, U extends IDescribable<IDescriptor>> implements IModelFunctionProvider<T, U>
{

	private IProviderContextFactory<C,D,T,U> factory;
	
	protected  AbstractServiceComponent( IProviderContextFactory<C,D,T,U> factory ){
		this.factory = factory;
	}

	protected IProviderContextFactory<C,D,T,U> getFactory() {
		return factory;
	}

	@SuppressWarnings("unused")
	public void activate(){
		try{
			ITemplateLeaf<C> template = factory.createTemplate();
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
	}
	
	public void deactivate(){
		factory = null;
	}

	/* (non-Javadoc)
	 * @see org.aieonf.template.context.IProviderContextFactory#addProvider(org.aieonf.model.builder.IFunctionProvider)
	 */
	public void addProvider( IFunctionProvider<T, IModelProvider<U>> function ){
		factory.addProvider(function);
	}
	

	/* (non-Javadoc)
	 * @see org.aieonf.template.context.IProviderContextFactory#addProvider(org.aieonf.model.builder.IFunctionProvider)
	 */
	public void removeProvider( IFunctionProvider<T,IModelProvider<U>> function ){
		factory.removeProvider(function);
	}

	
	@Override
	public boolean supportsDomain(IDomainAieon domain) {
		return ( domain != null );
	}

	@Override
	public boolean canProvide(T key) {
		return factory.hasFunction(key);
	}

	@Override
	public IModelProvider<U> getFunction(T key) {
		if( !supportsDomain( factory.getDomain()))
			return null;
		return factory.getFunction(key);
	}
	
	
}
