package org.italiangrid.wm.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.italiangrid.utils.voms.VOMSSecurityContext;
import org.italiangrid.wm.model.User;
import org.italiangrid.wm.repositories.UserRepository;

@Path("/delegation")
public class DelegationResource {

	private UserRepository userRepository;

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@GET
	public Response getDelegation() {

		VOMSSecurityContext securityContext = VOMSSecurityContext.getCurrentContext();
		String authenticatedUserDn = securityContext.getClientDN().getRFCDN();
		
		User user = userRepository.findByDn(authenticatedUserDn);
		
		return Response.ok(user.getDn()).build();
	}

}
