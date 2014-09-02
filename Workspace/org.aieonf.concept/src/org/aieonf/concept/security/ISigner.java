package org.aieonf.concept.security;

import java.io.IOException;

import org.aieonf.concept.IConcept;

/**
 * A signer provides methods to sign and verify concepts.
 * @author Kees Pieters
 */
public interface ISigner 
{
	/**
	 * Sign a concept 
	 * @param concept
	 * @return
	 * @throws SecurityException
	*/
	public String getSignature( IConcept concept ) throws SecurityException;
	
	/**
	 * Verify the concept
	 * @param concept
	 * @param signature
	 * @return
	 * @throws SecurityException
	*/
	public boolean verify( IConcept concept ) throws SecurityException;

	/**
	 * Create a checksum for the given concept
	 * @param concept
	 * @return
	 * @throws IOException
	 * @throws SecurityException
	*/
	public long checksum( IConcept concept )
		throws IOException, SecurityException;

}
