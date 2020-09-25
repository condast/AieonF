package org.aieonf.commons.ui.flow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;

public abstract class AbstractFlowControl<T extends Object> implements IFlowControl {

	private Stack<Integer> history;
	private Collection<IFlowControlListener<T>> listeners;
	private boolean completed;
	private Direction direction;

	protected AbstractFlowControl() {
		this.completed = false;
		this.direction = Direction.PROCEED;
		history = new Stack<Integer>();
		listeners = new ArrayList<IFlowControlListener<T>>();
	}

	@Override
	public void init() {
		this.history.clear();
		this.notifyListeners( new FlowEvent<T>(this, FlowEvents.INIT ));
	}

	public void addListener( IFlowControlListener<T> listener ){
		this.listeners.add( listener );
	}

	public void removeListener( IFlowControlListener<T> listener ){
		this.listeners.remove( listener );
	}

	protected void notifyListeners( FlowEvent<T> event  ){
		for( IFlowControlListener<T> listener: listeners )
			listener.notifyEventChanged(event);
	}
	
	public int getIndex(){
		return ( this.history.isEmpty() )? 0: this.history.lastElement();		
	}
	
	protected void setIndex( int index ){
		this.history.push( index );
	}

	/**
	 * Calculate the next index, based on the current position
	 * @param currentIndex
	 * @return
	 */
	protected int onNextIndex( int currentIndex ){
		return ++currentIndex;
	}
	
	@Override
	public int next() {
		if( this.completed )
			return -1;
		this.direction = Direction.PROCEED;
		int index = this.onNextIndex( this.getIndex());
		this.history.push(index);
		this.notifyListeners( new FlowEvent<T>(this, FlowEvents.NEXT, index ));
		return index;
	}

	@Override
	public int previous() {
		if( this.completed )
			return -1;
		this.direction = Direction.BACKTRACK;
		int current = ( this.history.isEmpty() )? -1: this.history.pop();
		if( current < 0 )
			return current;
		int index = ( this.history.isEmpty() )? 0: this.history.lastElement(); 
		this.notifyListeners( new FlowEvent<T>(this, FlowEvents.PREVIOUS, index ));
		return index;
	}

	@Override
	public Direction getDirection() {
		return direction;
	}

	@Override
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	@Override
	public boolean isCompleted() {
		return completed;
	}

	@Override
	public void complete() {
		this.completed = true;//first set the boolean, then clean up
		int index = this.getIndex();
		this.notifyListeners( new FlowEvent<T>(this, FlowEvents.NEXT, index ));
	}
}