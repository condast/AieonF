package org.aieonf.concept.datauri;

import org.aieonf.commons.strings.StringStyler;
import org.aieonf.concept.IConcept;

public interface IDataURI extends IDataResource, IConcept{
	public enum Attribute{
		MIME_TYPE,
		CHARSET,
		ENCODING;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
	}

	/**
	 * Set the uri
	 * @param uri
	 */
	public void setURI( String uri );

	/**
	 * Get the mimetype (e.g. images/png), or null if none is
	 * provided
	 * @return
	 */
	public String getMimeType();

	/**
	 * Get the mime type extension
	 * @return
	 */
	public String getMimeTypeExtension();

	/**
	 * Get the charset (e.g. UTF-8) or null if none is provided
	 * @return
	 */
	public String getCharset();

	/**
	 * Get the encoding (e.g. base64) or null if none is provided
	 * @return
	 */
	public String getEncoding();
	
	/**
	 * Returns true if the encoding is base64
	 * @return
	 */
	public boolean isBase64Encoded();

	@Override
	public void fill(String string, String resource);
}
