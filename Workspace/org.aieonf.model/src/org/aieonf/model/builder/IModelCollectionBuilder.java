package org.aieonf.model.builder;

import java.util.Collection;

import org.aieonf.concept.IDescribable;

public interface IModelCollectionBuilder<M extends IDescribable> {

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
