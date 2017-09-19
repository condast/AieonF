package org.aieonf.osgi.wizard;

import java.util.EventObject;

import org.aieonf.osgi.flow.IFlowControl;
import org.aieonf.osgi.wizard.IAddWizardPageListener.PageActions;
import org.eclipse.jface.wizard.IWizard;

public class PageActionEvent extends EventObject {
	private static final long serialVersionUID = 1L;

	private PageActions event;
	private String pageName;
	private String description;
	private String message;
	private IFlowControl flow;
	
	public PageActionEvent( IWizard source, IFlowControl flow, PageActions event, String pageName, String description, String message ){
		super(source);
		this.event = event;
		this.pageName = pageName;
		this.description = description;
		this.message = message;
		this.flow = flow;
	}

	public IFlowControl getFlow() {
		return flow;
	}

	public PageActions getEvent() {
		return event;
	}

	public String getPageName() {
		return pageName;
	}

	public String getDescription() {
		return description;
	}

	public String getMessage() {
		return message;
	}	
}
