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

/**
 * Message body provider for deserializing PEM encoded certificate signing request to BC's 
 * PKCS10CertificationRequest.
 * 
 */
@Provider
@Consumes("text/plain")
public class PKCS10CertificationRequestBodyReader implements MessageBodyReader<PKCS10CertificationRequest> {

  /**
   * Return true if and only if type is PKCS10CertificateRequest.
   * 
   */
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] arg2, MediaType arg3) {

		return PKCS10CertificationRequest.class == type;
	}

	/**
	 * Reads the PEM encoded CSR from input stream and deserialize it to a BC's PKCS10CertificateRequest 
	 * using BC's PEM deserialization.
	 * 
	 */
	public PKCS10CertificationRequest readFrom(Class<PKCS10CertificationRequest> type, Type genericType, Annotation[] annotations, 
			MediaType mediaType, MultivaluedMap<String, String> headers, InputStream inputStream)
			throws IOException, WebApplicationException {
		
		PEMReader pemReader = new PEMReader(new InputStreamReader(inputStream));
		
		PKCS10CertificationRequest certificationRequest = (PKCS10CertificationRequest) pemReader.readObject();
		
		pemReader.close();
		
		return certificationRequest;
	}
	
}
