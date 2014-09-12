package org.aieonf.template.builder;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.ContextAieon;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.Model;
import org.aieonf.model.ModelLeaf;
import org.aieonf.template.ITemplateLeaf;

public class DefaultModelBuilder extends AbstractModelBuilder<IContextAieon> {

	@Override
	protected IModelLeaf<IContextAieon> createRoot(IDescriptor descriptor) {
		return new Model<IContextAieon>( new ContextAieon( descriptor ));
	}

	@Override
	protected IModelLeaf<IDescriptor> createNode(
			ITemplateLeaf<? extends IDescriptor> template) {
		IDescriptor descriptor = new Descriptor();
		return template.isLeaf() ? new ModelLeaf<IDescriptor>( descriptor): new Model<IDescriptor>( descriptor );
	}
}
