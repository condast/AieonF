package org.aieonf.template;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;

public class TemplateNode<T extends IDescriptor>
		extends TemplateLeaf<T> implements ITemplateNode<T>
{
	private Collection<IModelLeaf<? extends IDescriptor>> children;

	public TemplateNode( T descriptor )
	{
		super( descriptor );
		this.children = new TreeSet<IModelLeaf<? extends IDescriptor>>();
	}

	@Override
	public boolean addChild(IModelLeaf<? extends IDescriptor> child)
	{
		if( this.children.add( child )){
			child.setParent( this );
			return true;
		}
		return false;
	}

	@Override
	public boolean removeChild(IModelLeaf<? extends IDescriptor> child)
	{
		return this.children.remove( child );
	}

	@Override
	public Collection<IModelLeaf<? extends IDescriptor>> getChildren()
	{
		return this.children;
	}

	@Override
	public IModelLeaf<? extends IDescriptor> getChild(IDescriptor descriptor)
	{
		for( IModelLeaf<? extends IDescriptor> child: children ){
			if( child.getDescriptor().equals( descriptor ))
				return child;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public IModelLeaf<? extends IDescriptor>[] getChildren(String name) {
		Collection<IModelLeaf<? extends IDescriptor>> results = new ArrayList<IModelLeaf<? extends IDescriptor>>();
		for( IModelLeaf<? extends IDescriptor> model: this.children ){
			if( model.getIdentifier().equals( name ))
				results.add( model );
		}
		return results.toArray( new IModelLeaf[ results.size() ]);
	}

	@Override
	public boolean hasChildren()
	{
		return !this.children.isEmpty();
	}

	@Override
	public int nrOfchildren()
	{
		return this.children.size();
	}


	/* (non-Javadoc)
	 * @see org.condast.concept.model.ModelLeaf#isLeaf()
	 */
	@Override
	public boolean isLeaf()
	{
		return this.children.isEmpty();
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
			( ITemplateNode<? extends IDescriptor> )model;
		Collection<? extends IModelLeaf<? extends IDescriptor>> children = md.getChildren();		
		for( IModelLeaf<? extends IDescriptor> child: children )
			if( contains( store, child, descr ))
				return true;
		return false;
	}
}
