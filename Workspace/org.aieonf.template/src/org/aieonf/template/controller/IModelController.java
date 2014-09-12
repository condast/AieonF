package org.aieonf.template.controller;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.builder.IModelBuilderListener;

/**
 * The controller takes care of all the CRUD operations on a model. A controller can work with one
 * model, which is either created, or obtained from a descriptor
 * @author Kees
 *
 * @param <T>
 */
public interface IModelController<T extends IDescriptor> {

	/**
	 * Initialise the controller
	 */
	public void initialise();
	
	/**
	 * Close the controller
	 */
	public void close();

	/**
	 * add a model builder listener for additional tasks during the build
	 * @param listener
	 */
	public void addBuilderListener( IModelBuilderListener listener );

	/**
	 * remove the listener
	 * @param listener
	 */
	public void removeBuilderListener( IModelBuilderListener listener );

	/**
	 * Returns true if the controller is initialised
	 * @return
	 */
	public boolean isInitialised();
	
	/**
	 * Create a model
	 * @return
	 */
	public IModelLeaf<T> createModel();

	/**
	 * Get a model which contains the given descriptor
	 * @return
	 */
	public IModelLeaf<T> getModel( IDescriptor descriptor );

	/**
	 * Get a submodel which contains the given descriptor
	 * @return
	 */
	public IModelLeaf<IDescriptor> getSubModel( IDescriptor descriptor );

	/**
	 * add a model
	 * @param model
	 */
	public boolean addModel();

	/**
	 * remove a model
	 * @param model
	 */
	public boolean removeModel();

	/**
	 * update the given model. This means that the model must exist.
	 * @param model
	 */
	public void updateModel();

}
