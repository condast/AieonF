package org.aieonf.sketch.factory;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.sketch.Activator;
import org.aieonf.template.builder.DefaultModelBuilder;
import org.aieonf.template.context.AbstractProviderContextFactory;

public class SketchFactory extends AbstractProviderContextFactory<IContextAieon, IModelLeaf<IDescriptor>> {

	private static SketchFactory factory = new SketchFactory();
	
	private SketchFactory() {
		super( Activator.BUNDLE_ID, new DefaultModelBuilder( SketchFactory.class ));
	}
	
	public static SketchFactory getInstance() {
		return factory;
	}

}
