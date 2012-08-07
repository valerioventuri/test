package org.italiangrid.wm.providers;

import java.security.InvalidKeyException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidKeyExceptionMapper implements ExceptionMapper<InvalidKeyException> {

	public Response toResponse(InvalidKeyException arg0) {
		
		return Response.serverError().build();
	}

}
