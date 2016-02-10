package org.aieonf.template;

import java.util.Collection;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.ModelException;
import org.aieonf.model.ModelLeaf;

public class TemplateNodeWrapper<T extends IDescriptor> extends TemplateNode<T> implements
ITemplateNode<T>
{

	public TemplateNodeWrapper( IModelLeaf<T> model )
			throws ModelException
	{
		super( model.getDescriptor() );
		this.fillTemplateNode(model);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void fillTemplateNode( IModelLeaf<? extends IDescriptor>model ) throws ModelException{
		if(!( model instanceof ITemplateNode ))
			return;
		Collection<? extends IModelLeaf<? extends IDescriptor>> children = ModelLeaf.getChildren( model );
		for( IModelLeaf<? extends IDescriptor> child: children )
			super.addChild( new TemplateNodeWrapper( child ));
	}
}
