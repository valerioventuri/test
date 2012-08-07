package org.italiangrid.wm.providers;

import java.security.SignatureException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class SignatureExceptionMapper implements ExceptionMapper<SignatureException> {

	public Response toResponse(SignatureException arg0) {
		
		return Response.serverError().build();
	}

}
