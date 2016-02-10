package org.aieonf.model.filter;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.filter.AttributeFilter;
import org.aieonf.concept.filter.HierarchicalAttributeFilter;
import org.aieonf.model.IModelLeaf;
import org.aieonf.util.filter.FilterException;
import org.aieonf.util.filter.IFilter;

public class HierarchicalModelAttributeFilter<T extends IDescriptor> extends HierarchicalAttributeFilter<IModelLeaf<T>> implements IModelFilter<T>
{
	private static final String S_ERR_MIN_DEPTH_WRONG = "The minimum depth must be equal to, or larger than zero";

	private int minDepth, maxDepth;

	public HierarchicalModelAttributeFilter(AttributeFilter<IModelLeaf<T>> parentFilter, AttributeFilter<IModelLeaf<T>> childFilter)
			throws FilterException
	{
		this( parentFilter, childFilter, 0, -1 );
	}

	public HierarchicalModelAttributeFilter(AttributeFilter<IModelLeaf<T>> parentFilter, AttributeFilter<IModelLeaf<T>> childFilter, int maxDepth)
			throws FilterException
	{
		this( parentFilter, childFilter, 0, maxDepth );
	}

	public HierarchicalModelAttributeFilter(AttributeFilter<IModelLeaf<T>> parentFilter, AttributeFilter<IModelLeaf<T>> childFilter, int minDepth, int maxDepth)
			throws FilterException
	{
		super( Rules.OrChain, parentFilter, childFilter);
		if( minDepth < 0 )
			throw new IllegalArgumentException( S_ERR_MIN_DEPTH_WRONG);
		this.minDepth = minDepth;
		this.maxDepth = maxDepth;
	}

	public HierarchicalModelAttributeFilter(HierarchyRules rule,
			IFilter<IModelLeaf<T>> parentFilter, IFilter<IModelLeaf<T>> childFilter, int minDepth, int maxDepth ) throws FilterException
	{
		super( Rules.OrChain, parentFilter, childFilter);
		if( minDepth < 0 )
			throw new IllegalArgumentException( S_ERR_MIN_DEPTH_WRONG);
		this.minDepth = minDepth;
		this.maxDepth = maxDepth;
	}

	public HierarchicalModelAttributeFilter(HierarchyRules rule, AttributeFilter<IModelLeaf<T>> parentFilter, AttributeFilter<IModelLeaf<T>> childFilter)
			throws FilterException
	{
		this( rule, parentFilter, childFilter, 0, -1 );
	}

	public HierarchicalModelAttributeFilter(HierarchyRules rule, AttributeFilter<IModelLeaf<T>> parentFilter, AttributeFilter<IModelLeaf<T>> childFilter, int maxDepth)
			throws FilterException
	{
		this( rule, parentFilter, childFilter, 0, maxDepth );
	}

	@Override
	public int getMinDepth() {
		return this.minDepth;
	}

	@Override
	public int getMaxDepth() {
		return this.maxDepth;
	}
	
	@Override
	protected boolean acceptEnabled(Object obj) throws FilterException {
		if( !ModelFilterWrapper.acceptDepth( minDepth, maxDepth, obj ))
			return false;
		return super.getParentFilter().accept(obj);
	}

	@Override
	public boolean acceptChild( IModelLeaf<T> child) {
		HierarchyRules rules = super.getHierarchyRule();
		boolean retval = false;
		switch( rules ){
		case ALLCHILDREN:
			retval = ModelFilterWrapper.acceptDepth( minDepth, maxDepth, child );
			break;
		case ALLPARENTS:
			retval =  ModelFilterWrapper.acceptDepth( minDepth, maxDepth, child ) && super.getChildFilter().accept( child );
			break;
		case AS_IS:
			break;
		}
		return retval;
	}
}
