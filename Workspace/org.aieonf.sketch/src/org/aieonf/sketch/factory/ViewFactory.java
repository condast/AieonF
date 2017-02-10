package org.aieonf.sketch.factory;

import java.util.logging.Logger;

import org.aieonf.osgi.swt.IViewFactory;
import org.aieonf.sketch.swt.BodyComposite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class ViewFactory implements IViewFactory<Composite, Composite>
{
	public static final String S_SKETCH = "Sketch";
	
	private Logger logger;

	private SelectedFactory selected = SelectedFactory.getInstance();

	@Override
	public Composite createEntry( Views view, Composite parent, int style )
	{
		logger = Logger.getLogger( this.getClass().getName() );
		logger.info("Creating sketch composite");
		Composite composite = new BodyComposite(parent, SWT.NONE);		
		return composite;
	}

	@Override
	public String getIdentifier() {	
		return S_SKETCH;
	}
}
