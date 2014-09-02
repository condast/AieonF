package org.condast.aieonf.browsersupport.library.ie;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import org.aieonf.collections.CollectionException;
import org.aieonf.collections.persistence.AbstractFilePersistence;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.datauri.IDataResource;
import org.aieonf.concept.library.ManifestAieon;
import org.condast.aieonf.utils.base64.Base64;
import org.condast.aieonf.utils.base64.Base64Utils;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.library.FileModel;
import org.aieonf.util.io.IOUtils;

public class IEPersistence extends AbstractFilePersistence
{
	private Logger logger;

	public IEPersistence(ManifestAieon manifest)
	{
		super(manifest);
		logger = Logger.getLogger( this.getClass().getName() );
	}


	@Override
	protected void fillModel(IModelLeaf<IConcept> model, InputStream in)
			throws IOException {
		String str = IOUtils.toString( in, "UTF-8" );

		if(( model instanceof FileModel )){
			this.setURI(( FileModel )model, str );
			return;
		}

		IEDataResource data = new IEDataResource( model.getDescriptor() );
		data.fill( IEDataResource.S_ICON, str );
	}

	/**
	 * Set the url of the file eon
	 *
	 * @param fileEon FileEon
	 * @throws ConceptDatabaseException
	 */
	protected void setURI( FileModel model, String resourceString ) throws CollectionException
	{
		IConcept fileEon = model.getDescriptor();
		if( model.getFile().isDirectory() == true ){
			model.setSource( null );
			return;
		}
		logger.fine( "File Eon: '" + fileEon.getName() + "'  Is file: " +
				model.getFile().isFile() +
				" ends with url: " +
				fileEon.getSource().toLowerCase().endsWith( ".url") );
		if(( model.getFile().isFile() == false ) ||
				( fileEon.getSource().toLowerCase().endsWith( ".url") == false )){
			model.setSource( null );
			return;
		}

		boolean iconFound = false;
		try{
			String str = resourceString;
			String[] split = resourceString.split("[\n]");
			for ( int i = 0; i < split.length; i++ ) {
				if( split[i].startsWith("URL")){
					str = split[i].split("[=]")[1];
					model.setSource( str.split("[?]")[0] );
					logger.fine( "FileAion:" + fileEon.getName() + " has URI: "  + fileEon.getSource() );        	
				}

				if( split[i].startsWith("IconFile")){
					str = split[i].split("[=]")[1];
					str = str.replace("\r", "");
					if( str.startsWith("http")){
						model.setType( IDataResource.S_ICON );
						model.setResource(str );
						iconFound = true;
					}else{
						File file = new File( "file://" + str );

						if( file.exists() ){
							model.setType( IDataResource.S_ICON );
							String encode = Base64.encodeFromFile( file.getAbsolutePath() );
							model.setResource("data:image/png;base64," + encode );
							iconFound = true;
						}
					}
				}
			}
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}

		if( iconFound )
			return;
		String datauri = Base64Utils.getBase64DataURI( this.getClass(), "/images/explorer.png");
		try {
			if( !Descriptor.isNull( datauri ))
				model.setResource( datauri );
		}
		catch (ConceptException e) {
			throw new CollectionException( e );
		}
	}

}
