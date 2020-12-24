package org.aieonf.commons.ui.swt;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public abstract class AbstractIndexedComposite extends Composite {
	private static final long serialVersionUID = 1L;

	private Composite selected;
	
	private int index;
	
	private boolean disposed;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AbstractIndexedComposite(Composite parent, int style) {
		super(parent, style);
		this.index = 0;
		this.disposed = false;
		this.setLayout( new FillLayout());
		createComposite( this, index );
	}

	protected abstract Composite onCreateControl( Composite parent, int index );
	
	protected void createComposite( Composite indexedComposite, int index) {
		if( disposed )
			return;
		getDisplay().asyncExec( ()->onRun( indexedComposite ));
	}

	protected void onRun( Composite indexedComposite ) {
		for( Control control: getChildren()) {
			control.dispose();
		}
		try {
			selected = onCreateControl( indexedComposite, index );	
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
	}

	protected int getIndex() {
		return index;
	}

	protected void setIndex(int index) {
		this.index = index;
	}

	protected Composite getSelected() {
		return selected;
	}

	protected void refresh() {
		createComposite(this, index);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	@Override
	public void dispose() {
		this.disposed = true;
		super.dispose();
	}

}
