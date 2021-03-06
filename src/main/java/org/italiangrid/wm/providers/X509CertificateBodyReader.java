package org.italiangrid.wm.providers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.security.cert.X509Certificate;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import org.bouncycastle.openssl.PEMReader;

/**
 * Message body provider for deserializing PEM encoded certificate to java.security.cert.X509Certificate.
 * 
 */
@Provider
@Consumes("text/plain")
public class X509CertificateBodyReader implements MessageBodyReader<X509Certificate> {

  /**
   * Return true if and only if type is X509Certificate.
   * 
   */
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		
		return X509Certificate.class == type;
	}

	/**
	 * Reads the PEM encoded certificate from input stream and deserialize it to a X509Certificate
	 * using BC's PEM serialization.
	 * 
	 */
	public X509Certificate readFrom(Class<X509Certificate> type, Type genericType, Annotation[] annotations, 
			MediaType mediaType, MultivaluedMap<String, String> headers, InputStream inputStream)
			throws IOException, WebApplicationException {

		PEMReader pemReader = new PEMReader(new InputStreamReader(inputStream));
		
		X509Certificate certificate = (X509Certificate) pemReader.readObject();
		
		pemReader.close();
		
		return certificate;
	}

}
