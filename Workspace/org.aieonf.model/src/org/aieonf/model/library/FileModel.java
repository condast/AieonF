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

import org.aieonf.commons.hex.HexConvertor;
import org.aieonf.commons.hex.HexConvertor.Convert;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.core.Concept;
import org.aieonf.concept.datauri.IDataResource;
import org.aieonf.concept.library.CategoryAieon;
import org.aieonf.model.core.Model;
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
		super( file.getAbsolutePath().hashCode(), selectConcept(file), FILE);
		this.file = file;
		if( this.hasChildren() ){
			for( File child: file.listFiles() )
				super.addChild( new FileModel( child ));
		}
		super.setData(new FileAieon( file ));
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
	//TODO check this!
	public File getFile()
	{
		if( file == null )
			this.file = new File( String.valueOf( this.getID() ));
		return this.file;
	}

	@Override
	public void fill(String type, String resource) {
	}

	public void setSource( String source ){
		this.getDescriptor().set( IConcept.Attributes.SOURCE.name(), source );
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
		return super.get( IDataResource.Attribute.TYPE.name() );
	}

	public void setType( String type ) throws ConceptException{
		super.set(IDataResource.Attribute.TYPE.name(), type);
	}

	@Override
	public String getResource()
	{
		return super.get( IDataResource.Attribute.RESOURCE.name() );
	}

	public void setResource( String resource ) throws ConceptException{
		super.set(IDataResource.Attribute.RESOURCE.name(), resource);
	}

	public static String getFilePath( IDescriptor descriptor ){
		return descriptor.get( IConcept.Attributes.SOURCE.name() );
	}

	public static final void convertToFileAieon( IDescriptor descriptor, String uri ){
		descriptor.set( IDescriptor.Attributes.NAME.name(), FILE );
		descriptor.set(IConcept.Attributes.SOURCE.name(), uri);
	}

	/**
	 * Select the correct concept for the given file
	 * @param file
	 * @return
	 */
	protected static IConcept selectConcept( File file ){
		return file.isDirectory() ? new DirAieon( file ): new FileAieon( file );
	}
	
	private static class FileAieon extends Concept{
		private static final long serialVersionUID = 1L;
		
		private File file;
		
		private FileAieon( File file ) {
			this.file = file;
			super.setSource( Descriptor.makeValidName( file.getName().split( "[\\.]")[ 0 ]));
			this.setDescription( file.getAbsolutePath());
			this.setScope( Scope.PRIVATE );
			this.set( IConcept.Attributes.SOURCE.name(), this.file.toURI().toString());
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
			this.set( IConcept.Attributes.SOURCE.name(), this.file.toURI().toString());
			this.set( IConcept.Attributes.HIDDEN.name(), String.valueOf( file.isHidden() ));
			this.set( IConcept.Attributes.READ_ONLY.name(), String.valueOf( !file.canWrite()) );
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
