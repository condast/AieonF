package org.aieonf.osgi.service;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.builder.IFunctionProvider;
import org.aieonf.model.provider.IModelDelegate;
import org.aieonf.model.provider.IModelFunctionProvider;
import org.aieonf.template.ITemplateLeaf;
import org.aieonf.template.context.IModelContextFactory;
import org.aieonf.template.context.IProviderContextFactory;

public abstract class AbstractServiceComponent implements IModelFunctionProvider<IDescriptor,IModelLeaf<IDescriptor>>
{

	private IProviderContextFactory<IContextAieon> factory;
	
	protected  AbstractServiceComponent( IProviderContextFactory<IContextAieon> factory ){
		this.factory = factory;
	}

	protected IModelContextFactory<IContextAieon> getFactory() {
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
	public void addProvider( IFunctionProvider<IDescriptor,IModelDelegate<IModelLeaf<IContextAieon>>> function ){
		factory.addProvider(function);
	}
	

	/* (non-Javadoc)
	 * @see org.aieonf.template.context.IProviderContextFactory#addProvider(org.aieonf.model.builder.IFunctionProvider)
	 */
	public void removeProvider( IFunctionProvider<IDescriptor,IModelDelegate<IModelLeaf<IContextAieon>>> function ){
		factory.removeProvider(function);
	}

	//Every domain may use this function
	@Override
	public boolean supportsDomain(IDomainAieon domain) {
		return ( domain != null );
	}

	@Override
	public boolean canProvide(IModelLeaf<IDescriptor> leaf) {
		// NOTHING FOR NOW
		return false;
	}

	@Override
	public IModelDelegate<IModelLeaf<IDescriptor>> getFunction(IModelLeaf<IDescriptor> leaf) {
		// NOTHING FOR NOW
		return null;
	}

}
