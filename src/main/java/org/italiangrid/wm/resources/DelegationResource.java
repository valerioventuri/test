package org.italiangrid.wm.resources;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.provider.JDKKeyPairGenerator;
import org.italiangrid.utils.voms.VOMSSecurityContext;
import org.italiangrid.wm.model.Delegation;
import org.italiangrid.wm.model.User;
import org.italiangrid.wm.repositories.UserRepository;

import eu.emi.security.authn.x509.proxy.ProxyCSR;
import eu.emi.security.authn.x509.proxy.ProxyCSRGenerator;
import eu.emi.security.authn.x509.proxy.ProxyCertificateOptions;

@Path("/delegation")
@Produces("text/plain")
@Consumes("text/plain")
public class DelegationResource {

	private UserRepository userRepository;

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@POST
	public Response initDelegation() {
		
		VOMSSecurityContext securityContext = VOMSSecurityContext.getCurrentContext();
		String authenticatedUserDn = securityContext.getClientDN().getRFCDN();
		X509Certificate[] chain = securityContext.getClientCertChain();
		
		User user = userRepository.findByDn(authenticatedUserDn);

		if(user == null) {
			
			user = new User();
			user.setDn(authenticatedUserDn);
			
			userRepository.save(user);
		}

		Delegation delegation = new Delegation();
		delegation.setChain(chain);
		
		JDKKeyPairGenerator.RSA keyPairGenerator = new JDKKeyPairGenerator.RSA();
		keyPairGenerator.initialize(1024, new SecureRandom());
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		
		delegation.setPrivateKey(keyPair.getPrivate());
		
		user.setDelegation(delegation);

		ProxyCertificateOptions options = 
				new ProxyCertificateOptions(chain);
		
		ProxyCSR certificateRequest = null;
		
		try {
			
			certificateRequest = ProxyCSRGenerator.generate(options);
		
		} catch (InvalidKeyException e) {
			
			return Response.serverError().build();
		
		} catch (CertificateEncodingException e) {
			
			return Response.serverError().build();
		
		} catch (SignatureException e) {
			
			return Response.serverError().build();
		
		} catch (NoSuchAlgorithmException e) {
			
			return Response.serverError().build();
		}
		
		PKCS10CertificationRequest certificationRequest = certificateRequest.getCSR();
		
		userRepository.save(user);
		
		return Response.created(null).entity(certificationRequest).build();
	}
	
	@PUT
	public Response finalizeDelegation(X509Certificate certificate) {
		
		VOMSSecurityContext securityContext = VOMSSecurityContext.getCurrentContext();
		String authenticatedUserDn = securityContext.getClientDN().getRFCDN();
		
		User user = userRepository.findByDn(authenticatedUserDn);

		Delegation delegation = user.getDelegation();
		
		delegation.setCertificate(certificate);
		
		userRepository.save(user);
		
		return Response.ok().build();
	}

}
