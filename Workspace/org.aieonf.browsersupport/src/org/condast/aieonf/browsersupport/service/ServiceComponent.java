package org.condast.aieonf.browsersupport.service;

import org.eclipselabs.osgi.ds.broker.service.AbstractAttendeeProviderComponent;

public class ServiceComponent extends AbstractAttendeeProviderComponent
{

	private ActiveLinkProvider activeLinkProvider;
	private ModelFunctionProvider mf_provider;
	
	public ServiceComponent()
	{
		super();
	}


	@Override
	protected void initialise() {
		mf_provider = ModelFunctionProvider.getInstance();
		super.addAttendee( mf_provider );

		activeLinkProvider = ActiveLinkProvider.getInstance();
		super.addAttendee(activeLinkProvider);
	}


	@Override
	protected void finalise() {
		super.removeAttendee( this.activeLinkProvider );
		this.activeLinkProvider = null;
		
		super.removeAttendee( this.mf_provider );
		this.mf_provider = null;
		
		super.finalise();
	}
	
	
}
