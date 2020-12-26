package org.aieonf.template.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.aieonf.concept.IConcept.Scope;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.template.core.TemplateAieon;
import org.aieonf.template.def.ITemplate;
import org.aieonf.template.def.ITemplateAieon;
import org.aieonf.template.def.ITemplateLeaf;
import org.aieonf.template.def.ITemplateNode;

public class TemplateWrapper<D extends IDescriptor> implements ITemplate
{
	private ITemplateLeaf<D> model;

	private Map<IModelLeaf<? extends IDescriptor>, String> children;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	TemplateWrapper( ITemplateLeaf<D>model )
	{
		this.model = model;
		if(!(model instanceof ITemplateNode ))
			return;
		ITemplateNode<? extends IDescriptor> node = (ITemplateNode )model; 
		this.children = node.getChildren();
	}

	@Override
	public long getID() {
		return this.model.getID();
	}

	@Override
	public String getName() {
		return this.model.getName();
	}

	@Override
	public String getVersion() {
		return this.model.getVersion();
	}

	@Override
	public String getType() {
		return this.model.getType();
	}

	@Override
	public String getIdentifier() {
		return this.model.getIdentifier();
	}

	@Override
	public void setIdentifier(String identifier) {
		this.model.setIdentifier(identifier);
	}

	
	@Override
	public boolean isReadOnly() {
		return this.model.isReadOnly();
	}

	@Override
	public void setReadOnly(boolean choice) {
		this.model.setReadOnly(choice);
	}

	@Override
	public String get(Enum<?> attr) {
		return this.model.get(attr);
	}
	
	@Override
	public String get( String attr) {
		return this.model.get(attr);
	}
	
	@Override
	public void set(Enum<?> attr, String value) {
		this.model.set(attr, value);
	}

	@Override
	public void set( String attr, String value) {
		this.model.set(attr, value);
	}

	/**
	 * Returns true if the node is a root
	 * @return
	 */
	@Override
	public boolean isRoot(){
		return this.model.isRoot();
	}

	/**
	 * If true, the values have changed
	 * @return
	 */
	@Override
	public boolean hasChanged()
	{
		return this.model.hasChanged();
	}

	/**
	 * Get the descriptor that this tree node represents
	 * @return
	 */
	@Override
	public ITemplateAieon getDescriptor()
	{
		if( this.model.getDescriptor() instanceof ITemplateAieon )
			return (ITemplateAieon) this.model.getDescriptor();
		TemplateAieon aieon = new TemplateAieon( this );
		return aieon;
	}

	
	@Override
	public ITemplateAieon getData() {
		return (ITemplateAieon) this.model.getData();
	}


	@SuppressWarnings("unchecked")
	@Override
	public void setData(ITemplateAieon descriptor) {
		this.model.setData((D) descriptor);
	}


	@SuppressWarnings("unchecked")
	@Override
	public boolean isReverse() {
		if( this.model instanceof IModelNode ){
			IModelNode<IDescriptor> node = (org.aieonf.model.core.IModelNode<IDescriptor> )this.model;
			return node.isReverse();
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setReverse( boolean choice ) {
		if( this.model instanceof IModelNode ){
			IModelNode<IDescriptor> node = (org.aieonf.model.core.IModelNode<IDescriptor> )this.model;
			node.setReverse(choice);
		}
	}

	/**
	 * Get the direction of this model with 
	 * respect to its children
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Direction getDirection()
	{
		if( this.model instanceof IModelNode ){
			IModelNode<IDescriptor> node = (org.aieonf.model.core.IModelNode<IDescriptor> )this.model;
			return node.getDirection();
		}
		return Direction.UNI_DIRECTIONAL;
	}

	/**
	 * Add a child model to the model
	 * @param model
	 * @returns the created model
	 */
	@Override
	public boolean addChild(IModelLeaf<? extends IDescriptor> child)
	{
		return addChild( child, null );
	}

	
	@Override
	@SuppressWarnings("unchecked")
	public boolean addChild(IModelLeaf<? extends IDescriptor> child, String type) {
		if( this.model instanceof IModelNode ){
			IModelNode<IDescriptor> node = (org.aieonf.model.core.IModelNode<IDescriptor> )this.model;
			return node.addChild( child );
		}
		children.put(child, type );
		return true;
	}


	/**
	 * Remove a child model from the parent
	 * @param concept
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean removeChild(IModelLeaf<? extends IDescriptor> child)
	{
		if( this.model instanceof IModelNode ){
			IModelNode<IDescriptor> node = (org.aieonf.model.core.IModelNode<IDescriptor> )this.model;
			return node.removeChild( child );
		}
		children.remove(child );
		return true;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public void removeAllChildren() {
		if( this.model instanceof IModelNode ){
			IModelNode<IDescriptor> node = (org.aieonf.model.core.IModelNode<IDescriptor> )this.model;
			node.removeAllChildren();
		}
		children.clear();
	}

	/**
	 * Remove a child model from the parent
	 * @param concept
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<IModelLeaf<? extends IDescriptor>, String> getChildren()
	{
		if( this.model instanceof ITemplateNode ){
			IModelNode<IDescriptor> node = (org.aieonf.model.core.IModelNode<IDescriptor> )this.model;
			return node.getChildren();
		}
		return children;
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
	 * Get the child with the given descriptor
	 * @param descriptor
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public IModelLeaf<? extends IDescriptor> getChild( IDescriptor descriptor)
	{
		if( this.model instanceof ITemplateNode ){
			IModelNode<IDescriptor> node = (org.aieonf.model.core.IModelNode<IDescriptor> )this.model;
			return node.getChild( descriptor );
		}
		for( IModelLeaf<? extends IDescriptor> child: children.keySet() ){
			if( child.getDescriptor().equals( descriptor ))
				return child;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String getChildIdentifier(IModelLeaf<? extends IDescriptor> child) {
		if( this.model instanceof ITemplateNode ){
			IModelNode<IDescriptor> node = (IModelNode<IDescriptor>) this.model;
			return node.getChildIdentifier( child );
		}
		return null;
	}


	/**
	 * Returns true if the model is a leaf ( has no children )
	 * @return
	 */
	@Override
	public boolean isLeaf(){
		return this.model.isLeaf();
	}

	/**
	 * Returns true if the model has children
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean hasChildren()
	{
		if( this.model instanceof IModelNode ){
			IModelNode<IDescriptor> node = (org.aieonf.model.core.IModelNode<IDescriptor> )this.model;
			return node.hasChildren();
		}
		return ( children.size() > 0 );
	}

	/**
	 * Get the number of children
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int nrOfchildren()
	{
		if( this.model instanceof IModelNode ){
			IModelNode<IDescriptor> node = (org.aieonf.model.core.IModelNode<IDescriptor> )this.model;
			return node.nrOfchildren();
		}
		return children.size();
	}

	/**
	 * Returns true if the model, or one of its descendants contains
	 * the given descriptor
	 * @param descriptor
	 * @return
	 */
	@Override
	public boolean contains(IDescriptor descriptor)
	{
		return this.model.contains( descriptor );
	}

	/**
	 * Get the depth of the model. This is the maximum amount of the
	 * root to the farthest ancestor in the tree
	 * @return
	 */
	@Override
	public int getDepth()
	{
		return this.model.getDepth();
	}

	@Override
	public void setDepth(int depth) throws ConceptException
	{
		this.model.setDepth( depth );
	}

	@Override
	public ITemplateNode<?> getParent()
	{
		return (ITemplateNode<?>) this.model.getParent();
	}

	@Override
	public void setParent(IModelNode<?> parent)
	{
		this.model.setParent(parent);
	}

	@Override
	public int implies(IDescriptor descriptor) {
		return this.model.implies(descriptor);
	}

	@Override
	public int compareTo(IDescribable o) {
		return this.model.compareTo(o);
	}

	@Override
	public void setLeaf(boolean choice) {
		this.model.setLeaf(choice);
	}

	@Override
	public Scope getScope() {
		return model.getScope();
	}

	@Override
	public Set<Entry<String, String>> entrySet() {
		return model.entrySet();
	}
}
