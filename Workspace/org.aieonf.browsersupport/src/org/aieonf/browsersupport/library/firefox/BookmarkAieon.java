package org.aieonf.browsersupport.library.firefox;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.datauri.DataURI;
import org.aieonf.concept.datauri.IDataResource;
import org.aieonf.concept.datauri.IDataURI;
import org.aieonf.concept.library.CategoryAieon;

public class BookmarkAieon extends DataURI implements IDataResource
{
	private static final long serialVersionUID = 3919937519277313629L;

	public enum BookmarkAttribute{
		fk,
		primkey,
		position,
		parent,
		title,
		type,
		keyword_id,
		folder_type,
		guid,
		dateAdded,
		lastModified
	}

	
	public BookmarkAieon() {
		super();
	}

	public void fill( ResultSet rs ) throws ConceptException{
		try {
			setValue( IDescriptor.Attributes.ID, rs.getString( BookmarkAttribute.guid.name() ));
			setVersion( 1 );
			setValue( IDescriptor.Attributes.CREATE_DATE, rs.getString( BookmarkAttribute.dateAdded.name() ));
			setValue( IDescriptor.Attributes.UPDATE_DATE, rs.getString( BookmarkAttribute.lastModified.name() ));

			String fk = rs.getString( BookmarkAttribute.fk.name() );
			set( BookmarkAttribute.fk, fk );
			long id = StringUtils.isEmpty(fk)?-1: Long.parseLong(fk);
			setID( id );

			int type = rs.getInt( BookmarkAttribute.type.name() );
			set( IDataResource.Attribute.TYPE, rs.getString( BookmarkAttribute.type.name() ));

			int parent = rs.getInt( BookmarkAttribute.parent.name() );
			set( BookmarkAttribute.primkey, rs.getString( IDescriptor.Attributes.ID.toString().toLowerCase() ));
			set( BookmarkAttribute.parent, Integer.toString( parent ));
			set( BookmarkAttribute.position, rs.getString( BookmarkAttribute.position.name() ));
			set( BookmarkAttribute.keyword_id, rs.getString( BookmarkAttribute.keyword_id.name() ));
			set( BookmarkAttribute.folder_type, rs.getString( BookmarkAttribute.folder_type.name() ));
			set( BookmarkAttribute.guid, rs.getString( BookmarkAttribute.guid.name() ));
			String title = rs.getString( "title" );
			setDescription( title );							
			switch( type ){
			case 1:
				if(( !Descriptor.assertNull( fk ) && !Descriptor.assertNull( title ))) {
					if( parent != 5 ){
						String name = Descriptor.createValidName( title );
						set( IDescriptor.Attributes.NAME.name(), name );
						if( name.length() < title.length() )
							this.setDescription( title );
					}else{
						set( CategoryAieon.Attributes.CATEGORY.name(), title );
						setValue( IDescriptor.Attributes.NAME, CategoryAieon.Attributes.CATEGORY.name() );							
					}
				}
				break;
			case 2:
				if( !Descriptor.assertNull( title )) {
					set( CategoryAieon.Attributes.CATEGORY.name(), title );
					setValue( IDescriptor.Attributes.NAME, CategoryAieon.Attributes.CATEGORY.name() );
				}
				break;
			default:
				break;
			}
		}
		catch (SQLException e) {
			throw new ConceptException( e );
		}
		super.set(IDataResource.Attribute.IS_DATA_URI, Boolean.TRUE.toString());
		super.set(IDataURI.Attribute.MIME_TYPE, S_DEFAULT_MIMETYPE );
	}

	@Override
	public void fill(String type, String resource)
	{
		super.set( IDataResource.Attribute.TYPE, type);
		super.set( IDataResource.Attribute.RESOURCE, resource);
		super.set(IDataResource.Attribute.IS_DATA_URI, Boolean.TRUE.toString());
		super.set(IDataURI.Attribute.MIME_TYPE, S_DEFAULT_MIMETYPE );
	}


	@Override
	public void setSource(String source) {
		super.setSource(source);
	}

	@Override
	public String getType()
	{
		return super.get( IDataResource.Attribute.TYPE );
	}

	@Override
	public String getResource()
	{
		return super.get( IDataResource.Attribute.RESOURCE );
	}

	public String getPrimKey(){
		return this.get(BookmarkAttribute.primkey );
	}

	public String getFK(){
		return this.get(BookmarkAttribute.fk );
	}

	public String getParentID(){
		return this.get(BookmarkAttribute.parent );
	}

	public static String getFK( IConcept concept ){
		return concept.get(BookmarkAttribute.fk );
	}

	public static Integer getPrimKey( IConcept concept ){
		return Integer.valueOf( concept.get(BookmarkAttribute.primkey ));
	}
}
