package org.aieonf.template;

import java.util.Map;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelLeaf;
import org.aieonf.template.property.ITemplateProperty;

/**
 * A template leaf extends a regular model leaf with a list of attributes that
 * assist in validation and creating of model nodes
 * 
 * @author Kees Pieters
 * @since 2012
 *
 * @param <T>
 */
public interface ITemplateLeaf<T extends IDescriptor> extends IModelLeaf<T>
{

	public static final String S_TEMPLATE = "Template";

	public void addAttribute( Enum<?> key, ITemplateProperty.Attributes attr, String value );

	public void addAttributes( Enum<?> key, Map<ITemplateProperty.Attributes, String> attrs );

	public void removeAttribute( Enum<?> key, ITemplateProperty.Attributes attr );

	public ITemplateProperty.Attributes[] attributes( Enum<?> key );

}