package org.aieonf.concept.library;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.aieonf.commons.strings.StringStyler;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.implicit.IImplicitAieon;

public class DateAieon extends LocaleAieon implements IImplicitAieon<IDescriptor>
{
	public static String S_YYYY_MM_DD = "yyyy-mm-dd";
	public static String S_YYYY_MM_DD_HH_MM_SS = "yyyy-mm-dd:hh-mm-ss";
	
	public enum Date
	{
		DAY,
		MONTH,
		YEAR;
		
		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		public static boolean isValid( String str ){
			for( Date attr: values() ){
				if( attr.name().equals( str ))
					return true;
			}
			return false;
		}

	}
	
	/*
	 * Serialisation id 
	*/
	private static final long serialVersionUID = -326500342024376510L;

	//Supported attributes
	public static final String DATE = "Date";
	
	/**
	 * Supported attributes
	 * @author Kees Pieters
	 */
	public enum Attributes{
		TIME_IN_MILLIS;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}

	/**
	 * The months in the year
	 * @author Kees Pieters
	*/
	public enum Months{
		January,
		February,
		March,
		April,
		May,
		June,
		July,
		August,
		September,
		October,
		November,
		December
	}
	
	/**
	 * Create a default date aieon 
	*/
	public DateAieon() 
	{
		this( DATE );
	}

	/**
	 * Create a default date aieon 
	*/
	protected DateAieon( String name ) 
	{
		super( name);
		super.setImplicit( Attributes.TIME_IN_MILLIS.name() );
	}

	/**
	 * Create a date aieon for the given calendar
	 * @param calendar Calendar
	 */
	public DateAieon( Calendar calendar )
	{
		super( DATE);
		super.setImplicit( Attributes.TIME_IN_MILLIS.name() );
		this.setTimeInMillis( calendar.getTimeInMillis() );
	}

	/**
	 * Create a date aieon for the given calendar
	 * @param calendar Calendar
	 */
	protected DateAieon( String name, Calendar calendar )
	{
		super( name );
		super.setImplicit( Attributes.TIME_IN_MILLIS.name() );
		this.setTimeInMillis( calendar.getTimeInMillis() );
	}

	/**
	 * Create a date aieon for the given calendar
	 * @param locale
	 * @param calendar Calendar
	 */
	public DateAieon(Locale locale, Calendar calendar )
	{
		super(DATE, locale);
		super.setImplicit( Attributes.TIME_IN_MILLIS.name() );
		this.setTimeInMillis( calendar.getTimeInMillis() );
	}

	/**
	 * Create an aieon for the given calendar and use the given id
	 * @param id
	 * @param locale Locale
	 * @param calendar Calendar
	 */
	public DateAieon(String id, Locale locale, Calendar calendar )
	{
		super(id, locale);
		super.setImplicit( Attributes.TIME_IN_MILLIS.name() );
		this.setTimeInMillis( calendar.getTimeInMillis() );
	}

	public DateAieon( IDescriptor descriptor ) {
		super( descriptor );
		setClassName(this.getClass().getCanonicalName());
		super.setName( DATE );
		super.setImplicit( Attributes.TIME_IN_MILLIS.name() );
	}
	
	/**
	 * Get the calendar for this aieon
	 * @return
	*/
	public Calendar getCalendar()
	{
		Calendar calendar;
		if( super.getLocale() == null )
			calendar = Calendar.getInstance();
		else
			calendar = Calendar.getInstance( super.getLocale() );
		String time = super.get( Attributes.TIME_IN_MILLIS );
		if( Descriptor.assertNull( time ))
			time = String.valueOf( Descriptor.getCreateDate(this).getTime());
		if( time == null )
			return calendar;
		
		calendar.setTimeInMillis( Long.valueOf( time ));
		return calendar;
	}

	/**
	 * Get the time in milliseconds, or the current time if it isn't set
	 * @return
	*/
	public long getTimeInMillis()
	{
		String time = super.get( Attributes.TIME_IN_MILLIS );
		if( time == null ){
			Calendar calendar = Calendar.getInstance( super.getLocale() );
			return calendar.getTimeInMillis();
		}
		return Long.valueOf( time );
	}
	
	/**
	 * Set the time in milliseconds
	 * @param time
	 */
	public void setTimeInMillis( long time )
	{
		super.set( Attributes.TIME_IN_MILLIS, String.valueOf( time ) );
	}
  
	/**
	 * Returns true if the given descriptor is a town aieon
	 * @param descriptor
	 * @return
	*/
	public static boolean isDateAieon( IDescriptor descriptor ){
		return descriptor.getName().equals( LocaleAieon.S_LOCALE );
	} 
	
	/**
	 * Get a formatted date, according to the given format protocol
	 * @param format
	 * @param date
	 * @return
	 */
	public static String getFormattedDate( String format, DateAieon date ){
		SimpleDateFormat dateFormatter = new SimpleDateFormat( format );
		return dateFormatter.format( date.getCalendar().getTime() );
	}	
}