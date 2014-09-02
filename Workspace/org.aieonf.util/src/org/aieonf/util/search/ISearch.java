package org.aieonf.util.search;

import java.util.Collection;

import org.aieonf.util.filter.IFilter;

public interface ISearch<T extends Object> {

	/**
	 * Search for elements that are accepted by the given filter
	 * @param filter
	 * @return
	 */
	public Collection<T> search( IFilter<T> filter );
}
