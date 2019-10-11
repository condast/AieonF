package org.aieonf.orientdb.rest;

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

import org.aieonf.orientdb.core.Dispatcher;
import org.aieonf.orientdb.graph.OrientDBModel;

import com.google.gson.Gson;

//Sets the path to base URL + /rest
@Path("/")
public class ModelRestService{

	public static final String S_ERR_UNKNOWN_REQUEST = "An invalid request was rertrieved: ";
	public static final String S_ERR_INVALID_VESSEL = "A request was received from an unknown vessel:";
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	private Dispatcher dispatcher = Dispatcher.getInstance();

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/add")
	public Response addModel( @QueryParam("id") String id, @QueryParam("token") String token, String data ) {
		try{
 			if( !dispatcher.isRegistered(id, token))
 				return Response.status( Status.UNAUTHORIZED ).build();
 			Gson gson = new Gson();
			OrientDBModel node = gson.fromJson(data, OrientDBModel.class);
			return ( dispatcher.isAllowed(node))?Response.ok().build(): Response.status(Status.FORBIDDEN).build();
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
			@QueryParam("vessel-id") long vesselId) {
		try{
 			if( !dispatcher.isLoggedIn( id, token))
 				return Response.status( Status.UNAUTHORIZED ).build();
			Gson gson = new Gson();
			//String str = gson.toJson(vesselData, VesselData.class);
			return Response.ok( ).build();
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
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