package org.aieonf.template.processor;

import java.util.Collection;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.model.constraints.IAspect;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.ModelException;
import org.aieonf.template.ITemplate;
import org.aieonf.template.ITemplateAieon;
import org.aieonf.template.ITemplateNode;
import org.aieonf.template.TemplateNodeWrapper;

public class TemplateProcessor<T extends ITemplateAieon>
{
	public static final String REG_EX = "[.]";
	private ITemplate template;
	
	public TemplateProcessor( ITemplate template )
	{
		this.template = template;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ITemplateNode<IDescriptor> findNode( String aspect ) throws ModelException{
		if( Descriptor.isNull( aspect ))
			return null; 
		return findNode( new TemplateNodeWrapper( this.template ), aspect );
	}

	/**
	 * Find the node with given aspect. In this case the aspect may be a sequence, such as 
	 * register.person.address.
	 * 
	 * @param node
	 * @param aspect
	 * @return
	 * @throws ModelException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected ITemplateNode<IDescriptor> findNode(ITemplateNode<IDescriptor> node,
			String aspect) throws ModelException
	{
		IAspect aspectAieon = null;//node.getTemplateAttributes().getAspect();
		if( aspectAieon == null )
			return null;
		if( Descriptor.isNull( aspectAieon.getAspect() ))
			return null;
		String asp = aspectAieon.getAspect().trim().toLowerCase();
		String[] split = aspect.trim().toLowerCase().split( REG_EX );
		if( !asp.startsWith(split[0]))
			return null;
		if( split.length == 1)
			return node;
    Collection<? extends IModelLeaf<? extends IDescriptor>>children = node.getChildren();
    ITemplateNode<IDescriptor> tn = null;
    for( IModelLeaf<? extends IDescriptor> child: children ){
      tn = findNode( new TemplateNodeWrapper( child ), asp.replace(split[0], ""));
      if( tn != null )
      	return tn;
    }
    return null;
	}
}
