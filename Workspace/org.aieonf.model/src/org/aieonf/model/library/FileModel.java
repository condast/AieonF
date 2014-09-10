/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Condast BV</p>
 * @author Kees Pieters
 * @version 1.0
 */
package org.aieonf.model.library;

//J2SE imports
import java.io.File;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.core.MinimalConcept;
import org.aieonf.concept.datauri.IDataResource;
import org.aieonf.concept.library.CategoryAieon;
import org.aieonf.model.Model;
import org.aieonf.util.hex.HexConvertor;
import org.aieonf.util.hex.HexConvertor.Convert;
/**
 * Create a concept, using a properties file
 */
public class FileModel extends Model<IDescriptor> implements IDataResource
{
	public static final String FILE = "File";

	//The file eon revolves around a file object in JAVA
	private File file;
	
	/**
	 * Create a file eon for the given name
	 *
	 * @param fileName String
	 */
	public FileModel( String fileName ) 
	{
		this( new File( fileName ));
	}

	/**
	 * Create a file eon for the given file
	 *
	 * @param file File
	 */
	public FileModel( File file )
	{
		super( selectConcept(file));
		this.file = file;
		if( this.hasChildren() ){
			for( File child: file.listFiles() )
				super.addChild( new FileModel( child ));
		}
	}

	/**
	 * Create a file eon for the given file
	 *
	 * @param file File
	 */
	public FileModel( String id, File file )
	{
		this( file );
		this.getDescriptor().set( IDescriptor.Attributes.ID, id );
	}

	/**
	 * Get the file
	 *
	 * @return File
	 */
	public File getFile()
	{
		if( file == null )
			this.file = new File( this.getID() );
		return this.file;
	}

	@Override
	public void fill(String type, String resource) {
	}

	public void setSource( String source ){
		this.getDescriptor().set( IConcept.Attributes.SOURCE, source );
	}
	
	/**
	 * Clones the concept
	 *
	 * @return Object
	 */
	@Override
	public Object clone()
	{
		try{
			return new FileModel( new File( this.file.getAbsolutePath() ));
		}
		catch( Exception e ){
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * If true, the given concept is a category
	 *
	 * @param concept IConcept
	 * @return boolean
	 */
	public static boolean isFileEon( IConcept concept )
	{
		return ( concept.getName().equals( FILE ));
	}

	@Override
	public String getType()
	{
		return super.get( IDataResource.Attribute.Type );
	}

	public void setType( String type ) throws ConceptException{
		super.set(IDataResource.Attribute.Type, type);
	}

	@Override
	public String getResource()
	{
		return super.get( IDataResource.Attribute.Resource );
	}

	public void setResource( String resource ) throws ConceptException{
		super.set(IDataResource.Attribute.Resource, resource);
	}

	public static String getFilePath( IDescriptor descriptor ){
		return descriptor.get( IConcept.Attributes.SOURCE.toString() );
	}

	public static final void convertToFileAieon( IDescriptor descriptor, String uri ){
		descriptor.set( IDescriptor.Attributes.NAME.name(), FILE );
		descriptor.set(IConcept.Attributes.SOURCE.toString(), uri);
	}

	/**
	 * Select the correct concept for the given file
	 * @param file
	 * @return
	 */
	protected static IConcept selectConcept( File file ){
		return file.isDirectory() ? new DirAieon( file ): new FileAieon( file );
	}
	
	private static class FileAieon extends MinimalConcept{
		private static final long serialVersionUID = 1L;
		
		private File file;
		
		private FileAieon( File file ) {
			this.file = file;
			super.setSource( Descriptor.makeValidName( file.getName().split( "[\\.]")[ 0 ]));
			this.setDescription( file.getAbsolutePath());
			this.setScope( Scope.PRIVATE );
			this.set( IConcept.Attributes.SOURCE.toString(), this.file.toURI().toString());
			this.setProviderName( file.getName().split( "[\\.]")[ 0 ] );
		}		
	}
	
	private static class DirAieon extends CategoryAieon{
		private static final long serialVersionUID = 1L;

		private File file;
		
		private DirAieon( File file ) {
			super( file.getName() );
			this.file = file;
			this.set( IDescriptor.Attributes.ID, HexConvertor.convert( Convert.CONVERT_TO_HEX, file.getAbsolutePath() ));
			this.setDescription( file.getAbsolutePath());
			this.setScope( Scope.PRIVATE );
			this.set( IConcept.Attributes.SOURCE.toString(), this.file.toURI().toString());
			this.set( IConcept.Attributes.HIDDEN.toString(), String.valueOf( file.isHidden() ));
			this.set( IConcept.Attributes.READ_ONLY.toString(), String.valueOf( !file.canWrite()) );
			this.setProviderName( file.getName().split( "[\\.]")[ 0 ] );
		}		
	}

	@Override
	public boolean hasChildren() {
		return ( file.isDirectory() ) && ( file.listFiles().length > 0 );
	}

	@Override
	public int nrOfchildren() {
		if( ! file.isDirectory() )
			return 0;
		return file.listFiles().length;
	}
}
