package org.aieonf.osgi.service;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.builder.IFunctionProvider;
import org.aieonf.model.provider.IModelDelegate;
import org.aieonf.model.provider.IModelFunctionProvider;
import org.aieonf.template.ITemplateLeaf;
import org.aieonf.template.context.IProviderContextFactory;

public abstract class AbstractServiceComponent<T extends IContextAieon, U extends IDescriptor> implements IModelFunctionProvider<T, U>
{

	private IProviderContextFactory<T> factory;
	
	protected  AbstractServiceComponent( IProviderContextFactory<T> factory ){
		this.factory = factory;
	}

	protected IProviderContextFactory<T> getFactory() {
		return factory;
	}

	@SuppressWarnings("unused")
	public void activate(){
		try{
			ITemplateLeaf<IContextAieon> template = factory.createTemplate();
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
	}
	
	public void deactivate(){
	}

	/* (non-Javadoc)
	 * @see org.aieonf.template.context.IProviderContextFactory#addProvider(org.aieonf.model.builder.IFunctionProvider)
	 */
	public void addProvider( IFunctionProvider<IDescriptor, IModelDelegate<IContextAieon, T>> function ){
		factory.addProvider(function);
	}
	

	/* (non-Javadoc)
	 * @see org.aieonf.template.context.IProviderContextFactory#addProvider(org.aieonf.model.builder.IFunctionProvider)
	 */
	public void removeProvider( IFunctionProvider<IDescriptor,IModelDelegate<IContextAieon, T>> function ){
		factory.removeProvider(function);
	}

	//Every domain may use this function
	@Override
	public boolean supportsDomain(IDomainAieon domain) {
		return ( domain != null );
	}
}
