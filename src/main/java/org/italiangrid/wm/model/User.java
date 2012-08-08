package org.italiangrid.wm.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

/**
 * Models a user of the service.
 * <p>
 * A user is uniquely identified by the distinguished name of the certificate with which she connects to the
 * service.
 * 
 */
@Entity
public class User {

  private Long id;

  /**
   * The distinguished name of the user certificate.
   */
  private String dn;

  /**
   * The user may have delegated her credential to the service.
   */
  private Delegation delegation;

  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  public Long getId() {

    return id;
  }

  public void setId(Long id) {

    this.id = id;
  }

  @Column(unique = true, nullable = false)
  public String getDn() {
    return dn;
  }

  public void setDn(String dn) {
    this.dn = dn;
  }

  @OneToOne(cascade = CascadeType.ALL)
  public Delegation getDelegation() {

    return delegation;
  }

  public void setDelegation(Delegation delegation) {

    this.delegation = delegation;
  }

}
