package org.aieonf.sketch.service;

import java.net.MalformedURLException;

import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.osgi.swt.AbstractViewFactory;
import org.aieonf.osgi.swt.IViewFactory;
import org.aieonf.sketch.controller.SketchController;
import org.aieonf.sketch.controller.SketchController.Pages;
import org.aieonf.sketch.factory.SketchFactory;
import org.aieonf.sketch.swt.SketchWizard;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.osgi.service.component.annotations.Component;

@Component( name=SketchViewProvider.S_SKETCH_ID, immediate=true)
public class SketchViewProvider extends AbstractViewFactory<Composite> implements IViewFactory<Composite, Composite>{

	private SketchFactory selected = SketchFactory.getInstance();

	private SketchController barController;
	private SketchWizard wizard;

	public static final String S_SKETCH_ID = "org.aieonf.sketch.view";
	private static final String S_SKETCH = "SKETCH";
	

	public SketchViewProvider() {
		super( DomainProvider.getDomain());
	}

	public void activate(){ /* NOTHING */}
	
	public void deactivate(){/* NOTHING */}
	
	@Override
	public String getIdentifier() {
		try {
			if( selected.getFactory() == null )
				return null;
			IDomainAieon domain = selected.getFactory().getDomain();
			return domain.getShortName().toLowerCase();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Composite onCreateEntry( Views view, Composite parent, int style) {
		Browser browser = null;
		try{
		switch( view ){
		case BAR:
			browser = new Browser( parent, style );
			barController = new SketchController( browser );
			barController.setBrowser( Pages.BAR );
			break;
		case BODY:
			wizard = new SketchWizard( parent, style );
			break;
		default:
			break;
		}
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
		return browser;
	}
}
