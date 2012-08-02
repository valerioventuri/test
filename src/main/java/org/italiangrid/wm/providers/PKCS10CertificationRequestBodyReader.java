package org.italiangrid.wm.providers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.openssl.PEMReader;

@Provider
@Consumes("text/plain")
public class PKCS10CertificationRequestBodyReader implements MessageBodyReader<PKCS10CertificationRequest> {

	public boolean isReadable(Class<?> type, Type genericType, Annotation[] arg2, MediaType arg3) {

		return PKCS10CertificationRequest.class == type;
	}

	public PKCS10CertificationRequest readFrom(Class<PKCS10CertificationRequest> type, Type genericType, Annotation[] annotations, 
			MediaType mediaType, MultivaluedMap<String, String> headers, InputStream inputStream)
			throws IOException, WebApplicationException {
		
		PEMReader pemReader = new PEMReader(new InputStreamReader(inputStream));
		
		PKCS10CertificationRequest certificationRequest = (PKCS10CertificationRequest) pemReader.readObject();
		
		pemReader.close();
		
		return certificationRequest;
	}

	
}
