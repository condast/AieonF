package org.aieonf.concept.loader;

import java.io.File;
import java.net.URI;
import java.util.Collection;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.ConceptException;

public abstract class AbstractLocationManager<T extends IDescribable<?>> implements ILocationManager<T>
{
	//Error messages
  public static final String S_ERR_INVALID_NAME = "Invalid name: ";

  private ILoaderAieon loader;
  
  /**
   * Create a location manager from the given source
   * @param source 
  */
	public AbstractLocationManager( ILoaderAieon loader )
	{
		this.loader = loader;
	}

	/**
	 * @return the loader
	 */
	protected ILoaderAieon getLoader()
	{
		return loader;
	}


	/**
	 * Get the source of the location manager
	 * @return
	 */
	@Override
	public URI getSource(){
		return this.loader.getURI();
	}
	
	/**
	 * Get the path of the source
	 * @return
	 */
	@Override
	public String getSourcePath(){
		return this.loader.getURI().getPath();
	}
	
	/**
	 * Get the descriptor corresponding to the given location
	 * @param location
	 * @return
	 * @throws ConceptException
	*/
	@Override
	public abstract T getDescribable(String location) throws ConceptException;

	/**
	 * Locate the given descriptor
	 * @param descriptor IDescriptor
	 * @return
	 * @throws ConceptException
	*/
	@Override
	public abstract String locate( IDescribable<?> changeable ) throws ConceptException;

  /**
   * Returns true if the position corresponds with the name
   *
   * @param obj IDescriptor
   * @param position String
   * @return boolean
   * @throws ConceptException
  */
	@Override
  public boolean isCorrectPosition( IDescribable<?> obj, String position )
  	throws ConceptException
  {
    if( obj.getDescriptor().getID() == null )
    	return false;
    
  	String location = this.locate( obj );
  	return position.endsWith( location );
  }

  /**
   * Remove the file extension
   * concepts with the given name
   *
   * @param position String
   * @return String
  */
  protected String getRemoveExtension( String position )
  {
    File file = new File( position );

    String regexp = new String( "[\\.]");
    String[] split = file.getName().split( regexp );
    StringBuffer buffer = new StringBuffer();
    buffer.append( split[0] );
    for( int i=1; i < split.length - 1; i++ )
    	buffer.append("." + split[i] );
    return buffer.toString();
  }

  /**
   * Get the locations of the given collection of descriptors
   * @param descriptors
   * @return
   * @throws ConceptException
   */
  @Override
	public String[] getLocations( Collection<? extends IDescribable<?>> objects )
  	throws ConceptException
  {
  	String[] results = new String[ objects.size() ];
  	int index = 0;
  	for( IDescribable<?> obj: objects ){
  		results[ index ] = this.locate( obj );
  		index++;
  	}
  	return results;
  }

  /**
   * Returns true if the given position is contained in the array.
   * @param manager ILocationManager
   * @param position String
   * @param descriptors Collection<? extends IDescriptor>
   * @return boolean
  */
  public static boolean contains( ILocationManager<IDescriptor> manager, String location, Collection<? extends IDescriptor>descriptors )
  	throws ConceptException
  {
    for( IDescriptor descriptor: descriptors ){
      if( location.equals( manager.locate( descriptor ) ))
        return true;
    }
    return false;
  }
}