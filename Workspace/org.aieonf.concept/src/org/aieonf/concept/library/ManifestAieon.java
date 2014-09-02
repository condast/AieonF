package org.aieonf.concept.library;

//Concept
import java.net.MalformedURLException;
import java.net.URI;

import org.aieonf.concept.*;
import org.aieonf.concept.body.BodyFactory;
import org.aieonf.concept.core.*;
import org.aieonf.concept.loader.LoaderAieon;
import org.aieonf.concept.security.AieonFEncryption;
import org.aieonf.concept.xml.StoreConcept;
import org.aieonf.util.encryption.IEncryption;

/**
 * <p>Title: Conceptual Network Database</p>
 *
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Condast</p>
 *
 * @author Kees Pieters
 * @version 1.0
*/
public class ManifestAieon extends LoaderAieon
{
  /**
   * For serialization purposes
   */
  private static final long serialVersionUID = 4750218318581610568L;

  //The manifest aieon
  public static final String MANIFEST = "manifest";
  public static final String LOCATION = "META-INF/MANIFEST.CPT";

  /**
   * The Access types
  */
  public enum AccessType
  {
    Blocked,
    System,
    Private,
    Public
  }

  /**
   * Create a default manifest aieon
   * @throws ConceptException
  */
  public ManifestAieon() throws ConceptException
  {
    super();
    super.setName( MANIFEST );
  }

  /**
   * Create a descriptor for the given data
   *
   * @param identifier String
   * @param source String
   * @throws ConceptException
   * @throws MalformedURLException 
  */
  public ManifestAieon( String identifier, URI source ) throws ConceptException, MalformedURLException
  {
    super( identifier, source );
    super.setName( MANIFEST );
  }

  public ManifestAieon( IDescriptor descriptor )
  {
  	super( descriptor );
    super.setName( MANIFEST );
  }

  public void fill( IDescriptor descriptor ) throws ConceptException{
  	BodyFactory.transfer(this, descriptor, false);
    super.setName( MANIFEST );
  }
  
  /**
   * Sign the manifest
   *
   * @param applicationKey String
   * @throws Exception
  */
  public void sign( ) throws Exception
  {
    IEncryption encryption = new AieonFEncryption( this );
    StoreConcept store = new StoreConcept( this );
    String signature =
      encryption.encryptData( store.createDocument().toString() );
    this.set( IDescriptor.Attributes.SIGNATURE, signature );
  }
}
