package org.aieonf.browsersupport.library.firefox;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.core.Concept;
import org.aieonf.concept.datauri.IDataResource;

public class HistoryVisitsAieon extends Concept implements IDataResource
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3919937519277313629L;
	
	public static final String S_HISTORY_VISITS = "HistoryVisits";

	public enum InputHistoryAttribute{
		id,
		place_id,
		from_visit,
		visit_date,
		visit_type,
		session
	}

	
	public HistoryVisitsAieon() {
		super();
		super.set(IDataResource.Attribute.IS_DATA_URI, Boolean.TRUE.toString());
	}

	public void fill( ResultSet rs ) throws ConceptException{
		try {		  
			setName( S_HISTORY_VISITS );
			setVersion( 1 );
			String str = rs.getString( InputHistoryAttribute.place_id.name() );
			if( !Descriptor.assertNull(str))
				set( InputHistoryAttribute.place_id, str );
			str = rs.getString( InputHistoryAttribute.session.name() );
			if( !Descriptor.assertNull(str))
				setDescription( str );
			str = rs.getString( InputHistoryAttribute.id.name() );
			if( !Descriptor.assertNull(str))
				set( InputHistoryAttribute.id, str );
			str = rs.getString( InputHistoryAttribute.from_visit.name() );
			if( !Descriptor.assertNull(str))
				set( InputHistoryAttribute.from_visit, str );
			str = rs.getString( InputHistoryAttribute.visit_date.name() );
			if( !Descriptor.assertNull(str))
				set( InputHistoryAttribute.visit_date, str );
			str = rs.getString( InputHistoryAttribute.visit_type.name() );
			if( !Descriptor.assertNull(str))
				set( InputHistoryAttribute.visit_type, str );
		}
		catch (SQLException e) {
			throw new ConceptException( e );
		}
	}

	@Override
	public void fill(String type, String resource)
	{
		super.set( IDataResource.Attribute.TYPE.name(), type);
		super.set( IDataResource.Attribute.RESOURCE.name(), resource);
	}

	@Override
	public String getType()
	{
		return super.get( IDataResource.Attribute.TYPE.name() );
	}

	@Override
	public String getResource()
	{
		return super.get( IDataResource.Attribute.RESOURCE.name() );
	}
	
	public int getPlacesId(){
		String str = super.get( InputHistoryAttribute.place_id );
		if( Descriptor.assertNull( str))
			return -1;
		return Integer.parseInt( str );
	}
}
