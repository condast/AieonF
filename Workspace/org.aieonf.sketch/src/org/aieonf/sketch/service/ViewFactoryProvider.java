package org.aieonf.sketch.service;

import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.osgi.swt.IViewFactory;
import org.aieonf.sketch.factory.SelectedFactory;
import org.aieonf.sketch.swt.BarComposite;
import org.aieonf.sketch.swt.BodyComposite;
import org.eclipse.swt.widgets.Composite;

public class ViewFactoryProvider implements IViewFactory<Composite, Composite>{

	private SelectedFactory selected = SelectedFactory.getInstance();

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
		Composite composite = null;
		switch( view ){
		case BAR:
			composite = new BarComposite( parent, style );
			break;
		case BODY:
			composite = new BodyComposite( parent, style );
			break;
		default:
			break;
		}
		return composite;
	}
}
