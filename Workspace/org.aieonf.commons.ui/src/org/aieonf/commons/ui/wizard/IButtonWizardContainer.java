package org.aieonf.commons.ui.wizard;

import org.aieonf.commons.strings.StringStyler;
import org.eclipse.swt.widgets.Composite;

public interface IButtonWizardContainer extends IHeadlessWizardContainer{

	public static final String RWT_WIZARD = "wizard";
	public static final String RWT_WIZARD_TOOLBAR = "wizard-toolbar";
	public static final String RWT_WIZARD_TITLE = "wizard-title";
	public static final String RWT_WIZARD_CONTAINER = "wizard-container";

	enum Buttons{
		CONTINUE,//special event when next button is pressed at a finish index
		FINISH, 
		NEXT,
		PREVIOUS,
		SAVE,
		DELETE,
		HELP,
		CANCEL;
	
		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}

	void addListener(IButtonSelectionListener<?> listener);

	void removeListener(IButtonSelectionListener<?> listener);
	
	Composite getButtonBar();

	boolean isButtonEnabled(IButtonWizardContainer.Buttons button);

	void buttonVisible(IButtonWizardContainer.Buttons button, boolean choice);

	void setButtonEnabled(IButtonWizardContainer.Buttons button, boolean choice);
	
	/**
	 * Get the index position(s) when a finish is allowed
	 * @return
	 */
	@Override
	public int getFinishIndex();
	
	/**
	 * The amount ofpages in the container
	 * @return
	 */
	@Override
	public int size();

	Composite getToolBar();

	void clearButtons();
}