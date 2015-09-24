package org.anmol.desai.services;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/hwsubmission")
public class HomeworkSubmission {
		
	@Path("/user/{id}")
	@Consumes("application/xml")
	public Response createUser(InputStream is, @PathParam("id") String id){
		return null;
	}

}
