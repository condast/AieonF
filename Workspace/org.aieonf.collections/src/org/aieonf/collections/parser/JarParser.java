package org.aieonf.collections.parser;

import java.io.InputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.aieonf.collections.persistence.EncryptedStream;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.util.filter.IFilter;
import org.aieonf.util.io.IOUtils;
import org.aieonf.util.parser.ParseException;
import org.aieonf.util.persistence.IPersistence;

public class JarParser<T extends IDescribable<?>> extends AbstractJarParser<T>
{
  public JarParser( IPersistence<T> persistence, ILoaderAieon loader )
	{
		super( persistence, loader );
	}

  /* (non-Javadoc)
	 * @see org.condast.concept.database.collections.IBasicCollection#search(org.condast.util.filter.Filter, boolean, int)
	 */
  @SuppressWarnings("unchecked")
	@Override
	public void parse( IFilter<? extends IDescribable<?>> filter ) throws ParseException
  {
  	Set<IDescribable<?>> results = new TreeSet<IDescribable<?>>();
    if( filter.getAmount() == 0 ){
      super.setCollection( (Collection<T>) results );
    	return;
    }

    JarFile jar = super.getExternalJarFile();
    InputStream entryStream;
    InputStream ein = null;
 
 	IPersistence<T> persistence = super.getPersistence();
    IDescribable<?> describable = null;
    boolean descriptorOnly = this.isDescriptorOnly();
    T result = null;
    JarEntry entry;
    for( Enumeration<JarEntry> list = jar.entries(); list.hasMoreElements(); ){
      int amount = filter.getAmount();
      if(( amount >= 0 ) && ( results.size() >= amount ))
        break;

      entry = list.nextElement();
    	describable = this.getEntry( entry, false );
      if( describable == null )
        continue;
      try{
        if( descriptorOnly  && !filter.accept( describable )) 
      		continue;

        entryStream = jar.getInputStream( entry );
        ein = EncryptedStream.getEncryptedInputStream(super.getManifest(), entryStream );
        persistence.open();
        result = persistence.read( ein );
        persistence.close();
        if( filter.accept(result ))
        	results.add( result );
      }
      catch( Exception ex ){
      	ex.printStackTrace();
       	super.addIncorrectentry( entry.getName() );
        continue;
      }
      finally{
        IOUtils.closeInputStream( ein );
      	super.setCollection( (Collection<T>) results );
      }
      super.notifyParsed( result );     
    }
  }
}
