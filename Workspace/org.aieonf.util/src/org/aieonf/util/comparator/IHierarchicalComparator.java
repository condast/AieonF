package org.aieonf.util.comparator;

import java.util.Comparator;

public interface IHierarchicalComparator<T extends Object> extends Comparator<T>
{

	/**
	 * Returns a negative number if the argument is smaller than the range, zero if equal and greater if
	 * larger. 
	 * @param Comparator
	 * @return
	 */
	public abstract int compareRange(Comparable<?> reference, Comparable<?> target);

	/**
	 * Returns a negative number if the argument is smaller than the subrange, zero if equal and greater if
	 * larger. 
	 * @param Comparator
	 * @return
	 */
	public abstract int compareSubrange(int level, Comparable<?> reference, Comparable<?> target);

	/**
	 * Get the size of the ranges
	 * @return
	 */
	public abstract int size();
}