package org.italiangrid.wm.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/delegation")
public class DelegationResource {

	@GET
	public Response getDelegation() {

		return Response.ok(null).build();
	}

}
