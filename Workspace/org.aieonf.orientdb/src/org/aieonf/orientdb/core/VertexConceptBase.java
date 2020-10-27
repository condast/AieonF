package org.aieonf.orientdb.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.aieonf.commons.strings.StringStyler;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.ConceptBase;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.core.IConceptBase;

import com.tinkerpop.blueprints.Vertex;

public class VertexConceptBase implements IConceptBase {

	//The properties collection that contains the data
	private transient Vertex vertex;

	//If true, something has changed
	private transient boolean changed;

	public VertexConceptBase( Vertex vertex ){
		this.vertex = vertex;
		this.changed = false;
	}
	
	public Vertex getVertex() {
		return vertex;
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
		Iterator<String> iterator = vertex.getPropertyKeys().iterator();
		while( iterator.hasNext() )
			this.vertex.removeProperty( iterator.next());
		this.changed = true;
	}

	/* (non-Javadoc)
	 * @see org.aieonf.concept.core.IConceptBase#get(java.lang.String)
	 */
	@Override
	public final String get( String key )
	{
		return vertex.getProperty( StringStyler.fromPackageString( key ));
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
		vertex.setProperty(StringStyler.fromPackageString( key ), value );
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
		vertex.removeProperty( StringStyler.styleToEnum( id ));
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
		this.vertex.removeProperty( StringStyler.prettyString( key ));
	}

	/* (non-Javadoc)
	 * @see org.aieonf.concept.core.IConceptBase#size()
	 */
	@Override
	public int size()
	{
		return vertex.getPropertyKeys().size();
	}

	/* (non-Javadoc)
	 * @see org.aieonf.concept.core.IConceptBase#iterator()
	 */
	@Override
	public final Iterator<String> keySet()
	{
		Iterator<String> iterator = this.vertex.getPropertyKeys().iterator() ;
		Collection<String> results = new ArrayList<String>();
		while( iterator.hasNext() ){
			results.add( StringStyler.toPackageString( iterator.next() ));
		}
		return results.iterator();
	}

	
	@Override
	public Set<Map.Entry<String, String>> entrySet() {
		Iterator<String> iterator = this.vertex.getPropertyKeys().iterator() ;
		Map<String, String> results = new HashMap<>();
		while( iterator.hasNext() ){
			String key = StringStyler.toPackageString(iterator.next());
			results.put( key, (String) vertex.getProperty(key));
		}
		return results.entrySet();
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
		Iterator<String> iterator = vertex.getPropertyKeys().iterator();
		while( iterator.hasNext() ){
			String key = iterator.next();
			buffer.append( "\t"+ StringStyler.toPackageString( key ) + "=" +
			vertex.getProperty( key ) + "\n");
		}		
		return buffer.toString();
	}

}
