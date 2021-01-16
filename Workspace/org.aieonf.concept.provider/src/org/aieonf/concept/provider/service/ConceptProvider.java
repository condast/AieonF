package org.aieonf.concept.provider.service;

import java.io.InputStream;

import org.aieonf.commons.base64.Base64Utils;
import org.aieonf.commons.io.IOUtils;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.datauri.DataURI;
import org.aieonf.concept.datauri.IDataURI;
import org.aieonf.model.provider.IConceptProvider;
import org.osgi.service.component.annotations.Component;

@Component( name="org.aionf.concept.provider.service",
immediate=true)
public class ConceptProvider implements IConceptProvider
{
	public static final String S_WIKIPEDIA_NAME = "Wikipedia";
	public static final String S_WIKIPEDIA_DESC = "Wikipedia Link";

	public static final String S_WIKIPEDIA_URL = "http://en.wikipedia.org/wiki/";

	public static final String S_FAVICON = "/resources/Wikipedia.png";

	private String id, name;
	
	public ConceptProvider(){
		this.id = S_WIKIPEDIA_URL;
		this.name = S_WIKIPEDIA_NAME;
	}
	
	public String getId() {
		return id;
	}


	public String getName() {
		return name;
	}

	private String createURL( String query ){
		String url = S_WIKIPEDIA_URL + query;
		return url;
	}
	
	/**
	 * Create a 64 base image from the favicon
	 * @return
	 */
	private String createBase64Image(){
		InputStream in = this.getClass().getResourceAsStream(S_FAVICON); 
		try{
			return Base64Utils.getBase64DataURI( in );
		}
		finally{
			IOUtils.closeQuietly( in ); 
		}
	}

	@Override
	public synchronized IDataURI[] query(String datum) {
		String url = this.createURL( datum );
		long id = (long)url.hashCode();
		IDataURI concept = new DataURI( id, S_WIKIPEDIA_NAME );
		concept.setVersion( 1 );
		concept.set( IDescriptor.Attributes.NAME.name(), S_WIKIPEDIA_NAME);
		concept.setDescription( S_WIKIPEDIA_DESC );
		concept.setURI(url);
		IDataURI dataURI = ( IDataURI )concept;
		String base64 = this.createBase64Image();
		dataURI.fill( IDataURI.S_ICON, base64 );
		IDataURI[] results = new IDataURI[1];
		results[0] = dataURI;
		return results;
	}
}