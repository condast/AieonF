package org.aieonf.osgi.selection;

import org.aieonf.concept.domain.IDomainAieon;

public interface ISelectionBean {

	/**
	 * Get the domain for this selection
	 * @return
	 */
	public IDomainAieon getDomain();
	
	/**
	 * Get the name for this selection
	 * @return
	 */
	public String getName();
	
	/**
	 * Returns true if the selection is active
	 * @return
	 */
	public boolean isActive();
	
	/**
	 * Set the selection
	 * @param choice
	 */
	public void setActive( boolean choice );
}
