package org.aieonf.concept.context;

import org.aieonf.concept.core.ConceptException;

public interface IContextAieonFactory<T extends IContextAieon>
{
	/**
	 * get the context aieon
	 * @return
	 * @throws ConceptException
	 */
	public IContextAieon getContextAieon() throws ConceptException;
}
