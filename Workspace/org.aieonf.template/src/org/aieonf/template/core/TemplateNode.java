package org.aieonf.template.core;

import java.util.Collection;
import java.util.TreeSet;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.template.def.ITemplateNode;

public class TemplateNode<T extends IDescriptor>
		extends TemplateLeaf<T> implements ITemplateNode<T>
{
	public TemplateNode( T descriptor ){
		super( descriptor );
	}
	
	public TemplateNode(org.xml.sax.Attributes attrs) {
		super(attrs);
	}

	public TemplateNode( T descriptor, org.xml.sax.Attributes attributes )
	{
		super( descriptor, attributes );
	}



	/**
	 * Returns true if the model, or one of its descendants contains
	 * the given descriptor
	 * @param descriptor
	 * @return
	 */
	@Override
	public boolean contains( IDescriptor descr )
	{
		Collection<IModelLeaf<? extends IDescriptor>> store = 
			new TreeSet<IModelLeaf<? extends IDescriptor>>();
		return contains( store, this, descr );
	}
	
	/**
	 * Returns true if the model, or one of its descendants contains
	 * the given descriptor
	 * @param descriptor
	 * @return
	 */
	protected boolean contains( Collection<IModelLeaf<? extends IDescriptor>> store, IModelLeaf<? extends IDescriptor> model, IDescriptor descr )
	{
		if( store.contains( model ))
			return false;
		store.add( model );
		if( model.getDescriptor().equals( descr ))
			return true;

		ITemplateNode<? extends IDescriptor> md = 
			(org.aieonf.template.def.ITemplateNode<? extends IDescriptor> )model;
		Collection<? extends IModelLeaf<? extends IDescriptor>> children = md.getChildren();		
		for( IModelLeaf<? extends IDescriptor> child: children )
			if( contains( store, child, descr ))
				return true;
		return false;
	}
}
