package org.aieonf.concept.wrapper;

import java.util.Iterator;
import java.util.Map.Entry;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.ConceptBase;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.Descriptor;

/**
 * In general, a concept can best be wrapped in
 * another object in order to more efficiently change its 
 * content. This can for instance, be the case when the
 * contents of an concept stored in an xml sheet is processed.
 * This class provides the base functionality for such a wrapper 
 * @author Kees Pieters
 *
 */
public class DescriptorWrapper implements IDescriptor
{
	public static final String S_ERR_INVALID_DESCRIPTOR = 
			"The provided descriptor is not valid for this type: ";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1342477347056704267L;
	private IDescriptor descriptor;

	public DescriptorWrapper( IDescriptor descriptor )
	{
		this.descriptor = descriptor;
	}

	protected DescriptorWrapper( String id, IDescriptor descriptor ) throws ConceptException
	{
		this( descriptor );
		set( IDescriptor.Attributes.ID, id );
	}

	/**
	 * Clear the concept attributes
	 */
	@Override
	public void clear() 
	{
		this.descriptor.clear();
	}

	/**
	 * If true, the values have changed
	 * @return
	 */
	@Override
	public boolean hasChanged()
	{
		return this.descriptor.hasChanged();
	}

	/**
	 * Get the id of the concept
	 *
	 * @return String
	 */
	@Override
	public final long getID()
	{
		return this.descriptor.getID();
	}

	/**
	 * Get the name of the concept
	 *
	 * @return String
	 */
	@Override
	public final String getName()
	{
		return this.descriptor.getName();
	}

	/**
	 * Get the version of the concept
	 *
	 * @return int
	 */
	@Override
	public final int getVersion()
	{
		return this.descriptor.getVersion();
	}

	/**
	 * Set the version of the concept
	 *
	 * @param version int
	 */
	@Override
	public final void setVersion( int version )
	{
		this.descriptor.setVersion(version );
	}

	/**
	 * Get the signature. May be null if the concept has not been assigned to
	 * a database
	 * @return String
	 */
	@Override
	public final String getSignature()
	{
		return this.descriptor.getSignature();
	}

	/**
	 * Get the description of the product
	 *
	 * @return String
	 */
	@Override
	public final String getDescription()
	{
		return this.descriptor.getDescription();
	}

	/**
	 * Set the description of the product
	 *
	 * @param description String
	 */
	@Override
	public final void setDescription( String description )
	{
		set( IDescriptor.Attributes.DESCRIPTION, description );
	}

	/**
	 * Get the class name of the concept
	 *
	 * @return String
	 */
	@Override
	public final String getClassName()
	{
		return this.descriptor.getClassName();
	}

	/**
	 * Get the class name of the concept
	 *
	 * @param String 
	 */
	protected final void setClassName( String className )
	{
		set( IDescriptor.Attributes.CLASS, className );
	}

	/**
	 * Sign the given descriptor 
	 * @param signature
	 * @throws ConcpetException
	 */
	@Override
	public void sign( String signature ) throws ConceptException
	{
		this.descriptor.sign( signature );
	}  

	/**
	 * Get the provider of the concept.
	 * This is the source that provides the concept (usually a database or a provider)
	 *
	 * @return String
	 */
	@Override
	public String getProvider()
	{
		return this.descriptor.getProvider(); 	
	}

	/**
	 * Set the provider of the concept
	 * This is the source that provides the concept (usually a database or a provider)
	 *
	 * @param propator String
	 */
	@Override
	public void setProvider( String provider )
	{
		this.descriptor.setProvider( provider );  	
	}

	/**
	 * Get the common provider name of the concept.
	 * This is the source that provides the concept (usually a database or a provider)
	 *
	 * @return String
	 */
	@Override
	public String getProviderName()
	{
		return this.descriptor.getProviderName(); 	
	}

	/**
	 * Set the common provider name of the concept
	 * This is the source that provides the concept (usually a database or a provider)
	 *
	 * @param propator String
	 */
	@Override
	public void setProviderName( String provider )
	{
		this.descriptor.setProviderName( provider );  	
	}

	/**
	 * Get the value belonging to the given key
	 * @param key String
	 * @return String
	 */
	@Override
	public final String get( String key )
	{
		return this.descriptor.get( key );
	}

	/**
	 * Set the value for the given key
	 * @param key String
	 * @param value String
	 */
	@Override
	public final void set( String key, String value )
	{
		this.descriptor.set( key, value );
	}

	/**
	 * Set the value for the given enum type
	 * @param enm
	 * @param value
	 */
	@Override
	public void set( Enum<?> enm, String value )
	{
		this.descriptor.set( ConceptBase.getAttributeKey( enm ), value );
	}

	/**
	 * Get the attribute value associated with the given enum 
	 * @param enm
	 * @param name
	 * @return
	 */
	@Override
	public String get( Enum<?> enm )
	{
		return descriptor.get( enm );
	}

	/**
	 * Get the key name
	 * @param key
	 * @return
	 */
	public final String getKeyName( Enum<?> key )
	{
		return key.getDeclaringClass().getCanonicalName() + "." + key.toString();
	}

	/**
	 * Get the boolean value of a concept. Throws an exception if the
	 * attribute is not a boolean
	 *
	 * @param attribute String
	 * @return boolean
	 */
	@Override
	public final boolean getBoolean( String attribute )
	{
		return this.descriptor.getBoolean( attribute );
	}

	/**
	 * Get the integer representation of an attribute. Throws an exception if
	 * the attribute does not have the correct format
	 * @param attribute String
	 * @return int
	 */
	@Override
	public final int getInteger( String attribute )
	{
		return this.descriptor.getInteger( attribute );
	}

	/**
	 * The size of a concept is defined as the amount of properties of the concept
	 *
	 * @return int
	 */
	@Override
	public int size()
	{
		return this.descriptor.size();
	}

	/**
	 * Get an iterator of the concept properties
	 *
	 * @return Iterator<String>
	 */
	@Override
	public final Iterator<String> keySet()
	{
		return this.descriptor.keySet();
	}

	
	@Override
	public Iterator<Entry<String, String>> iterator() {
		return this.descriptor.iterator();
	}

	/**
	 * Implement a compare to function
	 * @param obj Object
	 * @return int
	 */
	@Override
	public int compareTo( IDescriptor obj )
	{
		return this.descriptor.compareTo( obj );
	}

	/**
	 * Get a hash code for this concept
	 */
	@Override
	public int hashCode()
	{
		return this.descriptor.hashCode();
	}

	/**
	 * returns true if the given object equals this one
	 */
	@Override
	public boolean equals( Object obj )
	{
		return this.descriptor.equals( obj );
	}

	/**
	 * Get the descriptor. 
	 * NOTE: the wrapper does NOT return the underlying descriptor because the
	 * additional data (relationships, etc) will not be persisted if we do
	 */
	@Override
	public IDescriptor getDescriptor(){
		return this.descriptor;
	}

	/**
	 * Returns a string representation of the concept (XML)
	 *
	 * @return String
	 */
	@Override
	public String toString()
	{
		return this.descriptor.toString();
	}

	/**
	 * Creates a string array from the given descriptor: [name, id, version]
	 * @return String
	 */
	@Override
	public String toStringArray()
	{
		return this.descriptor.toStringArray();
	}

	/**
	 * Create the XML representation of this descriptor
	 *
	 * @return String
	 * @throws Exception
	 */
	@Override
	public String toXML() throws Exception
	{
		return this.descriptor.toXML();
	}

	/**
	 * returns true if the given object equals this one at
	 * object level (== object equals)
	 * @param obj Object
	 * @return boolean
	 */
	@Override
	public boolean objEquals(Object obj)
	{
		return this.descriptor.objEquals( obj );
	}

	@Override
	public String getFromExtendedKey(String key)
	{
		return this.descriptor.getFromExtendedKey(key);
	}

	/**
	 * Set the extended key
	 * @param extended
	 */
	protected void setExtendedKey( String extended ){
		Descriptor.setExtendedKey( this, extended );
	}
}
