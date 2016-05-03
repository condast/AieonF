package org.aieonf.concept.persist;

import org.aieonf.commons.persistence.IPersistence;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.loader.ILoaderAieon;

public interface ILocatedPersistence<T extends IDescribable<?>> extends
		IPersistence<T>
{
	/**
	 * Concert the given concept to a byte string, using this form of persistence
	 * @param loader
	 * @param describable
	 * @return
	 */
	public byte[] toBytes( ILoaderAieon loader, T describable );

}
