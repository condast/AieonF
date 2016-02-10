package org.aieonf.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.filter.AttributeFilter;
import org.aieonf.concept.filter.DescriptorFilter;
import org.aieonf.util.filter.AbstractFilter;
import org.aieonf.util.filter.FilterException;
import org.aieonf.util.logger.Logger;

public class ReadPerspectiveCaller implements Callable<Collection<IConcept>>
{
	//The special functions provides the concepts
	private IDescribableCollection<IConcept> collection;
	
	//All the calls are translated to filter calls
	private AbstractFilter<IConcept> filter;
	
	private Logger logger;
	
	/**
	 * Create the caller
	 * @param rdb
	*/
	public ReadPerspectiveCaller( IDescribableCollection<IConcept> collection )
	{
		this.collection = collection;
		this.filter = null;
		this.logger = Logger.getLogger( this.getClass() );
	}
	
	/**
	 * Get all the concepts with the given name. Returns null if none were found
	 *
	 * @param name String
	 * @throws CollectionException
	*/
	public void get(String name) throws CollectionException
	{
  	try{
  		name = name.trim().toLowerCase();
  		this.filter = 
  			new AttributeFilter<IConcept>( AttributeFilter.Rules.Equals, IDescriptor.Attributes.NAME.name(), name );
  	}
  	catch( FilterException ex ){
  		throw new CollectionException( ex.getMessage(), ex );
  	}
	}

	/**
	 * Get all the concepts complying with the given descriptor.
	 * Strict determines a filter for the concepts based on version info
	 *
	 * @param descriptor IDescriptor
	 * @throws CollectionException
	*/
	public void get( IDescriptor descriptor) throws CollectionException
	{
  	try{
  		this.filter = 
  			new DescriptorFilter<IConcept>( DescriptorFilter.Rules.Equals, descriptor, true );
  	}
  	catch( Exception ex ){
  		throw new CollectionException( ex.getMessage(), ex );
  	}
	}

  /**
   * Get all the concepts with the wild card. This search is not case sensitive
   *
   * @param wildcard String
   * @throws CollectionException
  */
	public void search(String wildcard) throws CollectionException
	{
    wildcard = wildcard.trim().toLowerCase();
    try{
      this.filter = new AttributeFilter<IConcept>( AttributeFilter.Rules.Wildcard, IDescriptor.Attributes.NAME.name(), wildcard );
    }
    catch( Exception ex ){
      throw new CollectionException( ex );
    }
	}

  /**
   * Get all the concepts whose attribute value matched the wild card,
   *
   * @param attr String
   * @param wildcard String
   * @throws CollectionException
  */
	public void search(String attr, String wildcard) throws CollectionException
	{
    try{
    	this.filter = new AttributeFilter<IConcept>( AttributeFilter.Rules.Wildcard, attr, wildcard );
    }
    catch( FilterException ex ){
    	throw new CollectionException( ex.getMessage(), ex );
    }
	}

	/**
	 * Perform the call
	*/
	@Override
	public Collection<IConcept> call()
	{
		try{
			Collection<IConcept> results = this.collection.search( this.filter, false );
			return results;
		}
		catch( CollectionException ex ){
			logger.error( ex.getMessage(), ex );
			return new ArrayList<IConcept>();
		}
	}
}