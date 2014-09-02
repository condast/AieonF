package org.aieonf.concept.filter;

import org.aieonf.concept.IDescribable;
import org.aieonf.util.StringStyler;
import org.aieonf.util.filter.FilterException;
import org.aieonf.util.filter.HierarchicalFilter;
import org.aieonf.util.filter.IFilter;
import org.aieonf.util.filter.WildcardFilter;

public class HierarchicalAttributeFilter<T extends IDescribable<?>> extends HierarchicalFilter<T>
{
	public enum HierarchyRules{
		ALLPARENTS,
		ALLCHILDREN,
		AS_IS;

		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString()
		{
			return StringStyler.prettyString( super.toString() );
		}
	}
	private HierarchyRules rule;
	
	private String defaultParentRefVal;
	private String defaultChildRefVal;
	

	public HierarchicalAttributeFilter(AttributeFilter<T> parentFilter, AttributeFilter<T> childFilter)
			throws FilterException
	{
		this( Rules.OrChain, parentFilter, childFilter);
	}

	public HierarchicalAttributeFilter(Rules chainRule, IFilter<T> parentFilter,
			IFilter<T> childFilter) throws FilterException
	{
		super(chainRule, parentFilter, childFilter);
		rule = HierarchyRules.AS_IS;
	}

	public HierarchicalAttributeFilter(HierarchyRules rule,
			IFilter<T> parentFilter, IFilter<T> childFilter) throws FilterException
	{
		this( Rules.OrChain, parentFilter, childFilter);
		this.rule = rule;
	}

	/**
	 * @return the rule
	 */
	public final HierarchyRules getHierarchyRule()
	{
		return rule;
	}

	/**
	 * @param rule the rule to set
	 */
	public final void setHierarchyRule(HierarchyRules rule)
	{
		this.rule = rule;
		this.setFiltersForRule();
	}

	protected void setFiltersForRule(){
		AttributeFilter<T> parentFilter = ( AttributeFilter<T> )super.getParentFilter();
		this.defaultParentRefVal = parentFilter.refVal;
		AttributeFilter<T> childFilter = ( AttributeFilter<T> )super.getChildFilter();
		this.defaultChildRefVal = childFilter.refVal;
		switch( rule ){
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
