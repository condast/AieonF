package org.aieonf.graph.context;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.template.ITemplateLeaf;
import org.aieonf.template.builder.DefaultModelCreator;
import org.aieonf.template.context.AbstractSimpleContextFactory;
import org.aieonf.template.context.DefaultContext;
import org.aieonf.template.graph.IGraphModelProvider;

/**
 * The simple context factory creates a default context and model
 * @author Kees Pieters
 */
public class GraphContextFactory extends AbstractSimpleContextFactory<IGraphModelProvider<IContextAieon, IConcept>> 
{
	protected GraphContextFactory( String bundle_id ) {
		super( bundle_id );
	}

	@Override
	public ITemplateLeaf<IContextAieon> onCreateTemplate() {
		return this.createDefaultTemplate( super.getBundleId(), new DefaultModelCreator( this.getClass()));	
	}

	
	@Override
	protected DefaultContext onCreateContext(ITemplateLeaf<IContextAieon> model) {
		return new DefaultContext( super.getTemplate() );
	}
}