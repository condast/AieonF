/**
 * <p>Title: Condast Database</p>
 * <p>Description: Provides methods to connect to a database</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Condast BV</p>
 * @author Kees Pieters
 * @version 1.0
 */
package org.aieonf.util.config;

//J2SE imports

/**
 This method defines the configuration files
*/
public interface ConfigurationDefinition
{

  //The keys that are stored
  public static final String AUTHOR        = "author";
  public static final String COMMENTS      = "comments";
  public static final String COPYRIGHT     = "copyright";
  public static final String COUNTRY       = "country";
  public static final String DESCRIPTION   = "description";
  public static final String EXTENSION     = "extension";
  public static final String LANGUAGE      = "language";
  public static final String LOG_DIR       = "logDir";
  public static final String LOG_FILE      = "logFile";
  public static final String LOG_LEVEL     = "loglevel";
  public static final String LOOK_AND_FEEL = "lookandfeel";
  public static final String PRODUCT       = "product";
  public static final String ROOT          = "root";
  public static final String CONFIGURATION = "configuration";
  public static final String CONFIG        = "config";
  public static final String IMAGES        = "images";
  public static final String SPECURI       = "specuri";
  public static final String VERSION       = "version";

  //Look and feel
  public static final String LF_WINDOWS =
                       "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
  public static final String LF_MOTIF =
                       "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
  public static final String LF_METAL =
                       "javax.swing.plaf.metal.MetalLookAndFeel";
  public static final String XP_WINDOWS =
    "com.sun.java.swing.plaf.windows.XPStyle";

  /**
   * Gets the look and feel
   *
   * @return String
  */
  public String getLookAndFeel();

  /**
   * Set the look and feel
   *
   * @param lookandfeel String
  */
  public void setLookAndFeel( String lookandfeel );

  /**
   * Get the author name
   *
   * @return String author name
  */
  public String getAuthor();

  /**
   * Set the author name
   *
   * @param author String
  */
  public void setAuthor( String author );

  /**
   * Get the specification uri
   *
   * @return String specification uri
  */
  public String getSpecURI();

  /**
   * Set the specification uri
   *
   * @param specURI String
  */
  public void setSpecURI( String specURI );

  /**
   * Get the product name
   *
   * @return String product name
  */
  public String getProduct();

  /**
   * Sets the product name
   *
   * @param productName String
  */
  public void setProduct( String productName );

  /**
   * Get the version
   *
   * @return version int
  */
  public int getVersion();

  /**
   * Sets the version
   *
   * @param version int
  */
  public void setVersion( int  version );

  /**
   * Get the copyright notice
   *
   * @return String copyright
  */
  public String getCopyright();

  /**
   * Sets the copyright notice
   *
   * @param copyright String
  */
  public void setCopyright( String copyright );

  /**
   * Get the comments
   *
   * @return comments String
  */
  public String getComments();

  /**
   * Sets the comments
   *
   * @param comments String
  */
  public void setComments( String comments );

  /**
   * Get the description of the product
   *
   * @return String description
  */
  public String getDescription();

  /**
   * Set the description of the product
   *
   * @param description String
  */
  public void setDescription( String description );
}
