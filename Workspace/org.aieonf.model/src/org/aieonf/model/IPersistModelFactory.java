package org.aieonf.model;

import java.io.InputStream;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.ModelException;

public interface IPersistModelFactory< T extends IModelLeaf<? extends IDescriptor>>
{
	/**
	 * The supported persistence properties.
	 * Flexible means that the plug-in can create a new model from scratch
	 * Fixed means that the model is provided from a predetermined input stream
	 * Expandable means that the model can be expanded. This implies flexibility as well 
	 * @author keesp
	 *
	 */
	public enum Persistence{
		Flexible,
		Fixed,
		Expandable
	}

	/**
	 * Get the input stream for this model
	 * @return
	 */
	public InputStream getInputStream();
	
	/**

	/**
	 * Get the model
	 * @return
	 * @throws ModelException
	 */
	public T getModel() throws ModelException;
	
	/**
	 * returns true if the model exists at the provided resource location
	 * @return
	 */
	public boolean modelExists();

	/**
	 * Create a model if it doesn't exist
	 * @throws ModelException
	 */
	public void create() throws ModelException;
}
