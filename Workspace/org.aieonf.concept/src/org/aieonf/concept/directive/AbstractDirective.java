/**
 * 
 */
package org.aieonf.concept.directive;

import org.aieonf.concept.IDescriptor;

/**
 * @author keesp
 *
 */
public abstract class AbstractDirective<T extends IMember<IDescriptor>> implements IDirective
{
	private T member;
	
	/**
	 * Create a constraint
	 */
	public AbstractDirective( T member )
	{
		this.member = member;
	}
	
	/**
	 * Get the member
	 * @return
	 */
	protected T getMember()
	{
		return this.member;
	}
}
