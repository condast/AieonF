package org.aieonf.graph.context;

import org.aieonf.concept.context.IContextAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.xml.IModelInterpreterFactory;
import org.aieonf.template.builder.TemplateInterpreterFactory;
import org.aieonf.template.context.AbstractProviderContextFactory;


/**
 * The simple context factory creates a default context and model
 * @author Kees Pieters
 */
public class GraphContextFactory extends AbstractProviderContextFactory<IContextAieon, IModelLeaf<IContextAieon>>{
	
	public GraphContextFactory( String bundle_id, IModelInterpreterFactory<IContextAieon> creator ) {
		super( bundle_id, new TemplateInterpreterFactory<IContextAieon>( GraphContextFactory.class ));
	}
}