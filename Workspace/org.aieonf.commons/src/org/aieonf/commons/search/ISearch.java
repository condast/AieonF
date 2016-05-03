package org.aieonf.commons.search;

import java.util.Collection;

import org.aieonf.commons.filter.IFilter;

public interface ISearch<T extends Object> {

	/**
	 * Search for elements that are accepted by the given filter
	 * @param filter
	 * @return
	 */
	public Collection<T> search( IFilter<T> filter );
}
