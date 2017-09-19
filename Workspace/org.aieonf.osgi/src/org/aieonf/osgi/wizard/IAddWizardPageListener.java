package org.aieonf.osgi.wizard;

import org.eclipse.jface.wizard.IWizardPage;

public interface IAddWizardPageListener {

	/**
	 * wizard pages can be addded internally by the
	 * wizard itself. if not, it returns a null event
	 * @author Kees
	 *
	 */
	public enum PageActions{
		INTERNAL,
		NULL;
	}
	
	public IWizardPage notifyWizardPageAction( PageActionEvent event );
}
