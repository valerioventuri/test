package org.italiangrid.wm.providers;

import java.security.InvalidKeyException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Maps a java.security.InvalidKeyException to an Internal Server Error JAX-RS Response.
 * <p>
 * When a resource methods throws such an exception, an Internal Server Error is sent to the client.
 * 
 */
@Provider
public class InvalidKeyExceptionMapper implements ExceptionMapper<InvalidKeyException> {

	public Response toResponse(InvalidKeyException arg0) {
		
		return Response.serverError().build();
	}

}
