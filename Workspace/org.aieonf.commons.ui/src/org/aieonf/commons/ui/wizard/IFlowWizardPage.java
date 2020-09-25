package org.aieonf.commons.ui.wizard;

import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.widgets.Composite;

public interface IFlowWizardPage extends IWizardPage {

	/**
	 * When creating a new page, first a check is done to see if the
	 * correct container is selected. If not,the correct oner is created 
	 * @param currentContainer
	 * @return
	 */
	public IWizardContainer selectContainer( IWizardContainer currentContainer );

	/**
	 * A wizard page can create an icon in the title bar
	 * @param toolbar
	 */
	void createIcon(Composite iconbar);

	/**
	 * A wizard page can create controls in the tool bar
	 * @param toolbar
	 */
	void createToolBar(Composite toolbar);

	/**
	 * allows a data object to synchronize with the widgets of the control.
	 * sometimes needed prior to saving or finishing a wizard
	 */
	public void synchronizeData();

	/**
	 * Returns true if the control is disposed
	 * @return
	 */
	boolean isDisposed();
}
