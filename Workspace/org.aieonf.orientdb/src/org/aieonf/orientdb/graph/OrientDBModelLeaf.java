package org.aieonf.orientdb.graph;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.commons.Utils;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IConcept.Scope;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class OrientDBModelLeaf extends VertexConceptBase implements IModelLeaf<IDescriptor> {

	//The concept that is modelled
	private IDescriptor descriptor;
	private boolean leaf;
	
	private IModelNode<? extends IDescriptor> parent;

	public OrientDBModelLeaf(  ) {
		super(null );
	}

	public OrientDBModelLeaf( Vertex vertex, IDescriptor descriptor ) {
		this( null, vertex, descriptor);
	}
	
	public OrientDBModelLeaf( IModelNode<? extends IDescriptor> parent, Vertex vertex, IDescriptor descriptor ) {
		super( vertex );
		this.parent = parent;
		this.descriptor = descriptor;
		setIdentifier( descriptor.get( IModelLeaf.Attributes.IDENTIFIER ));
		this.leaf = true;
	}

	public OrientDBModelLeaf( OrientDBModel parent, Object id, OrientGraph graph, IDescriptor descriptor ) {
		this( parent, graph.addVertex(id), descriptor );
		try {
			graph.addEdge(null, parent.getVertex(), super.getVertex(), IModelLeaf.Attributes.CHILD.name());
			graph.commit();
		}
		catch( Exception ex ) {
			graph.rollback();
			ex.printStackTrace();
		}
	}

	@Override
	public IDescriptor getDescriptor() {
		return descriptor;
	}

	
	public void init( IDescriptor descriptor ){
		this.descriptor = descriptor;
		this.set( IDescriptor.Attributes.NAME, descriptor.getName() );
		this.set( IDescriptor.Attributes.ID, descriptor.getID() );
		this.set( IDescriptor.Attributes.VERSION, String.valueOf( descriptor.getVersion() ));		
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
		String str = get( IConcept.Attributes.SCOPE.name());
		return StringUtils.isEmpty(str)?Scope.PUBLIC: Scope.valueOf(str);
	}

	/**
	 * @return the parent
	 */
	@Override
	public IModelNode<? extends IDescriptor> getParent()
	{
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	@Override
	public void setParent(IModelNode<? extends IDescriptor> parent)
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
	
  /**
   * If true, the values have changed
   * @return
  */
	@Override
  public boolean hasChanged(){
  	if( descriptor.hasChanged() )
  		return true;
  	return super.hasChanged();
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
		return descr.equals( this.descriptor );
	}

	/**
	 * Implement the compare to
	 */
	@Override
	public int compareTo( IDescribable<?> node ) 
	{
		if( this.descriptor == null )
			return -1;
		return this.descriptor.compareTo( node.getDescriptor() );
	}

	
	@Override
	public int implies(IDescriptor descriptor) {
		return 0;//implies.compareTo( descriptor);
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
		return md.getChildren();
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
		for( IModelLeaf<?> child: node.getChildren() ){
			IModelLeaf<IDescriptor> result = getModel( child, id ); 
			if( result != null )
				return result;
		}
		return null;
	}
}
