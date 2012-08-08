package org.italiangrid.wm.repositories;

import org.italiangrid.wm.model.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for the user model.
 * <p>
 * Extends Spring Data CrudRepository, inheriting CRUD operations (save, findOne, findAll, count,
 * delete, exists), and automagically created query methods.
 * 
 */
public interface UserRepository extends CrudRepository<User, Long> {

  /**
   * Find a user given the distinguished name.
   * 
   * @param dn the distinguished name.
   * @return the user, or null if a user with such distinguished name does not exist
   */
  User findByDn(String dn);

}
