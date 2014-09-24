package org.aieonf.concept.library;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.implicit.AttributeImplies;

public class ImpliesCategory<T extends IDescriptor> extends
		AttributeImplies<CategoryAieon, T> {

	public ImpliesCategory(CategoryAieon reference) {
		super(reference, CategoryAieon.Attributes.CATEGORY );
	}
}
