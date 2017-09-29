package org.aieonf.commons.ui.swt;

import org.aieonf.commons.ui.verification.IWidgetVerificationDelegate;
import org.aieonf.commons.ui.verification.IWidgetVerificationDelegate.VerificationTypes;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyListener;

public interface IEntityController<T extends Object> {

	/**
	 * Returns true if the composite can only be viewed and not edited
	 * @return
	 */
	boolean isViewOnly();
	public void setViewOnly( boolean choice );
	
	boolean isUpdate();
	
	public SelectionListener getListener();
	
	void setUpdate(boolean update);

	void addVerifyListener(VerifyListener listener);

	void removeVerifyListener(VerifyListener listener);

	T getInput();

	void setInput(T input);

	boolean isDirty();

	void addEditListener( IEditListener<?> listener);

	void removeEditListener( IEditListener<?> listener);

	void updateInput();

	VerifyListener getVerifyListener();

	IEditListener<T> getEditListener();

	void notifyWidgetSelected(SelectionEvent event);

	void notifyWidgetSelected(ModifyEvent arg0, VerificationTypes name);

	/**
	 * Verifier for control input
	 * @return
	 */
	public IWidgetVerificationDelegate getVerifier();
	
	public void setVerifier( IWidgetVerificationDelegate verifier );

	boolean isBlocked();

	void setBlockEntry(boolean blockEntry);

	void setDirty(boolean dirty);
}