package org.aieonf.template.core;

import java.util.Collection;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.Model;
import org.aieonf.template.def.ITemplateNode;

public class TemplateNodeWrapper<T extends IDescriptor> extends TemplateNode<T> implements
ITemplateNode<T>
{

	@SuppressWarnings("unchecked")
	public TemplateNodeWrapper( IModelLeaf<?> model )
	{
		super( (T) model.getDescriptor() );
		this.fillTemplateNode((IModelLeaf<? extends IDescriptor>) model);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void fillTemplateNode( IModelLeaf<? extends IDescriptor>model ){
		if(!( model instanceof ITemplateNode ))
			return;
		Collection<? extends IModelLeaf<? extends IDescriptor>> children = Model.getChildren( model );
		for( IModelLeaf<? extends IDescriptor> child: children )
			super.addChild( new TemplateNodeWrapper( child ));
	}
}
