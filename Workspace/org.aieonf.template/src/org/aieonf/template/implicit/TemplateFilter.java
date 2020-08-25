package org.aieonf.template.implicit;

import java.util.Collection;
import java.util.TreeSet;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.template.def.ITemplateLeaf;

public class TemplateFilter {

	private Collection<IDescriptor> descriptors;
	
	public TemplateFilter( ITemplateLeaf<IContextAieon> template ) {
		this.descriptors = new TreeSet<>();
		addDescriptor(template);
	}

	@SuppressWarnings("unchecked")
	protected void addDescriptor( IModelLeaf<? extends IDescriptor> model ) {
		descriptors.add(model.getData());
		if( model.isLeaf())
			return;
		IModelNode<IDescriptor> node = (IModelNode<IDescriptor>) model;
		for( IModelLeaf<? extends IDescriptor> child: node.getChildren().keySet())
			addDescriptor(child);
	}
	
}
