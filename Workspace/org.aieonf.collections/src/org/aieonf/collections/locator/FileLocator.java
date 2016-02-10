package org.aieonf.collections.locator;

//J2SE
import java.io.*;
import java.net.MalformedURLException;

//concept



import org.aieonf.collections.CollectionException;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.locator.ILocatorAieon;

/**
 * <p>Title: Conceptual Network Database</p>
 *
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p> The file locator uses a file to locate the database
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Condast</p>
 *
 * @author Kees Pieters
 * @version 1.0
 */
public class FileLocator extends Locator
{
	//error messages
	public static final String S_ERR_INVALID_FILENAME =
			"The file name is incorrect";

	//The source of the database
	private File source;

	/**
	 * Get a file locator instance
	 * @param identifier String
	 * @param name String
	 * @param location String
	 * @throws MalformedURLException 
	 */
	public FileLocator( String identifier, File file ) throws ConceptException, MalformedURLException
	{
		super( identifier, setName( file ), file.toURI() );
		this.source = file;
	}

	/**
	 * Get a file locator instance
	 * @param LocatorDescriptor descriptor
	 * @param location String
	 * @todo TEST
	 */
	public FileLocator( ILocatorAieon descriptor )
	{
		super( descriptor );
		String source = descriptor.getURI().getPath();
		this.source = new File( source );
	}

	/**
	 * Get the source that is database source
	 * @return File
	 */
	public String getSource()
	{
		try{
			return this.source.getCanonicalPath();
		}
		catch( IOException ex ){
			return null;
		}
	}

	/**
	 * Get the file that is database source
	 * @return File
	 */
	public File getSourceFile()
	{
		return this.source;
	}

	/**
	 * A utlity that returns the parent name of this file
	 * @return String
	 */
	public String getParent()
	{
		return this.source.getParentFile().getName();
	}

	/**
	 * Create the database by making the parent directories
	 *
	 * @throws CollectionException
	 */
	@Override
	public void create() throws CollectionException
	{
		File directory = this.source.getParentFile();
		if( directory.exists() == false )
			directory.mkdirs();
	}

	/**
	 * Return true if the database exists at the given location
	 * @return boolean
	 */
	public boolean exists()
	{
		return this.source.exists();
	}

	/**
	 * Open the database
	 * @throws CollectionException
	 */
	@Override
	public void open() throws CollectionException
	{
		if( this.source.exists() == false )
			throw new CollectionException( Locator.S_DATABASE_DOES_NOT_EXIST_ERR +
					this.source.getAbsolutePath());
		super.open();
	}

	/**
	 * Delete the database
	 *
	 * @throws CollectionException
	 */
	public void delete() throws CollectionException
	{
		if( this.source.exists() == false )
			return;

		if( this.source.delete() == false )
			throw new CollectionException( Locator.S_COULD_NOT_DELETE_DATABASE_ERR + this.source );
	}

	/**
	 * Isolate the name of the file
	 * @param file File
	 * @return String
	 * @throws ConceptException
	 */
	protected static String setName( File file )
	{
		String fileName = file.getName();
		String[] split = fileName.split( "[.]" );
		return split[ 0 ];
	}

	/**
	 * Return true if the database exists for the given root location
	 * @return boolean
	 */
	public static boolean exists( String location )
	{
		File root = new File( location );
		return root.exists();
	}
}
