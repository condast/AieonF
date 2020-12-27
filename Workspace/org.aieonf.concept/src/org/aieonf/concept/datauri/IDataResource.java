package org.aieonf.concept.datauri;

public interface IDataResource
{
	public enum Attribute{
		TYPE,
		RESOURCE,
		IS_DATA_URI
	}

	public static final String S_ICON = "ICON";
	
	/**
	 * Get the type of this Data URI (e.g. ICON)
	 * @return
	 */
	public String getType();
		
	/**
	 * Get the resource
	 * @return
	 */
	public String getResource();

	/**
	 * Fill the Data URI with the given datauri string
	 * @param type
	 * @param resource
	 */
	void fill(String type, String resource);	
}
