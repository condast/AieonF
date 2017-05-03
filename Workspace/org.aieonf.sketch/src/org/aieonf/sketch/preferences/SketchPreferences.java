package org.aieonf.sketch.preferences;

import java.util.Calendar;
import java.util.Date;

import org.aieonf.commons.filter.WildcardFilter;
import org.aieonf.commons.strings.StringStyler;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.osgi.service.prefs.Preferences;

public class SketchPreferences
{
	public static final String PLUGIN_ID = "org.condast.aieonf.links";
	public static final String USER_SETTINGS = "user.settings";

	public static final String S_CATEGORY = "Category";
	

	private Preferences preferences;
	private Preferences userPreferences;
	
	public enum Keys{
		CLEAR,
		SEARCH_TYPE,
		WILDCARD,
		GET_DATE;

		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString()
		{
			return StringStyler.prettyString( super.toString() );
		}
	}
	
	private static SketchPreferences linksPreferences = new SketchPreferences();
	
	private SketchPreferences()
	{
		preferences = ConfigurationScope.INSTANCE.getNode(PLUGIN_ID );
		userPreferences = preferences.node(USER_SETTINGS);
	}
	
	public static SketchPreferences getInstance(){
		return linksPreferences;
	}
	
	public boolean isCleared(){
		return Boolean.valueOf( this.userPreferences.get( Keys.CLEAR.toString(), String.valueOf( true )));
	}

	public void setCleared( boolean choice ){
		this.userPreferences.put( Keys.CLEAR.toString(), String.valueOf( choice ));
	}

	public String getSearchType(){
		return this.userPreferences.get( Keys.SEARCH_TYPE.toString(), S_CATEGORY);
	}
	
	public void setSearchType( String value ){
		this.userPreferences.put( Keys.SEARCH_TYPE.toString(), value);
	}

	public String getWildcard(){
		return this.userPreferences.get( Keys.WILDCARD.toString(), WildcardFilter.S_ALL );
	}
	
	public void setWildcard( String value ){
		this.userPreferences.put( Keys.WILDCARD.toString(), value);
	}

	/**
	 * Get the date that a get operation was performed
	 * @return
	 */
	public Date getGetDate(){
		Calendar calendar = Calendar.getInstance();
		String dt = this.userPreferences.get( Keys.GET_DATE.toString(), String.valueOf( calendar.getTimeInMillis()));
		calendar.setTimeInMillis( Long.valueOf( dt ));
		return calendar.getTime();
	}
	
	public void setGetDate(){
		Calendar calendar = Calendar.getInstance();
		this.userPreferences.put( Keys.GET_DATE.toString(), String.valueOf( calendar.getTimeInMillis()));
	}

}
