package org.aieonf.template;

import java.util.Collection;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.ModelLeaf;

public class TemplateNodeWrapper<T extends IDescriptor> extends TemplateNode<T> implements
ITemplateNode<T>
{

	@SuppressWarnings("unchecked")
	public TemplateNodeWrapper( IModelLeaf<?> model )
	{
		super( (T) model.getDescriptor() );
		this.fillTemplateNode(model);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void fillTemplateNode( IModelLeaf<? extends IDescriptor>model ){
		if(!( model instanceof ITemplateNode ))
			return;
		Collection<? extends IModelLeaf<? extends IDescriptor>> children = ModelLeaf.getChildren( model );
		for( IModelLeaf<? extends IDescriptor> child: children )
			super.addChild( new TemplateNodeWrapper( child ));
	}
}
