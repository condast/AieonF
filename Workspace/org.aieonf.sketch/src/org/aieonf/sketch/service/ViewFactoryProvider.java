package org.aieonf.sketch.service;

import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.osgi.swt.IViewFactory;
import org.aieonf.sketch.factory.SelectedFactory;
import org.aieonf.sketch.swt.SketchController;
import org.aieonf.sketch.swt.SketchController.Pages;
import org.aieonf.sketch.swt.SketchWizard;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;

public class ViewFactoryProvider implements IViewFactory<Composite, Composite>{

	private SelectedFactory selected = SelectedFactory.getInstance();

	private SketchController barController;
	private SketchWizard wizard;
	
	public ViewFactoryProvider() {
		super();
	}

	public void activate(){ /* NOTHING */}
	
	public void deactivate(){/* NOTHING */}
	
	@Override
	public String getIdentifier() {
		if( selected.getFactory() == null )
			return null;
		IDomainAieon domain = selected.getFactory().getDomain();
		return domain.getShortName().toLowerCase();
	}

	@Override
	public Composite createEntry( Views view, Composite parent, int style) {
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
