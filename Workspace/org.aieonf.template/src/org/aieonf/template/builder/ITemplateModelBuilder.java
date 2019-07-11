package org.aieonf.template.builder;

import org.aieonf.commons.parser.IParserListener;
import org.aieonf.commons.parser.ParseException;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.template.ITemplateLeaf;
import org.aieonf.template.core.ITemplate;
import org.aieonf.template.databinding.TemplateConceptValidator;

public interface ITemplateModelBuilder extends IParserListener<ITemplateLeaf<? extends IDescriptor>>
{

	/**
	 * Assemble the model
	 * @param template
	 * @throws ConceptException
	 * @throws ParseException
	 */
	public void assembleModel(  ITemplateLeaf<? extends IDescriptor> template) throws ConceptException, ParseException;	
	
	/**
	 * Get the template
	 * @return
	 */
	public ITemplate getTemplate();

		/**
	 * Get the model that was built
	 * @return
	 */
	public IModelLeaf<? extends IDescriptor> getModel();

	/**
	 * Get the root of the model
	*/
	public IDescriptor getRoot();

	/**
	 * Get a validator for the given concept
	 * @param concept
	 * @return
	*/
	public TemplateConceptValidator getValidator( IConcept concept );
}
