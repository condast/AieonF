package org.aieonf.graph.context;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.xml.IXMLModelInterpreter;
import org.aieonf.template.context.AbstractModelContextFactory;
import org.aieonf.template.def.ITemplateLeaf;


/**
 * The simple context factory creates a default context and model
 * @author Kees Pieters
 */
public class GraphContextFactory extends AbstractModelContextFactory<IDescriptor, IModelLeaf<IDescriptor>> 
{
	private String bundle_id;
	private IXMLModelInterpreter<ITemplateLeaf<IDescriptor>> creator;
	
	protected GraphContextFactory( String bundle_id, IXMLModelInterpreter<ITemplateLeaf<IDescriptor>> creator ) {
		this.bundle_id = bundle_id;
		this.creator = creator;
	}

	@Override
	public ITemplateLeaf<IContextAieon> onCreateTemplate() {
		return this.createDefaultTemplate( this.bundle_id, creator );	
	}
}