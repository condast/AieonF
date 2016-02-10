package org.aieonf.template.factory;

import java.io.InputStream;

import org.aieonf.model.ModelException;
import org.aieonf.template.ITemplate;

public interface ITemplateFactory
{
	/**
	 * Get the input stream for this model
	 * @return
	 */
	public InputStream getInputStream();

	/**
	 * Get the template
	 * @return
	 */
	public ITemplate getTemplate();
	
	/**
	 * returns true if the template exists at the provided resource location
	 * @return
	 */
	public boolean templateExists();

	/**
	 * Create a template if it doesn't exist
	 * @throws ModelException
	 */
	public ITemplate create() throws ModelException;
}
