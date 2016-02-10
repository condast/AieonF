package org.aieonf.util.stardate;

import java.text.DecimalFormat;
import java.util.Calendar;

public class StarDateConvertor
{

	/**
	 * Create a star date from a calendar
	 * @param calendar
	 * @return
	 */
	public static float getStarDate( Calendar calendar ){
		int year = ( calendar.get( Calendar.YEAR ) - 2000 ) * 1000 + (int)(( float)calendar.get( Calendar.DAY_OF_YEAR ) * 1000/365);
		double seconds = (( calendar.get( Calendar.HOUR_OF_DAY )*60 + calendar.get( Calendar.MINUTE ))*60 + calendar.get( Calendar.SECOND ))/86.4;  
		return ( year/10 + ( float )seconds/10000 );
	}

	public static String getStarDateString( Calendar calendar ){
		float starDate = getStarDate( calendar );
		return new DecimalFormat("00000.00").format( starDate );
	}
}
