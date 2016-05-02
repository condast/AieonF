package org.aieonf.template.context;

import java.io.File;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.template.ITemplateLeaf;
import org.aieonf.template.builder.DefaultModelCreator;
import org.aieonf.template.context.AbstractModelContextFactory;
import org.aieonf.util.Utils;

/**
 * The simple context factory creates a default context and model
 * @author Kees Pieters
 */
public abstract class AbstractSimpleContextFactory<T extends Object> extends AbstractModelContextFactory<IContextAieon> 
{
	private static final String S_MODEL = "Model";
	private String bundle_id;
	
	protected AbstractSimpleContextFactory( String bundle_id ) {
		this.bundle_id = bundle_id;
	}

	
	protected String getBundleId() {
		return bundle_id;
	}

	@Override
	public ITemplateLeaf<IContextAieon> onCreateTemplate() {
		ITemplateLeaf<IContextAieon> template  = this.createDefaultTemplate( bundle_id, new DefaultModelCreator( this.getClass()));	
		IDescriptor descriptor = template.getDescriptor();
		String source = descriptor.get( IConcept.Attributes.SOURCE );
		if( Utils.isNull( source ))
			descriptor.set( IConcept.Attributes.SOURCE, this.bundle_id + File.separator + S_MODEL );
		return template;
	}
}