package org.aieonf.template;

import org.aieonf.concept.IDescriptor;

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
		return template.getChildren().contains( this );
	}

	/**
	 * Returns true if the given model is a child from this template node
	 * @param model
	 * @return
	*/
	public boolean isChild( ITemplateNode<? extends IDescriptor> template ){
		return this.getChildren().contains( template );
	}

	/**
	 * Returns true if the child is valid for the given parent
	 * @param parent
	 * @param child
	 * @return
	*/
	public boolean validateChild( ITemplateNode<? extends IDescriptor> parent, ITemplateNode<? extends IDescriptor> child )
	{
		if( this.isParent( parent ) && this.isChild( child ))
			return this.validate( child.getDescriptor() );
		if( parent.getDirection().equals( Direction.BI_DIRECTIONAL ) == false )
			return false;
		return(( this.isParent( child ) && this.isChild( parent )));
	}
	
	/**
	 * Validate the given descriptor. Returns true if the 
	 * descriptor is valid in this template
	 * @return
	*/
	public abstract boolean validate( IDescriptor descriptor );
}