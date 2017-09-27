package org.aieonf.orientdb.cache;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.ConceptException;

import com.orientechnologies.orient.core.record.impl.ODocument;

public class ODescriptor implements IDescriptor{
	private static final long serialVersionUID = 1L;
	
	private ODocument document;
	
	public ODescriptor( ODocument document ) {
		this.document = document;
	}

	@Override
	public IDescriptor getDescriptor() {
		return this;
	}

	@Override
	public boolean hasChanged() {
		// TODO Auto-generated method stub
		return false;
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

	@Override
	public String getName() {
		return document.field( IDescriptor.Attributes.NAME.toString() );
	}

	@Override
	public String getID() {
		return document.field( IDescriptor.Attributes.ID.toString() );
	}

	@Override
	public int getVersion() {
		return document.field( IDescriptor.Attributes.VERSION.toString() );
	}

	@Override
	public void clear() {
		this.document.clear();
	}

	@Override
	public String get(String key) {
		return document.field( key);
	}

	@Override
	public void set(String key, String value) {
		document.field(key, value );
	}

	@Override
	public String get(Enum<?> enm) {
		return document.field( enm.name() );
	}

	@Override
	public void set(Enum<?> enm, String value) {
		document.field( enm.name(), value );
	}

	@Override
	public String getFromExtendedKey(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setVersion(int version) {
		this.set( Attributes.VERSION, String.valueOf( version ));
	}

	@Override
	public String getSignature() {
		return this.get( Attributes.SIGNATURE );
	}

	@Override
	public String getDescription() {
		return this.get( Attributes.DESCRIPTION );
	}

	@Override
	public void setDescription(String description) {
		this.set( Attributes.DESCRIPTION, description );
	}

	@Override
	public String getClassName() {
		return this.get( Attributes.CLASS );
	}

	@Override
	public String getProvider() {
		return this.get( Attributes.PROVIDER );
	}

	@Override
	public void setProvider(String provider) {
		this.set( Attributes.PROVIDER, provider );		
	}

	@Override
	public String getProviderName() {
		return this.get( Attributes.PROVIDER_NAME );
	}

	@Override
	public void setProviderName(String provider) {
		this.set( Attributes.PROVIDER_NAME, provider );		
	}

	@Override
	public void sign(String signature) throws ConceptException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getBoolean(String attribute) {
		return Boolean.parseBoolean( get( attribute ));
	}

	@Override
	public int getInteger(String attribute) {
		return Integer.parseInt( get( attribute ));
	}

	@Override
	public boolean objEquals(Object obj) {
		return this.equals( obj );
	}

	@Override
	public String toStringArray() {
		return null;
	}

	@Override
	public String toXML() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<String> iterator() {
		Collection<String> names = Arrays.asList( document.fieldNames() );
		return names.iterator();
	}

	public ODocument getDocument() {
		return document;
	}

	@Override
	public int size() {
		return document.fields();
	}
}
