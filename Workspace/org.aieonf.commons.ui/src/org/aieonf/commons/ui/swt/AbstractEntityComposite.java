package org.aieonf.commons.ui.swt;

import org.eclipse.swt.widgets.Composite;

import org.aieonf.commons.ui.verification.IWidgetVerificationDelegate;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyListener;

public abstract class AbstractEntityComposite<T extends Object> extends Composite implements IManagedProvider<T> {
	private static final long serialVersionUID = 1L;
	
	private IEntityController<T> controller;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	protected AbstractEntityComposite(Composite parent, int style ) {
		this( parent, style, new DefaultEntityController<T>( null ), false);
	}

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	protected AbstractEntityComposite(Composite parent, int style, boolean delayCreation ) {
		this( parent, style, new DefaultEntityController<T>( null ), delayCreation);
	}

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	protected AbstractEntityComposite(Composite parent, int style, IEntityController<T> controller, boolean delayCreation) {
		super( parent, style );
		this.controller = controller;
		this.controller.setBlockEntry(true);
		if( !delayCreation)
			this.createComposite(parent, style);
		this.controller.setBlockEntry(false);
	}

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	protected AbstractEntityComposite(Composite parent, int style, IEntityController<T> controller ) {
		this( parent, style, controller, false );
	}
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	protected AbstractEntityComposite(Composite parent, int style, IWidgetVerificationDelegate verifier, boolean delayCreation ) {
		this( parent, style, new DefaultEntityController<T>( verifier ), delayCreation );
	}

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	protected AbstractEntityComposite(Composite parent, int style, IWidgetVerificationDelegate verifier) {
		this( parent, style, verifier, false );
	}
	
	protected abstract void createComposite( Composite parent, int style );

	protected IEntityController<T> getController() {
		return controller;
	}

	public VerifyListener getVerifyListener(){
		return this.controller.getVerifyListener();
	}
	
	/**
	 * Returns true if the compositie can only be viewed and not modified
	 * @return
	 */
	public boolean isViewOnly(){
		return controller.isViewOnly();
	}
	
	public void setViewOnly( boolean choice ){
		this.controller.setDirty( choice );
	}
	
	/**
	 * Returns true if the widgets have changed but the imnput is not yet updated
	 * @return
	 */
	public boolean isDirty(){
		return controller.isDirty();
	}
	
	/**
	 * Returns true if the required fields all contain data 
	 * @return
	 */
	public abstract boolean checkRequiredFields();
	
	/**
	 * Get the model that is displayed in its current state
	 * @return
	 */
	protected T getModel(){
		return controller.getInput();
	}

	@Override
	public void addVerifyListener(VerifyListener listener) {
		getController().addVerifyListener(listener);
	}

	@Override
	public void removeVerifyListener(VerifyListener listener) {
		getController().removeVerifyListener(listener);
	}

	@Override
	public void addEditListener( IEditListener<?> listener) {
		controller.addEditListener(listener);
	}

	@Override
	public void removeEditListener( IEditListener<?> listener) {
		controller.removeEditListener(listener);
	}
	
	protected void notifySelectionListeners( SelectionEvent event ){
		controller.notifyWidgetSelected( event );
	}

	@Override
	public void notifyInputEdited( EditEvent<T> event ){
		controller.getEditListener().notifyInputEdited(event);
	}

	protected SelectionListener getListener(){
		return controller.getListener();
	}

	/**
	 * Upon getting the input from the controller, the values of the widgets will be
	 * added
	 * @param input
	 */
	protected abstract T onGetInput( T input );
	
	@Override
	public T getInput() {
		T input = null;
		try{
			input = this.controller.getInput();
			T inp = this.onGetInput(input);
			if(( input != null ) &&  !input.equals( inp ))
				this.controller.setInput(inp);
			this.controller.setDirty(false);//reset..it should be synchronized
			return inp;
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
		return input;
	}

	/**
	 * When overwrite is true, the values of the widgets will be replaced with the value from the
	 *input source
	 * @param input
	 */
	protected abstract void onSetInput( T input, boolean overwrite );

	@Override
	public void setInput( T input, boolean overwrite) {
		try{
			this.controller.setBlockEntry( true );
			IWidgetVerificationDelegate wvd = this.controller.getVerifier();
			if( wvd != null )
				wvd.disable(true);
			this.onSetInput(input, overwrite );
			this.controller.setInput(input);
			if( wvd != null )
				wvd.disable(false);
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
		finally{
			this.controller.setBlockEntry( false );
		}
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}