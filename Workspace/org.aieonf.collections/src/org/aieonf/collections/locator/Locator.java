package org.aieonf.collections.locator;

//Third party
//import org.apache.log4j.*;
//import org.condast.util.logger.*;

//Concept
import java.net.MalformedURLException;
import java.net.URI;

import org.aieonf.collections.CollectionException;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.locator.ILocatorAieon;
import org.aieonf.concept.locator.LocatorAieon;

/**
 * <p>Title: Conceptual Network Database</p>
 *
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p> A locator object is pointer to a database. The location and name
 * together uniquely locate the database. The identifier is a string,
 * such as an URL, that can be used identify the database in a generic manner
 * be used
 * </p>
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: Condast BV</p>
 *
 * @author Kees Pieters
 * @version 1.0
*/
public abstract class Locator
{

  //Supported error messages
  public static final String S_COULD_NOT_DELETE_DATABASE_ERR =
    "could not delete database: ";
  public static final String S_COULD_NOT_CREATE_DATABASE_ERR =
    "could not create database: ";
  public static final String S_DATABASE_DOES_NOT_EXIST_ERR =
    "Database does not exist: ";
  public static final String S_DATABASE_EXISTS_ERR =
    "Database already exist: ";
  public static final String S_INVALID_ROOT_ERR =
    "The root file is not valid for this database type: ";
  public static final String S_COULD_NOT_OPEN_DATABASE_ERR =
    "could not open the database: ";

  /**
   * A locator is identified by its location and a name
  */
  private ILocatorAieon aieon;

  //A locator has to be opened and closed in order to access the content
  private boolean open;

  //Get the logger
  //private Logger logger;

  /**
   * Create a locator
   *
   * @param identifier String
   * @param name String
   * @param location String
   * @throws ConceptException
   * @throws MalformedURLException 
  */
  public Locator( String identifier, String name, URI location )
    throws ConceptException, MalformedURLException
  {
    super();
    this.aieon = new LocatorAieon( name, identifier, location );
   }

   /**
    * Create a locator from the given aieon
    *
    * @param aieon LocatorAieon
  */
   public Locator( ILocatorAieon aieon )
  {
    super();
    this.aieon = aieon;
  }

  /**
   * Get the identifier of the database. Should be unique, and must be used to
   * retrieve the database, for instnace a file location
   *
   * @return String
  */
  public String getIdentifier()
  {
    return this.aieon.getIdentifier();
  }

  /**
   * Get the locator aieon;
   * @return LocatorAieon
  */
  public ILocatorAieon getLocatorAieon()
  {
    return this.aieon;
  }

  /**
   * Create the database
   *
   * @throws CollectionException
  */
  public abstract void create() throws CollectionException;

  /**
   * Open the database
   * @throws CollectionException
  */
  public void open() throws CollectionException
  {
    this.open = true;
  }

  /**
   * If true, the database is open
   * @return boolean
  */
  public boolean isOpen()
  {
    return open;
  }

  /**
   * Close the database
   * @throws CollectionException
  */
  public void close() throws CollectionException
  {
    this.open = false;
  }
}
