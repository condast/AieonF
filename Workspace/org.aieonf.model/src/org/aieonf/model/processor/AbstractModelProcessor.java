package org.aieonf.model.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.model.core.ModelException;

public class AbstractModelProcessor<T extends IDescriptor>
{
	public static final String S_ERR_NO_MODEL = 
		"Can't process because no model has been provided";
	
	private IModelNode<T> model;

	private Stack<IModelNode<T>> parents;
	private int index;
	
	private boolean completed;
	
	private List<IModelFactory<IModelNode<T>>> listeners;
	
	public enum ProcessMode{
		Continuous,
		Step
	}
	private ProcessMode mode;
	
	public AbstractModelProcessor(){
		this( ProcessMode.Continuous );
	}
	
	public AbstractModelProcessor( ProcessMode mode ){
		this.parents = new Stack<IModelNode<T>>();
		this.index = 0;
		this.completed = false;
		
		this.mode = mode;
		this.listeners = new ArrayList<IModelFactory<IModelNode<T>>>();
	}

	/**
	 * Returns true if the processor has completed
	 * @return
	 */
	public boolean hasCompleted(){
		return this.completed;
	}
	
	/**
	 * Set the completed flag to true
	 */
	protected void setCompleted(){
		this.completed = true;
	}
	
	/**
	 * @return the model
	 */
	public IModelNode<T> getModel()
	{
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(IModelNode<T> model)
	{
		this.model = model;
	}

	/**
	 * @return the index
	 */
	protected int getIndex()
	{
		return index;
	}

	/**
	 * @param index the index to set
	 */
	protected void setIndex(int index)
	{
		this.index = index;
	}

	/**
	 * Get the parent stack
	 * @return
	 */
	protected Stack<IModelNode<T>> getStack(){
		return this.parents;
	}
	
	/**
	 * 
	 * @return
	 */
	protected Collection<IModelFactory<IModelNode<T>>> getListeners(){
		return this.listeners;
	}
	
	/**
	 * Get the process mode
	 * @return
	 */
	public ProcessMode getProcessMode(){
		return this.mode;
	}
	
	/**
	 * Set the process mode
	 * @param mode
	 */
	public void setProcessMode( ProcessMode mode ){
		this.mode = mode;
	}
	/**
	 * Add a listener to the processor
	 * @param listener
	 */
	public void addListener( IModelFactory<IModelNode<T>> listener ){
		this.listeners.add( listener );
	}

	/**
	 * Remove a listener from the processor
	 * @param listener
	 */
	public void removeListener( IModelFactory<IModelNode<T>> listener ){
		this.listeners.remove( listener );
	}

	/**
	 * Process the model
	 * @throws ModelException
	 */
	public void process() throws ModelException{
		if( this.model == null )
			throw new NullPointerException( S_ERR_NO_MODEL );
		this.completed = false;
		switch( this.mode ){
			case Continuous:
				this.parents.add( this.model );	
				this.process( this.model );
				break;
			case Step:
				if( this.parents.size() == 0 )
					this.processStep();
				else
					this.processNext();
				break;
		}
	}

	/**
	 * Call all the listeners and allow them to process the node
	 * @param node
	 * @throws ModelException
	 */
	@SuppressWarnings("unchecked")
	protected void process( IModelNode<T> node ) throws ModelException{
		for( IModelFactory<IModelNode<T>> listener: listeners ){
			listener.processModelNode( node );
		}
		for( IModelLeaf<? extends IDescriptor> child: node.getChildren().keySet() ){
			this.process((org.aieonf.model.core.IModelNode<T> )child );
		}
	}

	/**
	 * Call all the listeners and allow them to process the node
	 * @param node
	 * @throws ModelException
	 */
	protected void processStep() throws ModelException{
	  this.index = 0;
	  this.parents.clear();
	  this.parents.push( this.model );
		for( IModelFactory<IModelNode<T>> listener: listeners ){
			listener.processModelNode( this.model );
		}
	}

	/**
	 * Call all the listeners and allow them to process the node
	 * @param node
	 * @throws ModelException
	 */
	@SuppressWarnings("unchecked")
	protected void processNext() throws ModelException{
		
		if( this.parents.size() == 0 )
			return;
		
		IModelNode<T> parent = this.parents.lastElement();
		List<IModelLeaf<? extends IDescriptor>> children;
		
		int size = parent.getChildren().size();
		if(( size == 0 ) || ( index == size )){
			this.parents.pop();
			if( this.parents.size() == 0 ){
				this.completed = true;
				return;
			}
			IModelNode<T> previous = this.parents.lastElement();
			children = new ArrayList<IModelLeaf<? extends IDescriptor>>( previous.getChildren().keySet() );			
			this.index = children.indexOf( parent ) + 1;
			return;
		}
		
		children = new ArrayList<IModelLeaf<? extends IDescriptor>>( parent.getChildren().keySet() );
		IModelNode<T> node = (org.aieonf.model.core.IModelNode<T> )children.get( index );
		this.parents.push( node );
		this.index = 0;
		for( IModelFactory<IModelNode<T>> listener: listeners ){
			listener.processModelNode( node );
		}
	}
}