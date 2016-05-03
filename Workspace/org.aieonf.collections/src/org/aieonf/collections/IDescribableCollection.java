/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Condast</p>
 * @author Kees Pieters
 * @version 1.0
*/
package org.aieonf.collections;

//J2SE imports
import java.util.Collection;

import org.aieonf.commons.filter.IFilter;
import org.aieonf.concept.*;

/**
 * Defines the base interface to read concepts from the database.
 * The database is identified by a location (URI)
*/
public interface IDescribableCollection<T extends IDescribable<?>> extends Collection<T>
{
  //The error messages
  //The error messages
	public static final String S_ERR_COLLECTION_NOT_OPEN_EXCEPTION = 
			"Could not open the collection";
	public static final String S_ERR_CONCEPT_DOES_NOT_EXIST =
    "The requested concept does not exist: ";
  public static final String S_ERR_INVALID_VERSION = "Invalid version";
  
  public static final String S_ERR_COULD_NOT_ADD_CONCEPT =
    "Could not add concept. It may already exist";
  public static final String S_ERR_COULD_NOT_UPDATE_CONCEPT =
    "Could not update concept";
  public static final String S_ERR_COULD_NOT_ADD_CONCEPT_EXISTS =
    "Could not add concept, because it already exists";
  public static final String S_ERR_COULD_NOT_UPDATE_CONCEPT_NOT_EXISTS =
    "Could not update concept, because it does not exist. Please add it first: ";
  public static final String S_ERR_COULD_NOT_UPDATE_CONCEPT_TOO_MANY =
    "Could not update concept, because more than one descriptor was found: ";
  public static final String S_ERR_ADD_DOCUMENT_BUFFER =
    "Attempt to add a document that  is already contained in the collection";
  public static final String S_ERR_UPDATE_MANIFEST =
    "Could not update the manifest file. The collection may be corrupted";
  public static final String S_ERR_UPDATE_MANIFEST_NOT_SUPPORTED =
    "Could not update the manifest file. The collection does not support this";

  /**
   * The three strategies for replacement. 
   * If true, a concept may be replaced if it exists
   * If false, a concept may not be replaced if it exists
   * If IgnoreNotExist, a concept may be added if it does not exist
   * @author Kees Pieters
   *
   */
  public enum ReplaceOptions
  {
  	TRUE,
  	FALSE,
  	IGNORENOTEXIST,
  	REMOVE
  }

	/**
	 * If true, the given key is used by the parser to create a unique location. This is usually based on the
	 * descriptor data {name, id, version}. These can thus be retrieved without opening the content
	 */
	public boolean canParseFromDescriptor( String key );

	/**
   * Get all the concepts that pass the given filter. If descriptorOnly is set, then the
   * collection allows some checks without opening the body of the concepts. This speeds up
   * the checks
   *
   * @param filter Filter
   * @param descriptorOnly boolean
   * @return List<IConcept>
   * @throws CollectionException
  */
  public Collection<T> search( IFilter<T> filter, boolean descriptorOnly ) throws CollectionException;

  /**
   * Get the descriptors of this collection
   *
   * @return Collection<IDescriptor>
   * @throws CollectionException
  */
  public Collection<IDescriptor> getDescriptors() throws CollectionException;

  /**
   * Returns true if the given descriptor is acceptable for storage in the collection
   * By default it checks if the descriptor details (name, id, version) are filled in and
   * the version is zero or greater.
   * @param descriptor
   * @return
   */
  public boolean accept( IDescribable<?> changeable );
  
  /**
   * Set the concept. Can overwrite equivalent concepts
   * already stored in the collection. Returns false if the concept
   * could not be set.
   *
   * @param concept IConcept
   * @return boolean
   * @throws CollectionException
  */
  public boolean set( T concept ) throws CollectionException;

  /**
   * Set the concepts in the collection. Depending on the replace options, equivalent concepts
   * already stored in the collection can be overwritten. Returns false if one or more of concept
   * could not be set.
   *
   * @param concept IConcept
   * @return boolean
   * @throws CollectionException
  */
  public boolean setAll( Collection<T> concept, ReplaceOptions replace ) throws CollectionException;

  /**
   * Remove a concept from the database
   *
   * @param descriptor IDescriptor
   * @return List<IConcept>
   * @throws CollectionException
  */
  public Collection<T> remove( IDescriptor descriptor ) throws CollectionException;
}
