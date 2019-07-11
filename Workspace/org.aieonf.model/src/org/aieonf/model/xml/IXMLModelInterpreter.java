package org.aieonf.model.xml;

import java.io.InputStream;

import org.aieonf.commons.strings.StringStyler;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.xml.sax.Attributes;

public interface IXMLModelInterpreter<T extends IDescriptor, U extends IDescribable<T>> {

	/**
	 * The keys of attributes with special meaning
	 * @author Kees
	 *
	 */
	public enum Keys{
		CLASS;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
	}

	/**
	 * Get an identifier for this interpreter. This is a human readable
	 * name that can be used to follow the parsing.
	 * @return
	 */
	public String getIdentifier();
	
	/**
	 * Clear the creator
	 */
	void clear();

	/**
	 * Get the InputStream of the template
	 * @return
	 */
	public InputStream getInputStream();	
	
	/**
	 * Returns true if the creator is active
	 * @return
	 */
	public boolean isActive();
	
	/**
	 * Returns true if the name is valid
	 * @return
	 */
	public boolean isValid( String name );
	
	/**
	 * Returns true if the descriptor with the given name or attributes can be created
	 * @param attributes
	 * @return
	 */
	public boolean canCreate( String name, Attributes attributes );
	
	/**
	 * Create a model from the given attributes
	 * @param attributes
	 * @return
	 */
	public U create( String name, Attributes attributes );

	/**
	 * Get the currently selected descriptor
	 * @return
	 */
	public U getModel();

	/**
	 * Set the property with the given value
	 * @param key
	 * @param attr
	 * @return
	 */
	public boolean setProperty( String key, Attributes attr );

	/**
	 * Set the value of the selected property
	 * @param key
	 * @return
	 */
	public boolean setValue( String value );

	/**
	 * When the property is parsed, this method is called to clean up the code
	 */
	public void endProperty();
	
	/**
	 * Get the key that currently is being processed
	 * @return
	 */
	public String getKey();
}
