package org.aieonf.concept.library;

import java.util.Calendar;
import java.util.Locale;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.util.StringStyler;
import org.aieonf.util.Utils;

public class LogEntryAieon extends DateAieon
{
	public enum Attributes{
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

	public LogEntryAieon(String title, Calendar calendar)
	{
		super(calendar);
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
		return super.get(Attributes.LOG_ENTRY );
	}
	
	public void setTitle( String title ){
		super.set( Attributes.LOG_ENTRY, title);
	}

	@Override
	public void setSource( String source ){
		super.set( IConcept.Attributes.SOURCE, source);
	}
	
	public String getCategory(){
		if( Utils.isNull( super.getDescription()))
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
			desc = descriptor.getName();
		return desc;

	}
}
