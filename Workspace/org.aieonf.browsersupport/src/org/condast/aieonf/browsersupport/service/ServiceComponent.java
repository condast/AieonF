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

public class ServiceComponent extends AbstractServiceComponent<IContextAieon, IDomainAieon, IModelLeaf<IDescriptor>>
{
	
	public ServiceComponent()
	{
		super( new ContextFactory());
	}

	public void activate(){
		try{
			ITemplateLeaf<IContextAieon> template = super.getFactory().createTemplate();
			IFunctionProvider<IDomainAieon, IModelProvider<IDomainAieon, IModelLeaf<IDescriptor>>> provider =  
					new ModelFunctionProvider( template.getDescriptor() );
			super.addProvider( provider);
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
	}

	
	@Override
	public IModelProvider<IDomainAieon, IModelLeaf<IDescriptor>> getFunction(String functionName ) {
		if( !canProvide(functionName))
			return null;
		return getFactory().getFunction( functionName );
	}

	protected void initialise() {
		//mf_provider = ModelFunctionProvider.getInstance();
		//super.addAttendee( mf_provider );

		//activeLinkProvider = ActiveLinkProvider.getInstance();
		//super.addAttendee(activeLinkProvider);
	}


	protected void finalise() {
		//super.removeAttendee( this.activeLinkProvider );
		//this.activeLinkProvider = null;
		
		//super.removeAttendee( this.mf_provider );
		//this.mf_provider = null;
		
		//super.finalise();
	}	
}
