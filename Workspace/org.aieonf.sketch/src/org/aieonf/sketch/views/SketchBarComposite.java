package org.aieonf.sketch.views;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;

public class SketchBarComposite extends Composite {
	private static final long serialVersionUID = 1L;

	private Browser browser;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SketchBarComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		browser=  new Browser( this, style);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
