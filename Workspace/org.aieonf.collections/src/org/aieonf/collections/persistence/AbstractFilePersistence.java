package org.aieonf.collections.persistence;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.aieonf.collections.CollectionException;
import org.aieonf.collections.locator.FileLocator;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.body.BodyFactory;
import org.aieonf.concept.library.CategoryAieon;
import org.aieonf.concept.library.ManifestAieon;
import org.aieonf.concept.security.AieonFEncryption;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.library.FileModel;
import org.aieonf.util.encryption.IEncryption;
import org.aieonf.util.hex.HexConvertor;
import org.aieonf.util.persistence.AbstractPersistence;

public abstract class AbstractFilePersistence extends
AbstractPersistence<IModelLeaf<IDescriptor>> implements IFilePersistence<IModelLeaf<IDescriptor>>
{
	private File file;
	private ManifestAieon manifest;

	public AbstractFilePersistence( ManifestAieon manifest )
	{
		super();
		this.manifest = manifest;
	}

	public AbstractFilePersistence( File file, ManifestAieon manifest )
	{
		this( manifest );
		this.file = file;
	}


	/**
	 * @return the file
	 */
	protected final File getFile()
	{
		return file;
	}

	/* (non-Javadoc)
	 * @see org.condast.concept.database.collections.persistence.IFilePersistence#setFile(java.io.File)
	 */
	@Override
	public final void setFile(File file)
	{
		this.file = file;
	}

	/**
	 * Fill the given concept with the data provided by the input stream
	 * @param concept
	 * @param in
	 * @throws IOException
	 */
	protected abstract void fillModel( IModelLeaf<IDescriptor> concept, InputStream in ) throws IOException;

	@Override
	public IModelLeaf<IDescriptor> read(InputStream in) throws IOException
	{
		FileModel model;
		try {
			model = new FileModel( this.IDFactory( file ), file );
			this.setModel( model );
			FileLocator locator = new FileLocator( manifest );
			model.set( IDescriptor.Attributes.ID.toString(), locator.getIdentifier() + "." + this.file.getAbsolutePath().hashCode() );
			if( !CategoryAieon.isCategory( model.getDescriptor())){
				this.fillModel( model, in);
			}
			return model;
		}
		catch (CollectionException e) {
			throw new IOException( e );
		}
	}

	@Override
	public void write(IModelLeaf<IDescriptor> object, OutputStream out) throws IOException
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Set the basics for a concept for this file database
	 *
	 * @param concept IConcept
	 * @throws CollectionException
	 */
	protected void setModel( IModelLeaf<IDescriptor> model ) throws CollectionException
	{
		try{
			IConcept concept = (IConcept) model.getDescriptor();
			concept.set( IConcept.Attributes.SOURCE.name(), manifest.getID() );
			concept.setVersion( 1 );
			BodyFactory.sign( manifest, concept );
		}
		catch( Exception ex ){
			throw new CollectionException( ex );
		}
	}

	/**
	 * Create an id for this file collection
	 *
	 * @param file File
	 * @return String
	 * @throws CollectionException
	 */
	protected String IDFactory( File file ) throws CollectionException
	{
		ManifestAieon manifest = this.manifest;
		try{
			IEncryption encrypt = new AieonFEncryption( manifest );
			return HexConvertor.convertHex( encrypt.encryptData( file.getAbsolutePath().
					getBytes() ) );
		}
		catch( Exception ex ){
			throw new CollectionException( ex );
		}
	}

}
