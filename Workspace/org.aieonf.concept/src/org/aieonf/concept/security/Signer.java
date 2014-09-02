package org.aieonf.concept.security;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.body.BodyFactory;
import org.aieonf.concept.body.ConceptBody;
import org.aieonf.concept.core.ConceptInstance;
import org.aieonf.util.encryption.Encryption;
import org.aieonf.util.encryption.IEncryption;

/**
 * The default signer creates an encrypted byte array of a concept, and 
 * folds them together until a long remains. This does not guarantee the unicity
 * of the concept.
 * @author Kees Pieters
*/
public class Signer implements ISigner
{
	//Error messages
	public static final String S_ERR_NO_SIGNATURE_FOUND = 
		"No signature was found for the concept: ";
	
	//The key that is used for encryption
	private String key;
	
	//The algorithm that is used to encrypt the concept
	private IEncryption.Algorithms algorithm;

	/**
	 * Create a signer 
	 * @param key
	 * @param algorithm
	 */
	public Signer( String key, IEncryption.Algorithms algorithm )
	{
		this.key = key;
		this.algorithm = algorithm;
	}

	/**
	 * Get a signer for the given aieon
	 * @param aieon
	 */
	public Signer( IEncryptionAieon aieon )
	{
		this( aieon.getEncryptionKey(), aieon.getEncryptionAlgorithm() );
	}
	
	/**
	 * Sign a concept 
	 * @param concept
	 * @return
	 */
	@Override
	public final String getSignature( IConcept concept ) throws SecurityException
	{	
		try{
			IConcept clone = new ConceptInstance();
			BodyFactory.transfer( clone, concept, true );
			return this.condense( clone );
		}
		catch( Exception ex ){
			throw new SecurityException( ex.getMessage(), ex );
		}
	}
	
	/**
	 * Verify the concept
	 * @param concept
	 * @param signature
	 * @return
	 */
	@Override
	public final boolean verify( IConcept concept ) throws SecurityException
	{
		String signature = concept.getSignature();
		if( signature == null )
			throw new NullPointerException( S_ERR_NO_SIGNATURE_FOUND + concept.toString() );
		String verify = this.getSignature( concept );
		return ( verify.equals( signature ));
	}

	/**
	 * Create a checksum for the given concept
	 * @param concept
	 * @return
	 * @throws IOException
	 * @throws SecurityException
	*/
	@Override
	public final long checksum( IConcept concept )
		throws IOException, SecurityException
	{
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		OutputStream eOut = null;
		IEncryption encrypt = null;
		
		try{
			encrypt = new Encryption( this.algorithm, key );
			eOut = encrypt.getOutputStream( Cipher.ENCRYPT_MODE, bout );
			ConceptBody<IConcept> body = new ConceptBody<IConcept>( concept );
			body.saveToXML( eOut);
			eOut.flush();
			eOut.close();
		}
		catch( Exception ex ){
			if( eOut != null ){
				eOut.flush();
				eOut.close();
			}
			throw new SecurityException( ex.getMessage(), ex );
		}
		finally{
			bout.flush();
			bout.close();
		}
		byte[] bytes = bout.toByteArray();
		long checksum = 0;
		for( byte bt: bytes ){
			if( bt < 0 )
				bt = ( byte )-bt;
			checksum += bt;
		}
		return checksum;
	}

	/**
	 * Condense the contents of a given concept to a single String
	 * @param concept
	 * @return
	 * @throws IOException
	 * @throws SecurityException
	*/
	protected final String condense( IConcept concept )
		throws IOException, SecurityException
	{
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		OutputStream eOut = null;
		IEncryption encrypt = null;
		
		try{
			encrypt = new Encryption( this.algorithm, key );
			eOut = encrypt.getOutputStream( Cipher.ENCRYPT_MODE, bout );
			ConceptBody<IConcept> body = new ConceptBody<IConcept>( concept );
			body.saveToXML( eOut);
			eOut.flush();
			eOut.close();
		}
		catch( Exception ex ){
			if( eOut != null ){
				eOut.flush();
				eOut.close();
			}
			throw new SecurityException( ex.getMessage(), ex );
		}
		finally{
			bout.flush();
			bout.close();			
		}
		byte[] bytes = bout.toByteArray();

		//First condense the bytes to integers and store them as longs
		List<Long> longs = new ArrayList<Long>();
		long value = 0;
		for( int i = 0; i < bytes.length; i++ ){
			if(( i % 4 ) == 0 ){
				if( i > 0 ){
					if( value < 0 )
						value = -value;
					longs.add( new Long( value ));
				}else{
					value = bytes[ i ];
				}
				continue;	
			}
			value <<= 8;
			value += bytes[ i ];
		}

		int length = longs.size();
		if( length == 1 )
			return Long.toHexString( longs.get( 0 ));
		
		if(( length % 2 ) != 0 )
			length++;
		long[] condensed = new long[ length ];
		for( int i = 0; i < longs.size(); i++ ){
			condensed[ i ] = longs.get( i );
		}
		
		//int index = 0;

		int half = condensed.length;
		long[] halved;
		long combine;
		while( length > 2 ){
			//index++;
			if(( length % 2 ) != 0 )
				length++;
			half = length / 2;
			if(( half % 2 ) == 0 )
				halved = new long[ half ];
			else
				halved = new long[ half + 1 ];
			
			for( int i = 0; i < half; i++ ){
				if(( half + i ) > condensed.length )
					break;
				combine = condensed[ i ] + condensed[ half + i ];
				if( combine < 0 )
					combine = -combine;
				halved[ i ] = combine;
			}
			condensed = halved;
			length = half;
		}
		Long result = condensed[ 0 ]<<32 + condensed[ 1 ]; 
		return Long.toHexString( result );
	}
}
