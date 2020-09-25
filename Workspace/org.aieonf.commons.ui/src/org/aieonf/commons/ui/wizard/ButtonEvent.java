package org.aieonf.commons.ui.wizard;

import java.util.EventObject;

import org.eclipse.jface.wizard.IWizardPage;

public class ButtonEvent<T extends Object> extends EventObject{
	private static final long serialVersionUID = 1L;
	private IButtonWizardContainer.Buttons button;

	public String query;
	
	private T data;
	
	public ButtonEvent(Object source, IButtonWizardContainer.Buttons button ) {
		this( source, button, null );
	}

	public ButtonEvent(Object source, IButtonWizardContainer.Buttons button, T data ) {
		this( source, button, null, data );
	}
	
	public ButtonEvent(Object source, IButtonWizardContainer.Buttons button, IWizardPage current, T data ) {
		super(source);
		this.data = data;
		this.button = button;
	}

	public IButtonWizardContainer.Buttons getButton() {
		return button;
	}

	public T getData() {
		return data;
	}

	public String getQuery() {
		return query;
	}
}