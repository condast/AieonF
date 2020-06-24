package org.aieonf.osgi.service;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.provider.IModelFunctionProvider;
import org.aieonf.template.context.IModelContextFactory;
import org.aieonf.template.context.IProviderContextFactory;
import org.aieonf.template.def.ITemplateLeaf;

public abstract class AbstractServiceComponent<K extends Object, D extends IDescriptor, M extends IDescribable> implements IModelFunctionProvider<K, D, M>
{
	private IModelContextFactory<M> factory;
	
	protected  AbstractServiceComponent( IProviderContextFactory<K,D, M> factory ){
		this.factory = factory;
	}

	protected IModelContextFactory<M> getFactory() {
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
		factory = null;
	}

	@Override
	public boolean supportsDomain(IDomainAieon domain) {
		return ( domain != null );
	}
}
