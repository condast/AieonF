/**
 * <p>Title: Saight</p>
 * <p>Description: A favourites generator for browsers</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Condast B.V.</p>
 * @author Kees Pieters
 * @version 1.0
 */
package org.condast.aieonf.browsersupport.library.ie;

import java.io.File;
import java.util.Calendar;
import java.util.Collection;
import java.util.logging.Logger;

import org.aieonf.collections.persistence.IFilePersistence;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.library.CategoryAieon;
import org.aieonf.concept.library.ManifestAieon;
import org.aieonf.concept.library.URLAieon;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.model.library.FileModel;
import org.aieonf.template.provider.AbstractModelProvider;

/**
 * This class overrides the default concept database in order to
 * exploit a unique key within the scope of this package
 */
public class IEFavoritesProvider<T extends ILoaderAieon> extends AbstractModelProvider<T,IDescriptor, IModelLeaf<IDescriptor>>
{
	private IFilePersistence<IModelLeaf<IDescriptor>> persistence;
	
	private File root;
	
	private Logger logger;

	public IEFavoritesProvider( IContextAieon context, IModelLeaf<T> model )
	{
		super( context, model );
		logger = Logger.getLogger( this.getClass().getName() );
	}

	@Override
	protected void onSetup( ManifestAieon manifest ) {
		persistence = new IEPersistence( manifest );
	}

	public void open()
	{
		root = new File( super.getManifest().getSource() );
		if( root.isDirectory())
			super.open();
		persistence.open();
	}
	
	@Override
	public void close() {
		persistence.close();
		super.close();
	}

	@Override
	public Collection<IModelLeaf<IDescriptor>> onSearch(IModelFilter<IDescriptor> filter) {
		super.getModels().clear();
		FileModel model = new FileModel( root );
		this.parseFileModel(model);
		return super.getModels();
	}

	protected void parseFileModel( FileModel model ){
		try {
			this.setFileEon(model);
			persistence.setFile( model.getFile() );
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
	/**
	 * Makes Fileons that represents a directory a category and a link
	 *
	 * @param fileEon FileEon
	 * @throws Exception
	 */
	protected void setFileEon( FileModel model ) throws Exception
	{
		IConcept fileEon = (IConcept) model.getDescriptor();
		logger.fine( "Concept found: " + fileEon.getName() );
		fileEon.setVersion(1);
		fileEon.setReadOnly( true );
		fileEon.setScope( IConcept.Scope.PRIVATE );
		fileEon.setProvider( super.getManifest().getIdentifier() );
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis( model.getFile().lastModified() );
		fileEon.set( IDescriptor.Attributes.CREATE_DATE, calendar.getTime().toString() );
		fileEon.set( IDescriptor.Attributes.UPDATE_DATE, calendar.getTime().toString() );
		if( model.getFile().isDirectory() ){
			fileEon.set( IDescriptor.Attributes.NAME, CategoryAieon.Attributes.CATEGORY.toString() );
			fileEon.set( CategoryAieon.Attributes.CATEGORY, fileEon.getName() );
		}else{
			fileEon.set( IDescriptor.Attributes.NAME, URLAieon.Attributes.URL.toString() );
		}
	}

	/**
	 * Set the name of the descriptor
	 * @param descriptor Descriptor
	 * @throws ConceptException
	 */
	protected void setDescription( IDescriptor descriptor ) throws ConceptException
	{
		if(( descriptor == null ) || (descriptor.getID() == null ))
			return;
		File file = new File( descriptor.getID() );
		if ( file.isDirectory() ){
			String[] split = file.getName().split( "/" );
			descriptor.set( IDescriptor.Attributes.DESCRIPTION, split[ split.length - 1 ]);
		}
		else
		{
			String[] split = file.getName().split( "[.]" );
			descriptor.set( IDescriptor.Attributes.DESCRIPTION, split[ 0 ]);
		}
	}

	/**
	 * Returns true if the file is valid for a saight concept
	 *
	 * @param model IConcept
	 * @return boolean
	 */
	public static boolean isValidModel( IModelLeaf<IDescriptor> model )
	{
		Logger logger = Logger.getLogger( IEFavoritesProvider.class.getName() );
		if(!( model instanceof FileModel))
			return true;

		FileModel fileEon = ( FileModel )model;
		if( fileEon.getFile().isDirectory() == true )
			return true;

		File file = fileEon.getFile();
		logger.fine( "File is file: " + file.isFile() +
				" ends with url: " + file.getName().endsWith( ".url") );
		if(( file.isFile() == true ) &&
				( file.getName().endsWith( ".url") == true ))
			return true;
		return false;
	}

	protected IModelLeaf<IDescriptor> getDirectoryAieon(File directory)
	{
		FileModel fileAieon = new FileModel( directory );
		return fileAieon;
	}
}
