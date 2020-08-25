package org.aieonf.osgi.swt;

import org.aieonf.concept.domain.IDomainAieon;
import org.eclipse.swt.widgets.Control;

public interface IViewFactory<T,U extends Control>
{
	public enum Views{
		ANY,
		BAR,
		BODY,
		GET,
		ADD,
		EDIT,
		AUTHENTICATION,
		OPTIONS,
		SELECTION,
		SPONSOR;
	}

	/**
	 * Returns true if the views belong to this domain
	 * @param domain
	 * @return
	 */
	public boolean isOfDomain( IDomainAieon domain );
	
	/**
	 * Add an identifier for this factory
	 * @return
	 */
	public String getIdentifier();
	
	/**
	 * Create an entry, that is bound to the given parent view
	 * @param parent
	 * @param style: the SWT style
	 * @return
	 */
	public T createEntry( Views view, U parent, int style );
}
