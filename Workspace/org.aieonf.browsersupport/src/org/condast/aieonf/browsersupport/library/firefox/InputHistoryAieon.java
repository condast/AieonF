package org.condast.aieonf.browsersupport.library.firefox;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.core.MinimalConcept;
import org.aieonf.concept.datauri.IDataResource;

public class InputHistoryAieon extends MinimalConcept implements IDataResource
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3919937519277313629L;
	
	public static final String S_INPUT_HISTORY = "InputHistory";

	public enum InputHistoryAttribute{
		place_id,
		input,
		use_count
	}

	
	public InputHistoryAieon()
	{
		super( S_INPUT_HISTORY );
	}

	public void fill( ResultSet rs ) throws ConceptException{
		try {		  
			setName( S_INPUT_HISTORY );
			setVersion( 1 );
			String str = rs.getString( InputHistoryAttribute.place_id.name() );
			if( !Descriptor.isNull(str))
				set( InputHistoryAttribute.place_id, str );
			str = rs.getString( InputHistoryAttribute.input.name() );
			if( !Descriptor.isNull(str))
				setDescription( str );
			str = rs.getString( InputHistoryAttribute.use_count.name() );
			if( !Descriptor.isNull(str))
				set( InputHistoryAttribute.use_count, str );
		}
		catch (SQLException e) {
			throw new ConceptException( e );
		}
	}

	@Override
	public void fill(String type, String resource)
	{
		super.set( IDataResource.Attribute.Type, type);
		super.set( IDataResource.Attribute.Resource, resource);
	}

	@Override
	public String getType()
	{
		return super.get( IDataResource.Attribute.Type );
	}

	@Override
	public String getResource()
	{
		return super.get( IDataResource.Attribute.Resource );
	}
	
	public int getPlacesId(){
		String str = super.get( InputHistoryAttribute.place_id );
		if( Descriptor.isNull( str))
			return -1;
		return Integer.parseInt( str );
	}
}
