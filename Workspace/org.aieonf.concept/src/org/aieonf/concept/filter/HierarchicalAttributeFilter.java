package org.aieonf.concept.filter;

import org.aieonf.commons.filter.FilterException;
import org.aieonf.commons.filter.HierarchicalFilter;
import org.aieonf.commons.filter.IAttributeFilter;
import org.aieonf.commons.filter.WildcardFilter;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;

public class HierarchicalAttributeFilter<T extends IDescriptor, M extends IDescribable> extends HierarchicalFilter<M>
{
	private String defaultParentRefVal;
	private String defaultChildRefVal;
	
	public HierarchicalAttributeFilter( IAttributeFilter<M> parentFilter, IAttributeFilter<M> childFilter)
			throws FilterException
	{
		this( Rules.OR_CHAIN, parentFilter, childFilter);
	}

	public HierarchicalAttributeFilter(Rules chainRule, IAttributeFilter<M> parentFilter,
			IAttributeFilter<M> childFilter) throws FilterException
	{
		super(chainRule, parentFilter, childFilter);
	}

	public HierarchicalAttributeFilter(HierarchyRules rule,
			IAttributeFilter<M> parentFilter, IAttributeFilter<M> childFilter) throws FilterException
	{
		this( Rules.OR_CHAIN, parentFilter, childFilter);
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
		AttributeFilter<T> parentFilter = (org.aieonf.concept.filter.AttributeFilter<T> )super.getParentFilter();
		this.defaultParentRefVal = parentFilter.refVal;
		AttributeFilter<T> childFilter = (org.aieonf.concept.filter.AttributeFilter<T> )super.getChildFilter();
		this.defaultChildRefVal = childFilter.refVal;
		switch( super.getHierarchyRule()){
			case ALLPARENTS:
				parentFilter.setValue( WildcardFilter.S_ALL );
				break;
			case ALLCHILDREN:
				childFilter.setValue( WildcardFilter.S_ALL );
				break;
			default:
			  if(( this.defaultParentRefVal != null ) && ( parentFilter.refVal.equals( WildcardFilter.S_ALL )))
			  		parentFilter.setValue( this.defaultParentRefVal );
			  if(( this.defaultChildRefVal != null ) && ( childFilter.refVal.equals( WildcardFilter.S_ALL )))
		  		childFilter.setValue( this.defaultChildRefVal );
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
