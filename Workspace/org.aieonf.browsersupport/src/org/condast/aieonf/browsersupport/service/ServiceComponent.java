package org.condast.aieonf.browsersupport.service;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.provider.IModelProvider;
import org.aieonf.osgi.service.AbstractServiceComponent;
import org.aieonf.template.ITemplateLeaf;
import org.condast.aieonf.browsersupport.context.ContextFactory;
import org.condast.aieonf.browsersupport.context.ModelFunctionProvider;

public class ServiceComponent extends AbstractServiceComponent<IContextAieon, IModelLeaf<IDescriptor>>
{
	
	public ServiceComponent()
	{
		super( new ContextFactory());
	}

	public void activate(){
		try{
			ITemplateLeaf<IContextAieon> template = super.getFactory().createTemplate();
			super.addProvider( new ModelFunctionProvider( template.getDescriptor() ));
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
	}
	
	//Every domain may use this function
	@Override
	public boolean supportsDomain(IDomainAieon domain) {
		return ( domain != null );
	}

	
	@Override
	public boolean canProvide( String functionName ) {
		return ( super.getFactory().hasFunction( functionName ));
	}

	@Override
	public IModelProvider<IContextAieon, IModelLeaf<IDescriptor>> getFunction( String functionName) {
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
