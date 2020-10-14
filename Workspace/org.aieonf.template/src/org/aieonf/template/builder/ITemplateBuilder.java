package org.aieonf.template.builder;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.template.def.ITemplateLeaf;

public interface ITemplateBuilder<D extends IDescribable> {

	void addListener(IModelBuilderListener<D> listener);

	void removeListener(IModelBuilderListener<D> listener);

	D createModel( ITemplateLeaf<? extends IDescriptor> template);
}
