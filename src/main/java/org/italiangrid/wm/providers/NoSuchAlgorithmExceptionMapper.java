package org.italiangrid.wm.providers;

import java.security.NoSuchAlgorithmException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NoSuchAlgorithmExceptionMapper implements
		ExceptionMapper<NoSuchAlgorithmException> {

	public Response toResponse(NoSuchAlgorithmException arg0) {

		return Response.serverError().build();
	}

}
