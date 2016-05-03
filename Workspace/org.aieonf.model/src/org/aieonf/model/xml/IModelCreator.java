package org.aieonf.model.xml;

import java.net.URL;

import org.aieonf.commons.strings.StringStyler;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelLeaf;
import org.xml.sax.Attributes;

public interface IModelCreator<T extends IDescriptor, U extends IModelLeaf<T>> {

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
	 * Clear the creator
	 */
	void clear();

	/**
	 * Get the location of the template
	 * @return
	 */
	public String getLocation();

	/**
	 * Get the URL of the template
	 * @return
	 */
	public URL getURL();	
	
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
	 * End filling in the property
	 * @param key
	 */
	public void endProperty( Enum<?> key);
	
	/**
	 * Notification that a descriptor was created
	 * @param event
	 */
	public void notifyDescriptorCreated( ModelCreatorEvent event );

}
