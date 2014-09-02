package org.aieonf.concept.security;

import org.aieonf.util.encryption.IEncryption;

/**
 * Designates a concept that contains a key and algorithm for encryption.
 * These concepts shall normally only be used locally in an application
 * @author Kees Pieters
 *
 */
public interface IEncryptionAieon 
{
	/**
	 * Get the key used for encryption
	 * @return
	*/
	public String getEncryptionKey();
	
	/**
	 * Get the algorithm that is used for encryptn 
	 * @return
	*/
	public IEncryption.Algorithms getEncryptionAlgorithm();
}
