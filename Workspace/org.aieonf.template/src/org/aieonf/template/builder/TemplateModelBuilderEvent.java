package org.aieonf.template.builder;

import org.aieonf.model.IModelLeaf;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.template.ITemplateLeaf;

public class TemplateModelBuilderEvent extends ModelBuilderEvent {
	private static final long serialVersionUID = 1L;

	private ITemplateLeaf<?> template;
	
	public TemplateModelBuilderEvent(Object source, ITemplateLeaf<?> template, IModelLeaf<?> model) {
		super(source, model);
		this.template = template;
	}

	public ITemplateLeaf<?> getTemplate() {
		return template;
	}
}
