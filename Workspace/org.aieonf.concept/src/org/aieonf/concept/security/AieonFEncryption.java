package org.aieonf.concept.security;

//J2SE imports
import java.security.*;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.*;

import org.aieonf.commons.encryption.Encryption;
import org.aieonf.commons.encryption.IEncryption;

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
public class AieonFEncryption extends Encryption implements IEncryption
{
  /**
   * Get the encryption for the given loader
   * 
   * @param loader
   * @throws NoSuchPaddingException
   * @throws NoSuchAlgorithmException
 * @throws InvalidKeySpecException 
   */
  public AieonFEncryption( IEncryptionAieon aieon ) 
  	throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException
  {
  	super( aieon.getEncryptionAlgorithm(), aieon.getEncryptionKey() );	
  }
}
