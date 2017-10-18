package org.aieonf.graph.context;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.model.xml.IXMLModelInterpreter;
import org.aieonf.template.ITemplateLeaf;
import org.aieonf.template.context.AbstractModelContextFactory;


/**
 * The simple context factory creates a default context and model
 * @author Kees Pieters
 */
public class GraphContextFactory extends AbstractModelContextFactory<IContextAieon> 
{
	private String bundle_id;
	private IXMLModelInterpreter<IDescriptor, ITemplateLeaf<IDescriptor>> creator;
	
	protected GraphContextFactory( String bundle_id, IXMLModelInterpreter<IDescriptor, ITemplateLeaf<IDescriptor>> creator ) {
		this.bundle_id = bundle_id;
		this.creator = creator;
	}

	@Override
	public ITemplateLeaf<IContextAieon> onCreateTemplate() {
		return this.createDefaultTemplate( this.bundle_id, creator );	
	}
}