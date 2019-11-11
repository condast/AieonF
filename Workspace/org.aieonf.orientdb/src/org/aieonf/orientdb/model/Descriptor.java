/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Condast BV</p>
 * @author Kees Pieters
 * @version 1.0
 */
package org.aieonf.orientdb.model;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.Map;

import org.aieonf.commons.Utils;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.body.BodyFactory;
import org.aieonf.concept.core.ConceptBase;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.IConceptBase;

/**
 * Create a descriptor, using a properties file
 */
public class Descriptor implements IDescriptor
{
	/**
	 * Needed for serialisation
	 */
	private static final long serialVersionUID = -6598910361985097745L;

	//Supported error messages
	public static final String S_ERR_NULL_NAME =
			"The provided concept has got no name ";
	public static final String S_ERR_INVALID_NAME =
			"The provided name is invalid. May only contain alphanumeric " +
					"characters, '-' and '_'";
	public static final String S_ERR_INVALID_DESCRIPTOR =
			"Can not parse a descriptor from the given string: ";

	//The regular expression that splits a string into an string array of
	//which the first element always contains the correct form for a name
	public static final String NAME_REGEXP = "[^0-9][A-Za-z_0-9\\-\\.]*";
	public static final String NOT_NAME_REGEXP = "[^A-Za-z_]*[^A-Za-z0-9_\\-]*";

	public static final String URL_REGEXP = "[^0-9][A-Za-z_0-9\\-]@[a-zA-Z0-9_\\-\\.]";

	public static final String VALID_NAME_REG_EX = "[^A-Za-z0-9_\\-]+";

	private ConceptBase base;
	
	public Descriptor() {
		this.base = new ConceptBase();
		this.setClassName( this.getClass().getName());
		set( IDescriptor.Attributes.VERSION, String.valueOf(0));
	}

	/**
	 * Create a desc riptor with the given id and name
	 * @param name String
	 * @throws ConceptException
	 */
	public Descriptor( String name )
	{
		this();
		if( isEmpty( name ))
			throw new NullPointerException( S_ERR_NULL_NAME );
		if( this.checkName( name) == false )
			throw new IllegalArgumentException( S_ERR_INVALID_NAME + ": " + name );

		set( IDescriptor.Attributes.NAME, name );
	}

	/**
	 * Create a descriptor with the given id and name
	 * @param id String
	 * @param name String
	 */
	public Descriptor( String id, String name )
	{
		this( name );
		setValue( IDescriptor.Attributes.ID, id );
	}

	/**
	 * Create a descriptor from the given concept
	 *
	 * @param descriptor IDescriptor
	 */
	public Descriptor( IDescriptor descriptor )
	{
		this( descriptor.getName() );
		setValue( IDescriptor.Attributes.ID, String.valueOf( descriptor.getID() ));
		this.setVersion( descriptor.getVersion() );
		this.setDescription( descriptor.getDescription() );
		this.setClassName( descriptor.getClass().getName() );
	}

	
	/**
	 * Fill the descriptor with default values
	 */
	public void fill(){
		this.setClassName(  this.getClass().getName() );
		this.setVersion( -1 );  	
	}

	/**
	 * Returns true if the name is correct
	 * @param name
	 * @return
	 */
	protected boolean checkName( String name )
	{
		if( isEmpty( name ))
			return false;
		return ( name.matches( NAME_REGEXP ));
	}

	/**
	 * Get the id of the concept
	 *
	 * @return String
	 */
	@Override
	public final long getID()
	{
		String str = this.get( IDescriptor.Attributes.ID ); 
		return Long.parseLong(str);
	}

	/**
	 * Get the name of the concept
	 *
	 * @return String
	 */
	@Override
	public final String getName()
	{
		return this.get( IDescriptor.Attributes.NAME.name() );
	}

	/**
	 * Get the version of the concept
	 *
	 * @return int
	 */
	@Override
	public final int getVersion()
	{
		String version = this.get( Attributes.VERSION );
		if( Descriptor.isNull( version ))
			return Integer.MIN_VALUE;
		return Integer.parseInt( version );
	}

	/**
	 * Set the version of the concept
	 *
	 * @param version int
	 * @throws ConceptException
	 */
	@Override
	public final void setVersion( int version )
	{
		setValue( Attributes.VERSION, String.valueOf( version ));
	}

	/**
	 * Get the signature. May be null if the concept has not been assigned to
	 * a database
	 * @return String
	 */
	@Override
	public final String getSignature()
	{
		return this.get( IDescriptor.Attributes.SIGNATURE );
	}

	/**
	 * Get the description of the product
	 *
	 * @return String
	 */
	@Override
	public final String getDescription()
	{
		return this.get( IDescriptor.Attributes.DESCRIPTION );
	}

	/**
	 * Set the description of the product
	 *
	 * @param description String
	 */
	@Override
	public final void setDescription( String description )
	{
		setValue( IDescriptor.Attributes.DESCRIPTION, description );
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
		return this.get( IDescriptor.Attributes.PROVIDER );  	
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
		setValue( IDescriptor.Attributes.PROVIDER, provider );  	
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
		return this.get( IDescriptor.Attributes.PROVIDER_NAME );  	
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
		setValue( IDescriptor.Attributes.PROVIDER_NAME, provider );  	
	}

	/**
	 * Get the class name of the concept
	 *
	 * @return String
	 */
	@Override
	public final String getClassName()
	{
		return this.get( Attributes.CLASS );
	}

	/**
	 * Get the class name of the concept
	 *
	 * @param className String
	 */
	protected final void setClassName( String className )
	{
		setValue( Attributes.CLASS, className );
	}

	protected void setValue( Attributes attr, String value ) {
		set(attr.name().toLowerCase(), value);
	}
	
	protected void remove( Enum<?> key ){
		this.base.remove(key);
	}

	protected void remove( String key ){
		this.base.remove(key);
	}

	/**
	 * Sign the given descriptor 
	 * @param signature
	 */
	@Override
	public void sign( String signature )
	{
		if( signature == null ){
			base.remove( Attributes.SIGNATURE );
			return;
		}
		this.set( Attributes.SIGNATURE, signature );
	}  

	/**
	 * Return a hash code for this descriptor
	 */
	@Override
	public int hashCode()
	{
		if(( this.getSignature() == null ) || ( this.getSignature().equals("" )))
			return this.createDescriptorString().hashCode();
		String signature = this.getSignature();
		long lng;
		if( signature.matches("\\-?[0-9]*" ) == false )
			return signature.hashCode();

		lng = Long.valueOf( this.getSignature() );
		lng &= 0xFFFFFFFF;
		return ( int )lng; 			
	}

	/**
	 * Returns true if the given concept is equal to this one
	 * Note that it only checks on name and id. The 'compareTo' method also checks the
	 * version. As compareTo is the preferred check for set collections,
	 * they will return false if the versions don't match. Other collections
	 * use the equals method, and these return true if the version are not equal. 
	 * Therefore a set and a list may respond differently to a 'contains' method   *
	 * @param obj Object
	 * @return boolean
	 */
	@Override
	public boolean equals( Object obj )
	{
		if( super.equals( obj ))
			return true;
		if(( obj == null ) || (( obj instanceof IDescriptor ) == false ))
			return false;
		IDescriptor descriptor = ( IDescriptor )obj;
		if( this.getID() == descriptor.getID() ){
			return true;
				
		}
		if( descriptor.getID() < 0 )
			return false;
		if( this.getName() == null ){
			String name = descriptor.getName();
			return (( name == null ) || ( name.equals( IConceptBase.NULL )));
		}
		if( descriptor.getName() == null )
			return false;
		if( !descriptor.getName().equals( this.getName() ))
			return false;
		return ( descriptor.getID() == this.getID() );
	}

	/**
	 * Returns true if the given object is equal to this one at object level
	 * (is equals at object level)
	 *
	 * @param obj Object
	 * @return boolean
	 */
	@Override
	public boolean objEquals( Object obj )
	{
		return super.equals( obj );
	}

	/**
	 * Implement a compare to function. Note that this is the preferred check for
	 * set collections. They will return false if the versions don't match. Other collections
	 * use the equals method, and these return true if the version are not equal. 
	 * Therefore a set and a list may respond differently to a 'contains' method
	 * @param obj Object
	 * @return int
	 */
	@Override
	public int compareTo( IDescriptor descriptor )
	{
		if(( this.getName() == null ) && ( descriptor.getName() == null ))
			return 0;
		if( this.getName() == null )
			return -1;
		if( descriptor.getName() == null )
			return 1;
		long compareTo = this.getName().compareTo( descriptor.getName() );
		if( compareTo != 0 )
			return (int) compareTo;
		long id = this.getID();
		if( id < 0 )
			return -1;
		compareTo = id - descriptor.getID();
		if( compareTo != 0 )
			return ( compareTo < 0)?-1:1;
		return ( this.getVersion() - descriptor.getVersion() );
	}

	/**
	 * Get the descriptor
	 */
	@Override
	public IDescriptor getDescriptor(){
		return this;
	}

	
	@Override
	public Iterator<Map.Entry<String, String>> iterator() {
		return base.iterator();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * Returns a string representation of the concept (XML)
	 *
	 * @return String
	 */
	public String createDescriptorString()
	{
		String name = this.getName();

		if( name == null )
			name = "null";
		long id = this.getID();

		String versionStr = base.get( IDescriptor.Attributes.VERSION );
		int version = -1;
		if(( versionStr != null ) && ( versionStr.equals("") == false ))
			version = this.getVersion();

		String str = name + ":" + id + ":" + version;
		str = str.replace( " ", "" ).trim();
		return str;
	}

	/**
	 * Returns a string representation of the concept (XML)
	 *
	 * @return String
	 */
	@Override
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append( this.createDescriptorString());
		buffer.append( base.toString() );
		return buffer.toString();
	}

	/**
	 * Creates a string array from the given descriptor: [name, id, version]
	 * @return String
	 */
	@Override
	public String toStringArray()
	{
		String name = this.getName();
		if( name == null )
			name = "null";
		long id = this.getID();
		return "["+ name + "," + id + "," + this.getVersion() + "]";
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
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		BodyFactory.saveToXML( this, bStream );
		bStream.close();
		return String.valueOf( bStream.toByteArray() );
	}

	/**
	 * Create a valid name from the given base name
	 * @param baseName
	 * @return
	 */
	public static String createValidName( String baseName ){
		String name = baseName.replace(" ", "").trim();
		name = name.replaceAll( VALID_NAME_REG_EX, "-");
		String replace = name.replaceFirst("\\d+", "" ).trim();

		if( !name.startsWith( replace ))
			name = replace;
		else if( isEmpty( replace ))
			name = "num-" + name;
		return name;
	}

	/**
	 * Returns true if the attribute is empty
	 * @param attribute
	 * @return
	 */
	public static boolean isEmpty( String attribute )
	{
		return ( attribute == null ) || ( attribute.trim().isEmpty() );
	}

	/**
	 * Make a valid name from the given one
	 * @param name
	 * @return
	 */
	public static String makeValidName( String name )
	{
		String NotRegexp = "[^a-zA-Z0-9_\\-]";
		if( name.contains( "ostbank") == true ){
			System.out.print("stop");
		}
		name = name.replace( ' ', '_');
		name = name.replaceAll( NotRegexp, "").trim();
		if( name.matches( Descriptor.NAME_REGEXP ) == false )
			name = "_" + name;
		return name;
	}

	/**
	 * Returns ture if the given string is null or empty
	 * @param str
	 * @return
	 */
	public final static boolean isNull( String str ){
		return ( str == null ) || ( str.trim().length() == 0 );
	}

	/**
	 * Returns true if the descriptor is valid
	 * @param descriptor IDescriptor
	 * @return boolean
	 */
	public final static boolean isValid( IDescriptor descriptor )
	{
		String str = descriptor.getName();
		if( isNull( str ))
			return false;
		str = String.valueOf( descriptor.getID() );
		if( isNull( str ))
			return false; 
		return ( descriptor.getVersion() >= 0 );
	}



	@Override
	public String getFromExtendedKey(String key)
	{
		String extended = base.get( IDescriptor.Attributes.EXTENDED_KEY );
		if( Descriptor.isNull( extended ))
			return base.get(key);
		return base.get( extended + "." + key);
	}

	/**
	 * The extended key is used to group attributes together. It is set once for a concept, usually
	 * based on the enum type
	 * 
	 * @param extended
	 */
	protected void setExtendedKey( String extended ){
		setExtendedKey( this, extended );
	}

	public static void setExtendedKey( IDescriptor descriptor, String key ){
		key = key.replaceAll("[$]", ".");
		descriptor.set( IDescriptor.Attributes.EXTENDED_KEY, key );
	}

	public static boolean isDescriptorAttribute( String key ){
		for( IDescriptor.Attributes attr: IDescriptor.Attributes.values() ){
			if( attr.toString().equals(key ))
				return true;
		}
		return false;
	}

	@Override
	public boolean hasChanged() {
		return base.hasChanged();
	}

	protected void setChanged( boolean choice ){
		base.setChanged(choice);
	}
	
	@Override
	public void clear() {
		base.clear();
	}

	@Override
	public String get(String key) {
		return base.get( key );
	}

	@Override
	public void set(String key, String value) {
		base.set(key, value);
	}

	@Override
	public String get(Enum<?> enm) {
		return base.get(enm.name());
	}

	@Override
	public void set( Enum<?> enm, String value) {
		base.set(enm.name(), value);
	}

	@Override
	public boolean getBoolean(String attribute) {
		return base.getBoolean(attribute);
	}

	@Override
	public int getInteger(String attribute) {
		return base.getInteger(attribute);
	}

	
	@Override
	public Iterator<String> keySet() {
		return base.keySet();
	}

	@Override
	public int size() {
		return base.size();
	}		

	/**
	 * Get the given attribute as a boolean. If the value is null, a default is returned
	 * @param descriptor
	 * @param attribute
	 * @param defaultValue
	 * @return
	 */
	public static boolean getBoolean( IDescriptor descriptor, Enum<?> attribute, boolean defaultValue ){
		String str = descriptor.get(attribute);
		return Utils.assertNull(str)?defaultValue: Boolean.valueOf(str );
	}
	
	/**
	 * Overwrite the values of the target with those of the reference, if they are not null
	 * @param target
	 * @param reference
	 */
	public static void overwrite( IDescriptor target, IDescriptor reference ){
		Iterator<String> iterator = reference.keySet();
		while( iterator.hasNext() ){
			String key = iterator.next();
			String ref = reference.get( key );
			if( ref != null )
				target.set(key, ref);
		}
	}

	/**
	 * Returns true if the given concept contains the given attribute
	 * @param descriptor
	 * @param attribute
	 * @return
	 */
	public static final boolean hasAttribute( IDescriptor descriptor, String attribute ){
		String attr = descriptor.get( attribute );
		if( attr == null )
			return false;
		attr = attr.replace( " ", "" );
		return ( attr.length() > 0 );
	}


}