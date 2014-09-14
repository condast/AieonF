/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Condast</p>
 * @author Kees Pieters
 * @version 1.0
*/
package org.aieonf.collections;

import org.aieonf.concept.library.ManifestAieon;
import org.aieonf.concept.util.IDescribableSet;

/**
 * Defines the base interface to read concepts from the collection.
 * The collection is identified by a location (URI)
*/
public interface IAccessible extends IDescribableSet<ManifestAieon>
{
  //The error messages
  public static final String S_ERR_NO_MANIFEST_STORED_INTERNALLY =
      "The manifest should be stored internally, but was not found";
  public static final String S_ERR_NO_MANIFEST_CREATED =
      "The manifest was not created";
  public static final String S_ERR_INCORRECT_ENTRY_NAME_MANIFEST =
    "The given entry name, 'manifest' is not allowed in this collection";
  public static final String S_ERR_INVALID_ENTRY =
      "The given entry name is not allowed in this collection: ";
  public static final String S_ERR_DOUBLE_DESCRIPTOR =
      "The given descriptor is already present in this collection: ";
  public static final String S_ERR_COLLECTION_NOT_OPEN_EXCEPTION =
    "The model database is not open for retrieval: ";
  public static final String S_ERR_MANIFEST_NOT_FOUND =
    "The manifest file was not found. The collection may be corrupted";
  public static final String S_ERR_UPDATE_MANIFEST_NOT_FOUND =
    "The manifest file cannot be updated because it was not loaded: ";
  public static final String S_ERR_ALREADY_PRESENT =
    "Could not add the object as it is already present: ";
  public static final String S_ERR_INVALID_ENTRYNAME =
  	"The provided entry name is invalid. Should contain name:id:version: ";
  public static final String S_ERR_SECURITY_LOADER_EXCEPTION =
      "Failed to open the collection as the loader does not match the manifest: ";

  /**
   * Try to access the collection. Returns false if the attempt failed
   *
  */
  public boolean access( );

  /**
   * Returns true if the database can be accessed
   *
   * @return boolean
  */
  public boolean canAccess();
  
  /**
   * Close access to the database
   *
  */
  public void leave();

  /**
   * Update manifest.
   */
	public void updateManifest(ManifestAieon manifest) throws CollectionException;
}
