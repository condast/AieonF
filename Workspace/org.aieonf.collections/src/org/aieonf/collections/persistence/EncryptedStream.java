package org.aieonf.collections.persistence;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.crypto.Cipher;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.concept.security.AieonFEncryption;
import org.aieonf.util.encryption.IEncryption;

public class EncryptedStream<T extends IDescribable<?>>
{
	public static InputStream getEncryptedInputStream( ILoaderAieon loader, InputStream in ) throws IOException{
		try{
      IEncryption encrypt = new AieonFEncryption( loader );
	  	return encrypt.getInputStream( Cipher.DECRYPT_MODE, in );
    }
    catch( Exception ex ){
      throw new IOException( ex );
    }
	}
	
	public static OutputStream getEncryptedOutputStream( ILoaderAieon loader, OutputStream out ) throws IOException{
		try{
      IEncryption encrypt = new AieonFEncryption( loader );
	  	return new BufferedOutputStream( encrypt.getOutputStream( Cipher.ENCRYPT_MODE, out ));
    }
    catch( Exception ex ){
      throw new IOException( ex );
    }
	}
}