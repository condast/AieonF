package org.aieonf.orientdb.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.aieonf.commons.Utils;
import org.aieonf.commons.io.IOUtils;
import org.aieonf.commons.strings.StringStyler;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.filter.FilterFactory;
import org.aieonf.concept.filter.FilterFactory.Filters;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.Model;
import org.aieonf.model.filter.ModelFilter;
import org.aieonf.model.serialise.ModelTypeAdapter;
import org.aieonf.orientdb.core.Dispatcher;
import org.aieonf.orientdb.db.DatabaseService;
import org.aieonf.orientdb.db.ModelDatabase;
import org.aieonf.orientdb.filter.IGraphFilter;
import org.aieonf.orientdb.filter.VertexFilterFactory;
import org.aieonf.orientdb.serialisable.OrientModelTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tinkerpop.blueprints.Direction;

//Sets the path to base URL + /rest
@Path("/")
public class ModelRestService{

	public static final String S_ERR_UNKNOWN_REQUEST = "An invalid request was retrieved: ";
	public static final String S_ERR_INVALID_VESSEL = "A request was received from an unknown vessel:";
	
	private Dispatcher dispatcher = Dispatcher.getInstance();

	private Logger logger = Logger.getLogger(this.getClass().getName());

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/add")
	public Response addModel( @QueryParam("id") long domainId, @QueryParam("token") long token, 
			@QueryParam("domain") String domainstr, String data ) {
		DatabaseService dbService = DatabaseService.getInstance();
		try{
 			if( !dispatcher.isRegistered(domainId, token, domainstr))
 				return Response.status( Status.UNAUTHORIZED ).build();
			IDomainAieon domain = dispatcher.getDomain(domainId, token, domainstr);
			Response response = Response.serverError().build();
			GsonBuilder builder = new GsonBuilder(); 
			builder.enableComplexMapKeySerialization();
			dbService.open(domain);
			OrientModelTypeAdapter adapter = new OrientModelTypeAdapter( domain, dbService.getGraph());
			builder.registerTypeAdapter( IModelLeaf.class, adapter);
			Gson gson = builder.create();
			
			logger.fine( data );
			IModelLeaf<?>[] results = gson.fromJson(data, IModelLeaf[].class);
			response = ( Utils.assertNull(results))? Response.noContent().build(): 
				Response.ok( gson.toJson(results[0].getID(), Long.class)).build();
			return response;
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
		finally {
			IOUtils.closeQuietly( dbService);
		}
	}

	@SuppressWarnings("unchecked")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/add-node")
	public Response addNode( @QueryParam("id") long domainId, @QueryParam("token") long token, 
			@QueryParam("domain") String domainstr, @QueryParam("model-id") long modelId, @QueryParam("label") String label, String data ) {
		DatabaseService dbService = DatabaseService.getInstance();
		try{
 			if( !dispatcher.isRegistered(domainId, token, domainstr))
 				return Response.status( Status.UNAUTHORIZED ).build();
			IDomainAieon domain = dispatcher.getDomain(domainId, token, domainstr);
			dbService.open( domain );
			GsonBuilder builder = new GsonBuilder(); 
			builder.enableComplexMapKeySerialization();
			OrientModelTypeAdapter adapter = new OrientModelTypeAdapter( domain, dbService.getGraph());
			builder.registerTypeAdapter( IModelLeaf.class, adapter);
			Gson gson = builder.create();			
			logger.info( data );
			IModelLeaf<IDescriptor>[] results = gson.fromJson(data, IModelLeaf[].class);
			ModelDatabase<IDescriptor> factory = new ModelDatabase<IDescriptor>( dbService, domain );
			String str = StringUtils.isEmpty(label)?Model.IS_CHILD: label;
			boolean result = factory.addNode(modelId, results[0], str);
			return result? Response.ok( gson.toJson(modelId, Long.class)).build(): Response.serverError().build();
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
		finally {
			IOUtils.closeQuietly( dbService);
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/contains")
	public Response contains( @QueryParam("id") long id, @QueryParam("token") long token, 
			@QueryParam("domain") String domainstr, @QueryParam("model-id") long modelId) {
		DatabaseService dbService = DatabaseService.getInstance();
		Collection<IModelLeaf<IDescriptor>> result = null;
		try{
			if( !dispatcher.isRegistered(id, token, domainstr))
 				return Response.status( Status.UNAUTHORIZED ).build();
			IDomainAieon domain = dispatcher.getDomain(id, token, domainstr);
			dbService.open( domain );
			ModelFilter<IDescriptor, IModelLeaf<IDescriptor>> filter = new ModelFilter<IDescriptor, IModelLeaf<IDescriptor>>(null);
			result = filter.doFilter(result);
			return Response.ok(result).build();
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/adjacent")
	public Response adjacent( @QueryParam("id") long id, @QueryParam("token") long token, 
			@QueryParam("domain") String domainstr, @QueryParam("model-id") long modelId, @QueryParam("direction") String direction) {
		DatabaseService dbService = DatabaseService.getInstance();
		Collection<IModelLeaf<IDescriptor>> results = new ArrayList<>();
		try{
			if( !dispatcher.isRegistered(id, token, domainstr))
 				return Response.status( Status.UNAUTHORIZED ).build();
			IDomainAieon domain = dispatcher.getDomain(id, token, domainstr);
			dbService.open( domain );
			ModelDatabase<IDescriptor> database = new ModelDatabase<IDescriptor>( dbService, domain );
			results = database.adjacent( modelId, Direction.valueOf(direction));
			GsonBuilder builder = new GsonBuilder();
			builder.enableComplexMapKeySerialization();
			builder.registerTypeAdapter(IModelLeaf.class, new OrientModelTypeAdapter( domain, dbService.getGraph()));
			Gson gson = builder.create();
			String str = gson.toJson(results.toArray( new IModelLeaf[ results.size()]), IModelLeaf[].class );
			return Response.ok( str ).build();
		}
		catch( Exception ex ) {
			ex.printStackTrace();
			return Response.serverError().build();
		}
		finally {
			dbService.close();
		}	
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/find")
	public Response findModel( @QueryParam("id") long id, @QueryParam("token") long token, 
			@QueryParam("domain") String domainstr, @QueryParam("model-id") long modelId, @QueryParam("version") String version) {
		DatabaseService dbService = DatabaseService.getInstance();
		IModelLeaf<IDescriptor>[] results = null;
		try{
			if( !dispatcher.isRegistered(id, token, domainstr))
 				return Response.status( Status.UNAUTHORIZED ).build();
			IDomainAieon domain = dispatcher.getDomain(id, token, domainstr);
			dbService.open( domain );
			results = find( dbService, domain, modelId );
			if( Utils.assertNull(results))
				return Response.noContent().build();
			GsonBuilder builder = new GsonBuilder();
			builder.enableComplexMapKeySerialization();
			builder.registerTypeAdapter(IModelLeaf.class, new OrientModelTypeAdapter( domain, dbService.getGraph()));
			Gson gson = builder.create();
			String str = gson.toJson(results, IModelLeaf[].class );
			return Response.ok( str ).build();
		}
		catch( Exception ex ) {
			ex.printStackTrace();
			return Response.serverError().build();
		}
		finally {
			dbService.close();
		}	
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/find-on-descriptor")
	public Response findModelOnDescriptor( @QueryParam("id") long id, @QueryParam("token") long token, 
			@QueryParam("domain") String domainstr, @QueryParam("model-id") long modelId, @QueryParam("version") String version) {
		DatabaseService dbService = DatabaseService.getInstance();
		IModelLeaf<IDescriptor>[] results = null;
		try{
			if( !dispatcher.isRegistered(id, token, domainstr))
 				return Response.status( Status.UNAUTHORIZED ).build();
			IDomainAieon domain = dispatcher.getDomain(id, token, domainstr);
			dbService.open( domain );
			results = findOnDescriptor( dbService, domain, modelId );
			return getResponse( results );
		}
		catch( Exception ex ) {
			ex.printStackTrace();
			return Response.serverError().build();
		}
		finally {
			dbService.close();
		}	
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get-all")
	public Response getAll( @QueryParam("id") long id, @QueryParam("token") long token, 
			@QueryParam("domain") String domainstr, @QueryParam("domainonly") boolean domainOnly, 
			@QueryParam("user-id") long userId ) {
		DatabaseService dbService = DatabaseService.getInstance();
		try{
			if( !dispatcher.isRegistered(id, token, domainstr))
 				return Response.status( Status.UNAUTHORIZED ).build();
			IDomainAieon domain = dispatcher.getDomain(id, token, domainstr);
			dbService.open( domain );
			ModelDatabase<IDescriptor> database = new ModelDatabase<IDescriptor>( dbService, domain );
			return getResponse( database.getAllModels( domainOnly ));
		}
		catch( Exception ex ) {
			ex.printStackTrace();
			return Response.serverError().build();
		}
		finally {
			dbService.close();
		}	
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get-options")
	public Response getOptions( @QueryParam("id") long id, @QueryParam("token") long token, 
			@QueryParam("domain") String domainstr, @QueryParam("user-id") long userId ) {
		DatabaseService dbService = DatabaseService.getInstance();
		try{
			if( !dispatcher.isRegistered(id, token, domainstr))
 				return Response.status( Status.UNAUTHORIZED ).build();
			IDomainAieon domain = dispatcher.getDomain(id, token, domainstr);
			dbService.open( domain );
			ModelDatabase<IDescriptor> database = new ModelDatabase<IDescriptor>( dbService, domain );
			return getResponse( database.findOptions( userId ));
		}
		catch( Exception ex ) {
			ex.printStackTrace();
			return Response.serverError().build();
		}
		finally {
			dbService.close();
		}	
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get")
	public Response getModel( @QueryParam("id") long id, @QueryParam("token") long token, 
			@QueryParam("domain") String domainstr, @QueryParam("name") String name, @QueryParam("version") String version) {
		DatabaseService dbService = DatabaseService.getInstance();
		Collection<IModelLeaf<IDescriptor>> results = null;
		try{
			if( !dispatcher.isRegistered(id, token, domainstr))
 				return Response.status( Status.UNAUTHORIZED ).build();
			IDomainAieon domain = dispatcher.getDomain(id, token, domainstr);
			dbService.open( domain );
			ModelFilter<IDescriptor, IModelLeaf<IDescriptor>> filter = new ModelFilter<IDescriptor, IModelLeaf<IDescriptor>>(null);
			results = filter.doFilter(results);
			Gson gson = new Gson();
			String str = gson.toJson(results.toArray(new IModelLeaf[ results.size()]), Model[].class );
			return Response.ok( str ).build();
		}
		catch( Exception ex ) {
			ex.printStackTrace();
			return Response.serverError().build();
		}
		finally {
			dbService.close();
		}	
	}

	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/search-models")
	public Response searchModels( @QueryParam("id") long id, @QueryParam("token") long token, 
			@QueryParam("domain") String domainstr, @QueryParam("key") String key, @QueryParam("value") String value) {
		DatabaseService dbService = DatabaseService.getInstance();
		try{
			if( !dispatcher.isRegistered(id, token, domainstr))
 				return Response.status( Status.UNAUTHORIZED ).build();
			IDomainAieon domain = dispatcher.getDomain(id, token, domainstr);
			dbService.open( domain );
			ModelDatabase<IDescriptor> database = new ModelDatabase<IDescriptor>( dbService, domain );
			Collection<IModelLeaf<IDescriptor>> results = database.find( key, value );
			return getResponse( results.toArray( new IModelLeaf[ results.size() ]) );
		}
		catch( Exception ex ) {
			ex.printStackTrace();
			return Response.serverError().build();
		}
		finally {
			dbService.close();
		}	
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/search")
	public Response search( @QueryParam("id") long id, @QueryParam("token") long token, 
			@QueryParam("domain") String domainstr, @QueryParam("filter") String name, 
			@QueryParam("rules") String rule, @QueryParam("reference") String attribute, @QueryParam("value") String wildcard) {
		DatabaseService dbService = DatabaseService.getInstance();
		try{
			if( !dispatcher.isRegistered(id, token, domainstr))
 				return Response.status( Status.UNAUTHORIZED ).build();
			IDomainAieon domain = dispatcher.getDomain(id, token, domainstr);
			dbService.open( domain );
			VertexFilterFactory ff = new VertexFilterFactory( dbService.getGraph());
			Filters type = StringUtils.isEmpty(name)? Filters.ATTRIBUTES: Filters.valueOf(StringStyler.styleToEnum(name));
			Map<FilterFactory.Attributes, String> params = new HashMap<>();
			params.put(FilterFactory.Attributes.RULES, rule);
			params.put(FilterFactory.Attributes.REFERENCE, attribute);
			params.put(FilterFactory.Attributes.VALUE, wildcard);
			IGraphFilter filter = ff.createFilter(type, params);
			ModelDatabase<IDescriptor> database = new ModelDatabase<IDescriptor>( dbService, domain );
			Collection<IModelLeaf<IDescriptor>> results = database.search(filter.doFilter());
			//for( IModelLeaf<IDescriptor> model: results)
			//	logger.fine( PrintModel.printModel(model, true));
			if( Utils.assertNull(results))
				return Response.noContent().build();
			GsonBuilder builder = new GsonBuilder();
			builder.enableComplexMapKeySerialization();
			builder.registerTypeAdapter(IModelLeaf.class, new OrientModelTypeAdapter( domain, dbService.getGraph()));
			Gson gson = builder.create();
			String str = gson.toJson(results.toArray( new IModelLeaf[ results.size() ]), IModelLeaf[].class);
			logger.fine(str);
			return Response.ok( str ).build();
		}
		catch( Exception ex ) {
			ex.printStackTrace();
			return Response.serverError().build();
		}
		finally {
			IOUtils.closeQuietly( dbService );
		}	
	}

	@SuppressWarnings("unchecked")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/update")
	public Response update( @QueryParam("id") long id, @QueryParam("token") long token, 
			@QueryParam("domain") String domainstr, String data) {
		DatabaseService dbService = DatabaseService.getInstance();
		try{
			if( !dispatcher.isRegistered(id, token, domainstr))
 				return Response.status( Status.UNAUTHORIZED ).build();
			IDomainAieon domain = dispatcher.getDomain(id, token, domainstr);
			dbService.open( domain );
			ModelDatabase<IDescriptor> database = new ModelDatabase<IDescriptor>( dbService, domain );
			GsonBuilder builder = new GsonBuilder(); 
			builder.enableComplexMapKeySerialization();
			ModelTypeAdapter adapter = new ModelTypeAdapter();
			builder.registerTypeAdapter( IModelLeaf.class, adapter);
			Gson gson = builder.create();			
			logger.fine( data );
			IModelLeaf<IDescriptor>[] results = gson.fromJson(data, IModelLeaf[].class);
			database.update( results[0] );
			return Response.ok(0).build();
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
		finally {
			IOUtils.closeQuietly( dbService );
		}	
	}

	@DELETE
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/remove")
	public synchronized Response remove( @QueryParam("id") long id, @QueryParam("token") long token,
			@QueryParam("domain") String domainstr, @QueryParam("model-id") long modelId, String data) {
		DatabaseService dbService = DatabaseService.getInstance();
		int counter = 0;
		try{
			if( !dispatcher.isRegistered(id, token, domainstr))
				return Response.status( Status.UNAUTHORIZED ).build();
			IDomainAieon domain = dispatcher.getDomain(id, token, domainstr);
			dbService.open( domain );
			ModelDatabase<IDescriptor> database = new ModelDatabase<IDescriptor>( dbService, domain );
			counter = database.remove(modelId);
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
		finally {
			dbService.close();
		}
		return (counter == 0)?Response.ok( counter ).build():Response.noContent().build();
	}

	@DELETE
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/remove-children")
	public synchronized Response removeChildren( @QueryParam("id") long id, @QueryParam("token") long token,
			@QueryParam("domain") String domainstr, @QueryParam("model-id") long modelId, String data) {
		DatabaseService dbService = DatabaseService.getInstance();
		try{
			if( !dispatcher.isRegistered(id, token, domainstr))
				return Response.status( Status.UNAUTHORIZED ).build();
			IDomainAieon domain = dispatcher.getDomain(id, token, domainstr);
			dbService.open( domain );
			Gson gson = new Gson();
			long[] ids = gson.fromJson(data, long[].class);
			ModelDatabase<IDescriptor> database = new ModelDatabase<IDescriptor>( dbService, domain );
			database.removeChildren( modelId, ids );
			String str = findAndSerialise(dbService, domain, modelId);
			return StringUtils.isEmpty(str)? Response.noContent().build(): Response.ok( str ).build();
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
		finally {
			dbService.close();
		}
	}

	@DELETE
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/remove-children-on-descriptor")
	public synchronized Response removeChildrenOnDescriptor( @QueryParam("id") long id, @QueryParam("token") long token,
			@QueryParam("domain") String domainstr, @QueryParam("model-id") long modelId, String data) {
		DatabaseService dbService = DatabaseService.getInstance();
		try{
			if( !dispatcher.isRegistered(id, token, domainstr))
				return Response.status( Status.UNAUTHORIZED ).build();
			IDomainAieon domain = dispatcher.getDomain(id, token, domainstr);
			dbService.open( domain );
			Gson gson = new Gson();
			long ids = gson.fromJson(data, long.class);
			ModelDatabase<IDescriptor> database = new ModelDatabase<IDescriptor>( dbService, domain );
			database.removeChildrenOnDescriptors( modelId, ids );
			String str = findAndSerialise(dbService, domain, modelId);
			return StringUtils.isEmpty(str)? Response.noContent().build(): Response.ok( str ).build();
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
		finally {
			dbService.close();
		}
	}

	@DELETE
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/remove-all")
	public synchronized Response removeAll( @QueryParam("id") long id, @QueryParam("token") long token,
			@QueryParam("domain") String domainstr, String data) {
		DatabaseService dbService = DatabaseService.getInstance();
		int counter = 0;
		try{
			if( !dispatcher.isRegistered(id, token, domainstr))
				return Response.status( Status.UNAUTHORIZED ).build();
			IDomainAieon domain = dispatcher.getDomain(id, token, domainstr);
			if( StringUtils.isEmpty( data))
				return Response.noContent().build(); 
			Gson gson = new Gson();
			long[] ids = gson.fromJson(data, long[].class);
			dbService.open( domain );
			ModelDatabase<IDescriptor> database = new ModelDatabase<IDescriptor>( dbService, domain );
			counter = database.remove( ids );
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
		finally {
			dbService.close();
		}
		return (counter == 0)?Response.ok( counter ).build():Response.noContent().build();
	}

	/**
	 * Find the model with the given model id
	 * @param dbService
	 * @param domain
	 * @param modelId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected static IModelLeaf<IDescriptor>[] find( DatabaseService dbService, IDomainAieon domain, long modelId ){
		ModelDatabase<IDescriptor> database = new ModelDatabase<IDescriptor>( dbService, domain );
		Collection<IModelLeaf<IDescriptor>> results = database.find( modelId );
		if( Utils.assertNull(results))
			return null;
		return results.toArray( new IModelLeaf[ results.size()]); 
	}

	/**
	 * Find the model with the given model id
	 * @param dbService
	 * @param domain
	 * @param modelId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected static IModelLeaf<IDescriptor>[] findOnDescriptor( DatabaseService dbService, IDomainAieon domain, long modelId ){
		ModelDatabase<IDescriptor> database = new ModelDatabase<IDescriptor>( dbService, domain );
		Collection<IModelLeaf<IDescriptor>> results = database.findOnDescriptor( modelId );
		if( Utils.assertNull(results))
			return null;
		return results.toArray( new IModelLeaf[ results.size()]); 
	}

	/**
	 * As a above, but now serialises the result
	 * @param dbService
	 * @param domain
	 * @param modelId
	 * @return
	 */
	protected static String findAndSerialise( DatabaseService dbService, IDomainAieon domain, long modelId ){
		IModelLeaf<IDescriptor>[] results = find( dbService, domain, modelId );
		if( Utils.assertNull(results))
			return null;
		GsonBuilder builder = new GsonBuilder();
		builder.enableComplexMapKeySerialization();
		builder.registerTypeAdapter(IModelLeaf.class, new OrientModelTypeAdapter( domain, dbService.getGraph()));
		Gson gson = builder.create();
		return gson.toJson(results, IModelLeaf[].class );
	}

	/**
	 * Get the correct response for the given list
	 * @param results
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected static Response getResponse( Collection<IModelLeaf<IDescriptor>> results ) {
		if( Utils.assertNull(results))
			return Response.noContent().build();
		return getResponse(results.toArray( new IModelLeaf[ results.size() ]) );
	}
	
	/**
	 * Get the correct response for the given list
	 * @param results
	 * @return
	 */
	protected static Response getResponse( IModelLeaf<IDescriptor>[] results ) {
		if( Utils.assertNull(results ))
			return Response.noContent().build();
		GsonBuilder builder = new GsonBuilder(); 
		builder.enableComplexMapKeySerialization();
		ModelTypeAdapter adapter = new ModelTypeAdapter();
		builder.registerTypeAdapter( IModelLeaf.class, adapter);
		Gson gson = builder.create();
		String str = gson.toJson(results, IModelLeaf[].class );
		return Response.ok(str).build();

	}

}