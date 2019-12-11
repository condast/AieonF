package org.aieonf.model.seriliasable;

import org.aieonf.commons.Utils;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.*;
import org.aieonf.concept.IConcept.Scope;
import org.aieonf.concept.core.ConceptBase;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;

public class SerialisableModelLeaf extends ConceptBase implements IModelLeaf<IDescriptor>
{	
	//The concept that is modelled
	private ConceptBase base;
	private boolean leaf;
	
	private SerialisableModel parent;

	/**
	 * Only used for special models
	 */
	public SerialisableModelLeaf(){
		this.leaf = true;
		set( IDescriptor.Attributes.VERSION, String.valueOf(0));
	}
	
	/**
	 * Create the model
	 * @param concept
	 */
	public SerialisableModelLeaf( Descriptor descriptor ){
		this();
 	}

	/**
	 * Create the model
	 * @param concept
	 */
	public SerialisableModelLeaf( Descriptor descriptor, String type ){
		this( null, descriptor, type );
 	}

	/**
	 * Create the model
	 * @param concept
	 */
	public SerialisableModelLeaf( SerialisableModel parent ){
		this();
		this.parent = parent;
	}
	
	/**
	 * Create the model
	 * @param concept
	 */
	public SerialisableModelLeaf( SerialisableModel parent, Descriptor descriptor ){
		this();
		this.parent = parent;
 	}

	/**
	 * Create the model
	 * @param concept
	 */
	public SerialisableModelLeaf( SerialisableModel parent, Descriptor descriptor, String type ){
		this( parent, descriptor );
		this.set( IConcept.Attributes.TYPE, type );
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
	public String getID(){
		return get( Attributes.ID );
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
		String str = get( IConcept.Attributes.SCOPE ); 
		Scope scope = StringUtils.isEmpty(str)?Scope.PUBLIC: Scope.valueOf(str);
		return scope;
	}
	
	public void setScope( Scope scope ) {
		set( IConcept.Attributes.SCOPE, scope.name() );
	}

	/**
	 * @return the parent
	 */
	@Override
	public SerialisableModel getParent()
	{
		return parent;
	}


	@Override
	public void setParent(IModelNode<? extends IDescriptor> parent) {
		this.parent = (SerialisableModel) parent;
	}

	/**
	 * Returns true if the node is a root
	 * @return
	 */
	@Override
	public boolean isRoot(){
		return (( this.parent == null ) && ( this.getDepth() ) == 0 );
	}
	
  /**
   * If true, the values have changed
   * @return
  */
	@Override
  public boolean hasChanged(){
  	if( getDescriptor().hasChanged() )
  		return true;
  	return super.hasChanged();
  }
  
 	/**
	 * Get the descriptor that this tree node represents
	 * @return
	 */
	@Override
	public IDescriptor getDescriptor()
	{
		return new Descriptor( this );
	}


	@Override
	public void setData(IDescriptor descriptor) {
		//NOTHING
	}

 	/**
	 * Get the descriptor that this tree node represents
	 * @return
	 */
	@Override
	public IDescriptor getData()
	{
		return new Descriptor( this.base );
	}

	/**
	 * Get the direction of this model with 
	 * respect to its children
	 * @return
	*/
	@Override
	public Direction getDirection()
	{
		return Direction.valueOf( get( IModelLeaf.Attributes.DIRECTION ));
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
	public boolean contains( IDescriptor descr )
	{
		IDescriptor descriptor = getDescriptor();
		return descr.equals( descriptor );
	}

	/**
	 * Implement the compare to
	 */
	@Override
	public int compareTo( IDescribable node ) 
	{
		IDescriptor descriptor = getDescriptor();
		if( descriptor == null )
			return -1;
		return descriptor.compareTo( node.getDescriptor() );
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
		SerialisableModel md = 
			(SerialisableModel) model;
		return md.getChild( descriptor );
	}

	public static boolean hasChildren( IModelLeaf<? extends IDescriptor> model ){
		if(!( model instanceof IModelNode ))
			return false;
		SerialisableModel md = 
			(SerialisableModel) model;
		return md.hasChildren();		
	}
}