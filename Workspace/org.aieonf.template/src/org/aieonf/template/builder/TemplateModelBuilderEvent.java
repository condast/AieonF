package org.aieonf.template.builder;

import java.util.Arrays;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.template.ITemplateLeaf;

public class TemplateModelBuilderEvent<C extends IContextAieon, M extends IDescribable<? extends IDescriptor>> extends ModelBuilderEvent<M> {
	private static final long serialVersionUID = 1L;

	private ITemplateLeaf<C> template;
	
	public TemplateModelBuilderEvent(Object source, ITemplateLeaf<C> template, M model) {
		super(source, model);
		this.template = template;
	}
	public TemplateModelBuilderEvent(Object source, ITemplateLeaf<C> template, M[] model) {
		super(source, Arrays.asList( model ));
		this.template = template;
	}

	public ITemplateLeaf<C> getTemplate() {
		return template;
	}
}
