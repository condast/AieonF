package org.aieonf.sketch.service;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.osgi.swt.AbstractViewFactory;
import org.aieonf.osgi.swt.IViewFactory;
import org.aieonf.sketch.controller.SketchController;
import org.aieonf.sketch.controller.SketchController.Pages;
import org.aieonf.sketch.factory.SketchFactory;
import org.aieonf.sketch.factory.SketchModelFactory;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.osgi.service.component.annotations.Component;

@Component( name=SketchViewProvider.S_SKETCH_ID, immediate=true)
public class SketchViewProvider extends AbstractViewFactory<Composite> implements IViewFactory<Composite, Composite>{

	private SketchFactory selected = SketchFactory.getInstance();

	private SketchController barController;

	public static final String S_SKETCH_ID = "org.aieonf.sketch.view";	

	public SketchViewProvider() {
		super( DomainProvider.getDomain());
	}

	public void activate(){ /* NOTHING */}
	
	public void deactivate(){/* NOTHING */}
	
	@Override
	public boolean isOfDomain(IDomainAieon domain) {
		Map<IDomainAieon, SketchModelFactory> factories = new HashMap<>();
		try {
			factories = SketchFactory.getInstance().getFactories();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return factories.keySet().contains(domain);
	}

	@Override
	public String getIdentifier() {
		try {
			if( selected.getFactory() == null )
				return null;
			IDomainAieon domain = selected.getFactory().getDomain();
			return domain.getShortName().toLowerCase();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Composite onCreateEntry( Views view, Composite parent, int style) {
		Browser browser = new Browser( parent, style );
		barController = new SketchController( browser );
		try{
			switch( view ){
			case HOME:
				break;
			case BAR:
				//browser.setUrl("http://www.condast.com");
				barController.setBrowser( Pages.BAR );
				break;
			case BODY:
				barController.setBrowser( Pages.SEARCH );
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
