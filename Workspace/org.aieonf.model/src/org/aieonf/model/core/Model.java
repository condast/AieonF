package org.aieonf.model.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.*;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.IConceptBase;

public class Model<D extends IDescriptor> extends ModelLeaf<D> implements IModelNode<D>
{	
	private Map<IModelLeaf<? extends IDescriptor>, String> children;

	private boolean reverse;

	/**
	 * Create the model
	 * @param concept
	 */
	public Model( IConceptBase base ){
		super( base );
		this.children = new HashMap<>();
	}

	/**
	 * Create the model. Use only when parsing data
	 * @param concept
	 */
	public Model(){
		super();
		this.children = new HashMap<>();
	}

	/**
	 * Create the model. Use only when parsing data
	 * @param concept
	 */
	public Model( long id ){
		super( id );
		this.children = new HashMap<>();
	}

	/**
	 * Create the model. Use only when parsing data
	 * @param concept
	 */
	public Model( long id, D descriptor ){
		super( id, descriptor );
		this.children = new HashMap<>();
	}

	/**
	 * Create the model. Use only when parsing data
	 * @param concept
	 */
	public Model( D descriptor ){
		this( -1, descriptor );
	}

	@SuppressWarnings("unchecked")
	public Model( IModelLeaf<D> leaf ) {
		super(leaf.getDescriptor().getBase() );
		setParent((IModelNode<? extends IDescriptor>) leaf.getParent());
		setData(leaf.getData());
		this.children = new HashMap<>();
	}

	protected Model(long id, IModelNode<? extends IDescriptor> parent, D descriptor, String type) {
		super(id, parent, descriptor, type);
		this.children = new HashMap<>();
	}

	public Model(long id, IModelNode<? extends IDescriptor> parent) {
		this( id, parent, null );
	}
	
	public Model( long id, IModelNode<? extends IDescriptor> parent, D descriptor) {
		super(id, parent, descriptor);
		this.children = new HashMap<>();
	}

	/**
	 * Create the model. Use only when parsing data
	 * @param concept
	 */
	public Model( long id, D descriptor, String type )
	{
		super( id, descriptor, type );
		this.children = new HashMap<>();
	}
	
	@Override
	public boolean isReverse() {
		return this.reverse;
	}

	@Override
	public void setReverse( boolean choice ) {
		this.reverse = choice;
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
		Collection<IModelLeaf<? extends IDescriptor>> results = new ArrayList<>();
		if( StringUtils.isEmpty(name)) {
			results = this.children.keySet();
			return results.toArray( new IModelLeaf[ results.size() ]);
		}
		Iterator<Map.Entry<IModelLeaf<? extends IDescriptor>, String>> iterator = this.children.entrySet().iterator();
		while( iterator.hasNext() ) {
			Map.Entry<IModelLeaf<? extends IDescriptor>, String> entry = iterator.next();
			if( name.equals( entry.getValue()))
				results.add( entry.getKey() );
		}
		return results.toArray( new IModelLeaf[ results.size() ]);
	}

	
	@Override
	public String getChildIdentifier(IModelLeaf<? extends IDescriptor> child) {
		return this.children.get(child);
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

		IModelNode<?> md =  (IModelNode<?>) model;
		Collection<? extends IModelLeaf<? extends IDescriptor>> children = md.getChildren().keySet();		
		for( IModelLeaf<? extends IDescriptor> child: children )
			if( contains( store, child, descr ))
				return true;
		return false;
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

	@Override
	public void removeAllChildren() {
		this.children.clear();
	}

	/**
	 * Get the child with the given id
	 * @param parent
	 * @param id
	 * @return
	 */
	public static IModelLeaf<? extends IDescriptor> getChild( IModelNode<IDescriptor> parent, long id ){
		for( IModelLeaf<? extends IDescriptor> child: parent.getChildren().keySet()) {
			if( child.getID() == id )
				return child;
		}
		return null;
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
	
	@SuppressWarnings("unchecked")
	protected static void getDescriptors( IModelLeaf<?> model, Collection<IDescriptor> descriptors ){
		descriptors.add( model.getDescriptor() );		
		IModelNode<? extends IDescriptor> md = (IModelNode<? extends IDescriptor>) model;
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
	 * Get the first child of the given model if one exists.
	 * Is a convenience method when creating new models
	 * @param model
	 * @return
	 */
	public static IModelLeaf<? extends IDescriptor> getFirstChild( IModelNode<? extends IDescriptor> model ){
		if( !model.hasChildren())
			return null;
		return model.getChildren().keySet().iterator().next();
	}


	/**
	 * Get the model with the given id
	 */
	@SuppressWarnings("unchecked")
	public static IModelLeaf<IDescriptor> getModel( IModelLeaf<? extends IDescriptor> root, String id ){
		if( StringUtils.isEmpty( id ))
			return null;
		if( id.equals( root.getIdentifier() ))
			return (IModelLeaf<IDescriptor>) root;
		if(!( root instanceof IModelNode))
			return null;
		IModelNode<?> node = (IModelNode<?>) root;
		for( IModelLeaf<?> child: node.getChildren().keySet() ){
			IModelLeaf<IDescriptor> result = getModel( (IModelLeaf<? extends IDescriptor>) child, id ); 
			if( result != null )
				return result;
		}
		return null;
	}
	
	/**
	 * Transfer the source model to the target model leaf. The result is a model that contains all the
	 * properties of the target leaf. This method is sometimes convenient if an existing leaf needs to
	 * be expanded to a full model, given a certain template 
	 * @param <D>
	 * @param target
	 * @param source
	 * @return
	 */
	public static <D extends IDescriptor> IModelNode<D>transfer( IModelLeaf<D> target, IModelLeaf<D> source){
		IModelNode<D> model = new Model<D>( target );
		if( source.isLeaf())
			return model;
		IModelNode<D> node = (IModelNode<D>) source;
		Iterator<Map.Entry<IModelLeaf<? extends IDescriptor>, String>> iterator = node.getChildren().entrySet().iterator();
		while( iterator.hasNext()) {
			Map.Entry<IModelLeaf<? extends IDescriptor>, String> entry = iterator.next();
			model.addChild(entry.getKey(), entry.getValue());
		}
		return model;
	}
}