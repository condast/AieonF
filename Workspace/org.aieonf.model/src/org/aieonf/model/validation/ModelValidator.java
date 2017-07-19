/**
 * 
 */
package org.aieonf.model.validation;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelNode;

/**
 * @author keesp
 *
 */
public abstract class ModelValidator< T extends IDescriptor> implements IModelValidator<T>
{
	//The template that serves as reference
	private IModelNode<T> template;
	
	private Collection<IValidatorListener<T>> listeners;
	
	public ModelValidator( IModelNode<T> template ){
		this.template = template;
		listeners = new ArrayList<IValidatorListener<T>>();
	}
	
	/**
	 * Get the template
	 * @return
	*/
	@Override
	public IModelNode<T> getTemplate(){
		return this.template;
	}
	
	@Override
	public boolean addValidatorListener(IValidatorListener<T> listener)
	{
		return listeners.add( listener );
	}

	@Override
	public boolean removeValidatorListener(IValidatorListener<T> listener)
	{
		return listeners.add( listener );
	}
}
