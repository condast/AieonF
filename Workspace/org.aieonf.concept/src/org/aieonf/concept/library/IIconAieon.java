package org.aieonf.concept.library;

import org.aieonf.concept.IConcept;

public interface IIconAieon extends IConcept
{

  public enum Attributes{
  	File,
  	IconFile
  }

	/**
	 * Get the icon file location
	 * @return
	 */
	public abstract String getIconFile();

}