package org.aieonf.concept.library;

import java.util.Calendar;
import java.util.Locale;

import org.aieonf.commons.Utils;
import org.aieonf.commons.strings.StringStyler;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;

public class LogEntryAieon extends DateAieon
{
	public static final String S_DATE_FORMAT = "dd.MM.yyyy-HH:mm:ss";

	public enum Attributes{
		TITLE,
		LOG_ENTRY;

		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString()
		{
			return StringStyler.prettyString( super.toString() );
		}
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3112475589933520018L;

	public LogEntryAieon()
	{
		super( Attributes.LOG_ENTRY.toString() );
	}

	public LogEntryAieon(String title)
	{
		this( title, Calendar.getInstance());
	}

	public LogEntryAieon(String title, Calendar calendar)
	{
		super( Attributes.LOG_ENTRY.toString(), calendar);
		this.setTitle(title);
	}

	public LogEntryAieon( String title, Locale locale, Calendar calendar)
	{
		super( Attributes.LOG_ENTRY.toString(), locale, calendar);
		this.setTitle(title);
	}

	public LogEntryAieon(String id, String title, Locale locale, Calendar calendar)
	{
		super(id, locale, calendar);
		this.setTitle(title);
		super.setName( Attributes.LOG_ENTRY.toString() );
	}

	public LogEntryAieon(IDescriptor descriptor)
	{
		super(descriptor);
	}

	public String getTitle(){
		return super.get(Attributes.TITLE );
	}
	
	public void setTitle( String title ){
		super.set( Attributes.TITLE, title);
	}

	@Override
	public void setSource( String source ){
		super.set( IConcept.Attributes.SOURCE, source);
	}
	
	public String getCategory(){
		if( Utils.assertNull( super.getDescription()))
			return null;
		return super.getDescription().split("[\\s]")[0];
	}
	
	/**
	 * Give a description that is never null
	 * @param descriptor
	 * @return
	 */
	public static String getDescription( IDescriptor descriptor ){
		String desc = descriptor.getDescription();
		if( desc == null )
			desc = descriptor.get( Attributes.TITLE );
		if( desc == null )
			desc = descriptor.getName();
		return desc;

	}
}
