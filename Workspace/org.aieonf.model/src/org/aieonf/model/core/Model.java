package org.aieonf.model.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import org.aieonf.commons.Utils;
import org.aieonf.concept.*;
import org.aieonf.concept.core.ConceptException;

public class Model<T extends IDescriptor> extends ModelLeaf<T> implements IModelNode<T>
{	
	private Map<IModelLeaf<? extends IDescriptor>, String> children;

	/**
	 * Create the model. Use only when parsing data
	 * @param concept
	 */
	protected Model(){
		super();
		this.children = new HashMap<>();
	}

	/**
	 * Create the model. Use only when parsing data
	 * @param concept
	 */
	public Model( T descriptor )
	{
		super( descriptor );
		this.children = new HashMap<>();
	}

	
	protected Model(IModelNode<? extends IDescriptor> parent, T descriptor, String type) {
		super(parent, descriptor, type);
		this.children = new HashMap<>();
	}

	public Model(IModelNode<? extends IDescriptor> parent) {
		this( parent, null );
	}
	
	public Model(IModelNode<? extends IDescriptor> parent, T descriptor) {
		super(parent, descriptor);
		this.children = new HashMap<>();
	}

	/**
	 * Create the model. Use only when parsing data
	 * @param concept
	 */
	public Model( T descriptor, String type )
	{
		super( descriptor, type );
		this.children = new HashMap<>();
	}

	/**
	 * Get a list with child data
	 * @return
	 */
	@Override
	public Map<IModelLeaf<? extends IDescriptor>, String> getChildren()
	{
		return this.children;
	}

	/**
	 * Get the child with the given descriptor
	 * @param descriptor
	 * @return IModelNode<? extends IDescriptor>
	 */
	@Override
	public IModelLeaf<? extends IDescriptor> getChild( IDescriptor descriptor )
	{
		for( IModelLeaf<? extends IDescriptor> model: this.children.keySet() ){
			if( model.getDescriptor().equals( descriptor ))
				return model;
		}
		return null;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public IModelLeaf<? extends IDescriptor>[] getChildren(String name) {
		Collection<IModelLeaf<? extends IDescriptor>> results = new ArrayList<IModelLeaf<? extends IDescriptor>>();
		for( IModelLeaf<? extends IDescriptor> model: this.children.keySet() ){
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
	public boolean addChild( IModelLeaf<? extends IDescriptor> child ) {
		return addChild( child, null );
	}

	/**
	 * Add a child concept to the model
	 * @param child IModelNode<? extends IDescriptor>
	 */
	@Override
	public boolean addChild( IModelLeaf<? extends IDescriptor> child, String type )
	{
		child.setParent( this );
		try {
			child.setDepth( super.getDepth() + 1 );
		}
		catch (ConceptException e) {
			return false;
		}
		updateDepth( child );
		this.children.put( child, type );
		super.setChanged( true );
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
		if( !md.hasChildren())
			return;
		for( IModelLeaf<? extends IDescriptor> child: md.getChildren().keySet() ){
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
		this.children.remove( model );
		super.setChanged( true);
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
		Collection<? extends IModelLeaf<? extends IDescriptor>> children = md.getChildren().keySet();		
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
		Collection<? extends IModelLeaf<? extends IDescriptor>> children = md.getChildren().keySet();		
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
		for( IModelLeaf<? extends IDescriptor> child: parent.getChildren().keySet())
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
	
	/**
	 * Get the children of the given model leaf if they exist
	 * @param model
	 * @return
	 */
	public static Collection<? extends IModelLeaf<? extends IDescriptor>> getChildren( IModelLeaf<? extends IDescriptor> model ){
		Collection<IModelLeaf<? extends IDescriptor>> children = new ArrayList<IModelLeaf<? extends IDescriptor>>();
		if(!( model instanceof IModelNode ))
			return children;
		IModelNode<? extends IDescriptor> md = 
			(IModelNode<? extends IDescriptor>) model;
		return md.getChildren().keySet();
	}

	/**
	 * Get the model with the given id
	 */
	@SuppressWarnings("unchecked")
	public static IModelLeaf<IDescriptor> getModel( IModelLeaf<? extends IDescriptor> root, String id ){
		if( Utils.assertNull( id ))
			return null;
		if( id.equals( root.getIdentifier() ))
			return (IModelLeaf<IDescriptor>) root;
		if(!( root instanceof IModelNode))
			return null;
		IModelNode<?> node = (IModelNode<?>) root;
		for( IModelLeaf<?> child: node.getChildren().keySet() ){
			IModelLeaf<IDescriptor> result = getModel( child, id ); 
			if( result != null )
				return result;
		}
		return null;
	}

}