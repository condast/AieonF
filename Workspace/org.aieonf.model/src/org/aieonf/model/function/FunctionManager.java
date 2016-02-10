package org.aieonf.model.function;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.IModelNode;
import org.aieonf.model.builder.IFunctionProvider;
import org.aieonf.model.function.IModelCompleteListener.ParseEvents;

public class FunctionManager<T extends IContextAieon> implements IFunctionProvider<IDescriptor,Object>{

	private IModelLeaf<T> root;
	private Collection<IFunctionProvider<IDescriptor,Object>> providers;
	private Collection<IModelCompleteListener> listeners;
	private boolean completed;
	
	
	public FunctionManager( IModelLeaf<T> root ) {
		this.root = root;
		providers = new ArrayList<IFunctionProvider<IDescriptor,Object>>();
		listeners = new ArrayList<IModelCompleteListener>();
	}

	protected boolean isCompleted() {
		return completed;
	}

	public boolean isEmpty(){
		return this.providers.isEmpty();
	}

	@SuppressWarnings("unchecked")
	public synchronized void addProvider( IFunctionProvider<IDescriptor,Object> provider){
		this.providers.add( provider );
		completed = this.parseModel( (IModelLeaf<IDescriptor>) this.root );
		this.notifylisteners(completed);
	}

	@SuppressWarnings("unchecked")
	public synchronized void removeProvider( IFunctionProvider<IDescriptor,Object> provider ){
		this.providers.remove( provider );
		completed = this.parseModel( (IModelLeaf<IDescriptor>) this.root );
		this.notifylisteners(completed);
	}

	public void addListener( IModelCompleteListener listener){
		this.listeners.add( listener );
	}

	public void removeListener( IModelCompleteListener listener ){
		this.listeners.remove( listener );
	}

	protected void notifylisteners( boolean result ){
		ParseEvents event = ( result )?ParseEvents.COMPLETE: ParseEvents.INCOMPLETE; 
		ModelCompleteEvent mce = new ModelCompleteEvent( this, event, root);
		for( IModelCompleteListener listener: this.listeners )
			listener.notifyModelCompleted( mce );
	}

	@Override
	public Object getFunction(IModelLeaf<IDescriptor> leaf) {
		Object function = null;
		for( IFunctionProvider<IDescriptor,?> provider: this.providers ){
			function = provider.getFunction(leaf);
			if( function != null )
				return function;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private final boolean parseModel( IModelLeaf<IDescriptor> leaf ){
		Object function = getFunction(leaf);
		boolean retval = ( function != null );
		if( !( leaf instanceof IModelNode ))
			return retval;
		IModelNode<IDescriptor> node = (IModelNode<IDescriptor>) leaf;
		for( IModelLeaf<? extends IDescriptor> child: node.getChildren() ){
			if( !parseModel( (IModelLeaf<IDescriptor>) child ))
				return false;
		}
		return retval;
	}

	@Override
	public boolean canProvide(IModelLeaf<IDescriptor> leaf) {
		if(!completed )
			return false;
		return root.equals( leaf );
	}
}
