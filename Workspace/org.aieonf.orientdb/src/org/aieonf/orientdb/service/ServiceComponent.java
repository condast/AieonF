package org.aieonf.orientdb.service;

import org.aieonf.orientdb.service.ModelFunctionProvider;
import org.eclipselabs.osgi.ds.broker.service.AbstractAttendeeProviderComponent;

public class ServiceComponent extends AbstractAttendeeProviderComponent
{
	private ModelFunctionProvider modelProvider;
	
	@Override
	protected void initialise() {
		modelProvider = ModelFunctionProvider.getInstance();	
		super.addAttendee( modelProvider );
	}

	@Override
	protected void finalise() {
		this.removeAttendee( modelProvider );
		this.modelProvider = null;
		super.finalise();
	}
}
