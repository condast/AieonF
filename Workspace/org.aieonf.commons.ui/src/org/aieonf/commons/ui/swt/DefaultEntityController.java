package org.aieonf.commons.ui.swt;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.commons.ui.verification.IWidgetVerificationDelegate;
import org.aieonf.commons.ui.verification.IWidgetVerificationDelegate.VerificationTypes;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;

public class DefaultEntityController<T extends Object> implements IEntityController<T> {

	private T input;
	private boolean dirty;
	private boolean update;
	
	/**
	 * If true, the contents can only be viewed and not altered
	 */
	private boolean viewOnly;

	/**
	 * Allows temporaray blocking of verification and
	 * modifcation in the widgets, if this is inconvenient; 
	 */
	private boolean blockEntry;

	private Collection< IEditListener<T>> listeners;
	
	private SelectionListener listener = new SelectionAdapter(){
		private static final long serialVersionUID = 1L;

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public void widgetSelected(SelectionEvent arg0) {
			dirty = true;
			for( IEditListener<?> listener: listeners ){
				listener.notifyInputEdited( new EditEvent( arg0.getSource() ));
			}
		}	
	};

	private IEditListener<T> editListener = new IEditListener<T>(){

		@Override
		public void notifyInputEdited(EditEvent<T> event) {
			if( blockEntry )
				return;
			dirty = true;
			for( IEditListener<T> listener: listeners ){
				listener.notifyInputEdited( event );
			}
		}	
	};

	private IWidgetVerificationDelegate verifier;

	public DefaultEntityController( IWidgetVerificationDelegate verifier ) {
		listeners = new ArrayList< IEditListener<T>>();
		this.verifier = verifier;
		this.blockEntry = false;
	}

	
	@Override
	public boolean isViewOnly() {
		return this.viewOnly;
	}

	@Override
	public void setViewOnly(boolean choice) {
		this.viewOnly = choice;
	}

	/* (non-Javadoc)
	 * @see org.condast.commons.ui.controller.IEntityController#isUpdate()
	 */
	@Override
	public boolean isUpdate() {
		return update;
	}

	@Override	
	public void setUpdate(boolean update) {
		this.update = update;
	}

	@Override
	public boolean isBlocked() {
		return blockEntry;
	}

	@Override
	public void setBlockEntry(boolean blockEntry) {
		this.blockEntry = blockEntry;
	}

	@Override
	public IWidgetVerificationDelegate getVerifier() {
		return verifier;
	}

	@Override
	public void setVerifier(IWidgetVerificationDelegate verifier) {
		this.verifier = verifier;
	}

	/* (non-Javadoc)
	 * @see org.condast.commons.ui.controller.IEntityController#addVerifyListener(org.eclipse.swt.events.VerifyListener)
	 */
	@Override
	public void addVerifyListener( VerifyListener listener ){
		verifier.addVerifyListener(listener);
	}

	/* (non-Javadoc)
	 * @see org.condast.commons.ui.controller.IEntityController#removeVerifyListener(org.eclipse.swt.events.VerifyListener)
	 */
	@Override
	public void removeVerifyListener( VerifyListener listener){
		verifier.removeVerifyListener(listener);
	}

	public VerifyListener getVerifyListener() {
		return verifier.getVerifyListener();
	}

	@Override
	public IEditListener<T> getEditListener() {
		return editListener;
	}

	public SelectionListener getListener(){
		return listener;
	}
	
	/* (non-Javadoc)
	 * @see org.condast.commons.ui.controller.IEntityController#getInput()
	 */
	@Override
	public T getInput(){
		return input;
	}

	/* (non-Javadoc)
	 * @see org.condast.commons.ui.controller.IEntityController#setInput(T)
	 */
	@Override
	public void setInput( T input ){
		this.input = input;
		this.dirty = false;
	}

	/* (non-Javadoc)
	 * @see org.condast.commons.ui.controller.IEntityController#isDirty()
	 */
	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public void setDirty(boolean dirty) {
		if(!this.blockEntry )
			this.dirty = dirty;
	}

	/* (non-Javadoc)
	 * @see org.condast.commons.ui.controller.IEntityController#addSelectionListener(org.eclipse.swt.events.SelectionListener)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void addEditListener( IEditListener<?> listener ){
		this.listeners.add( (IEditListener<T>) listener );
	}

	/* (non-Javadoc)
	 * @see org.condast.commons.ui.controller.IEntityController#removeSelectionListener(org.eclipse.swt.events.SelectionListener)
	 */
	@Override
	public void removeEditListener( IEditListener<?> listener ){
		this.listeners.remove( listener );
	}

	/* (non-Javadoc)
	 * @see org.condast.commons.ui.controller.IEntityController#updateInput()
	 */
	@Override
	public void updateInput(){
		this.dirty = false;
	}
	
	protected void notifyWidgetSelected( EntryEvent arg0 ){
		if(!this.blockEntry )
			listener.widgetSelected( new SelectionEvent( arg0));
	}

	public void notifyWidgetSelected( ModifyEvent arg0 ){
		if( verifier.verifyWidget( new VerifyEvent( new EntryEvent( arg0 )), VerificationTypes.ALPHABET )) 
			this.notifyWidgetSelected( new EntryEvent( arg0));
	}

	public void notifyWidgetSelected( ModifyEvent arg0, VerificationTypes type ){
		if( verifier.verifyWidget( new VerifyEvent( new EntryEvent( arg0 )), type )) 
			this.notifyWidgetSelected( new EntryEvent( arg0));
	}

	
	public void notifyWidgetSelected( SelectionEvent arg0 ){
		if(!this.blockEntry )
			listener.widgetSelected( arg0 );
	}

	protected void notifyWidgetDefaultSelected( SelectionEvent arg0 ){
		if(!this.blockEntry )
			listener.widgetDefaultSelected(arg0);
	}

	protected static class EntryEvent extends Event{
		private static final long serialVersionUID = 1L;

		public EntryEvent(Event e) {
		}

		public EntryEvent( ModifyEvent e) {
			this.widget = e.widget;
			this.count = e.time;
			this.data = e.data;
			if( e.widget instanceof Text ){
				Text text = (Text) e.widget;
				this.text = text.getText();
			}
		}

		@Override
		public String toString() {
			return super.toString();
		}	
	}
}
