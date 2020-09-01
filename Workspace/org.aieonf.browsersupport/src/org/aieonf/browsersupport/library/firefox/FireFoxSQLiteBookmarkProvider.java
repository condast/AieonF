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
import org.aieonf.concept.context.IContextAieon;
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


	FireFoxSQLiteBookmarkProvider( IContextAieon template, IModelLeaf<IDescriptor> model )
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
	public Collection<IModelLeaf<IDescriptor>> onSearch( IModelFilter<IModelLeaf<IDescriptor>> filter ) throws ParseException {
		{
			getModels().clear();
			threadpool.submit( getBookmarksFuture(this, filter));
			return super.getModels();
		}
	}

	protected Callable<Collection<IModelLeaf<IDescriptor>>> getBookmarksFuture( final Object source, final IModelFilter<IModelLeaf<IDescriptor>> filter){
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
	 * Get the resources (places and favicons)
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
		String query_favicons = "SELECT * FROM moz_favicons";
		Map<Integer, FireFoxReference>resources = new HashMap<Integer, FireFoxReference>();
		try{
			//Find all the stored urls, and match them with the bookmarks
			Statement statement = connection.createStatement();
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
		catch(SQLException e)
		{
			throw new ConceptException(e );
		}
	}
	
	/**
	 * Get the 
	 * @param file
	 * @return
	 * @throws ConceptException
	 */
	private Collection<IModelNode<IDescriptor>> getBookmarks( Map<Integer, PlacesAieon> places, Map<Integer, FireFoxReference> resources, IModelFilter<IModelLeaf<IDescriptor>> filter ) throws ConceptException{
		String query_bookmarks = "SELECT * FROM moz_bookmarks";

		Collection<IModelLeaf<IDescriptor>> results = new ArrayList<IModelLeaf<IDescriptor>>();
		Map<Integer, IModelNode<IDescriptor>> categories = new TreeMap<Integer, IModelNode<IDescriptor>>();
		try
		{
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery( query_bookmarks );
			BookmarkAieon concept = null;
			CategoryAieon category;
			while(rs.next())
			{
				try {
					concept = new BookmarkAieon();
					concept.fill(rs);
					super.fill(concept);
					if( CategoryAieon.isCategory( concept )){
						category = new CategoryAieon( concept );
						category.setProvider( super.getManifest().getIdentifier() );
						if( filter.accept( new ModelLeaf<IDescriptor>( category )))
							categories.put( BookmarkAieon.getPrimKey( category ), new Model<IDescriptor>(category ));
					}else{
						IModelLeaf<IDescriptor> child = new ModelLeaf<IDescriptor>(concept);
						if( filter.acceptChild(child))
							results.add( child );
					}
				}
				catch (ConceptException e) {
					e.printStackTrace();
				}
			}

			BookmarkAieon aieon;
			PlacesAieon placesAieon;
			FireFoxReference faviconAieon;
			for( IModelLeaf<IDescriptor> result: results ){
				aieon = ( BookmarkAieon )result.getDescriptor();
				
				//skip categories
				String fk = aieon.getFK();
				if( Descriptor.isNull(fk))
					continue;

				placesAieon = places.get( Integer.parseInt( fk ));

				String uri = placesAieon.getSource();
				if( uri.startsWith("place:"))
					continue;
				aieon.setSource(uri );
				faviconAieon = resources.get( placesAieon.getFavIconID() );
				if(( faviconAieon != null ) && ! Descriptor.isNull( faviconAieon.getSource()))
					aieon.fill("ICON", faviconAieon.getSource() );
				IModelNode<IDescriptor> cat = categories.get( Integer.valueOf( aieon.getParentID()));
				if( cat == null )
					continue;

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
	private IModelLeaf<IDescriptor> getHistory( IModelFilter<IModelLeaf<IDescriptor>> filter ) throws ConceptException{
		String query_inputhistory = "SELECT * FROM moz_places, moz_inputhistory WHERE moz_places.id = moz_inputhistory.place_id";
		return this.getConceptsFromQuery("Input History", query_inputhistory, filter );
	}

	/**
	 * Get the most popular sites according to the Firefox database
	 * @param file
	 * @return
	 * @throws ConceptException
	 */
	protected IModelLeaf<IDescriptor> getPopularSites( IModelFilter<IModelLeaf<IDescriptor>> filter ) throws ConceptException{
		String query_inputhistory = "SELECT * FROM moz_places ORDER by visit_count DESC LIMIT 20";
		return this.getConceptsFromQuery("Most Popular", query_inputhistory, filter );
	}

	/**
	 * Get the most popular sites according to the Firefox database
	 * @param file
	 * @return
	 * @throws ConceptException
	 */
	private IModelLeaf<IDescriptor> getConceptsFromQuery( String title, String query, IModelFilter<IModelLeaf<IDescriptor>> filter ) throws ConceptException{
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
			if( !filter.accept( model ))
				return null;
			while(rs.next())
			{
				try {
					concept = new PlacesAieon();
					concept.fill(rs);
					super.fill(concept);
					IModelLeaf<IDescriptor> child = new ModelLeaf<IDescriptor>( concept ); 
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
	protected IModelLeaf<IDescriptor> getHistoryVisits( IModelFilter<IModelLeaf<IDescriptor>> filter ) throws ConceptException{
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
			if( !filter.accept( model ))
				return null;
			while(rs.next())
			{
				try {
					concept = new PlacesAieon();
					concept.fill(rs);
					super.fill(concept);
					model.addChild( new ModelLeaf<IDescriptor>( concept ));
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
			id,
			url,
			title,
			guid,
			rev_host,
			visit_count,
			hidden,
			typed,
			favicon_id,
			frecency,
			last_visit_date
		}
		
		public void fill( ResultSet rs ) throws ConceptException{
			try {
				set( IDescriptor.Attributes.ID, rs.getString( PlacesAttribute.guid.name() ));
				setVersion( 1 );
				set( IDescriptor.Attributes.UPDATE_DATE, rs.getString( PlacesAttribute.last_visit_date.name() ));
			  
				String str = rs.getString( PlacesAttribute.id.name() );
				set( BookmarkAttribute.primkey, str );
				str = rs.getString( PlacesAttribute.url.name() );
				if( !Descriptor.isNull(str))
				  set( IConcept.Attributes.SOURCE, str );
				str = rs.getString( PlacesAttribute.title.name() );
				if( !Descriptor.isNull(str)){
				  String name = Descriptor.createValidName( str );
				  this.setName( name );
					if( name.length() < str.length() )
						this.setDescription( str );
				}
				str = rs.getString( PlacesAttribute.rev_host.name() );
				if( !Descriptor.isNull(str))
				  set( PlacesAttribute.rev_host, str );
				str = rs.getString( PlacesAttribute.visit_count.name() );
				if( !Descriptor.isNull(str))
				  set( PlacesAttribute.visit_count, str );
				str = rs.getString( PlacesAttribute.rev_host.name() );
				if( !Descriptor.isNull(str))
				  set( PlacesAttribute.rev_host, str );
				str = rs.getString( PlacesAttribute.hidden.name() );
				if( !Descriptor.isNull(str))
				  set( IConcept.Attributes.HIDDEN.toString(), str );
				str = rs.getString( PlacesAttribute.typed.name() );
				if( !Descriptor.isNull(str))
				  set( PlacesAttribute.typed, str );
				str = rs.getString( PlacesAttribute.favicon_id.name() );
				if( !Descriptor.isNull(str))
				  set( PlacesAttribute.favicon_id, str );
				str = rs.getString( PlacesAttribute.frecency.name() );
				if( !Descriptor.isNull(str))
				  set( PlacesAttribute.frecency, str );
			  
			}
			catch (SQLException e) {
				throw new ConceptException( e );
			}
		}

		@Override
		public void fill(String type, String resource)
		{
			super.set( IDataResource.Attribute.Type, type);
			super.setSource( resource);
		}

		@Override
		public String getType()
		{
			return super.get( IDataResource.Attribute.Type );
		}

		@Override
		public String getResource()
		{
			return super.getSource();
		}

		public int getFavIconID()
		{
			String str = super.get( PlacesAttribute.favicon_id );
			if( !Descriptor.isEmpty( str ))
				return Integer.parseInt( str );
			return -1;
		}
	}
}
