package org.aieonf.concept.directive;

import java.util.Collection;

import org.aieonf.concept.IDescriptor;

public interface IMember<T extends IDescriptor> extends IDirective
{
	/**
	 * Get the descriptor of the member
	 * @return
	*/
	public T getDescriptor();
	
	/**
	 * Get the order of this member with respect to others. By default all have
	 * an equal order of zero
	 * @return
	 */
	public int getOrder();
	
	/**
	 * Returns a collection of directives
	 */
	public Collection<IDirective> getDirectives();

	/**
	 * Get a collection of unsatisfied directives. Returns an empty list if none
	 * were found
	 * @return
	*/
	public Collection<IDirective> getUnsatisfiedDirectives();

}
