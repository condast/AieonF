package org.condast.aieonf.browsersupport.service;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.provider.IModelDelegate;
import org.aieonf.model.provider.IModelFunctionProvider;
import org.aieonf.model.provider.IModelProvider;
import org.aieonf.osgi.service.AbstractServiceComponent;
import org.aieonf.template.ITemplateLeaf;
import org.condast.aieonf.browsersupport.context.ContextFactory;
import org.condast.aieonf.browsersupport.context.ModelFunctionProvider;

public class ServiceComponent extends AbstractServiceComponent<IContextAieon, IDescriptor>
{

	private IModelFunctionProvider<IContextAieon, IDescriptor> function;
	
	public ServiceComponent()
	{
		super( new ContextFactory());
	}

	public void activate(){
		try{
			ITemplateLeaf<IContextAieon> template = super.getFactory().createTemplate();
			function = new ModelFunctionProvider( template.getDescriptor() );
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
	}
	
	public void deactivate(){
		function = null;
	}

	//Every domain may use this function
	@Override
	public boolean supportsDomain(IDomainAieon domain) {
		return ( domain != null );
	}

	
	@Override
	public boolean canProvide(IContextAieon data) {
		return ( function != null ) && ( IModelProvider.S_MODEL_PROVIDER_ID.equals( data ));
	}

	@Override
	public IModelDelegate<IContextAieon, IDescriptor> getFunction(IContextAieon context) {
		if( !canProvide(context))
			return null;
		return function.getFunction( context );
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
