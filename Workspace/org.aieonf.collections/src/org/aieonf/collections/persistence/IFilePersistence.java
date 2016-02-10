package org.aieonf.collections.persistence;

import java.io.File;

import org.aieonf.concept.IDescribable;
import org.aieonf.util.persistence.IPersistence;

public interface IFilePersistence<T extends IDescribable<?>> extends IPersistence<T>
{

	/**
	 * @param file the file to set
	 */
	public abstract void setFile(File file);

}