package org.aieonf.template.builder;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.template.ITemplateLeaf;

public interface IModelBuilder<T extends IDescriptor> {

	void addListener(IModelBuilderListener listener);

	void removeListener(IModelBuilderListener listener);

	IModelLeaf<T> createModel(
			ITemplateLeaf<? extends IDescriptor> template);

}
