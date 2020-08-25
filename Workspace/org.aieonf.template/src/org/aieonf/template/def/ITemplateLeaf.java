package org.aieonf.template.def;

import org.aieonf.commons.strings.StringStyler;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;

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
	public enum Attributes{
		IMPLIES;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}	
	}

	public static final String S_TEMPLATE = "Template";
}