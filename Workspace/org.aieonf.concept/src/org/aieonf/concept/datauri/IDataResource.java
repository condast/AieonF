package org.aieonf.concept.datauri;

public interface IDataResource
{
	public enum Attribute{
		Type,
		Resource
	}

	public static final String S_ICON = "ICON";
	
	/**
	 * Fill the Data URI with the given datauri string
	 * @param datauri
	 */
	public void fill( String type, String resource );

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
}
