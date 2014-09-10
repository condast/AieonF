package org.aieonf.model.filter;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.filter.DescriptorFilter;
import org.aieonf.model.IModelLeaf;
import org.aieonf.util.filter.FilterException;
import org.aieonf.util.filter.HierarchicalFilter;

public class HierarchicalModelDescriptorFilter<T extends IDescriptor> extends HierarchicalFilter<IModelLeaf<T>> implements IModelFilter<T>
{
	public HierarchicalModelDescriptorFilter(IDescriptor descriptor )
			throws FilterException
	{
		this( descriptor, descriptor);
	}

	public HierarchicalModelDescriptorFilter(IDescriptor parent, IDescriptor child )
			throws FilterException
	{
		super( Rules.OrChain, new DescriptorFilter<IModelLeaf<T>>( DescriptorFilter.Rules.Equals, parent ), 
				new DescriptorFilter<IModelLeaf<T>>( DescriptorFilter.Rules.Equals, child ));
	}

	public HierarchicalModelDescriptorFilter(HierarchyRules rule, IDescriptor parent, IDescriptor child) throws FilterException
	{
		super( Rules.OrChain, new DescriptorFilter<IModelLeaf<T>>( DescriptorFilter.Rules.Equals, parent ), 
				new DescriptorFilter<IModelLeaf<T>>( DescriptorFilter.Rules.Equals, child ));
	}

	public HierarchicalModelDescriptorFilter( HierarchyRules rule, IDescriptor descriptor )
			throws FilterException
	{
		this( rule, descriptor, descriptor);
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
