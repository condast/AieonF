package org.aieonf.browsersupport.library.firefox;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import org.aieonf.commons.strings.StringStyler;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.core.Concept;
import org.aieonf.concept.datauri.IDataResource;
import org.aieonf.concept.datauri.IDataURI;
import org.aieonf.concept.wrapper.ConceptWrapper;

public class FireFoxReference extends ConceptWrapper implements IDataResource{
	private static final long serialVersionUID = -290451331218647891L;

	public static final String S_HREF = "href";
	public static final String S_ID= "id";
	public static final String S_EMPTY = "empty";
	public static final String S_DATA = "data";

	public static final String S_ERR_ILLEGAL_REFERENCE_1 = 
			"Illegal Reference; Should minimally have the form <A HREF ID>";
	public static final String S_ERR_ILLEGAL_REFERENCE_2 = 
			"Illegal Reference; Invalid <key> =<value> pair: ";
	public static final String S_ERR_ILLEGAL_DATA_URI_3 = 
			"Illegal Data URI; Should have the form <type>=data:mimetype/encoding,data>";
	public static final String S_ERR_ILLEGAL_DATA_URI_4 = 
			"Illegal MIMETYPE; Should have the form <type>/<value>";

	public enum Attribute{
		DataUri
	}

	public enum Columns{
		ID,
		ICON_URL,
		FIXED_ICON_URL_HASH,
		WIDTH,
		ROOT,
		COLOR,
		EXPIRE_MS,
		DATA;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		public static String toColumnName( Columns column){
			String str = column.name().toLowerCase();
			return str;
		}	
	}

	public FireFoxReference()
	{
		super( new Concept());
	}

	/**
	 * Fill the Data URI with the given datauri string
	 * @param datauri
	 * @throws ConceptException
	 */
	public void fill( String datauri ) throws ConceptException{
		String[] split = datauri.split("[ ]");
		if( split.length < 3 )
			throw new ConceptException( S_ERR_ILLEGAL_REFERENCE_1 );

		String[] strSplit;		
		String key,value;
		String str;
		boolean correct = false;
		for( int i=1; i<split.length; i++ ){
			str = split[ i ].replace("\"", "").trim();
			strSplit = str.split("[=]");
			if( strSplit.length != 2 )
				throw new ConceptException( S_ERR_ILLEGAL_REFERENCE_2 + split[i]);

			key = strSplit[0].toLowerCase().trim();
			value = strSplit[1].replace("\"", "").trim();
			if( key.equals( S_HREF )){
				super.setSource( value );
				correct = true;
			}
			if( key.equals( S_ID ))
				super.set( IDescriptor.Attributes.ID.toString(), value );
			if(( split.length > 3 ) && ( i==2 )){
				this.fill( key, value );
			}
		}
		if( correct == false )
			throw new ConceptException( S_ERR_ILLEGAL_REFERENCE_2 );
	}

	public boolean isDataUri() throws ConceptException{
		return this.getBoolean( Attribute.DataUri.name() );
	}

	/**
	 * Fill the Data URI with the given datauri string
	 * @param type
	 * @param resource
	 */
	@Override
	public void fill( String type, String resource ){
		set( IDataResource.Attribute.Type, type );		
		set( IDataResource.Attribute.Resource, resource );
		if( resource.startsWith( S_DATA))
			super.set( Attribute.DataUri.name(), "true");
		setVersion( 1 );
		setReadOnly( true );
		setScope( IConcept.Scope.PRIVATE );
		Calendar calendar = Calendar.getInstance();
		set( IDescriptor.Attributes.CREATE_DATE.toString(), calendar.getTime().toString() );
		set( IDescriptor.Attributes.UPDATE_DATE.toString(), calendar.getTime().toString() );
	}

	/**
	 * Fill the Data URI with the given datauri string
	 * @param TYPE
	 * @param resource
	 * @throws ConceptException
	 */
	public void fill( ResultSet rs ) throws ConceptException{
		try {
			String str =  rs.getString( Columns.toColumnName( Columns.ID ));
			if( !Descriptor.assertNull(str))
				set( IDescriptor.Attributes.ID, str );		
			set( IDataURI.Attribute.MIME_TYPE, "text/plain" );		
			str =  rs.getString( Columns.toColumnName( Columns.DATA ));
			if( !Descriptor.assertNull(str)){
				set( IDataResource.Attribute.Resource, str );		
				super.set( Attribute.DataUri.name(), "true");
			}
			str =  rs.getString( Columns.toColumnName( Columns.ICON_URL ));
			if( !Descriptor.assertNull(str)){
				setSource( str );		
			}
			set( BookmarkAieon.BookmarkAttribute.primkey, rs.getString( "id" ));
		}
		catch (SQLException e) {
			throw new ConceptException( e );
		}
		setVersion( 1 );
		setReadOnly( true );
		setScope( IConcept.Scope.PRIVATE );
		Calendar calendar = Calendar.getInstance();
		set( IDescriptor.Attributes.CREATE_DATE.toString(), calendar.getTime().toString() );
		set( IDescriptor.Attributes.UPDATE_DATE.toString(), calendar.getTime().toString() );
	}

	@Override
	public String getType()
	{
		String str = super.get( IDataResource.Attribute.Type );
		if( Descriptor.assertNull( str ))
			return S_EMPTY;
		return str;
	}

	@Override
	public String getResource()
	{
		return super.get(  IDataResource.Attribute.Resource );
	}

	public void setResource( String resource ) throws ConceptException{
		super.set( IDataResource.Attribute.Resource, resource );
	}

}
