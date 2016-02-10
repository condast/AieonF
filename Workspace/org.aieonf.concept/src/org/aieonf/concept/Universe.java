/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Condast BV</p>
 * @author Kees Pieters
 * @version 1.0
 */
package org.aieonf.concept;

/**
 * Defines a universe, the elements of a location that are considered
 * valid identifiers for a group of users. An instance of an Universe is
 * defined as:
 * cpt.<...>...<org, com, org, ...>.<group name>.<address>.<directory><...>
*/
public interface Universe
{

  /**
   * A Universe consists of the following identifiers
  */
  public final String UNIVERSE    = "Universe";
  public final String ENVIRONMENT = "Environment";  //org, com, net,...
  public final String GROUP       = "Group";
  public final String ADDRESS     = "Address";
  public final String SPECIFIER   = "Specifier";
  public final String DIRECTORY   = "Directory";

  /**
   * The types of universe currently supported
  */
  public final String CPT    = "cpt"; // A concept universe
  public final String CDX    = "cdx"; // Concept Database Extension
  public final String WWW    = "www"; // World wide Web

  /**
   * The types of environment currently supported. also country codes
   * could be included or used
  */
  public final String ORG = "org";
  public final String COM = "com";
  public final String NET = "net";
  public final String BIZ = "biz";

  //optionally, a country and locale / culture can be identified
  public final String COUNTRY = "Country";
  public final String LOCALE  = "Locale";
  public final String CULTURE = "Culture";

  /**
   * The addresses supported by default
  */
  public final String LOCAL    = "local"; // alocal address

  /**
   * Get the universe
   *
   * @return String
  */
  public String getUniverse();

  /**
   * Get the environment
   *
   * @return String
  */
  public String getEnvironment();

  /**
   * Get the group
   *
   * @return String
  */
  public String getGroup();

  /**
   * Get the address
   *
   * @return String
  */
  public String getAddress();

  /**
   * Get the specifier
   *
   * @return String
  */
  public String getSpecifier();

  /**
   * Get the country. As default, the country code of the application
   * or OS should be used
   *
   * @return String
  */
  public String getCountry();

  /**
   * Get the locale. As default, the locale of the application
   * or OS should be used
   *
   * @return String
  */
  public String getLocale();

  /**
   * Get the locale. As default, the culture is the locale
   *
   * @return String
  */
  public String getCulture();
}
