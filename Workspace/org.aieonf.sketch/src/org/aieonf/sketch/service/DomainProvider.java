package org.aieonf.sketch.service;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.osgi.domain.AbstractDomainProvider;
import org.aieonf.osgi.domain.IDomainProvider;
import org.aieonf.sketch.factory.SketchFactory;
import org.aieonf.template.context.IProviderContextFactory;
import org.osgi.service.component.annotations.Component;

@Component( name="org.saight.sketch.domain")
public class DomainProvider extends AbstractDomainProvider<IContextAieon, IModelLeaf<IDescriptor>> implements IDomainProvider{

	@Override
	protected IProviderContextFactory<IContextAieon, IDomainAieon, String, IModelLeaf<IDescriptor>>  getFactory() {
		return SketchFactory.getInstance();
	}	
}