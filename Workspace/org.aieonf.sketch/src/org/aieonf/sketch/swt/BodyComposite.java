package org.aieonf.sketch.swt;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import java.net.MalformedURLException;

import org.aieonf.sketch.factory.SelectedFactory;
import org.aieonf.sketch.factory.SketchModelFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;

public class BodyComposite extends Composite{
	private static final long serialVersionUID = 6213038771633411002L;
	
	private Browser browser;

	//private BarLinkController service = BarLinkController.getInstance();

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public BodyComposite(Composite parent, int style)
	{
		super(parent, style);
		
		setLayout( new FillLayout() );	
		browser = new Browser(this, SWT.BORDER);

		SelectedFactory selected = SelectedFactory.getInstance();
		SketchModelFactory smf = selected.getFactory();
		try {
			String location = smf.getWebPath("body.html");
			browser.setUrl( location );
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
