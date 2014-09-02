/**
 * 
 */
package org.aieonf.model.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelNode;
import org.aieonf.model.xml.StoreModel;
import org.aieonf.util.persistence.AbstractPersistence;
import org.aieonf.util.xml.StoreDocument;
import org.w3c.dom.Document;

/**
 * @author keesp
 *
 */
public class ModelPersistence extends AbstractPersistence<IModelNode<? extends IDescriptor>>
{

	/* (non-Javadoc)
	 * @see org.condast.concept.database.IPersistence#read(java.io.InputStream)
	 */
	@Override
	public IModelNode<? extends IDescriptor> read(InputStream in) throws IOException
	{
    if( super.isOpen() == false )
    	throw new IOException( S_ERR_PERSISTENCE_NOT_OPEN );
		try{
      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
      Document doc = docBuilder.parse ( in );
      StoreModel<IDescriptor> sm = new StoreModel<IDescriptor>();
      IModelNode<? extends IDescriptor> model = sm.parseDocument( doc );
      return model;
    }
    catch( IOException ex ){
      throw ex;
    }
    catch( Exception ex ){
      throw new IOException( ex );
    }
	}

	/* (non-Javadoc)
	 * @see org.condast.concept.database.IPersistence#write(org.condast.concept.IChangeable, java.io.OutputStream)
	 */
	@Override
	public void write( IModelNode<? extends IDescriptor> model, OutputStream out) throws IOException
	{
    if( !super.isOpen() )
    	throw new IOException( S_ERR_PERSISTENCE_NOT_OPEN );
    try{
    	StoreModel<IDescriptor> sm = new StoreModel<IDescriptor>();
    	Document doc = sm.createDocument( model );
    	StoreDocument.sendToStream( doc, out);
    }
    catch( Exception ex ){
    	throw new IOException( ex.getMessage(), ex );
    }
	}
}
