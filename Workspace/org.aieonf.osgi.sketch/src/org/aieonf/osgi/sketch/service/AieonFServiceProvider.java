package org.aieonf.osgi.sketch.service;

import org.aieonf.osgi.service.AbstractAieonFServiceProvider;
import org.aieonf.osgi.swt.IViewFactory;
import org.eclipse.swt.widgets.Composite;

public class AieonFServiceProvider extends AbstractAieonFServiceProvider<Composite>{

	private static AieonFServiceProvider provider = new AieonFServiceProvider();
	
	private AieonFServiceProvider() {
	}

	public static AieonFServiceProvider getInstance(){
		return provider;
	}

	@Override
	public IViewFactory<Composite, Composite> getCompositeFactory(String id) {
		// TODO Auto-generated method stub
		return null;
	};

}
