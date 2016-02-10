package org.aieonf.collections.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.collections.CollectionException;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.body.BodyFactory;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.library.ManifestAieon;
import org.aieonf.util.filter.IFilter;
import org.aieonf.util.parser.IParserListener;
import org.aieonf.util.parser.ParseException;
import org.aieonf.util.persistence.IPersistence;

public class DefaultCollectionParser<T extends IDescribable<?>> implements
		ICollectionParser<T>
{
	public static final String S_ERR_PARSER_NOT_OPEN = "The parser is not open";
	
	private boolean open = false;
	
	private Collection<T> collection;
	
	private ManifestAieon manifest;
	
	private IPersistence<T> persistence;
	
	private Collection<IParserListener<T>> listeners;

  //If true, the parser only parses descriptors. This may result in more efficient behaviour when
  //for instance descriptor data is contained in the file or entry name
  private boolean descriptorOnly;
	
	public DefaultCollectionParser()
	{
		this.open = false;
		this.descriptorOnly = false;
		this.listeners = new ArrayList<IParserListener<T>>();
	}

	public DefaultCollectionParser( IPersistence<T> persistence)
	{
		this();
		this.persistence = persistence;
	}

	
	/**
	 * @return the persistence
	 */
	@Override
	public IPersistence<T> getPersistence()
	{
		return persistence;
	}

	
	/**
	 * @param persistence the persistence to set
	 */
	protected void setPersistence(IPersistence<T> persistence)
	{
		this.persistence = persistence;
	}

	/**
	 * @return the descriptorOnly
	 */
	@Override
	public final boolean isDescriptorOnly()
	{
		return descriptorOnly;
	}

	/**
	 * If true, the given key is used by the parser to create a unique location. This is usually based on the
	 * descriptor data {name, id, version}. These can thus be retrieved without opening the content
	 */
	@Override
	public boolean canParseFromDescriptor( String key ){
		return false;
	}

	/**
	 * @param descriptorOnly the descriptorOnly to set
	 */
	@Override
	public final void setDescriptorOnly( boolean descriptorOnly )
	{
		this.descriptorOnly = descriptorOnly;
	}


	/**
	 * Initialise the parser
	 * @param loader
	 * @throws ConceptException
	 */
	@Override
	public void initialise( ManifestAieon manifest ) throws ParseException{
		this.manifest = manifest;
	}

	@Override
	public void open() throws ParseException
	{
		this.open = true;
	}

	/**
	 * Returns true if the parser is open
	 * @return
	 */
	public boolean isOpen(){
		return this.open;
	}
	
	@Override
	public void parse( IFilter<? extends IDescribable<?>> filter ) throws ParseException
	{
		if( !open )
			throw new ParseException( S_ERR_PARSER_NOT_OPEN );
	}

	@Override
	public Collection<T> getResults()
	{
		if( this.collection == null )
			return new ArrayList<T>();
		return this.collection;
	}

	/**
	 * @param collection the collection to set
	 */
	protected void setCollection(Collection<T> collection)
	{
		this.collection = collection;
	}
	
	/**
	 * @return the manifest
	 */
	@Override
	public ManifestAieon getManifest()
	{
		return manifest;
	}

	/**
	 * Fill the concept with the relevant values obtained from the manifest
	 * @param concept
	 * @throws ConceptException
	 */
	protected void fill( IConcept concept ) throws ConceptException{
    try {
      IDFactory( concept );
      concept.setProvider( this.manifest.getIdentifier() );
      concept.setProviderName( this.manifest.getProviderName() );
      concept.set( IConcept.Attributes.SOURCE.name(), manifest.getIdentifier() );
      BodyFactory.sign( this.manifest, concept );
		}
		catch (IOException e) {
			throw new ConceptException( e );
		}
		catch (CollectionException e) {
			throw new ConceptException( e );
		}		
	}

  /**
   * Create a unique id for the concepts
   * @return String
   * @throws CollectionException
  */
  protected void IDFactory( IConcept concept ) throws CollectionException
  {
    if( !Descriptor.isNull( concept.getID()))
    		return;
  	try{
  		BodyFactory.sign( this.manifest, concept );
      String id = IDFactory( concept, this.collection );
      concept.set( IDescriptor.Attributes.ID.name(), id);
    }
    catch( Exception ex ){
      throw new CollectionException( ex );
    }
    //return this.manifest.getIdentifier() + "." + id++;
  }
  
  /**
   * Create an id for the given concept
   *
   * @param descriptor IConcept
   * @param collection IConceptCollection
   * @return String
   * @throws CollectionException
  */
  public String IDFactory( IDescriptor descriptor, Collection<? extends IDescribable<?>> descriptors ) throws CollectionException
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append( this.manifest.getID() + ":" );

    long newId = descriptor.hashCode();
    boolean containsId = false;
    String hexStr;
    do{
      containsId = false;
      newId = ( long )( Math.random() * Long.MAX_VALUE );
      hexStr = Long.toHexString( newId );
      for( IDescribable<?> desc: descriptors ){
        if( desc.getDescriptor().getID().equals( hexStr )){
          containsId = true;
          break;
        }
      }
    }
    while( containsId == true );

    buffer.append( hexStr.toUpperCase());
    return buffer.toString();
  }

	@Override
	public void close() throws ParseException
	{
		this.open = false;
	}

	@Override
	public void addListener(IParserListener<T> listener)
	{
		this.listeners.add( listener );
	}

	@Override
	public void removeListener(IParserListener<T> listener)
	{
		this.listeners.remove(listener);
	}
	
	protected void notifyParsed( T parsedObject ){
		for( IParserListener<T> listener: this.listeners )
			listener.notifyParsed(parsedObject);
	}
}
