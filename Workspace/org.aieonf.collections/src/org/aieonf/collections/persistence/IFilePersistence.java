package org.aieonf.collections.persistence;

import java.io.File;

import org.aieonf.commons.persistence.IPersistence;
import org.aieonf.concept.IDescribable;

public interface IFilePersistence<T extends IDescribable> extends IPersistence<T>
{

	/**
	 * @param file the file to set
	 */
	public abstract void setFile(File file);

}