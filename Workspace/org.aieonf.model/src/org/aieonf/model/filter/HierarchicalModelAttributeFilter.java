package org.aieonf.model.filter;

import org.aieonf.commons.filter.FilterException;
import org.aieonf.commons.filter.IAttributeFilter;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.filter.AttributeFilter;
import org.aieonf.concept.filter.FilterFactory.Filters;
import org.aieonf.concept.filter.HierarchicalAttributeFilter;
import org.aieonf.model.core.IModelLeaf;

public class HierarchicalModelAttributeFilter<D extends IDescriptor> extends HierarchicalAttributeFilter<D, IModelLeaf<D>> implements IModelFilter<IModelLeaf<D>>
{
	private static final String S_ERR_MIN_DEPTH_WRONG = "The minimum depth must be equal to, or larger than zero";

	private int minDepth, maxDepth;

	public HierarchicalModelAttributeFilter( IAttributeFilter<IModelLeaf<D>> parentFilter, AttributeFilter<IModelLeaf<D>> childFilter)
			throws FilterException
	{
		this( parentFilter, childFilter, 0, -1 );
	}

	public HierarchicalModelAttributeFilter( IAttributeFilter<IModelLeaf<D>> parentFilter, AttributeFilter<IModelLeaf<D>> childFilter, int maxDepth)
			throws FilterException
	{
		this( parentFilter, childFilter, 0, maxDepth );
	}

	public HierarchicalModelAttributeFilter( IAttributeFilter<IModelLeaf<D>> parentFilter, AttributeFilter<IModelLeaf<D>> childFilter, int minDepth, int maxDepth)
			throws FilterException
	{
		super( Rules.OrChain, parentFilter, childFilter);
		if( minDepth < 0 )
			throw new IllegalArgumentException( S_ERR_MIN_DEPTH_WRONG);
		this.minDepth = minDepth;
		this.maxDepth = maxDepth;
	}

	public HierarchicalModelAttributeFilter(HierarchyRules rule,
			IAttributeFilter<IModelLeaf<D>> parentFilter, IAttributeFilter<IModelLeaf<D>> childFilter, int minDepth, int maxDepth ) throws FilterException
	{
		super( Rules.OrChain, parentFilter, childFilter);
		if( minDepth < 0 )
			throw new IllegalArgumentException( S_ERR_MIN_DEPTH_WRONG);
		this.minDepth = minDepth;
		this.maxDepth = maxDepth;
	}

	public HierarchicalModelAttributeFilter(HierarchyRules rule, AttributeFilter<IModelLeaf<D>> parentFilter, AttributeFilter<IModelLeaf<D>> childFilter)
			throws FilterException
	{
		this( rule, parentFilter, childFilter, 0, -1 );
	}

	public HierarchicalModelAttributeFilter(HierarchyRules rule, AttributeFilter<IModelLeaf<D>> parentFilter, AttributeFilter<IModelLeaf<D>> childFilter, int maxDepth)
			throws FilterException
	{
		this( rule, parentFilter, childFilter, 0, maxDepth );
	}
	
	@Override
	public Filters getType() {
		return Filters.ATTRIBUTES;
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
	protected boolean acceptEnabled( IModelLeaf<D> obj) throws FilterException {
		if( !ModelFilterWrapper.acceptDepth( minDepth, maxDepth, obj ))
			return false;
		return super.getParentFilter().accept(obj);
	}

	@Override
	public boolean acceptChild( IModelLeaf<D> child) {
		HierarchyRules rules = super.getHierarchyRule();
		boolean retval = false;
		switch( rules ){
		case ALLCHILDREN:
			retval = ModelFilterWrapper.acceptDepth( minDepth, maxDepth, child );
			break;
		case ALLPARENTS:
			retval =  ModelFilterWrapper.acceptDepth( minDepth, maxDepth, child ) && super.getChildFilter().accept((IModelLeaf<D>) child );
			break;
		case AS_IS:
			break;
		}
		return retval;
	}

	@Override
	public String getReference() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}
}
