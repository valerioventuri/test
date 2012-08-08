package org.italiangrid.wm.model;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.italiangrid.wm.types.PrivateKeyType;
import org.italiangrid.wm.types.X509CertificateArrayType;
import org.italiangrid.wm.types.X509CertificateType;

/**
 * Models a user's delegated credential.
 * <p>
 * Delegation of credentials is done by making available to the service a proxy certificate (see
 * http://www.ietf.org/rfc/rfc3820.txt) derived from an end entity certificate.
 * <p>
 * The delegation is made of the proxy certificate, the correspondent private key, and the chain from which
 * the proxy certificate derived.
 * 
 */
@Entity
@TypeDefs({@TypeDef(defaultForType = X509Certificate[].class, typeClass = X509CertificateArrayType.class),
           @TypeDef(defaultForType = X509Certificate.class, typeClass = X509CertificateType.class),
           @TypeDef(defaultForType = PrivateKey.class, typeClass = PrivateKeyType.class)})
public class Delegation {

  private Long id;

  /**
   * The chain from which the proxy certificate derived.
   * 
   */
  private X509Certificate[] chain;

  /**
   * The proxy certificate.
   * 
   */
  private X509Certificate certificate;

  /**
   * The private key.
   * 
   */
  private PrivateKey privateKey;

  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  public Long getId() {

    return id;
  }

  public void setId(Long id) {

    this.id = id;
  }

  public X509Certificate[] getChain() {

    return chain;
  }

  public void setChain(X509Certificate[] chain) {

    this.chain = chain;
  }

  public X509Certificate getCertificate() {

    return certificate;
  }

  public void setCertificate(X509Certificate certificate) {

    this.certificate = certificate;
  }

  public PrivateKey getPrivateKey() {

    return privateKey;
  }

  public void setPrivateKey(PrivateKey privateKey) {

    this.privateKey = privateKey;
  }

}
