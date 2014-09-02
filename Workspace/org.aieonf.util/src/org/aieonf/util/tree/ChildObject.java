package org.aieonf.util.tree;

import org.aieonf.util.tree.IChildObject;

public class ChildObject<T extends Object> implements IChildObject<T> {

	private T parent;
	private Object child;
	
	public ChildObject( T parent ) {
		super();
		this.parent = parent;
	}

	public ChildObject( T parent, Object child ) {
		this( parent );
		this.child = child;
	}

	@Override
	public T getParent() {
		return parent;
	}
	
	public Object getChild(){
		if( child == null )
			return this;
		return child;
	}

}
