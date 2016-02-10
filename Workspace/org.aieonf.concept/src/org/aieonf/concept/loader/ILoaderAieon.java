package org.aieonf.concept.loader;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.locator.ILocatorAieon;
import org.aieonf.concept.security.IEncryptionAieon;
import org.aieonf.util.StringStyler;

public interface ILoaderAieon extends ILocatorAieon, IEncryptionAieon, IConcept
{
  /**
   * The supported attributes for this aieon
   * @author Kees Pieters
   */
  public static enum Attributes
  {
    LOADER,
    TYPE,
    BODY_CLASS,
    AIEON_CREATOR_CLASS,
    INTERNAL,
    CREATABLE;
    
	@Override
	public String toString() {
		return StringStyler.prettyString( super.toString() );
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
   * Returns true if the loader points to a source that is read only
   * @return
   */
  @Override
public boolean isReadOnly();
  
  /**
   * Set the aieon class that is needed to create the body
   *
   * @param clss Class
  */
  public void setAieonCreatorClass( Class<? extends IDescriptor> clss );

  /**
   * Returns true if loader is stored internally in the object that is loaded
   * @return
  */
  public boolean getStoreInternal();
  
  /**
   * Set the internal flag. If true, the loader is stored internally, for instance
   * as a manifest aieon;
   * @param choice
   */
  public void setStoreInternal( boolean choice );

  /**
   * Returns true if the loader can create the loaded object
   * @return
   */
  public boolean isCreatable();
  
  /**
   * If true, the loader can create the object that is loaded
   * @param choice
   */
  public void setCreatable( boolean choice );
    
  public void verify() throws NullPointerException;
}