package org.aieonf.concept.context;

import java.io.File;


public interface IContextAieonCreator
{

	/**
	 * Create an aieon for the given context aieon factory
	 * @param factory
	 * @param fileName
	 * @param useEncryption
	 */
	public void create( IContextAieonFactory<?> factory, File file, boolean useEncryption );
}
