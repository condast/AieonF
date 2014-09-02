package org.aieonf.concept.loader;

import java.net.URI;
import java.util.Collection;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.core.ConceptException;

/**
 * Default interface for translating a descriptor to a location
 * @author Kees
 *
 */
public interface ILocationManager< T extends IDescribable<?>>
{	
	/**
	 * Get the source of the location manager
	 * @return
	 */
	public URI getSource();
	
	/**
	 * Get the path of the source
	 * @return
	 */
	public String getSourcePath();
	
	/**
	 * Locate the given object
	 * @param descriptor IDescriptor
	 * @return
	 * @throws ConceptException
	*/
	public String locate( IDescribable<?> changeable ) throws ConceptException;
	
	/**
	 * Get the changeable object corresponding to the given location
	 * @param location
	 * @return
	 * @throws ConceptException
	*/
	public T getDescribable( String location ) throws ConceptException;

  /**
   * Returns true if the position corresponds with the name
   *
   * @param descriptor IDescriptor
   * @param position String
   * @return boolean
   * @throws ConceptException
  */
  public boolean isCorrectPosition( IDescribable<?> changeable, String position )
  	throws ConceptException;

  /**
   * Get the locations of the given collection of descriptors
   * @param descriptors
   * @return
   * @throws ConceptException
   */
  public String[] getLocations( Collection<? extends IDescribable<?>> descriptors )
  	throws ConceptException;
	
}
