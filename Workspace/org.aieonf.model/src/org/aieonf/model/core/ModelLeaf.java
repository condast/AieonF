package org.aieonf.model.core;

import java.util.Map.Entry;
import java.util.Set;

import org.aieonf.commons.Utils;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.*;
import org.aieonf.concept.IConcept.Scope;
import org.aieonf.concept.core.ConceptBase;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.core.IConceptBase;

public class ModelLeaf<D extends IDescriptor> implements IModelLeaf<D>
{	
	//The concept that is modelled
	private D descriptor;
	private boolean leaf;

	private IModelNode<?> parent;
	
	private IConceptBase base;
	private transient boolean changed;

	/**
	 * Create the model
	 * @param concept
	 */
	public ModelLeaf( IConceptBase base ){
		this.base = base;
		this.leaf = true;
		this.changed = false;
	}

	/**
	 * Only used for special models
	 */
	public ModelLeaf(){
		this( new ConceptBase());
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
	 * Get the id of the model. 
	 * @return
	 */
	@Override
	public long getID(){
		String str =  get( IDescriptor.Attributes.ID.name() );
		return StringUtils.isEmpty(str)?0: Long.parseLong(str);
	}

	/**
	 * Get the (optional) id of the model. 
	 * @return
	 */
	@Override
	public String getName(){
		String str =  get( IDescriptor.Attributes.NAME.name() );
		return str;
	}

	/**
	 * Get the (optional) id of the model. 
	 * @return
	 */
	@Override
	public String getVersion(){
		String str =  get( IDescriptor.Attributes.VERSION.name() );
		return str;
	}

	/**
	 * Set the leaf with the given value
	 * @param attr
	 * @param value
	 */
	@Override
	public String get( Enum<?> attr ){
		return base.get( attr.name());
	}

	/**
	 * Set the leaf with the given value
	 * @param attr
	 * @param value
	 */
	@Override
	public void set( Enum<?> attr, String value ){
		base.set( attr.name(), value);
	}

	@Override
	public String getType() {
		return getValue( Attributes.TYPE );
	}

	/**
	 * Get the (optional) identifier of the model. 
	 * @return
	 */
	@Override
	public String getIdentifier(){
		return getValue( IModelLeaf.Attributes.IDENTIFIER);
	}

	/**
	 * Set the identifier for this leaf
	 * @param identifier
	 */
	@Override
	public void setIdentifier( String identifier ){
		setValue( IModelLeaf.Attributes.IDENTIFIER , identifier );
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
		return new Descriptor( base );
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

	@Override
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
		String str = getValue( IModelLeaf.Attributes.DEPTH);
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
		setValue( IModelLeaf.Attributes.DEPTH, String.valueOf( depth ));
	}

	protected String getValue( IModelLeaf.Attributes attr ) {
		return get(attr.name());
	}

	protected void setValue( IModelLeaf.Attributes attr, String value ) {
		set(attr.name(), value);
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

	@Override
	public boolean hasChanged() {
		return changed;
	}

	protected void setChanged(boolean changed) {
		this.changed = changed;
	}

	@Override
	public Set<Entry<String, String>> entrySet() {
		return base.entrySet();
	}

	@Override
	public String get(String key) {
		return base.get(key);
	}

	@Override
	public void set(String key, String value) {
		base.set(key, value);
	}
}