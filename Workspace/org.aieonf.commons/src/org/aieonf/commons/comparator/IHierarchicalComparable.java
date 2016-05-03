package org.aieonf.commons.comparator;

import java.util.Collection;

public interface IHierarchicalComparable<T extends Comparable<?>> extends Comparable<T>
{

	/**
	 * Returns a negative number if the argument is smaller than the range, zero if equal and greater if
	 * larger. 
	 * @param Comparator
	 * @return
	 */
	public int compareRange( Comparable<?> target);

	/**
	 * Returns a negative number if the argument is smaller than the subrange, zero if equal and greater if
	 * larger. 
	 * @param Comparator
	 * @return
	 */
	public int compareSubrange(int level, Comparable<?> target);

	/**
	 * Returns true if the collection is in the range of the reference. 
	 * @param Comparator
	 * @return
	 */
	public boolean isInRange( Collection<Comparable<?>> targets);

	/**
	 * Returns a negative number if the argument is smaller than the subrange, zero if equal and greater if
	 * larger. 
	 * @param Comparator
	 * @return
	 */
	public boolean isInSubrange(int level, Collection<T> targets);

	/**
	 * Get the size of the ranges
	 * @return
	 */
	public int size();
}