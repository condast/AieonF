package org.aieonf.template.builder;

import java.util.Arrays;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.template.ITemplateLeaf;

public class TemplateModelBuilderEvent<T extends IDescriptor> extends ModelBuilderEvent<IModelLeaf<T>> {
	private static final long serialVersionUID = 1L;

	private ITemplateLeaf<?> template;
	
	public TemplateModelBuilderEvent(Object source, ITemplateLeaf<T> template, IModelLeaf<T> model) {
		super(source, model);
		this.template = template;
	}
	public TemplateModelBuilderEvent(Object source, ITemplateLeaf<?> template, IModelLeaf<T>[] model) {
		super(source, Arrays.asList( model ));
		this.template = template;
	}

	public ITemplateLeaf<?> getTemplate() {
		return template;
	}
}
