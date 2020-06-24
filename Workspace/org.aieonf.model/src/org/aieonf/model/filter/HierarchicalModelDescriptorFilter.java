package org.aieonf.model.filter;

import org.aieonf.commons.filter.FilterException;
import org.aieonf.commons.filter.HierarchicalFilter;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.filter.DescriptorFilter;
import org.aieonf.concept.filter.FilterFactory.Filters;
import org.aieonf.model.core.IModelLeaf;

public class HierarchicalModelDescriptorFilter<D extends IDescriptor> extends HierarchicalFilter<IModelLeaf<D>> implements IModelFilter<IModelLeaf<D>>
{
	private static final String S_ERR_MIN_DEPTH_WRONG = "The minimum depth must be equal to, or larger than zero";

	private int minDepth, maxDepth;

	public HierarchicalModelDescriptorFilter(IDescriptor descriptor )
			throws FilterException
	{
		this( descriptor, descriptor, 0, -1 );
	}

	public HierarchicalModelDescriptorFilter(IDescriptor descriptor, int minDepth, int maxDepth )
			throws FilterException
	{
		this( descriptor, descriptor, minDepth, maxDepth);
	}

	public HierarchicalModelDescriptorFilter(IDescriptor parent, IDescriptor child, int minDepth, int maxDepth )
			throws FilterException
	{
		super( Rules.OrChain, new DescriptorFilter<IModelLeaf<D>>( DescriptorFilter.Rules.Equals, parent ), 
				new DescriptorFilter<IModelLeaf<D>>( DescriptorFilter.Rules.Equals, child ));
		if( minDepth < 0 )
			throw new IllegalArgumentException( S_ERR_MIN_DEPTH_WRONG);
		this.minDepth = minDepth;
		this.maxDepth = maxDepth;
	}

	public HierarchicalModelDescriptorFilter(HierarchyRules rule, IDescriptor parent, IDescriptor child, int minDepth, int maxDepth) throws FilterException
	{
		super( Rules.OrChain, new DescriptorFilter<IModelLeaf<D>>( DescriptorFilter.Rules.Equals, parent ), 
				new DescriptorFilter<IModelLeaf<D>>( DescriptorFilter.Rules.Equals, child ));
		if( minDepth < 0 )
			throw new IllegalArgumentException( S_ERR_MIN_DEPTH_WRONG);
		this.minDepth = minDepth;
		this.maxDepth = maxDepth;
	}

	public HierarchicalModelDescriptorFilter( HierarchyRules rule, IDescriptor descriptor, int minDepth, int maxDepth )
			throws FilterException
	{
		this( rule, descriptor, descriptor, minDepth, maxDepth);
		if( minDepth < 0 )
			throw new IllegalArgumentException( S_ERR_MIN_DEPTH_WRONG);
		this.minDepth = minDepth;
		this.maxDepth = maxDepth;
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
		if( ! ModelFilterWrapper.acceptDepth( minDepth, maxDepth, obj ))
			return false;
		return super.acceptEnabled(obj);
	}

	@Override
	public boolean acceptChild( IModelLeaf<D> child) {
		if(  ModelFilterWrapper.acceptDepth( minDepth, maxDepth, child ))
			return false;
		HierarchyRules rules = super.getHierarchyRule();
		boolean retval = false;
		switch( rules ){
		case ALLCHILDREN:
			retval = true;
			break;
		case ALLPARENTS:
			retval = super.getChildFilter().accept( (IModelLeaf<D>) child );
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
