package org.italiangrid.wm.providers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.openssl.PEMWriter;

/**
 * Message body provider for serializing BC's PKCS10CertificationRequest to PEM encoded certificate 
 * signing request.
 * 
 */
@Provider
@Produces("text/plain")
public class PKCS10CertificationRequestBodyWriter implements MessageBodyWriter<PKCS10CertificationRequest> {

  /**
   * Return -1, as the length cannot be determined in advance.
   * 
   */
	public long getSize(PKCS10CertificationRequest certificationRequest, Class<?> type, 
			Type genericType, Annotation[] annotations, MediaType mediaType) {
		
		return -1;
	}

	/**
	 * Return true if and only id type is PKCS10CertificationRequest.
	 * 
	 */
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		
		return PKCS10CertificationRequest.class == type;
	}

	/**
  * Get the PKCS10CefrtificateRequest object and serialize it to PEM encoded bytes using 
  * BC's PEM serialization.
  * 
  */
	public void writeTo(PKCS10CertificationRequest certificateRequest, Class<?> type, Type genericType, Annotation[] annotations, 
			MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) 
					throws IOException, WebApplicationException {
		
		OutputStreamWriter writer = new OutputStreamWriter(entityStream);
		
		PEMWriter pemWriter = new PEMWriter(writer);
		
		pemWriter.writeObject(certificateRequest);

		pemWriter.close();
	}

}
