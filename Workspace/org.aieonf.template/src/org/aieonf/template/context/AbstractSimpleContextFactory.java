package org.aieonf.template.context;

import org.aieonf.concept.context.IContextAieon;
import org.aieonf.template.ITemplateLeaf;
import org.aieonf.template.builder.DefaultModelCreator;
import org.aieonf.template.context.AbstractModelContextFactory;

/**
 * The simple context factory creates a default context and model
 * @author Kees Pieters
 */
public abstract class AbstractSimpleContextFactory<T extends Object> extends AbstractModelContextFactory<IContextAieon> 
{
	private String bundle_id;
	
	protected AbstractSimpleContextFactory( String bundle_id ) {
		this.bundle_id = bundle_id;
	}

	
	protected String getBundleId() {
		return bundle_id;
	}

	@Override
	public ITemplateLeaf<IContextAieon> onCreateTemplate() {
		return this.createDefaultTemplate( bundle_id, new DefaultModelCreator( this.getClass()));	
	}
}