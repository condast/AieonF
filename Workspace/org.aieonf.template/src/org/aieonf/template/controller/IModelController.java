package org.aieonf.template.controller;

import java.io.Closeable;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.model.core.IModelLeaf;

/**
 * The controller takes care of all the CRUD operations on a model. A controller can work with one
 * model, which is either created, or obtained from a descriptor
 * @author Kees
 *
 * @param <U>
 */
public interface IModelController<U extends IDescribable> extends Closeable{

	/**
	 * Initialise the controller
	 */
	public void initialise();
	
	/**
	 * Open ther controller
	 */
	public void open();
	
	/**
	 * Returns true if the controller is open
	 * @return
	 */
	public boolean isOpen();
	
	/**
	 * Close the controller
	 */
	@Override
	public void close();

	/**
	 * add a model builder listener for additional tasks during the build
	 * @param listener
	 */
	public void addBuilderListener( IModelBuilderListener<U> listener );

	/**
	 * remove the listener
	 * @param listener
	 */
	public void removeBuilderListener( IModelBuilderListener<U> listener );

	/**
	 * Returns true if the controller is initialised
	 * @return
	 */
	public boolean isInitialised();
	
	/**
	 * Create a model
	 * @return
	 */
	public U createModel();

	/**
	 * Get a model which contains the given descriptor
	 * @return
	 */
	public U getModel();
	
	/**
	 *Set the model
	 */
	public void setModel( U model );

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
