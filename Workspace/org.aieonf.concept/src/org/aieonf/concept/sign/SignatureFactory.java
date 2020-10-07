/**
 * The signature factory is intended to make signing of concepts quicker. Some key builders are very slow 
 */
package org.aieonf.concept.sign;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.crypto.NoSuchPaddingException;

import org.aieonf.commons.encryption.IEncryption;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.body.BodyFactory;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.concept.security.AieonFEncryption;

public class SignatureFactory {

	private ILoaderAieon loader;
	private IEncryption encryptor;
	private long signature;
	
	private static SignatureFactory factory = new SignatureFactory();

	private ExecutorService service;
	private boolean ready;
	private Runnable runnable = new Runnable(){

		@Override
		public void run() {
			try {
				//first create a hashcode based on the loader
				int hashCode = BodyFactory.hashCode( loader, loader );
				signature = ( long )hashCode << 32;
				encryptor = new AieonFEncryption( loader );
			    ready = true;
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}		
		}	
	};
	
	private SignatureFactory() {
		this.ready = false;
		service = Executors.newCachedThreadPool();
	}

	public static SignatureFactory getInstance(){
		return factory;
	}
	
	public void init( ILoaderAieon loader ) throws Exception{
		this.loader = loader;
		service.execute(runnable);
	}
	
	public boolean isReady() {
		return ready;
	}

	/**
	 * Sign the descriptor. Returns true if all went well.
	 * @param descriptor
	 * @return
	 * @throws IOException
	 */
	public synchronized boolean sign( IDescriptor descriptor ) throws IOException{
	    if( !this.ready )
	    	return ready;
		signature += BodyFactory.hashCode( encryptor, descriptor.getDescriptor() );
	    descriptor.getDescriptor().set( IDescriptor.Attributes.SIGNATURE.name(), String.valueOf( signature ));
		return ready;
	}
}
