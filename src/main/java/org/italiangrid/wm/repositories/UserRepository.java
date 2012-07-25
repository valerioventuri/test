package org.italiangrid.wm.repositories;

import org.italiangrid.wm.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
	
	User findByDn(String dn);
	
}
