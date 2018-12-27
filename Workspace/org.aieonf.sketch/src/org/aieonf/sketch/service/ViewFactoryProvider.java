package org.aieonf.sketch.service;

import java.io.File;

import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.osgi.swt.IViewFactory;
import org.aieonf.sketch.factory.SketchFactory;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;

public class ViewFactoryProvider implements IViewFactory<Composite, Composite>{

	public static final String S_VIEW = "view";
	public static final String S_TOOLBAR = "toolbar.html";
	public static final String S_BODY = "body.html";

	private SketchFactory factory = SketchFactory.getInstance();

	public ViewFactoryProvider() {
		super();
	}

	public void activate(){ /* NOTHING */}

	public void deactivate(){/* NOTHING */}

	@Override
	public String getIdentifier() {
		if( factory.getSelected() == null )
			return null;
		IDomainAieon domain = factory.getSelected().getDomain();
		return domain.getShortName().toLowerCase();
	}

	@Override
	public Composite createEntry( Views view, Composite parent, int style) {
		Browser browser = null;
		try{
			File root = factory.getSelectedRoot();
			File viewFolder = new File( root, S_VIEW );
			switch( view ){
			case BAR:
				browser = new Browser( parent, style );
				browser.setText(StringUtils.getContent(new File( viewFolder, S_TOOLBAR)));
				break;
			case BODY:
				browser = new Browser( parent, style );
				browser.setText(StringUtils.getContent(new File( viewFolder, S_BODY)));
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
