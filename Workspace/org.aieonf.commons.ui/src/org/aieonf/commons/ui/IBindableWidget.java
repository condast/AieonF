package org.aieonf.commons.ui;

import org.aieonf.concept.IDescribable;

public interface IBindableWidget<D extends IDescribable>
{

	/**
	 * A bindable widget has to contain a title that can be displayed
	 * @return
	 */
	public String getTitle();
	
	/**
	 * The widget implements a setInput method that uses a describable
	 * @param describable
	 */
	public void setInput( D describable );
}
