package org.aieonf.osgi.sketch.service;

import org.aieonf.osgi.service.AbstractAieonFServiceProvider;
import org.aieonf.osgi.sketch.context.SketchFactory;
import org.aieonf.osgi.swt.IViewFactory;
import org.eclipse.swt.widgets.Composite;

public class AieonFServiceProvider extends AbstractAieonFServiceProvider<Composite>{

	public AieonFServiceProvider() {
	}

	@Override
	protected void initSelection() {
		try{
			SketchFactory factory = SketchFactory.getInstance();
			factory.createTemplate();
			super.createBean( factory.getDomain() );
		}
		catch( Exception e ){
			e.printStackTrace();
		}
	}
	@Override
	public IViewFactory<Composite, Composite> getCompositeFactory(String id) {
		// TODO Auto-generated method stub
		return null;
	};

}
