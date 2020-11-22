package org.aieonf.concept.library;

import java.util.Calendar;
import java.util.Locale;

import org.aieonf.commons.strings.StringStyler;
import org.aieonf.commons.strings.StringUtils;
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

	public LogEntryAieon(){
		super( Attributes.LOG_ENTRY.toString() );
	}

	public LogEntryAieon(String title){
		this( title, Calendar.getInstance());
	}

	public LogEntryAieon(String title, Calendar calendar){
		super( Attributes.LOG_ENTRY.toString(), calendar);
		this.setTitle(title);
	}

	public LogEntryAieon( String title, Locale locale, Calendar calendar){
		super( Attributes.LOG_ENTRY.toString(), locale, calendar);
		this.setTitle(title);
	}

	public LogEntryAieon(String id, String title, Locale locale, Calendar calendar){
		super(id, locale, calendar);
		this.setTitle(title);
		super.setName( Attributes.LOG_ENTRY.toString() );
	}
	
	public LogEntryAieon( IDescriptor descriptor ) {
		super( descriptor );
		setClassName(this.getClass().getCanonicalName());
		super.setName( Attributes.LOG_ENTRY.toString() );
	}


	public String getTitle(){
		return super.get(Attributes.TITLE.name() );
	}
	
	public void setTitle( String title ){
		super.set( Attributes.TITLE.name(), title);
	}

	public String getLogEntry(){
		String str = super.get(Attributes.LOG_ENTRY ); 
		return StringUtils.isEmpty(str)?"{new log}": str;
	}
	
	public void setLogEntry( String log ){
		super.set( Attributes.LOG_ENTRY.name(), log);
	}

	@Override
	public void setSource( String source ){
		super.setValue( IConcept.Attributes.SOURCE, source);
	}
}
