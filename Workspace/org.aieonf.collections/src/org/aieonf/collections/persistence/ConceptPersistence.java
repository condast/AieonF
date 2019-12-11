package org.aieonf.collections.persistence;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.aieonf.commons.io.IOUtils;
import org.aieonf.commons.persistence.AbstractPersistence;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.body.ConceptBody;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.concept.persist.ILocatedPersistence;
import org.aieonf.concept.xml.ConceptParser;
import org.w3c.dom.Document;

public class ConceptPersistence extends AbstractPersistence<IConcept> implements ILocatedPersistence<IConcept>
{
	/**
	 * Read an object from the given location
	 * @param location String
	 * @param in inputStream
	 * @return T
	 * @throws CollectionException
	*/
	@Override
	public IConcept read( InputStream in ) throws IOException
	{
    if( super.isOpen() == false )
    	throw new IOException( S_ERR_PERSISTENCE_NOT_OPEN );
		try{
      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
      Document doc = docBuilder.parse ( in );
      ConceptParser parser = new ConceptParser();
      IConcept concept = parser.parse( doc );
      return concept;
    }
    catch( IOException ex ){
      throw ex;
    }
    catch( Exception ex ){
      throw new IOException( ex );
    }
 	}

	/**
	 * Persist an object 
	 * @param object T
	 * @param out OutputStream
	 * @throws IOException
	 */
	@Override
	public void write( IConcept object , OutputStream out ) throws IOException
	{
    if( !super.isOpen())
    	throw new IOException( S_ERR_PERSISTENCE_NOT_OPEN );
    ConceptBody<IDescriptor> action = new ConceptBody<IDescriptor>( object.getDescriptor() );
    action.saveToXML( out );
	}
	
	/**
	 * Concert the given concept to a byte string, using this form of persistence
	 * @param loader
	 * @param describable
	 * @return
	 */
	@Override
	public byte[] toBytes( ILoaderAieon loader, IConcept describable ){
    OutputStream eout = null;
    ByteArrayOutputStream bout = null;

    try{
      bout = new ByteArrayOutputStream();
      eout = EncryptedStream.getEncryptedOutputStream( loader, bout);

      this.open();
      this.write( describable, eout );
      this.close();
    }
    catch( IOException ex ){
    	ex.printStackTrace();
    }
    finally{
    	IOUtils.closeQuietly( eout );
    }
    return bout.toByteArray();
	}	
}
