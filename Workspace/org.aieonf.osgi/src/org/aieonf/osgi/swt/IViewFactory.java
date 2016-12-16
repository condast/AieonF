package org.aieonf.osgi.swt;

public interface IViewFactory<T,U extends Object>
{
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
	public T createEntry( U parent, int style );
}