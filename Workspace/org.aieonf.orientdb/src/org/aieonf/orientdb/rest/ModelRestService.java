package org.aieonf.orientdb.rest;

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
import org.aieonf.model.utils.PrintModel;
import org.aieonf.orientdb.core.Dispatcher;
import org.aieonf.orientdb.db.DatabaseService;
import org.aieonf.orientdb.filter.IGraphFilter;
import org.aieonf.orientdb.filter.VertexFilterFactory;
import org.aieonf.orientdb.graph.ModelFactory;
import org.aieonf.orientdb.serialisable.ModelTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
			ModelTypeAdapter adapter = new ModelTypeAdapter( dispatcher.getProvider(), dbService.getGraph());
			builder.registerTypeAdapter( IModelLeaf.class, adapter);
			Gson gson = builder.create();
			logger.info( data );
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

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/add-node")
	public Response addNode( @QueryParam("id") long domainId, @QueryParam("token") long token, 
			@QueryParam("domain") String domainstr, String data ) {
		DatabaseService dbService = DatabaseService.getInstance();
		try{
 			if( !dispatcher.isRegistered(domainId, token, domainstr))
 				return Response.status( Status.UNAUTHORIZED ).build();
			long id = -1;//factory.transform(data);
			Gson gson = new Gson();
			return Response.ok( gson.toJson(id, Long.class)).build();
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
			ModelFilter<IModelLeaf<IDescriptor>> filter = new ModelFilter<IModelLeaf<IDescriptor>>(null);
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
	@Path("/get")
	public Response getModel( @QueryParam("id") long id, @QueryParam("token") long token, 
			@QueryParam("domain") String domainstr, @QueryParam("name") String name, @QueryParam("version") String version) {
		DatabaseService dbService = DatabaseService.getInstance();
		Collection<IModelLeaf<IDescriptor>> result = null;
		try{
			if( !dispatcher.isRegistered(id, token, domainstr))
 				return Response.status( Status.UNAUTHORIZED ).build();
			IDomainAieon domain = dispatcher.getDomain(id, token, domainstr);
			dbService.open( domain );
			ModelFilter<IModelLeaf<IDescriptor>> filter = new ModelFilter<IModelLeaf<IDescriptor>>(null);
			result = filter.doFilter(result);
			Gson gson = new Gson();
			String str = gson.toJson(result.toArray(new IModelLeaf[ result.size()]), Model[].class );
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
			ModelFactory<IDescriptor> factory = new ModelFactory<IDescriptor>( dbService );
			Collection<IModelLeaf<IDescriptor>> result = factory.get(filter.doFilter());
			for( IModelLeaf<IDescriptor> model: result )
				logger.fine( PrintModel.printModel(model, true));
			GsonBuilder builder = new GsonBuilder();
			builder.enableComplexMapKeySerialization();
			builder.registerTypeAdapter(IModelLeaf.class, new ModelTypeAdapter( dispatcher.getProvider(), dbService.getGraph()));
			Gson gson = builder.create();
			String str = gson.toJson(result.toArray( new IModelLeaf[ result.size() ]), IModelLeaf[].class);
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

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/update")
	public Response update( @QueryParam("id") long id, @QueryParam("token") long token, String data) {
		try{
 			if( !dispatcher.isLoggedIn( id, token))
 				return Response.status( Status.UNAUTHORIZED ).build();
			//int index = service.getUserData().getSelectedVesselIndex();
			return Response.ok(0).build();
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
	}

	@DELETE
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/remove")
	public synchronized Response remove( @QueryParam("id") long id, @QueryParam("token") long token,
			@QueryParam("domain") String domainstr, @QueryParam("model-id") long modelId) {
		DatabaseService dbService = DatabaseService.getInstance();
		int counter = 0;
		try{
			if( !dispatcher.isRegistered(id, token, domainstr))
				return Response.status( Status.UNAUTHORIZED ).build();
			IDomainAieon domain = dispatcher.getDomain(id, token, domainstr);
			dbService.open( domain );
			counter = dbService.remove(modelId);
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
	@Path("/remove-all")
	public synchronized Response removeAll( @QueryParam("id") long id, @QueryParam("token") long token,
			@QueryParam("domain") String domainstr, String data) {
		DatabaseService dbService = DatabaseService.getInstance();
		int counter = 0;
		try{
			if( !dispatcher.isRegistered(id, token, domainstr))
				return Response.status( Status.UNAUTHORIZED ).build();
			IDomainAieon domain = dispatcher.getDomain(id, token, domainstr);
			dbService.open( domain );
			if( StringUtils.isEmpty( data))
				return Response.noContent().build(); 
			Gson gson = new Gson();
			long[] ids = gson.fromJson(data, long[].class);
			counter = dbService.remove( ids );
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

}