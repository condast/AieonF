package org.aieonf.model.core;

import org.aieonf.commons.Utils;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.*;
import org.aieonf.concept.IConcept.Scope;
import org.aieonf.concept.core.ConceptBase;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.Descriptor;

public class ModelLeaf<D extends IDescriptor> extends ConceptBase implements IModelLeaf<D>
{	
	//The concept that is modelled
	private D descriptor;
	private boolean leaf;

	private IModelNode<?> parent;

	/**
	 * Only used for special models
	 */
	public ModelLeaf(){
		this.leaf = true;
		set( IDescriptor.Attributes.VERSION.name(), String.valueOf(0));
		set( IDescriptor.Attributes.CLASS.name(), this.getClass().getCanonicalName() );
	}

	/**
	 * Only used for special models
	 */
	public ModelLeaf( long id ){
		this();
		set( IDescriptor.Attributes.ID.name(), String.valueOf( id ));
	}

	/**
	 * Create the model
	 * @param concept
	 */
	public ModelLeaf( long id, D descriptor ){
		this( id );
		this.descriptor = descriptor;
	}

	/**
	 * Create the model
	 * @param concept
	 */
	public ModelLeaf( D descriptor ){
		this( -1, descriptor );
	}

	/**
	 * Create the model
	 * @param concept
	 */
	public ModelLeaf( long id, D descriptor, String type ){
		this( id, null, descriptor, type );
	}

	/**
	 * Create the model
	 * @param concept
	 */
	public ModelLeaf( long id, IModelNode<? extends IDescriptor> parent ){
		this( id );
		this.parent = parent;
	}

	/**
	 * Create the model
	 * @param concept
	 */
	public ModelLeaf( long id, IModelNode<? extends IDescriptor> parent, D descriptor ){
		this( id );
		this.parent = parent;
	}

	/**
	 * Create the model
	 * @param concept
	 */
	public ModelLeaf( long id, IModelNode<? extends IDescriptor> parent, D descriptor, String type ){
		this( id, parent, descriptor );
		this.set( IConcept.Attributes.TYPE.name(), type );
	}


	/**
	 * Set the leaf with the given value
	 * @param attr
	 * @param value
	 */
	@Override
	public String get( Enum<?> attr ){
		return super.get( attr.name());
	}

	/**
	 * Set the leaf with the given value
	 * @param attr
	 * @param value
	 */
	@Override
	public void set( Enum<?> attr, String value ){
		super.set( attr.name(), value);
	}

	/**
	 * Get the (optional) id of the model. 
	 * @return
	 */
	@Override
	public long getID(){
		String str =  get( Attributes.ID );
		return StringUtils.isEmpty(str)?0: Long.parseLong(str);
	}


	@Override
	public String getType() {
		return get( Attributes.TYPE );
	}

	/**
	 * Get the (optional) identifier of the model. 
	 * @return
	 */
	@Override
	public String getIdentifier(){
		return get( Attributes.IDENTIFIER );
	}

	/**
	 * Set the identifier for this leaf
	 * @param identifier
	 */
	@Override
	public void setIdentifier( String identifier ){
		set( Attributes.IDENTIFIER , identifier );
	}

	@Override
	public Scope getScope() {
		String str = get( IConcept.Attributes.SCOPE.name() ); 
		Scope scope = StringUtils.isEmpty(str)?Scope.PUBLIC: Scope.valueOf(str);
		return scope;
	}

	public void setScope( Scope scope ) {
		set( IConcept.Attributes.SCOPE.name(), scope.name() );
	}

	/**
	 * @return the parent
	 */
	@Override
	public IModelNode<?> getParent()
	{
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	@Override
	public void setParent(IModelNode<?> parent)
	{
		this.parent = parent;
	}

	@Override
	public boolean isReverse() {
		String str = super.get(IModelLeaf.Attributes.REVERSE.name());
		return StringUtils.isEmpty(str)? false: Boolean.parseBoolean(str);
	}

	@Override
	public void setReverse( boolean choice ) {
		super.set( IModelLeaf.Attributes.REVERSE.name(), String.valueOf(choice));
	}

	/**
	 * Returns true if the node is a root
	 * @return
	 */
	@Override
	public boolean isRoot(){
		return (( this.parent == null ) && ( this.getDepth() ) == 0 );
	}
	
	@Override
	public IDescriptor getDescriptor() {
		return new Descriptor( this );
	}

	/**
	 * Get the descriptor that this tree node represents
	 * @return
	 */
	@Override
	public D getData(){
		return this.descriptor;
	}

	@Override
	public void setData(D descriptor) {
		this.descriptor = descriptor;
	}

	/**
	 * Returns true if the model is a leaf ( has no children )
	 * @return
	 */
	@Override
	public boolean isLeaf(){
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	/**
	 * Get the depth of the model. This is the maximum amount of the
	 * root to the farthest ancestor in the tree
	 * @return
	 */
	@Override
	public int getDepth()
	{
		String str = get( IModelLeaf.Attributes.DEPTH);
		if( Utils.assertNull(str))
			str = "0";
		return Integer.parseInt( str );
	}

	/**
	 * This support method sets the depth of a model
	 * @param depth
	 */
	@Override
	public void setDepth( int depth ) throws ConceptException{
		set( IModelLeaf.Attributes.DEPTH, String.valueOf( depth ));
	}

	/**
	 * Returns true if the model, or one of its descendants contains
	 * the given descriptor
	 * @param descriptor
	 * @return
	 */
	@Override
	public boolean contains( IDescriptor descr ){
		return descr.equals( this.descriptor );
	}

	/**
	 * Implement the compare to
	 */
	@Override
	public int compareTo( IDescribable node ) 
	{
		return getDescriptor().compareTo( node.getDescriptor() );
	}


	@Override
	public int implies(IDescriptor descriptor) {
		return 0;//implies.compareTo( descriptor);
	}

	/**
	 * Get the first child with the given descriptor, or null if it wasn't found
	 * @param model
	 * @return
	 */
	public static IModelLeaf<? extends IDescriptor> getChild( IModelLeaf<? extends IDescriptor> model, IDescriptor descriptor ){
		if(!( model instanceof IModelNode ))
			return null;
		IModelNode<? extends IDescriptor> md = 
				(IModelNode<? extends IDescriptor>) model;
		return md.getChild( descriptor );
	}

	public static boolean hasChildren( IModelLeaf<? extends IDescriptor> model ){
		if(!( model instanceof IModelNode ))
			return false;
		IModelNode<? extends IDescriptor> md = 
				(IModelNode<? extends IDescriptor>) model;
		return md.hasChildren();		
	}
}