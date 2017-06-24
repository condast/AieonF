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

public abstract class AbstractServiceComponent<C extends IContextAieon, D extends IDomainAieon, U extends IDescribable<IDescriptor>> implements IModelFunctionProvider<D, U>
{

	private IProviderContextFactory<C,D,U> factory;
	
	protected  AbstractServiceComponent( IProviderContextFactory<C,D,U> factory ){
		this.factory = factory;
	}

	protected IProviderContextFactory<C,D,U> getFactory() {
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
	public void addProvider( IFunctionProvider<D, IModelProvider<D, U>> function ){
		factory.addProvider(function);
	}
	

	/* (non-Javadoc)
	 * @see org.aieonf.template.context.IProviderContextFactory#addProvider(org.aieonf.model.builder.IFunctionProvider)
	 */
	public void removeProvider( IFunctionProvider<D,IModelProvider<D, U>> function ){
		factory.removeProvider(function);
	}
	
	//Every domain may use this function
	@Override
	public boolean supportsDomain( String domain) {
		return ( domain != null );
	}

	
	@Override
	public boolean canProvide( String functionName ) {
		return ( factory.hasFunction( functionName ));
	}

}
