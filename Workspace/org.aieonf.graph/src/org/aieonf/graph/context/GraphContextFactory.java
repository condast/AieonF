package org.aieonf.graph.context;

import org.aieonf.concept.context.IContextAieon;

import org.aieonf.template.ITemplateLeaf;
import org.aieonf.template.builder.DefaultModelCreator;
import org.aieonf.template.context.AbstractModelContextFactory;


/**
 * The simple context factory creates a default context and model
 * @author Kees Pieters
 */
public class GraphContextFactory extends AbstractModelContextFactory<IContextAieon> 
{
	private String bundle_id;
	
	protected GraphContextFactory( String bundle_id ) {
		this.bundle_id = bundle_id;
	}

	@Override
	public ITemplateLeaf<IContextAieon> onCreateTemplate() {
		return this.createDefaultTemplate( this.bundle_id, new DefaultModelCreator( this.getClass()));	
	}
}