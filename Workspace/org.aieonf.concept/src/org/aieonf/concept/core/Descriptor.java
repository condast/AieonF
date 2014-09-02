/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Condast BV</p>
 * @author Kees Pieters
 * @version 1.0
 */
package org.aieonf.concept.core;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Properties;

import org.aieonf.concept.DescriptorViewer;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.IDescriptorViewer;
import org.aieonf.concept.body.BodyFactory;
import org.aieonf.util.Utils;

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

	private IConceptBase base;
	
	public Descriptor() {
		this( new ConceptBase() );
	}

	protected Descriptor( IConceptBase base ) {
		this.base = base;
		this.setClassName( this.getClass().getName());
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

		base.set( IDescriptor.Attributes.NAME, name );
	}

	/**
	 * Create a descriptor with the given id and name
	 * @param id String
	 * @param name String
	 */
	public Descriptor( String id, String name )
	{
		this( name );
		base.set( IDescriptor.Attributes.ID, id );
	}

	/**
	 * Create a descriptor from the given concept
	 *
	 * @param descriptor IDescriptor
	 */
	public Descriptor( IDescriptor descriptor )
	{
		this( descriptor.getName() );
		base.set( IDescriptor.Attributes.ID, descriptor.getID() );
		this.setVersion( descriptor.getVersion() );
		this.setDescription( descriptor.getDescription() );
		this.setClassName( descriptor.getClass().getName() );
	}

	
	protected IConceptBase getBase() {
		return base;
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
	public final String getID()
	{
		return this.get( IDescriptor.Attributes.ID );
	}

	/**
	 * Get the name of the concept
	 *
	 * @return String
	 */
	@Override
	public final String getName()
	{
		return this.get( IDescriptor.Attributes.NAME );
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
		base.set( Attributes.VERSION, String.valueOf( version ));
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
		base.set( IDescriptor.Attributes.DESCRIPTION, description );
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
		base.set( IDescriptor.Attributes.PROVIDER, provider );  	
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
		base.set( IDescriptor.Attributes.PROVIDER_NAME, provider );  	
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
		base.set( Attributes.CLASS, className );
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
		if( this.getID() == null ){
			String id = descriptor.getID();
			return ( id == null )? super.equals(obj): false;
				
		}
		if( descriptor.getID() == null )
			return false;
		if( this.getName() == null ){
			String name = descriptor.getName();
			return (( name == null ) || ( name.equals( IConceptBase.NULL )));
		}
		if( descriptor.getName() == null )
			return false;
		if( !descriptor.getName().equals( this.getName() ))
			return false;
		return descriptor.getID().equals( this.getID() );
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
		int compareTo = this.getName().compareTo( descriptor.getName() );
		if( compareTo != 0 )
			return compareTo;
		String id = this.getID();
		if( id == null )
			return -1;
		if( descriptor.getID() == null )
			return 1;
		compareTo = id.compareTo( descriptor.getID() );
		if( compareTo != 0 )
			return compareTo;
		return ( this.getVersion() - descriptor.getVersion() );
	}

	/**
	 * Get the descriptor
	 */
	@Override
	public IDescriptor getDescriptor(){
		return this;
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
		String id = this.getID();
		if( id == null )
			id = "null";

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
		return this.createDescriptorString();
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
		String id = this.getID();
		if( id == null )
			id = "null";
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
		str = descriptor.getID();
		if( isNull( str ))
			return false; 
		return ( descriptor.getVersion() >= 0 );
	}

	/**
	 * Create a descriptor from a string array.
	 *
	 * @param stringArray String
	 * @return IDescriptor
	 * @throws Exception
	 */
	public static IDescriptor getDescriptor( String stringArray )
			throws ConceptException
	{
		stringArray = stringArray.replace( '[',' ' );
		stringArray = stringArray.replace( ']',' ' );
		stringArray = stringArray.trim();
		String[] split = stringArray.split( ":" );
		if( split.length < 3 )
			throw new ConceptException( S_ERR_INVALID_DESCRIPTOR + stringArray);

		String name = split[ 0 ];
		String version = split[ split.length - 1];
		String id = split[1];
		if( split.length > 3 )
			id = stringArray.substring( name.length() + 1, 
					stringArray.length() - version.length() - 1 );
		IDescriptor descriptor = new Descriptor( id, name );
		descriptor.setVersion( Integer.parseInt( version ));
		return descriptor;
	}

	/**
	 * Get the given attribute as a date. Returns numberformat exception if the
	 * representation is incorrect
	 *
	 * @param descriptor IDescriptor
	 * @return Date
	 */
	public final static Date getDate( IDescriptor descriptor, IDescriptor.Attributes attr )
	{
		String str = descriptor.get( attr );
		if(( str == null ) || ( str.trim().length() == 0 ))
			return Calendar.getInstance().getTime();

		GregorianCalendar calendar = new GregorianCalendar();
		long time = Long.parseLong( str );
		calendar.setTimeInMillis( time );
		return calendar.getTime();
	}

	/**
	 * Set the attribute of the given descriptor as a date
	 *
	 * @param descriptor IDescriptor
	 * @param date Date
	 */
	public final static void setDate( IDescriptor descriptor, IDescriptor.Attributes attr, Date date )
	{
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime( date );
		long time = calendar.getTimeInMillis();
		descriptor.set( attr, String.valueOf( time ) );
	}

	/**
	 * Get the create date of the concept
	 *
	 * @param concept IConcept
	 * @return Date
	 */
	public final static Date getCreateDate( IDescriptor descriptor )
	{
		return getDate( descriptor, IDescriptor.Attributes.CREATE_DATE );
	}

	/**
	 * Set the create date of the concept
	 *
	 * @param descriptor IDescriptor
	 * @param date Date
	 * @throws ConceptException
	 */
	public final static void setCreateDate( IDescribable<? extends IDescriptor> descriptor, Date date )
	{
		setDate( descriptor.getDescriptor(), IDescriptor.Attributes.CREATE_DATE, date );
		setDate( descriptor.getDescriptor(), IDescriptor.Attributes.UPDATE_DATE, date );
	}

	/**
	 * Get the update date of the descriptor
	 *
	 * @param descriptor IDescriptor
	 * @return Date
	 */
	public final static Date getUpdateDate( IDescribable<? extends IDescriptor> descriptor )
	{
		return getDate( descriptor.getDescriptor(), IDescriptor.Attributes.CREATE_DATE );
	}

	/**
	 * Set the update date of the concept
	 *
	 * @param descriptor IDescriptor
	 * @param date Date
	 */
	public final static void setUpdateDate( IDescribable<? extends IDescriptor> descriptor, Date date )
	{
		setDate( descriptor.getDescriptor(), IDescriptor.Attributes.UPDATE_DATE, date );
	}

	/**
	 * Get the difference between the given desc1 and desc2. all the properties of
	 * desc2 that are not in desc1 are returned
	 *
	 * @param desc1 Descriptor
	 * @param desc2 Descriptor
	 * @return Properties
	 */
	public static Properties getDifference( Descriptor desc1, Descriptor desc2 )
	{
		Properties properties = new Properties();
		Iterator<String> iterator = desc2.iterator();
		String key;
		while( iterator.hasNext() ){
			key = iterator.next();
			if( desc1.get( key ) == null )
				properties.setProperty( key, desc2.get( key ));
		}
		return properties;
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
	public IDescriptorViewer getViewer()
	{
		return new DescriptorViewer( this );
	}

	/**
	 * Get the text of the given object
	 * @param element
	 * @return
	 */
	public static String getText(Object element)
	{
		if(!( element instanceof IDescribable<?> ))
			return element.toString();
		IDescribable<?> desc = ( IDescribable<?> )element;
		String retval = desc.getDescriptor().getDescription();
		if( Utils.isNull( retval ))
			retval = desc.getDescriptor().getName();
		return retval;
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
		return base.get(enm);
	}

	@Override
	public void set( Enum<?> enm, String value) {
		base.set(enm, value);
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
	public Iterator<String> iterator() {
		return base.iterator();
	}

	@Override
	public int size() {
		return base.size();
	}		

	/**
	 * Overwrite the values of the target with those of the reference, if they are not null
	 * @param target
	 * @param reference
	 */
	public static void overwrite( IDescriptor target, IDescriptor reference ){
		Iterator<String> iterator = reference.iterator();
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
