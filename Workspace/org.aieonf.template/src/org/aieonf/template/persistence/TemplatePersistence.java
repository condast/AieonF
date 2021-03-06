package org.aieonf.template.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.aieonf.commons.parser.IParser;
import org.aieonf.commons.persistence.AbstractPersistence;
import org.aieonf.commons.persistence.IPersistence;
import org.aieonf.commons.xml.StoreDocument;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.xml.StoreModel;
import org.aieonf.template.def.ITemplate;
import org.aieonf.template.def.ITemplateLeaf;
import org.w3c.dom.Document;

public class TemplatePersistence extends AbstractPersistence<ITemplate> implements
IPersistence<ITemplate>

{
	private IParser<? extends ITemplateLeaf<? extends IDescriptor>> parser;


	public TemplatePersistence( IParser<? extends ITemplateLeaf<? extends IDescriptor>> parser )
	{
		super();
		this.parser = parser;
	}

	/**
	 * Read an object from the given location
	 * @param location String
	 * @return T
	 * @throws IOException
	 */
	@Override
	public ITemplate read( InputStream in ) throws IOException
	{
		try{
			return ( ITemplate )parser.parse( in );
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
	public void write( ITemplate model, OutputStream out ) throws IOException
	{
		try{
			StoreModel<IDescriptor> store = new StoreModel<IDescriptor>();
			Document doc = store.createDocument( model );
			StoreDocument.sendToStream( doc, out );
		}
		catch( Exception ex ){
			throw new IOException( ex.getMessage(), ex );
		}
	}

	}