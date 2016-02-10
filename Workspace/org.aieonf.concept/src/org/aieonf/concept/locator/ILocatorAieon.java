package org.aieonf.concept.locator;

import java.net.URI;

import org.aieonf.concept.IDescriptor;
import org.aieonf.util.StringStyler;

public interface ILocatorAieon extends IDescriptor
{
	/**
   * A locator is identified by its location and a name
  */
  public enum Attributes
  {
    LOCATOR,
    LOCATION,
    IDENTIFIER,
    URI;
    
	@Override
	public String toString() {
		return StringStyler.prettyString( super.toString());
	}

	public static boolean isValid( String str ){
		for( Attributes attr: values() ){
			if( attr.name().equals( str ))
				return true;
		}
		return false;
	}

  }

	/**
	 * A loader aieon is characterised by an identifier
	 * @return
	*/
	public String getIdentifier();

	/**
	 * Get the source
	 * @return
	*/
	public String getSource();

	/**
	 * Set the identifier
	 * @param identifier
	 * @throws ConceptException
	*/
	public void setIdentifier( String identifier );
	
	/**
	 * A loader aieon is characterised by a source
	 * @return
	*/
	public URI getURI();
	
  /**
   * Set the source
   *
   * @param source String
  */
  public void setURI( URI source );
}
