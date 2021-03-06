package org.aieonf.browsersupport.library.firefox;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.aieonf.browsersupport.library.firefox.BookmarkAieon.BookmarkAttribute;
import org.aieonf.commons.parser.ParseException;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.core.Concept;
import org.aieonf.concept.datauri.IDataResource;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.library.CategoryAieon;
import org.aieonf.concept.library.ManifestAieon;
import org.aieonf.model.collections.ModelCollections;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.model.core.Model;
import org.aieonf.model.core.ModelEvent;
import org.aieonf.model.core.ModelLeaf;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.template.provider.AbstractModelProvider;

class FireFoxSQLiteBookmarkProvider extends AbstractModelProvider<IDomainAieon, IDescriptor, IModelLeaf<IDescriptor>>
{
	private static final String S_IDENTIFER = "FirefoxSQLBookmarks";

	private Connection connection;
	private static final ExecutorService threadpool = 
			Executors.newFixedThreadPool(3);


	FireFoxSQLiteBookmarkProvider( IDescriptor template, IModelLeaf<IDescriptor> model )
	{
		super( S_IDENTIFER, template );
	}

	@Override
	protected void onSetup( ManifestAieon manifest ) {

	}

	@Override
	public boolean onOpen( IDomainAieon domain )
	{
		try {
			Class.forName("org.sqlite.JDBC");
		}
		catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		File file = new File( super.getManifest().getSource() );
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
			return ( connection != null );
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Collection<IModelLeaf<IDescriptor>> onSearch( IModelFilter<IDescriptor, IModelLeaf<IDescriptor>> filter ) throws ParseException {
		{
			getModels().clear();
			threadpool.submit( getBookmarksFuture(this, filter));
			return super.getModels();
		}
	}

	protected Callable<Collection<IModelLeaf<IDescriptor>>> getBookmarksFuture( final Object source, final IModelFilter<IDescriptor, IModelLeaf<IDescriptor>> filter){
		Callable<Collection<IModelLeaf<IDescriptor>>> task = new Callable<Collection<IModelLeaf<IDescriptor>>>(){

			@Override
			public Collection<IModelLeaf<IDescriptor>> call() throws Exception {
				Map<Integer, FireFoxReference>resources;
				Map<Integer, PlacesAieon> places;
				try {
					setBusy();
					places = getPlaces();
					resources = getResources();

					getModels().addAll( getBookmarks( places, resources, filter ));
					IModelLeaf<IDescriptor> result = getHistory(filter);
					if( result != null )
						getModels().add( result );
					result = getHistoryVisits( filter );
					if( result != null )
						getModels().add( result );
					result = getPopularSites( filter );
					if( result != null )
						getModels().add( result );
					notifyListeners( new ModelEvent<>(source, getModels()));
				}
				catch (Exception e) {
					e.printStackTrace();
					throw new ParseException( e );
				}
				finally{
					release();
					if( isRequestClose() )
						close( connection );
				}
				return getModels();
			}

		};
		return task;
	}

	/**
	 * Get the places
	 * @param file
	 * @return
	 * @throws ConceptException
	 */
	protected Map<Integer, PlacesAieon> getPlaces() throws ConceptException{
		String query_places = "SELECT * FROM moz_places";
		Map<Integer, PlacesAieon> places = new HashMap<Integer, PlacesAieon>();
		try{
			//Find all the stored urls, and match them with the bookmarks
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery( query_places );
			PlacesAieon placesAieon;
			while(rs.next())
			{
				placesAieon = new PlacesAieon();
				try {
					placesAieon.fill(rs);
					places.put( BookmarkAieon.getPrimKey(placesAieon ), placesAieon );
				}
				catch (ConceptException e) {
					e.printStackTrace();
				}
			}
		}
		catch(SQLException e)
		{
			throw new ConceptException(e );
		}
		return places;
	}

	/**
	 * Get the resources (places and favicons)
	 * @param file
	 * @return
	 * @throws ConceptException
	 */
	protected Map<Integer, FireFoxReference> getResources() throws ConceptException{
		String query_favicons = "SELECT * FROM moz_icons";
		Map<Integer, FireFoxReference>resources = new HashMap<Integer, FireFoxReference>();
		Connection connect = null;
		try{
			String path = super.getManifest().getSource();
			path = path.replace("places", "favicons");
			File file = new File( path );
			connect = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());

			//Find all the stored urls, and match them with the bookmarks
			Statement statement = connect.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery( query_favicons );
			FireFoxReference resource;
			while(rs.next())
			{
				resource = new FireFoxReference();
				try {
					resource.fill( rs );
					resources.put( BookmarkAieon.getPrimKey( resource ), resource);
				}
				catch (ConceptException e) {
					e.printStackTrace();
				}
			}
			return resources;
		}
		catch(SQLException e){
			throw new ConceptException(e );
		}
		finally {
			try {
				connect.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Get the bookamrks, and add the resources that were found earlier, like fav-icons
	 * @param file
	 * @return
	 * @throws ConceptException
	 */
	private Collection<IModelNode<IDescriptor>> getBookmarks( Map<Integer, PlacesAieon> places, Map<Integer, FireFoxReference> resources, IModelFilter<IDescriptor, IModelLeaf<IDescriptor>> filter ) throws ConceptException{
		String query_bookmarks = "SELECT * FROM moz_bookmarks";

		Collection<IModelLeaf<IDescriptor>> results = new ArrayList<IModelLeaf<IDescriptor>>();
		Map<Integer, IModelNode<IDescriptor>> categories = new TreeMap<Integer, IModelNode<IDescriptor>>();
		try
		{
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery( query_bookmarks );
			while(rs.next())
			{
				try {
					BookmarkAieon concept = new BookmarkAieon();
					concept.fill(rs);
					super.fill(concept);
					if( CategoryAieon.isCategory( concept )){
						CategoryAieon category = new CategoryAieon( concept );
						category.setProvider( super.getManifest().getIdentifier() );
						IModelLeaf<IDescriptor> leaf = new ModelLeaf<IDescriptor>( category ); 
						leaf.setReadOnly(true);
						if( filter.accept( leaf )) {
							Model<IDescriptor> model = new Model<IDescriptor>(category );
							model.setReadOnly(true);
							categories.put( BookmarkAieon.getPrimKey( category ), model );
						}
					}else{
						IModelLeaf<IDescriptor> child = new ModelLeaf<IDescriptor>(concept);
						child.setReadOnly(true);
						if( filter.acceptChild(child))
							results.add( child );
					}
				}
				catch (ConceptException e) {
					e.printStackTrace();
				}
			}

			for( IModelLeaf<IDescriptor> result: results ){
				result.setReadOnly(true);
				BookmarkAieon aieon = ( BookmarkAieon )result.getData();

				//skip categories
				String fk = aieon.getFK();
				if( Descriptor.assertNull(fk))
					continue;

				PlacesAieon placesAieon = places.get( Integer.parseInt( fk ));

				String uri = placesAieon.getSource();
				if( uri.startsWith("place:"))
					continue;
				aieon.setSource(uri );
				FireFoxReference faviconAieon = resources.get( placesAieon.getFavIconID() );
				if(( faviconAieon != null ) && ! Descriptor.assertNull( faviconAieon.getSource()))
					aieon.fill("ICON", faviconAieon.getSource() );
				IModelNode<IDescriptor> cat = categories.get( Integer.valueOf( aieon.getParentID()));
				if( cat == null )
					continue;
				cat.setReadOnly(true);
				cat.addChild( result );
			}
		}
		catch(SQLException e)
		{
			throw new ConceptException(e );
		}
		ModelCollections.cleanup( categories.values() );
		return categories.values();
	}

	/**
	 * Get the 
	 * @param file
	 * @return
	 * @throws ConceptException
	 */
	private IModelLeaf<IDescriptor> getHistory( IModelFilter<IDescriptor, IModelLeaf<IDescriptor>> filter ) throws ConceptException{
		String query_inputhistory = "SELECT * FROM moz_places, moz_inputhistory WHERE moz_places.id = moz_inputhistory.place_id";
		return this.getConceptsFromQuery("Input History", query_inputhistory, filter );
	}

	/**
	 * Get the most popular sites according to the Firefox database
	 * @param file
	 * @return
	 * @throws ConceptException
	 */
	protected IModelLeaf<IDescriptor> getPopularSites( IModelFilter<IDescriptor, IModelLeaf<IDescriptor>> filter ) throws ConceptException{
		String query_inputhistory = "SELECT * FROM moz_places ORDER by visit_count DESC LIMIT 20";
		return this.getConceptsFromQuery("Most Popular", query_inputhistory, filter );
	}

	/**
	 * Get the most popular sites according to the Firefox database
	 * @param file
	 * @return
	 * @throws ConceptException
	 */
	private IModelLeaf<IDescriptor> getConceptsFromQuery( String title, String query, IModelFilter<IDescriptor, IModelLeaf<IDescriptor>> filter ) throws ConceptException{
		IModelNode<IDescriptor> model = null;
		try
		{
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery( query );
			PlacesAieon concept = null;
			CategoryAieon category = new CategoryAieon( title );
			super.fill(category);
			category.setProvider( super.getManifest().getIdentifier() );
			model = new Model<IDescriptor>( category );
			model.setReadOnly(true);
			if( !filter.accept( model ))
				return null;
			while(rs.next())
			{
				try {
					concept = new PlacesAieon();
					concept.fill(rs);
					super.fill(concept);
					IModelLeaf<IDescriptor> child = new ModelLeaf<IDescriptor>( concept ); 
					child.setReadOnly(true);
					if( filter.acceptChild(child))
						model.addChild( child );
				}
				catch (ConceptException e) {
					e.printStackTrace();
				}
			}
		}
		catch(SQLException e)
		{
			throw new ConceptException(e );
		}
		return model;
	}

	/**
	 * Get the 
	 * @param file
	 * @return
	 * @throws ConceptException
	 */
	protected IModelLeaf<IDescriptor> getHistoryVisits( IModelFilter<IDescriptor, IModelLeaf<IDescriptor>> filter ) throws ConceptException{
		String query_inputhistory = "SELECT * FROM moz_places WHERE moz_places.id = ( SELECT place_id FROM moz_historyvisits )";
		IModelNode<IDescriptor> model;
		try
		{
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery( query_inputhistory );
			PlacesAieon concept = null;
			CategoryAieon category = new CategoryAieon("History Visits");
			super.fill(category);
			category.setProvider( super.getManifest().getIdentifier() );
			model = new Model<IDescriptor>( category );
			model.setReadOnly(true);
			if( !filter.accept( model ))
				return null;
			while(rs.next())
			{
				try {
					concept = new PlacesAieon();
					concept.fill(rs);
					super.fill(concept);
					IModelLeaf<IDescriptor> leaf = new ModelLeaf<IDescriptor>( concept );
					leaf.setReadOnly(true);
					model.addChild( leaf );
				}
				catch (ConceptException e) {
					e.printStackTrace();
				}
			}
		}
		catch(SQLException e)
		{
			throw new ConceptException(e );
		}
		return model;
	}

	protected Map<Integer, String> getPlaces( File file, String query ) throws ClassNotFoundException{
		// load the sqlite-JDBC driver using the current class loader
		Map<Integer, String> places = new TreeMap<Integer, String>();
		Class.forName("org.sqlite.JDBC");

		try
		{
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery( query );
			while(rs.next())
			{
				places.put( rs.getInt( "id" ), rs.getString("url" ));
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return places;
	}

	protected void execute( File file, String query ) throws ClassNotFoundException{
		// load the sqlite-JDBC driver using the current class loader
		Class.forName("org.sqlite.JDBC");

		try
		{
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery( query );
			while(rs.next())
			{
				// read the result set
				//System.out.println("name = " + rs.getString("name"));
				System.out.println("id = " + rs.getInt("id"));
			}
		}
		catch(SQLException e){
			// if the error message is "out of memory", 
			// it probably means no database file is found
			e.printStackTrace();
		}
	}

	protected void close( Connection connection )
	{
		if(isPending())
			return;
		super.close();
		try
		{
			if(connection != null)
				connection.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void close(){
		super.setRequestClose( true );
	}

	private static class PlacesAieon extends Concept implements IDataResource
	{
		private static final long serialVersionUID = 3919937519277313629L;

		public enum PlacesAttribute{
			ID,
			URL,
			TITLE,
			GUID,
			REV_HOST,
			VISIT_COUNT,
			HIDDEN,
			TYPED,
			FAVICON_ID,
			FRECENCY,
			LAST_VISIT_DATE;

			@Override
			public String toString() {
				return this.name().toLowerCase();
			}
		}

		public void fill( ResultSet rs ) throws ConceptException{
			try {
				for( PlacesAttribute pa: PlacesAttribute.values()) {
					int index = rs.findColumn(pa.toString());
					String value;
					switch( pa ) {
					case GUID:
						value = rs.getString( index );
						setValue( IDescriptor.Attributes.ID, value );
						setVersion( 1 );
						setValue( IDescriptor.Attributes.UPDATE_DATE, value );
						break;
					case ID:
						value = rs.getString( index );
						set( BookmarkAttribute.primkey, value );
						break;
					case URL:
						value = rs.getString( index );
						if( !Descriptor.assertNull(value))
							set( IConcept.Attributes.SOURCE.name(), value );
						break;
					case TITLE:
						value = rs.getString( index );
						if( !Descriptor.assertNull(value)){
							String name = Descriptor.createValidName( value );
							this.setName( name );
							if( name.length() < value.length() )
								this.setDescription( value );
						}
						break;
					default:
						value = rs.getString( index );
						if( !Descriptor.assertNull(value))
							set( pa, value );
						break;
						
					}
				}
			}
			catch (SQLException e) {
				throw new ConceptException( e );
			}
		}

		@Override
		public void fill(String type, String resource)
		{
			super.set( IDataResource.Attribute.TYPE.name(), type);
			super.setSource( resource);
		}

		@Override
		public String getType()
		{
			return super.get( IDataResource.Attribute.TYPE.name() );
		}

		@Override
		public String getResource()
		{
			return super.getSource();
		}

		public int getFavIconID()
		{
			String str = super.get( PlacesAttribute.FAVICON_ID );
			if( !Descriptor.isEmpty( str ))
				return Integer.parseInt( str );
			return -1;
		}
	}
}