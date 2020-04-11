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
		Collection<IModelLeaf<?>> store = 
			new TreeSet<IModelLeaf<?>>();
		return contains( store, this, descr );
	}
	
	/**
	 * Returns true if the model, or one of its descendants contains
	 * the given descriptor
	 * @param descriptor
	 * @return
	 */
	protected boolean contains( Collection<IModelLeaf<?>> store, IModelLeaf<?> model, IDescriptor descr )
	{
		if( store.contains( model ))
			return false;
		store.add( model );
		if( model.getDescriptor().equals( descr ))
			return true;

		ITemplateNode<?> md = (org.aieonf.template.def.ITemplateNode<?> )model;
		Collection<? extends IModelLeaf<? extends IDescriptor>> children = md.getChildren().keySet();		
		for( IModelLeaf<? extends IDescriptor> child: children )
			if( contains( store, child, descr ))
				return true;
		return false;
	}
}
