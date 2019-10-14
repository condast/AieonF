package org.aieonf.orientdb.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.aieonf.commons.strings.StringStyler;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.ConceptBase;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.core.IConceptBase;

import com.orientechnologies.orient.core.record.impl.ODocument;

public class DocumentConceptBase implements IConceptBase {

	//The properties collection that contains the data
	private ODocument document;

	//If true, something has changed
	private transient boolean changed;

	public DocumentConceptBase( ODocument document ){
		this.document = document;
		this.changed = false;
	}
	
	public ODocument getDocument() {
		return document;
	}

	/* (non-Javadoc)
	 * @see org.aieonf.concept.core.IConceptBase#hasChanged()
	 */
	@Override
	public boolean hasChanged()
	{
		return this.changed;
	}

	/* (non-Javadoc)
	 * @see org.aieonf.concept.core.IConceptBase#setChanged(boolean)
	 */
	@Override
	public void setChanged( boolean choice )
	{
		this.changed = choice;
	}

	/* (non-Javadoc)
	 * @see org.aieonf.concept.core.IConceptBase#clear()
	 */
	@Override
	public void clear()
	{
		document.clear();
		this.changed = true;
	}

	/* (non-Javadoc)
	 * @see org.aieonf.concept.core.IConceptBase#get(java.lang.String)
	 */
	@Override
	public final String get( String key )
	{
		return document.field( StringStyler.fromPackageString( key ));
	}

	/* (non-Javadoc)
	 * @see org.aieonf.concept.core.IConceptBase#set(java.lang.String, java.lang.String)
	 */
	@Override
	public final void set( String key, String value )
	{
		if( key == null )
			throw new NullPointerException();

		if( value == null )
			value = "";
		if( key.contains(" ")){
			throw new IllegalArgumentException( "Invalid key " + key + " contains <space> character" );
		}
		document.field(StringStyler.fromPackageString( key ), value );
		this.changed = true;
	}

	/**
	 * Get the value belonging to a key of the concept
	 *
	 * @param key String
	 * @return String
	 */
	protected final String get( Class<?> clss, String key )
	{
		return this.get( ConceptBase.getAttributeName( clss, key ));
	}

	/**
	 * This variant creates a key based on the class name.key
	 * @param clss
	 * @param key
	 * @param value
	 */
	protected void set( Class<?> clss, String key, String value )
	{
		this.set( ConceptBase.getAttributeName( clss, key ), value );
	}

	/* (non-Javadoc)
	 * @see org.aieonf.concept.core.IConceptBase#get(java.lang.Enum)
	 */
	@Override
	public String get( Enum<?> enm )
	{
		return get( ConceptBase.getAttributeKey( enm ));
	}

	/**
	 * Set the attribute value associated with the given enum 
	 * @param enm
	 * @param name
	 * @return
	 */
	@Override
	public void set( Enum<?> enm, String value )
	{
		set( ConceptBase.getAttributeKey( enm ), value );
	}

	/* (non-Javadoc)
	 * @see org.aieonf.concept.core.IConceptBase#remove(java.lang.Enum)
	 */
	@Override
	public void remove( Enum<?> enm )
	{
		String id =  ConceptBase.getAttributeKey( enm );
		document.removeField( StringStyler.styleToEnum( id ));
	}


	/* (non-Javadoc)
	 * @see org.aieonf.concept.core.IConceptBase#getBoolean(java.lang.String)
	 */
	@Override
	public final boolean getBoolean( String attribute )
	{
		if( attribute == null )
			return false;
		String val = this.get( attribute );
		if( val == null )
			return false;

		val = val.toLowerCase().trim();
		if( val.equals("" ))
			return false;
		boolean yes = val.equals( "true" );
		if(( yes == false ) && ( val.equals( "false" ) == false ))
			return false;
		return yes;
	}

	/* (non-Javadoc)
	 * @see org.aieonf.concept.core.IConceptBase#getInteger(java.lang.String)
	 */
	@Override
	public final int getInteger( String attribute )
	{
		String val = this.get( attribute );
		if( val == null )
			return 0;

		val = val.toLowerCase().trim();
		return Integer.parseInt( val );
	}


	/* (non-Javadoc)
	 * @see org.aieonf.concept.core.IConceptBase#remove(java.lang.String)
	 */
	@Override
	public final void remove( String key )
	{
		this.document.removeField( StringStyler.prettyString( key ));
	}

	/* (non-Javadoc)
	 * @see org.aieonf.concept.core.IConceptBase#size()
	 */
	@Override
	public int size()
	{
		return document.getSize();
	}

	/* (non-Javadoc)
	 * @see org.aieonf.concept.core.IConceptBase#iterator()
	 */
	@Override
	public final Iterator<String> keySet()
	{
		Collection<String> results = Arrays.asList( document.fieldNames());
		return results.iterator();
	}

	
	@Override
	public Iterator<Entry<String, String>> iterator() {
		Iterator<Entry<String, Object>> iterator = this.document.iterator();
		Map<String, String> results = new HashMap<>();
		while( iterator.hasNext() ){
			Entry<String, Object> entry = iterator.next();
			String key = StringStyler.toPackageString(entry.getKey());
			results.put( key, (String) entry.getValue());
		}
		return results.entrySet().iterator();
	}

	/* (non-Javadoc)
	 * @see org.aieonf.concept.core.IConceptBase#clone()
	 */
	@Override
	public Object clone()
	{
		try{
			IConceptBase clone = this.getClass().newInstance();
			Descriptor.overwrite(( IDescriptor) clone, (IDescriptor)this );
			return clone;
		}
		catch( Exception e ){
			e.printStackTrace();
			return null;
		}
	}

	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Concept:\n");
		Iterator<Entry<String, Object>> iterator = this.document.iterator();
		while( iterator.hasNext() ){
			Entry<String, Object> entry = iterator.next();
			buffer.append( "\t"+ StringStyler.toPackageString( entry.getKey()) + "=" +
			entry.getValue() + "\n");
		}		
		return buffer.toString();
	}

}
