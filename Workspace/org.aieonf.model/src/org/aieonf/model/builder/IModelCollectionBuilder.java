package org.aieonf.model.builder;

import java.util.Collection;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;

public interface IModelCollectionBuilder<T extends IDescriptor, M extends IDescribable<T>> {

	/**
	 * Build a model
	 * @return
	 */
	public Collection<M> build();
	
	/**
	 * Returns true if the build has completed
	 * @return
	 */
	public abstract boolean isCompleted();
}
