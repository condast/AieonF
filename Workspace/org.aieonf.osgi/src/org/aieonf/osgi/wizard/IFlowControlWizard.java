package org.aieonf.osgi.wizard;

import org.eclipse.jface.wizard.IWizardContainer;

public interface IFlowControlWizard<T extends Object> {

	/**
	 * Clear the wizard and set everything to defaults
	 */
	void clear();
	
	/**
	 * The wizard can optionally maintain a data object, which can be shared
	 * amongst the pages 
	 * @return
	*/
	public T getData();
	
	public IWizardContainer getContainer();

	void addListener(IButtonSelectionListener<T> listener);

	void removeListener(IButtonSelectionListener<T> listener);

	void addWizardPageListener(IAddWizardPageListener listener);

	void removeListener(IAddWizardPageListener listener);
}