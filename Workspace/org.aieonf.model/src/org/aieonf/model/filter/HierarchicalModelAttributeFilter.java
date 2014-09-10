package org.aieonf.model.filter;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.filter.AttributeFilter;
import org.aieonf.concept.filter.HierarchicalAttributeFilter;
import org.aieonf.model.IModelLeaf;
import org.aieonf.util.filter.FilterException;
import org.aieonf.util.filter.IFilter;

public class HierarchicalModelAttributeFilter<T extends IDescriptor> extends HierarchicalAttributeFilter<IModelLeaf<T>> implements IModelFilter<T>
{
	public HierarchicalModelAttributeFilter(AttributeFilter<IModelLeaf<T>> parentFilter, AttributeFilter<IModelLeaf<T>> childFilter)
			throws FilterException
	{
		super( Rules.OrChain, parentFilter, childFilter);
	}

	public HierarchicalModelAttributeFilter(HierarchyRules rule,
			IFilter<IModelLeaf<T>> parentFilter, IFilter<IModelLeaf<T>> childFilter) throws FilterException
	{
		super( Rules.OrChain, parentFilter, childFilter);
	}

	
	@Override
	public boolean acceptChild( IModelLeaf<T> child) {
		HierarchyRules rules = super.getHierarchyRule();
		boolean retval = false;
		switch( rules ){
		case ALLCHILDREN:
			retval = true;
			break;
		case ALLPARENTS:
			retval = super.getChildFilter().accept( child );
		case AS_IS:
			break;
		}
		return retval;
	}
}
