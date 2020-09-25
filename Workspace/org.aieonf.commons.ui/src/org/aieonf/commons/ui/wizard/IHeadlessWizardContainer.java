package org.aieonf.commons.ui.wizard;

import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.widgets.Composite;

public interface IHeadlessWizardContainer extends IWizardContainer{

	/**
	 * The types of containers that are supported
	 * @author Kees
	 *
	 */
	public enum ContainerTypes{
		HEAD,
		HEADLESS;
	}
	
	/**
	 * Creates the container for this composite
	 * @param composite
	 * @param wizard
	 */
	Composite createComposite( Composite composite, int style );

	/**
	 * Get the pages of this container
	 * @return
	 */
	public IWizardPage[] getPages();

	/**
	 * Set the pages of this container
	 * @param newPages
	 */
	void setPages(IWizardPage[] newPages);

	boolean isLastPage(int index);

	int getFinishIndex();

	void setFinishIndex(int finishIndex);

	void addPage(IWizardPage page);

	void removePage(IWizardPage page);

	IWizardPage getPage(int index);
	
	public Composite getActiveComposite();	
	
	/**
	 * Returns true if the wizard can finish
	 * @return
	 */
	boolean canFinish( int index );

	int size();

	void dispose();

	/**
	 * Refresh the container
	 */
	void refresh();
}