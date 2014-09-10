package org.aieonf.concept.body;

//J2SE
import java.io.*;
import java.util.*;

import org.aieonf.concept.*;
import org.aieonf.concept.core.*;
import org.aieonf.util.Utils;

/**
 * <p>Title: Conceptual Network Database</p>
 *
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Condast</p>
 *
 * @author Kees Pieters
 * @version 1.0
 */
public class ConceptBody<T extends IDescriptor>
{
	public static final String TO_JSON_STRING = "toJSONString";

	public static final String S_ERR_NO_CLASS_NAME = "This descriptor does not have a class name";

	//	The concept that is embodied
	private T descriptor;

	/**
	 * Create an action concept for the given concept
	 * @param concept IConcept
	 */
	public ConceptBody( T descriptor )
	{
		this.descriptor = descriptor;
	}

	/**
	 * returns a transformed concept that is of the correct subclass,
	 * for instance an application concept, or type concept.
	 *
	 * @return IConcept
	 * @throws ConceptException
	 */
	@SuppressWarnings("unchecked")
	public final T transform() throws ConceptException
	{
		Class<?> conceptClass = null;
		try{
			String className = descriptor.getClassName();
			if( Utils.isNull( className ))
				throw new NullPointerException( S_ERR_NO_CLASS_NAME );
			conceptClass = Class.forName( className );
			T newConcept = ( T )conceptClass.newInstance();
			BodyFactory.transfer( newConcept, descriptor, true );
			newConcept.set( IDescriptor.Attributes.CLASS.toString(), newConcept.getClass().getName() );
			if( newConcept instanceof IConcept ){
				IConcept concept = ( IConcept )newConcept;
				if( concept.isA( descriptor ) == false )
					throw new ConceptException( descriptor,
							ConceptException.S_TRANSFORMATION_FAILED_MSG );
			}
			return newConcept;
		}
		catch( Exception e ){
			throw new ConceptException( descriptor,
					ConceptException.S_TRANSFORMATION_FAILED_MSG +
					": " + this.getClass(), e );
		}
	}

	/**
	 * Get an output stream with the contents of the concept
	 * @return ByteArrayOutputStream
	 * @throws IOException
	 */
	public final ByteArrayOutputStream getOutputStream() throws IOException
	{
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		BodyFactory.saveToXML( this.descriptor, bStream );
		return bStream;
	}

	/**
	 * Save the given concept to the outputstream
	 *
	 * @param out OutputStream
	 * @throws IOException
	 */
	public final void saveToXML( OutputStream out ) throws IOException
	{
		BodyFactory.saveToXML( this.descriptor, out );
	}

	/**
	 * Get a string containing the attributes of the given concept
	 * @param descriptor
	 */
	public static String getAttributes( IDescriptor descriptor )
	{
		StringBuffer buffer = new StringBuffer();
		Iterator<String> iterator = descriptor.iterator();
		String key;
		buffer.append( "Attributes of: " + descriptor.toString() );
		int index = 0;
		while( iterator.hasNext() ){
			key = iterator.next();
			if( key.equals( TO_JSON_STRING ))
				continue;
			buffer.append( "\n\t" + key + "-" + descriptor.get( key ));
			index++;
		}
		buffer.append( "\n\n Number of Attributes found: " + index );
		return buffer.toString();
	}

	/**
	 * Get the key name
	 * @param key
	 * @return
	 */
	public static final String getKeyName( Enum<?> key )
	{
		return key.getDeclaringClass().getCanonicalName() + "." + key.toString();
	}

	/**
	 * Print the attributes of the given concept
	 * @param descriptor
	 */
	public static void printAttributes( IDescriptor descriptor )
	{
		String str = getAttributes( descriptor );
		System.err.println( str );
	}

	/**
	 * Print the given concept's structure. This is a tree of descriptors
	 * @param model
	 * @return
	 */
	public static final String printConceptStructure( IDescriptor descriptor )
	{
		try{
			StringBuffer buffer = new StringBuffer();
			printConceptStructure( buffer, descriptor, 0 );
			return "\nConcept:\n" + buffer.toString();
		}
		catch( ConceptException ex ){
			ex.printStackTrace();
			return ex.getMessage();
		}
	}

	/**
	 * quick print of the structure of the given model
	 * @param model
	 * @return
	 */
	protected static final void printConceptStructure( StringBuffer buffer, IDescriptor descriptor, int depth )
			throws ConceptException
	{
		for( int i =0; i< depth; i++ )
			buffer.append( "\t" );
		buffer.append( descriptor.toString() + "\n" );
	}  

	/**
	 * Returns true if the given string is a valid scope
	 * @param scopeStr
	 * @return
	 */
	public static boolean isCorrectScope( String scopeStr )
	{
		IConcept.Scope[] values = IConcept.Scope.values();
		for( IConcept.Scope scp: values ){
			if( scp.name().equals( scopeStr.trim() ))
				return true;
		}
		return false;
	}
}