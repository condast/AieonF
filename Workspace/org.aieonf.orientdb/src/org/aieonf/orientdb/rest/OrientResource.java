package org.aieonf.orientdb.rest;

import java.util.logging.Logger;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;



// Plain old Java Object it does not extend as class or implements
// an interface

// The class registers its methods for the HTTP GET request using the @GET annotation.
// Using the @Produces annotation, it defines that it can deliver several MIME types,
// text, XML and HTML.

// The browser requests per default the HTML MIME type.

//Sets the path to base URL + /smash
@Path("/db")
public class OrientResource{

	public static final String S_ERR_UNKNOWN_REQUEST = "An invalid request was rertrieved: ";
	public static final String S_ERR_INVALID_VESSEL = "An request was received from an unknown vessel:";
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public OrientResource() {
		super();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/add")
	public Response add( @QueryParam("domain") String domain, @QueryParam("token") long token,  @QueryParam("data") String data )
	{
		Response retval = null;
		try{
			//service.open();
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
		finally {
			//service.close();
		}
		return retval;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/find")
	public Response findLocation( @QueryParam("latitude") double latitude, @QueryParam("longitude") double longitude, @QueryParam("range") int range ) {
		Response retval = null;
		try{
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
		return retval;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getall")
	public Response getAll() {
		Response retval = null;
		try{
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
		return retval;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/findall")
	public Response findAll() {
		Response retval = null;
		try{
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
		return retval;
	}

	@DELETE
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/remove")
	public Response remove( @QueryParam("name") String userName, @QueryParam("token") long token, @QueryParam("locationid") long locationid ) {
		Response retval = null;
		try{
			return retval;
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
	}
}