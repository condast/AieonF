package org.aieonf.concept.library;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.wrapper.ConceptWrapper;

public class TemporalAieon extends ConceptWrapper
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5384266043533355573L;

	public enum Attributes{
		CREATEDATE,
		UPDATEDATE;

		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString()
		{
			if( super.toString().equals( Attributes.CREATEDATE.name() ))
				return IDescriptor.Attributes.CREATE_DATE.name();
			if( super.toString().equals( Attributes.UPDATEDATE.name() ))
				return IDescriptor.Attributes.UPDATE_DATE.name();
			return super.toString();
		}
	}
	
	public TemporalAieon(IDescriptor descriptor)
	{
		super(descriptor);
	}

	/**
	 * Get the create date
	 * @return
	 */
	public Calendar getCreateDate(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis( Long.valueOf( super.get( Attributes.CREATEDATE.toString() )));
		return calendar;
	}

	/**
	 * set the create date
	 * @param createDate
	 */
	public void setCreateDate( Calendar calendar ){
		super.set( Attributes.CREATEDATE.toString(), String.valueOf( calendar.getTimeInMillis() ));
	}

	/**
	 * Get the update date
	 * @return
	 */
	public Calendar getUpdateDate(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis( Long.valueOf( super.get( Attributes.UPDATEDATE.toString() )));
		return calendar;
	}

	/**
	 * set the update date
	 * @param createDate
	 */
	public void setUpdateDate( Calendar calendar ){
		super.set( Attributes.UPDATEDATE.toString(), String.valueOf( calendar.getTimeInMillis() ));
	}
	
	public static String getFormattedTime( String format, Calendar calendar ){
		SimpleDateFormat ft = new SimpleDateFormat ( format );
		return ft.format( calendar.getTime() );		
	}
}