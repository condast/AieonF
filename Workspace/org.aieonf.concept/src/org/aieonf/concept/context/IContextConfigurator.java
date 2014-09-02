package org.aieonf.concept.context;

import org.aieonf.concept.core.ConceptException;

/**
 * The application configurator is defined by an
 * application aien that contains all the necessary
 * info to create the application aieon
 * @author Kees Pieters
 */
public interface IContextConfigurator<T extends IContextAieon> 
{
	/**
	 * Defines the access modes of the configurator:
	 * -Read; implies that the configurator can only read a context aieon
	 * - Modify: the context aieon can be modified and read
	 * - Create: the context aioen can be created, modified and read
	 * @author keesp
	 *
	*/
	public enum AccessType{
		Read,
		Modify,
		Create
	}

	/**
	 * Get the access type of the configurator:
	 * -Read; implies that the configurator can only read a context aieon
	 * - Modify: the context aieon can be modified and read
	 * - Create: the context aioen can be created, modified and read
	 * @return
	 */
	public AccessType getAccess();
	
	/**
	 * Set the access type for the configurator
	 * @param access
	 */
	public void setAccess( AccessType access );
	
	/**
	 * Get the application aien that is used to load the application
	 * @return
	 * @throws Exception
	*/
	public IContextAieon getApplicationAieon() 
		throws ConceptException;
}
