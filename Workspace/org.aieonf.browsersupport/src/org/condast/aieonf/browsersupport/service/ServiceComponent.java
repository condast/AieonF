package org.condast.aieonf.browsersupport.service;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.builder.IFunctionProvider;
import org.aieonf.model.provider.IModelProvider;
import org.aieonf.osgi.service.AbstractServiceComponent;
import org.aieonf.template.ITemplateLeaf;
import org.condast.aieonf.browsersupport.context.ContextFactory;
import org.condast.aieonf.browsersupport.context.ModelFunctionProvider;

public class ServiceComponent extends AbstractServiceComponent<IContextAieon, IDomainAieon, String, IModelLeaf<IDescriptor>>
{
	
	public ServiceComponent()
	{
		super( new ContextFactory());
	}

	public void activate(){
		try{
			ITemplateLeaf<IContextAieon> template = super.getFactory().createTemplate();
			IFunctionProvider<String, IModelProvider<IDomainAieon, IModelLeaf<IDescriptor>>> provider =  
					new ModelFunctionProvider( template.getDescriptor() );
			ContextFactory factory = (ContextFactory) super.getFactory();
			factory.addProvider( provider);
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
	}

	@Override
	public boolean canProvide(String key) {
		ContextFactory factory = (ContextFactory) super.getFactory();
		return factory.hasFunction( key);
	}

	@Override
	public IModelProvider<IDomainAieon, IModelLeaf<IDescriptor>> getFunction(String key) {
		ContextFactory factory = (ContextFactory) super.getFactory();
		return factory.getFunction(key);
	}
	
	
}
