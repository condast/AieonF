package org.aieonf.template.context;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.IModelNode;
import org.aieonf.template.ITemplateLeaf;
import org.aieonf.util.Utils;

public abstract class AbstractModelContext<T extends IContextAieon> implements IContext<T> {

	private ITemplateLeaf<T> model;
	
	
	protected AbstractModelContext( ITemplateLeaf<T> model ) {
		this.model = model;
	}

	public void startup(){
		System.out.println("START " + this.getClass().getName());
	}

	/**
	 * Get the template for this application
	 * @return
	 */
	@Override
	public ITemplateLeaf<T> getTemplate() {
		return model;
	}

	@Override
	public IDomainAieon getDomain() {
		return model.getDescriptor().getDomain();
	}

	@Override
	public void clear() throws Exception {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Get the model with the given identifier
	 * @param identifier
	 * @return
	 */
	protected IModelLeaf<IDescriptor> getModel( String identifier ){
		return getModel( this.model, identifier );
	}

	@SuppressWarnings("unchecked")
	protected static IModelLeaf<IDescriptor> getModel( IModelLeaf<? extends IDescriptor> leaf, String identifier ){
		if( Utils.isNull( identifier ))
			return null;
		if( identifier.equals( leaf.getID()))
			return (IModelLeaf<IDescriptor>) leaf;
		if( leaf.isLeaf())
			return null;
		IModelNode<IDescriptor> node = (IModelNode<IDescriptor>) leaf;
		for( IModelLeaf<? extends IDescriptor> child: node.getChildren() ){
			IModelLeaf<IDescriptor> result = getModel( child, identifier );
			if( result != null )
				return result;
		}
		return null;
	}

}
