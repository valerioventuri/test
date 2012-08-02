package org.italiangrid.wm.client;

import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Properties;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.ws.rs.core.MediaType;

import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.italiangrid.wm.providers.PKCS10CertificationRequestBodyReader;
import org.italiangrid.wm.providers.X509CertificateBodyWriter;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import com.sun.security.auth.module.UnixSystem;

import eu.emi.security.authn.x509.impl.OpensslCertChainValidator;
import eu.emi.security.authn.x509.impl.PEMCredential;
import eu.emi.security.authn.x509.impl.SocketFactoryCreator;
import eu.emi.security.authn.x509.proxy.ProxyGenerator;
import eu.emi.security.authn.x509.proxy.ProxyRequestOptions;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args)  {
		
		Properties configuration = new Properties();
		
		URL propertiesFile = ClassLoader.getSystemResource("client.properties");

		if (propertiesFile == null) {
			
			System.err.println("Can't load properties file");
			System.exit(0);
		}
		
		try {
		
			configuration.load(propertiesFile.openStream());
		
		} catch (IOException e1) {
			
			System.err.println("Can't load properties");
			System.exit(0);
		}
		
	
		PEMCredential credential = null;
		
		String proxyCertificateFile = "/tmp/x509up_u" + new UnixSystem().getUid();

		System.out.println("Using proxy certificate in " + proxyCertificateFile);
		
		try {
		
			credential = new PEMCredential(proxyCertificateFile, null);
		
		} catch (KeyStoreException e) {
			
			System.err.println("Can't load proxy certificate: " + e.getMessage());
			System.exit(0);
			
			
		} catch (CertificateException e) {
		
			System.err.println("Can't load proxy certificate: " + e.getMessage());
			System.exit(0);
			
		} catch (IOException e) {
		
			System.err.println("Can't load proxy certificate: " + e.getMessage());
			System.exit(0);
		} 

		
		OpensslCertChainValidator validator = 
				new OpensslCertChainValidator("/etc/grid-security/certificates");

		SSLContext sslContext = SocketFactoryCreator.getSSLContext(credential, validator, null);
		
		ClientConfig clientConfig = new DefaultClientConfig();

		clientConfig.getClasses().add(PKCS10CertificationRequestBodyReader.class);
		clientConfig.getClasses().add(X509CertificateBodyWriter.class);
		
		clientConfig.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(
				new HostnameVerifier() {
					public boolean verify(String arg0, SSLSession arg1) {
						return true;
					}
				},sslContext));
		
		Client client = Client.create(clientConfig);
		
		
		String serviceBaseurl = configuration.getProperty("service.baseurl");
		
		if(serviceBaseurl == null || serviceBaseurl.isEmpty()) {
			
			System.err.println("Property service.baseurl not set in properties file");
			
			validator.dispose();
			client.destroy();
			
			System.exit(0);
		}
		
		WebResource resource = client.resource(serviceBaseurl + "/delegation");
		
		
		ClientResponse initResponse = resource.accept(MediaType.TEXT_PLAIN).post(ClientResponse.class);
		
		if(initResponse.getStatus() != 201) {
			
			System.err.println("There was a problem initiating the delegation process with the service");
			
			validator.dispose();
			client.destroy();
			
			System.exit(0);
		}
		
		PKCS10CertificationRequest certificationRequest = initResponse.getEntity(PKCS10CertificationRequest.class);
		
		try {
			
			certificationRequest.verify();
		
		} catch (InvalidKeyException e) {
			
			System.err.println("Can't verify the certificate request. " + e.getMessage());
			
			validator.dispose();
			client.destroy();
			
			System.exit(0);
			
		} catch (NoSuchAlgorithmException e) {
		
			System.err.println("Can't verify the certificate request. " + e.getMessage());
			
			validator.dispose();
			client.destroy();
			
			System.exit(0);
			
		} catch (NoSuchProviderException e) {
		
			System.err.println("Can't verify the certificate request. " + e.getMessage());
			
			validator.dispose();
			client.destroy();
			
			System.exit(0);
			
		} catch (SignatureException e) {
		
			System.err.println("Can't verify the certificate request. " + e.getMessage());
			
			validator.dispose();
			client.destroy();
			
			System.exit(0);
		}
		
		
		X509Certificate[] proxyChain = null;
		
		try {
			
			ProxyRequestOptions options = new ProxyRequestOptions(credential.getCertificateChain(), certificationRequest);

			proxyChain = ProxyGenerator.generate(options, credential.getKey());

		} catch (InvalidKeyException e) {
		
			System.err.println("Can't generate proxy certificate. " + e.getMessage());

			validator.dispose();
			client.destroy();
			
			System.exit(0);
			
		} catch (NoSuchAlgorithmException e) {
			
			System.err.println("Can't generate proxy certificate. " + e.getMessage());
		
			validator.dispose();
			client.destroy();
			
			System.exit(0);
			
		} catch (NoSuchProviderException e) {
		
			System.err.println("Can't generate proxy certificate. " + e.getMessage());
		
			validator.dispose();
			client.destroy();
			
			System.exit(0);
			
		} catch (CertificateParsingException e) {
			
			System.err.println("Can't generate proxy certificate. " + e.getMessage());
			
			validator.dispose();
			client.destroy();
			
			System.exit(0);
			
		} catch (SignatureException e) {
			
			System.err.println("Can't generate proxy certificate. " + e.getMessage());
			
		} 
		
		X509Certificate proxyCertificate = proxyChain[0];
		
		
		ClientResponse putResponse = resource.type(MediaType.TEXT_PLAIN_TYPE).put(ClientResponse.class, proxyCertificate);
		
		if(putResponse.getStatus() != 200) {
			
			System.err.println("Can't finalize delegation.");
			
			validator.dispose();
			client.destroy();
			
			System.exit(0);
		}
		
		System.out.println("Credential delegated to the service");
		
		client.destroy();
		
		validator.dispose();
	}

}
