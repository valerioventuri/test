package org.italiangrid.wm.providers;

import java.security.cert.CertificateEncodingException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class CertificateEncodingExceptionMapper implements ExceptionMapper<CertificateEncodingException> {

	public Response toResponse(CertificateEncodingException arg0) {
		
		return Response.serverError().build();
	}

	
}
