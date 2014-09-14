package org.aieonf.collections.parser;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.library.ManifestAieon;
import org.aieonf.util.filter.IFilter;
import org.aieonf.util.parser.IParserListener;
import org.aieonf.util.parser.ParseException;
import org.aieonf.util.persistence.IPersistence;

public class CombinedCollectionParser<T extends IDescribable<?>> implements
		ICollectionParser<T>
{
	private Collection<ICollectionParser<T>> parsers;
	
	public CombinedCollectionParser()
	{
		parsers = new ArrayList<ICollectionParser<T>>();
	}

	/**
	 * Add a parser
	 * @param parser
	 * @return
	 */
	public boolean addParser( ICollectionParser<T> parser ){
		return this.parsers.add( parser );
	}

	/**
	 * Remove a parser
	 * @param parser
	 * @return
	 */
	public boolean removeParser( ICollectionParser<T> parser ){
		return this.parsers.remove( parser );
	}

	@Override
	public void initialise(ManifestAieon manifest) throws ParseException
	{
		for( ICollectionParser<T> parser: parsers )
		  parser.initialise(manifest);	
	}

	@Override
	public IPersistence<T> getPersistence()
	{
		if( this.parsers.size() == 0 )
			return null;
		return parsers.iterator().next().getPersistence();
	}

	@Override
	public boolean isDescriptorOnly()
	{
		if( this.parsers.size() == 0 )
			return false;
		return parsers.iterator().next().isDescriptorOnly();
	}

	@Override
	public void setDescriptorOnly(boolean descriptorOnly)
	{
		for( ICollectionParser<T> parser: parsers )
		  parser.setDescriptorOnly(descriptorOnly);	
	}

	@Override
	public void open() throws ParseException
	{
		for( ICollectionParser<T> parser: parsers )
		  parser.open();	
	}

	@Override
	public void parse(IFilter<? extends IDescribable<?>> filter)
			throws ParseException
	{
		for( ICollectionParser<T> parser: parsers )
		  parser.parse( filter );	
	}

	@Override
	public Collection<T> getResults()
	{
		Collection<T> results = new ArrayList<T>();
		for( ICollectionParser<T> parser: parsers )
		  results.addAll( parser.getResults() );	
		return results;
	}

	@Override
	public void close() throws ParseException
	{
		for( ICollectionParser<T> parser: parsers )
		  parser.close();	
	}

	/**
	 * If true, the given key is used by the parser to create a unique location. This is usually based on the
	 * descriptor data {name, id, version}. These can thus be retrieved without opening the content
	 */
	@Override
	public boolean canParseFromDescriptor( String key ){
		for( ICollectionParser<T> parser: parsers ){
		  if( parser.canParseFromDescriptor(key))
		  	return true;
		}
		return false;
	}

	@Override
	public void addListener(IParserListener<T> listener)
	{
		for( ICollectionParser<T> parser: parsers )
		  parser.addListener( listener );	
	}

	@Override
	public void removeListener(IParserListener<T> listener)
	{
		for( ICollectionParser<T> parser: parsers )
		  parser.removeListener( listener );	
	}

	@Override
	public ManifestAieon getManifest()
	{
		return null;
	}
}
