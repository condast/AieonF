package org.aieonf.graph.context;

import org.aieonf.concept.context.IContextAieon;
import org.aieonf.graph.IGraphModel;
import org.aieonf.template.ITemplateLeaf;
import org.aieonf.template.builder.DefaultModelCreator;
import org.aieonf.template.context.AbstractSimpleContextFactory;
import org.aieonf.util.graph.IVertex;

/**
 * The simple context factory creates a default context and model
 * @author Kees Pieters
 */
public class GraphContextFactory extends AbstractSimpleContextFactory<IGraphModel<IContextAieon,IVertex<IContextAieon>>> 
{
	protected GraphContextFactory( String bundle_id ) {
		super( bundle_id );
	}

	@Override
	public ITemplateLeaf<IContextAieon> onCreateTemplate() {
		return this.createDefaultTemplate( super.getBundleId(), new DefaultModelCreator( this.getClass()));	
	}
}