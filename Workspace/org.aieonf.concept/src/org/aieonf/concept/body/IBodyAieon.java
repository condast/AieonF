package org.aieonf.concept.body;

import org.aieonf.concept.*;
import org.aieonf.concept.core.ConceptException;

public interface IBodyAieon extends IDescriptor
{
	//Supported error messages
  public static final String S_ERR_INVALID_LOADER =
    "The loader is not complete: ";
	
	/**
   * The body interface defines a body class that is used to
   * create the body
   * @return Class
   * @throws ConceptException
   * @throws ClassNotFoundException
   */
   public Class<?> getBodyClass()
     throws ConceptException, ClassNotFoundException;
   
   /**
    * A body requires a specific aieon for the constructor
     * @return Class
    * @throws ConceptException
    * @throws ClassNotFoundException
    */
    public Class<? extends IDescriptor> getAieonCreatorClass()
      throws ConceptException, ClassNotFoundException;
    
    /**
     * Every body aieon should test that all the required data
     * is actually available. Returns true if this is the case
     * @throws ConceptException if the verification failed
    */
    public void verify() throws NullPointerException;
}
