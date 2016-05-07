/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Condast BV</p>
 * @author Kees Pieters
 * @version 1.0
*/
package org.aieonf.orientdb;

import java.io.File;
import java.net.URI;

import org.aieonf.concept.Universe;
import org.aieonf.concept.context.ContextAieon;
import org.aieonf.concept.context.IContextAieon;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;

/**
 * Create a concept, using a properties file
 *
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: Condast</p>
 *
 * @author Kees Pieters
 * @version 1.0
*/
public class OrientGraphContextAieon extends ContextAieon
{
  /**
   * Serialization
   */
  private static final long serialVersionUID = -3648972713512835462L;
  
  public static final String S_AIEONF_DOMAIN = "AieonF";

  /**
   * Get the configuration source
   *
   * @return String
  */
  public URI getConfigSource()
  {
  	if( this.getApplicationName() == null )
  		throw new NullPointerException( S_ERR_NO_APPLICATION_NAME );
  	if( this.getOrganisation() == null )
  		throw new NullPointerException( S_ERR_NO_MANUFACTURER_NAME );
  	File file = new File( OrientGraphContextAieon.getDefaultApplicationDir( this ));
  	file = new File( file, this.getName() + "." + Universe.CPT );
  	return file.toURI();
   }


	/**
	 * Get the default application directory. This is '.\config\<organisation>\'
	 * @param aieon
	 * @return
	 */
	public static URI getDefaultApplicationDir( IContextAieon aieon )
	{
		Location location = Platform.getConfigurationLocation();
		File file = new File( location.getURL().getPath() );
		file = new File( file, File.separator +	aieon.getSource() + File.separator );
		return file.toURI();
	}
}