package org.aieonf.osgi.service;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.provider.IModelFunctionProvider;
import org.aieonf.template.context.IModelContextFactory;
import org.aieonf.template.context.IProviderContextFactory;
import org.aieonf.template.def.ITemplateLeaf;

public abstract class AbstractServiceComponent<C extends IContextAieon, D extends IDomainAieon, T extends Object, U extends IDescribable<IDescriptor>> implements IModelFunctionProvider<T, D, U>
{
	private IModelContextFactory<C,D> factory;
	
	protected  AbstractServiceComponent( IProviderContextFactory<C,D,T,U> factory ){
		this.factory = factory;
	}

	protected IModelContextFactory<C,D> getFactory() {
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

	@Override
	public boolean supportsDomain(IDomainAieon domain) {
		return ( domain != null );
	}
}
