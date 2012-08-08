package org.italiangrid.wm.providers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.security.cert.X509Certificate;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.bouncycastle.openssl.PEMWriter;

/**
 * Message body provider for serializing java.security.cert.X509Certificate to PEM encoded certificate.
 * 
 */
@Provider
@Produces("text/plain")
public class X509CertificateBodyWriter implements MessageBodyWriter<X509Certificate> {

  /**
   * Return -1, as the length cannot be determined in advance.
   * 
   */
	public long getSize(X509Certificate certificate, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		
		return -1;
	}

	/**
	 * Always returns true?
	 * 
	 */
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType arg4) {

		return true;
	}

	/**
	 * Get the X509Certificate object and serialize it to PEM encoded bytes using BC's PEM serialization.
	 * 
	 */
	public void writeTo(X509Certificate certificate, Class<?> type, Type genericType, Annotation[] annotations, 
			MediaType mediaType, MultivaluedMap<String, Object> headers, OutputStream entityStream)
			throws IOException, WebApplicationException {

		OutputStreamWriter writer = new OutputStreamWriter(entityStream);
		
		PEMWriter pemWriter = new PEMWriter(writer);
		
		pemWriter.writeObject(certificate);

		pemWriter.close();
	}

}
