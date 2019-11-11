package org.aieonf.orientdb.model;

import java.util.*;

import org.aieonf.commons.Utils;
import org.aieonf.concept.*;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;

public class Model extends ModelLeaf implements IModelNode<IDescriptor>
{	
	private Collection<ModelLeaf>children;

	/**
	 * Create the model. Use only when parsing data
	 * @param concept
	 */
	protected Model(){
		super();
		this.children = new ArrayList<>();
	}

	/**
	 * Get a list with child data
	 * @return
	 */
	@Override
	public Collection<IModelLeaf<? extends IDescriptor>> getChildren()
	{
		Collection<IModelLeaf<? extends IDescriptor>> results = new ArrayList<IModelLeaf<? extends IDescriptor>>( this.children);
		return results;
	}

	/**
	 * Get the child with the given descriptor
	 * @param descriptor
	 * @return IModelNode<? extends IDescriptor>
	 */
	@Override
	public IModelLeaf<? extends IDescriptor> getChild( IDescriptor descriptor )
	{
		for( IModelLeaf<? extends IDescriptor> model: this.children ){
			if( model.getDescriptor().equals( descriptor ))
				return model;
		}
		return null;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public IModelLeaf<? extends IDescriptor>[] getChildren(String name) {
		Collection<IModelLeaf<? extends IDescriptor>> results = new ArrayList<IModelLeaf<? extends IDescriptor>>();
		for( IModelLeaf<? extends IDescriptor> model: this.children ){
			if( model.getDescriptor().getName().equals( name ))
				results.add( model );
		}
		return results.toArray( new IModelLeaf[ results.size() ]);
	}

	/**
	 * Returns true if the model has children
	 * @return
	 */
	@Override
	public boolean hasChildren()
	{
		return ( this.children.isEmpty() == false );
	}

	/**
	 * Get the number of children
	 * @return
	 */
	@Override
	public int nrOfchildren()
	{
		return this.children.size();
	}

	/**
	 * Add a child concept to the model
	 * @param child IModelNode<? extends IDescriptor>
	 */
	@Override
	public boolean addChild( IModelLeaf<? extends IDescriptor> child )
	{
		child.setParent( this );
		try {
			child.setDepth( super.getDepth() + 1 );
		}
		catch (ConceptException e) {
			return false;
		}
		updateDepth( child );
		super.setChanged( this.children.add( (ModelLeaf) child ));
		super.setLeaf( children.isEmpty() );
		return super.hasChanged();
	}

	/**
	 * Update the depths of all the children
	 * @param model
	 */
	protected static void updateDepth( IModelLeaf<? extends IDescriptor> model )
	{
		if(!( model instanceof IModelNode ))
			return;
		IModelNode<? extends IDescriptor> md = 
			(org.aieonf.model.core.IModelNode<? extends IDescriptor> )model;
		if( Utils.assertNull(md.getChildren()))
			return;
		for( IModelLeaf<? extends IDescriptor> child: md.getChildren() ){
			try {
				child.setDepth( model.getDepth() + 1 );
			}
			catch (ConceptException e) {
				e.printStackTrace();
			}
			updateDepth( child );
		}
	}
	
	/**
	 * Remove a child model from the model
	 * @param concept
	*/
	@Override
	public boolean removeChild( IModelLeaf<? extends IDescriptor> model )
	{
		super.setChanged( this.children.remove( model ));
		super.setLeaf( children.isEmpty() );
		model.setParent( null );
		if( super.hasChanged() )
			try {
				model.setDepth( -1 );
			}
			catch (ConceptException e) {
				e.printStackTrace();
			}
		return super.hasChanged();
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

		IModelNode<? extends IDescriptor> md = 
			(org.aieonf.model.core.IModelNode<? extends IDescriptor> )model;
		Collection<? extends IModelLeaf<? extends IDescriptor>> children = md.getChildren();		
		for( IModelLeaf<? extends IDescriptor> child: children )
			if( contains( store, child, descr ))
				return true;
		return false;
	}
	
	/**
	 * Get the descriptors belonging to the given model
	 * @param model
	 * @return
	 */
	public static Collection<IDescriptor> getDescriptors( IModelLeaf<? extends IDescriptor> model ){
		Collection<IDescriptor> results = new ArrayList<IDescriptor>();
		getDescriptors( model, results );
		return results;
	}
	
	protected static void getDescriptors( IModelLeaf<?> model, Collection<IDescriptor> descriptors ){
		descriptors.add( model.getDescriptor() );		
		IModelNode<? extends IDescriptor> md = 
			(org.aieonf.model.core.IModelNode<? extends IDescriptor> )model;
		Collection<? extends IModelLeaf<? extends IDescriptor>> children = md.getChildren();		
		for( IModelLeaf<? extends IDescriptor> child: children )
		  getDescriptors( child, descriptors);
	}

	/**
	 * replaces a child node with the given descriptor with a new one. This is convenient, for instance when an initial leaf
	 * has to be replaced with a node. A regular add is carried out otherwise. Returns true only if a
	 * child node was found and replaced.
	 * @param node
	 * @param descriptor
	 * @return
	 */
	public static boolean replaceChild( IModelNode<? extends IDescriptor> parent, IModelLeaf<? extends IDescriptor> replace ){
		boolean retVal = false;
		for( IModelLeaf<? extends IDescriptor> child: parent.getChildren())
			if( child.getDescriptor().equals( replace.getDescriptor() )){
				parent.removeChild(child);
				retVal = true;
				break;
			}
		parent.addChild(replace);
		return retVal;		
	}
	
	/**
	 * Return the parents of the given collection, if they are not null
	 * @param models
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Collection<IModelLeaf<IDescriptor>> getParents( Collection<IModelLeaf<IDescriptor>> models ){
		Collection<IModelLeaf<IDescriptor>> results = new ArrayList<IModelLeaf<IDescriptor>>();
		for( IModelLeaf<?> model: models ){
			if( model.getParent() != null )
				results.add( (IModelLeaf<IDescriptor>) model.getParent() );
		}
		return results;
		
	}
}