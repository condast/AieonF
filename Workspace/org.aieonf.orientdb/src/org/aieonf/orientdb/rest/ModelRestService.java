package org.aieonf.orientdb.rest;

import java.util.Collection;
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

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.filter.ModelFilter;
import org.aieonf.model.xml.SerialisableModel;
import org.aieonf.orientdb.cache.CacheService;
import org.aieonf.orientdb.core.Dispatcher;
import org.aieonf.orientdb.db.DatabaseService;
import org.aieonf.orientdb.graph.ModelFactory;
import org.aieonf.orientdb.model.Model;

import com.google.gson.Gson;

//Sets the path to base URL + /rest
@Path("/")
public class ModelRestService{

	public static final String S_ERR_UNKNOWN_REQUEST = "An invalid request was retrieved: ";
	public static final String S_ERR_INVALID_VESSEL = "A request was received from an unknown vessel:";
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	private Dispatcher dispatcher = Dispatcher.getInstance();

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/add")
	public Response addModel( @QueryParam("id") long domainId, @QueryParam("token") String token, String data ) {
		CacheService cache = CacheService.getInstance();
		DatabaseService dbService = DatabaseService.getInstance();
		try{
 			if( !dispatcher.isRegistered(domainId, token))
 				return Response.status( Status.UNAUTHORIZED ).build();
			IDomainAieon domain = dispatcher.getDomain(domainId, token);
 			Gson gson = new Gson();
			IModelLeaf<IDescriptor> node = gson.fromJson(data, Model.class);
			if( !dispatcher.isAllowed(node))
				return Response.status(Status.FORBIDDEN).build();
			cache.open();
			dbService.open();
			ModelFactory factory = new ModelFactory( domain, cache, dbService.getGraph() );
			IModelLeaf<IDescriptor> result = factory.transform(node);
			return ( result == null )? Response.status(Status.NOT_IMPLEMENTED).build(): 
				Response.ok().build();
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
		finally {
			dbService.close();
			cache.close();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get")
	public Response getModel( @QueryParam("id") long domainId, @QueryParam("token") String token, 
			@QueryParam("name") String name, @QueryParam("version") String version) {
		CacheService cache = CacheService.getInstance();
		DatabaseService dbService = DatabaseService.getInstance();
		try{
			if( !dispatcher.isRegistered(domainId, token))
 				return Response.status( Status.UNAUTHORIZED ).build();
			IDomainAieon domain = dispatcher.getDomain(domainId, token);
 			cache.open();
			dbService.open();
			ModelFactory factory = new ModelFactory( domain, cache, dbService.getGraph() );
			Collection<IModelLeaf<IDescriptor>> result = factory.get(domain);
			ModelFilter<IDescriptor, IModelLeaf<IDescriptor>> filter = new ModelFilter<IDescriptor, IModelLeaf<IDescriptor>>(null);
			result = filter.doFilter(result);
			SerialisableModel[] sm = SerialisableModel.create(result);
			Gson gson = new Gson();
			String str = gson.toJson(sm, SerialisableModel[].class );
			return Response.ok( str ).build();
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
		finally {
			dbService.close();
			cache.close();
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
	public Response remove( @QueryParam("id") long id, @QueryParam("token") long token,
			@QueryParam("vessel-id") long vesselId) {
		try{
 			if( !dispatcher.isLoggedIn( id, token))
 				return Response.status( Status.UNAUTHORIZED ).build();
  			Gson gson = new Gson();
			//String str = gson.toJson(state, IVesselManager.ConnectionState.class);
			return Response.ok().build();
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
	}
}