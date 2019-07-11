package org.aieonf.orientdb.core;

import java.util.Arrays;
import java.util.Iterator;

import org.aieonf.concept.core.ConceptBase;
import org.aieonf.concept.core.IConceptBase;

import com.orientechnologies.orient.core.record.impl.ODocument;

public class OConceptBase implements IConceptBase {

	private ODocument doc;
	private boolean changed;

	public OConceptBase( String id ) {
		super();
		this.doc = new ODocument(id);
		this.changed = false;
	}

	@Override
	public boolean hasChanged() {
		return this.changed;
	}

	@Override
	public void setChanged(boolean choice) {
		this.changed = choice;
	}

	@Override
	public void clear() {
		this.changed = true;
		String id = this.doc.getClassName();
		this.doc.clear();
		this.doc.setClassName(id);
	}

	@Override
	public String get(String key) {
		return this.doc.field(key);
	}

	@Override
	public void set(String key, String value) {
		this.doc.field(key, value);
	}

	@Override
	public String get(Enum<?> enm) {
		return get( ConceptBase.getAttributeKey( enm ));
	}

	@Override
	public void set(Enum<?> enm, String value) {
		set( ConceptBase.getAttributeKey( enm ), value );
	}

	@Override
	public void remove(Enum<?> enm) {
		remove( ConceptBase.getAttributeKey( enm ));		
	}

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

	@Override
	public void remove(String key) {
		this.doc.removeField(key);
	}

	@Override
	public int size() {
		return this.doc.fields();
	}

	@Override
	public Iterator<String> iterator() {
		return Arrays.asList( this.doc.fieldNames()).iterator();
	}

}
