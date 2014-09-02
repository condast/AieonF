/**
 * <p>Title: Saight</p>
 * <p>Description: A favourites generator for browsers</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Condast B.V.</p>
 * @author Kees Pieters
 * @version 1.0
*/
package org.condast.aieonf.browsersupport.context;

//Concept imports
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Collection;

import org.aieonf.collections.AbstractDescribableCollection;
import org.aieonf.collections.CollectionException;
import org.aieonf.collections.IAccessible;
import org.aieonf.collections.IDescribableCollection;
import org.aieonf.collections.parser.ICollectionParser;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.library.ManifestAieon;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.model.IModelLeaf;

/**
 * This class overrides the default concept database in order to
 * exploit a unique key within the scope of this package
*/
public class BrowserCollection extends AbstractDescribableCollection<IModelLeaf<IConcept>> implements IDescribableCollection<IModelLeaf<IConcept>>,
  IAccessible
{
  /**
   * Create the firefox database
   * @param loader LoaderAieon
   * @param applicationKey String
   * @throws DatabaseException
   * @throws URISyntaxException 
   * @throws MalformedURLException 
  */
  public BrowserCollection( ILoaderAieon loader, ICollectionParser<IModelLeaf<IConcept>> parser )
    throws CollectionException, MalformedURLException, URISyntaxException
  {
    super( parser );	
  }

	@Override
	protected boolean create(ILoaderAieon loader) throws CollectionException
	{
		return true;
	}

	@Override
	protected ManifestAieon searchManifest(ILoaderAieon loader)
			throws CollectionException
	{
		return super.getLoader();
	}

	@Override
	protected boolean containsManifest(ILoaderAieon loader)
			throws CollectionException
	{
		if( super.getLoader() == null )
			return false;
		return super.getLoader().equals( loader );
	}

	@Override
	protected void write(Collection<? extends IModelLeaf<IConcept>> concepts)
			throws CollectionException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean access(ILoaderAieon loader)
	{
		return true;
	}
}
