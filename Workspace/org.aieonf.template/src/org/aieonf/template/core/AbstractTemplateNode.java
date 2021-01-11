package org.aieonf.template.core;

import org.aieonf.concept.IDescriptor;
import org.aieonf.template.def.ITemplateNode;

/**
 * Create a template from the given template aieon
 * @author Kees Pieters
 *
 */
public abstract class AbstractTemplateNode<T extends IDescriptor> extends TemplateNode<T>
{	
	
	public AbstractTemplateNode(T descriptor)
	{
		super(descriptor);
	}

	/**
	 * Returns true if the given model is a parent of this template node
	 * @param model
	 * @return
	*/
	public boolean isParent( ITemplateNode<? extends IDescriptor> template ){
		return template.getChildren().keySet().contains( this );
	}

	/**
	 * Returns true if the given model is a child from this template node
	 * @param model
	 * @return
	*/
	public boolean isChild( ITemplateNode<? extends IDescriptor> template ){
		return this.getChildren().keySet().contains( template );
	}

	/**
	 * Validate the given descriptor. Returns true if the 
	 * descriptor is valid in this template
	 * @return
	*/
	public abstract boolean validate( IDescriptor descriptor );
}