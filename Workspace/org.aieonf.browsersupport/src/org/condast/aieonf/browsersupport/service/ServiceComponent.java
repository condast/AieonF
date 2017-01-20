package org.condast.aieonf.browsersupport.service;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.provider.IModelDelegate;
import org.aieonf.model.provider.IModelFunctionProvider;
import org.aieonf.model.provider.IModelProvider;
import org.aieonf.template.ITemplateLeaf;
import org.condast.aieonf.browsersupport.context.ContextFactory;
import org.condast.aieonf.browsersupport.context.ModelFunctionProvider;

public class ServiceComponent implements IModelFunctionProvider<IDescriptor,IModelLeaf<IDescriptor>>
{

	//private ActiveLinkProvider activeLinkProvider;
	//private ModelFunctionProvider mf_provider;
	private IModelFunctionProvider<IDescriptor,IModelLeaf<IDescriptor>> function;
	
	public ServiceComponent()
	{
		super();
	}

	public void activate(){
		try{
			ContextFactory factory = new ContextFactory();
			ITemplateLeaf<IContextAieon> template = factory.createTemplate();
			function = new ModelFunctionProvider( template.getDescriptor() );
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
	}
	
	public void deactivate(){
		function = null;
	}

	@Override
	public boolean canProvide(IModelLeaf<IDescriptor> leaf) {
		return ( function != null ) && ( IModelProvider.S_MODEL_PROVIDER_ID.equals( leaf.getID()));
	}

	@Override
	public IModelDelegate<IModelLeaf<IDescriptor>> getFunction(IModelLeaf<IDescriptor> leaf) {
		if( !canProvide(leaf))
			return null;
		return function.getFunction(leaf);
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
