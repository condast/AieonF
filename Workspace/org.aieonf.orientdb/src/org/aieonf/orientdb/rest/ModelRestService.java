package org.aieonf.orientdb.rest;

import java.util.Collection;

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
import org.aieonf.model.core.Model;
import org.aieonf.model.filter.ModelFilter;
import org.aieonf.orientdb.cache.CacheService;
import org.aieonf.orientdb.core.Dispatcher;
import org.aieonf.orientdb.db.DatabaseService;
import org.aieonf.orientdb.graph.ModelFactory;

import com.google.gson.Gson;

//Sets the path to base URL + /rest
@Path("/")
public class ModelRestService{

	public static final String S_ERR_UNKNOWN_REQUEST = "An invalid request was retrieved: ";
	public static final String S_ERR_INVALID_VESSEL = "A request was received from an unknown vessel:";
	
	private Dispatcher dispatcher = Dispatcher.getInstance();

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/add")
	public Response addModel( @QueryParam("id") long domainId, @QueryParam("token") String token, String data ) {
		DatabaseService dbService = DatabaseService.getInstance();
		ModelFactory<IDescriptor> factory = null;
		try{
 			if( !dispatcher.isRegistered(domainId, token))
 				return Response.status( Status.UNAUTHORIZED ).build();
			IDomainAieon domain = dispatcher.getDomain(domainId, token);
			//if( !dispatcher.isAllowed(node))
			//	return Response.status(Status.FORBIDDEN).build();
			factory = new ModelFactory<IDescriptor>( domain, dbService );
			factory.transform(data);
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
		CacheService cache = CacheService.getInstance();
		try {
			cache.open();
			Collection<IDescriptor> descriptors = factory.getDescriptors().values();
			cache.add( descriptors.toArray( new IDescriptor[ descriptors.size()] ));
			return Response.ok().build();
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
		DatabaseService dbService = DatabaseService.getInstance();
		Collection<IModelLeaf<IDescriptor>> result = null;
		try{
			if( !dispatcher.isRegistered(domainId, token))
 				return Response.status( Status.UNAUTHORIZED ).build();
			IDomainAieon domain = dispatcher.getDomain(domainId, token);
			dbService.open();
			ModelFactory<IDescriptor> factory = new ModelFactory<IDescriptor>( domain, dbService );
			result = factory.get(domain);
			ModelFilter<IDescriptor, IModelLeaf<IDescriptor>> filter = new ModelFilter<IDescriptor, IModelLeaf<IDescriptor>>(null);
			result = filter.doFilter(result);
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
		CacheService cache = CacheService.getInstance();
		try {
			cache.open();	
			cache.fill(result);
			Gson gson = new Gson();
			String str = gson.toJson(result.toArray(new IModelLeaf[ result.size()]), Model[].class );
			return Response.ok( str ).build();
		}
		catch( Exception ex ) {
			ex.printStackTrace();
			return Response.serverError().build();
		}
		finally {
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