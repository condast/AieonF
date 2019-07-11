package org.aieonf.template.builder;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.template.ITemplateLeaf;

public interface IModelBuilder<T extends IDescriptor> {

	void addListener(IModelBuilderListener<T> listener);

	void removeListener(IModelBuilderListener<T> listener);

	IModelLeaf<T> createModel(
			ITemplateLeaf<? extends IDescriptor> template);

}
