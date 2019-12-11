package org.aieonf.concept.filter;

import org.aieonf.commons.filter.FilterException;
import org.aieonf.commons.filter.HierarchicalFilter;
import org.aieonf.commons.filter.IFilter;
import org.aieonf.commons.filter.WildcardFilter;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;

public class HierarchicalAttributeFilter<T extends IDescriptor, M extends IDescribable> extends HierarchicalFilter<M>
{
	private String defaultParentRefVal;
	private String defaultChildRefVal;
	
	public HierarchicalAttributeFilter(AttributeFilter<M> parentFilter, AttributeFilter<M> childFilter)
			throws FilterException
	{
		this( Rules.OrChain, parentFilter, childFilter);
	}

	public HierarchicalAttributeFilter(Rules chainRule, IFilter<M> parentFilter,
			IFilter<M> childFilter) throws FilterException
	{
		super(chainRule, parentFilter, childFilter);
	}

	public HierarchicalAttributeFilter(HierarchyRules rule,
			IFilter<M> parentFilter, IFilter<M> childFilter) throws FilterException
	{
		this( Rules.OrChain, parentFilter, childFilter);
	}

	/**
	 * @param rule the rule to set
	 */
	@Override
	public final void setHierarchyRule(HierarchyRules rule)
	{
		super.setHierarchyRule(rule);
		this.setFiltersForRule();
	}

	@SuppressWarnings("unchecked")
	protected void setFiltersForRule(){
		AttributeFilter<T> parentFilter = ( AttributeFilter<T> )super.getParentFilter();
		this.defaultParentRefVal = parentFilter.refVal;
		AttributeFilter<T> childFilter = ( AttributeFilter<T> )super.getChildFilter();
		this.defaultChildRefVal = childFilter.refVal;
		switch( super.getHierarchyRule()){
			case ALLPARENTS:
				parentFilter.setRefVal( WildcardFilter.S_ALL );
				break;
			case ALLCHILDREN:
				childFilter.setRefVal( WildcardFilter.S_ALL );
				break;
			default:
			  if(( this.defaultParentRefVal != null ) && ( parentFilter.refVal.equals( WildcardFilter.S_ALL )))
			  		parentFilter.setRefVal( this.defaultParentRefVal );
			  if(( this.defaultChildRefVal != null ) && ( childFilter.refVal.equals( WildcardFilter.S_ALL )))
		  		childFilter.setRefVal( this.defaultChildRefVal );
				break;
		}		
	}
	
	/* (non-Javadoc)
	 * @see org.condast.util.filter.AbstractFilter#prepareFilter()
	 */
	@Override
	protected void prepareFilter()
	{
		super.prepareFilter();
		this.setFiltersForRule();
	}
}
