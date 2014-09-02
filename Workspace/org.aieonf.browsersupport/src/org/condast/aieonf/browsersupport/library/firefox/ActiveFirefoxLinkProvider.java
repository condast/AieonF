package org.condast.aieonf.browsersupport.library.firefox;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import org.condast.aieonf.browsersupport.context.BrowserSupportContextFactory;
import org.condast.aieonf.browsersupport.service.ActiveLinkData;
import org.condast.aieonf.osgi.library.IWebResourceProvider;
import org.eclipselabs.osgi.ds.broker.service.AbstractPalaver;
import org.eclipselabs.osgi.ds.broker.service.AbstractProvider;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.body.BodyFactory;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.loader.LoaderAieon;


public class ActiveFirefoxLinkProvider extends AbstractProvider<String, String, ActiveLinkData>
{
	public static final String S_FIREFOX = "Firefox";

	private static ActiveFirefoxLinkProvider provider = new ActiveFirefoxLinkProvider();

	private ActiveLinkData data;

	private String S_DATE_ID = "datetime(moz_historyvisits.visit_date/1000000,'unixepoch')";
	private String S_URL_ID = "url";

	private ActiveFirefoxLinkProvider()
	{
		super( new Palaver());
	}

	public static ActiveFirefoxLinkProvider getInstance(){
		if( provider == null )
			provider = new ActiveFirefoxLinkProvider();
		return provider;
	}

	private void getActiveLink() throws ConceptException{
		BrowserSupportContextFactory factory = new BrowserSupportContextFactory();
		factory.createTemplate();
		LoaderAieon loader = new LoaderAieon();
		BodyFactory.transfer(loader, factory.getTemplate().getDescriptor(), true);
		FireFoxModelFunction.fillLoader(loader );
		FireFoxSQLiteConnection connection = new FireFoxSQLiteConnection( loader );
	}

	/**
	 * Get the 
	 * @param file
	 * @return
	 * @throws ConceptException
	 */
	private IWebResourceProvider getWebResource( Connection connection ){
		String query_inputhistory = 
				"SELECT " + S_DATE_ID + ", moz_places.url"+ "" +
						"FROM moz_places, moz_historyvisits WHERE moz_places.id = moz_historyvisits.place_id";

		Collection<IConcept> results = new ArrayList<IConcept>();
		try
		{
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery( query_inputhistory );
			InputHistoryAieon concept = null;

			while(rs.next())
			{
				String date = rs.getString( S_DATE_ID );
				String[] split = date.split("[/s]");
				String url = rs.getString( S_URL_ID );

				/*
			if( !results.isEmpty() )
				results.add(category);

			InputHistoryAieon aieon;
			PlacesAieon placesAieon;
			FireFoxReference faviconAieon;
			Collection<IConcept> removed = new ArrayList<IConcept>();
			for( IConcept result: results ){
				if( result instanceof InputHistoryAieon ){
				  aieon = ( InputHistoryAieon )result;
					int placesId = aieon.getPlacesId();
					if( placesId > 0 ){
				    placesAieon = places.get( placesId );
						String uri = placesAieon.getURI();
						if( uri.startsWith("place:"))
							removed.add( aieon );
				    aieon.setURI(uri );
				    faviconAieon = resources.get( placesAieon.getFavIconID() );
						if(( faviconAieon != null ) && ! Descriptor.isNull( faviconAieon.getURI()))
							aieon.fill("ICON", faviconAieon.getURI() );
				  }
				}
			}
			results.removeAll( removed );
				 */
			}
		}
		catch(SQLException e)
		{
			//throw new Exception(e );
		}
		return null;
	}

	private static class Palaver extends AbstractPalaver<String>{

		private static final String S_INTRODUCTION = "org.aieonf.activelink.introduction";
		private static final String S_TOKEN = "org.aieonf.activelink.token";

		private String providedToken;

		protected Palaver() {
			super( S_INTRODUCTION );
		}

		protected Palaver( String introduction, String token ) {
			super(  introduction );
			this.providedToken = token;
		}

		@Override
		public String giveToken() {
			if( providedToken == null )
				return S_TOKEN;
			return providedToken;
		}

		@Override
		public boolean confirm(Object token) {
			boolean retval = false;
			if( providedToken == null )
				retval = S_TOKEN .equals( token );
			else
				retval = providedToken.equals(token);
			return retval;
		}		
	}
}