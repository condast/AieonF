package org.aieonf.commons.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtils {

	public static final String S_SIMPLE_DATE_FORMAT = "dd MM yy";
	public static final String S_DEFAULT_DATE_FORMAT = "dd-MM-yyyy";
	public static final String S_NUMBER_DATE_FORMAT = "ddMMyyyy";

	public static final String S_DEFAULT_TIME_FORMAT = "dd-MM-yyyy, HH:mm:ss";

	public static String getFormatted( Date date ){
		return getFormatted( S_DEFAULT_DATE_FORMAT, date );
	}

	public static String getFormattedTime( Date date ){
		return getFormatted( S_DEFAULT_TIME_FORMAT, date );
	}

	public static String getDateAsNumber( Date date ){
		return getFormatted( S_NUMBER_DATE_FORMAT, date );
	}

	/**
	 * Checks if the check date is overdue, as compare to the given start date
	 * and elapsed time. the field is one of the Calendar constants
	 * @param start
	 * @param field
	 * @param time
	 * @param check
	 * @return
	 */
	public static boolean isOverdue( Date start, int field, int time, Date check) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(start);
		calendar.add(field, time);
		Date end = calendar.getTime();
		return check.after(end);	
	}

	/**
	 * Checks if the current time date is overdue, as compare to the given start date
	 * and elapsed time. the field is one of the Calendar constants
	 * @param start
	 * @param field
	 * @param time
	 * @return
	 */
	public static boolean isOverdue( Date start, int field, int time) {
		return isOverdue( start, field, time, Calendar.getInstance().getTime());
	}

	public static String getFormatted( String pattern, Date date ){
		if( date == null )
			return null;
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format( date );
	}
	
	 /**
	  * Add an offset to the current date
	  * @param field
	  * @param amount
	  * @param current
	  * @return
	  */
	 public static Date addDate( int field, int amount, Date current ){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(current);
			calendar.add( field, amount );
			return calendar.getTime();
	 }
	
	 public static long getDifference(TimeUnit unit, Date d1, Date d2) {
		    long diff = d2.getTime() - d1.getTime();
		    return unit.convert(diff, TimeUnit.MILLISECONDS);
		}
	 
	 public static long getDifferenceDays(Date d1, Date d2) {
		 return getDifference( TimeUnit.DAYS, d1, d2 );
	 }
}
