package org.aieonf.collections.parser;

import java.util.Collection;

import org.aieonf.commons.filter.IFilter;
import org.aieonf.commons.parser.IParserListener;
import org.aieonf.commons.parser.ParseException;
import org.aieonf.commons.persistence.IPersistence;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.library.ManifestAieon;

public interface ICollectionParser<T extends IDescribable<?>>
{
	/**
	 * Initialise the parser
	 * @param loader
	 * @throws ConceptException
	 */
	public void initialise( ManifestAieon manifest ) throws ParseException;

	/**
	 * Get the manifest aieon for this parswer
	 * @return
	 */
	public ManifestAieon getManifest();
	
	/**
	 * Get the persistence object that reads and writes the content
	 * @return
	 */
	public IPersistence<T> getPersistence();
	
	/**
	 * If true, the parser only parses descriptors. This can be very efficient if the descriptor info is
	 * contained, for instance in a file or entry name. In such a case, the content of the file or entry does
	 * not need to be parsed
	 */
	public boolean isDescriptorOnly();

	/**
	 * If true, the given key is used by the parser to create a unique location. This is usually based on the
	 * descriptor data {name, id, version}. These can thus be retrieved without opening the content
	 */
	public boolean canParseFromDescriptor( String key );

	/**
	 * Set whether or not the descriptor alone is parsed
	 */
	public void setDescriptorOnly( boolean descriptorOnly );

	/**
	 * Open the parser
	 * @throws ParseException
	 */
	public void open() throws ParseException;
		
  /**
   * Parse the collection according to the provided filter
   * @throws ParseException
   */
  public void parse( IFilter<? extends IDescribable<?>> filter ) throws ParseException;
  
  /**
   * Get the results that was parsed
   * @return
   */
  public Collection<T> getResults();
  
	/**
	 * Close the parser
	 * @throws ParseException
	 */
	public void close() throws ParseException;
	
	/**
	 * @param listeners the listener to add
	 */
	public void addListener( IParserListener<T> listener);

	/**
	 * @param listeners the listener to add
	 */
	public void removeListener( IParserListener<T> listener);


}
