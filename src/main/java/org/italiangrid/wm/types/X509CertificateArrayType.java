package org.italiangrid.wm.types;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.cert.X509Certificate;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PEMWriter;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.TextType;
import org.hibernate.usertype.UserType;

/**
 * Hibernate custom type for persisting properties of type X509Certificate[].
 * 
 */
public class X509CertificateArrayType implements UserType {

	public Object assemble(Serializable arg0, Object arg1) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object deepCopy(Object object) throws HibernateException {

		return object;
	}

	public Serializable disassemble(Object arg0) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean equals(Object arg0, Object arg1) throws HibernateException {
		// TODO Auto-generated method stub
		return false;
	}

	public int hashCode(Object arg0) throws HibernateException {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isMutable() {

		return false;
	}

	public Object nullSafeGet(ResultSet results, String[] names, SessionImplementor session, Object object) 
			throws HibernateException, SQLException {
		
		assert names.length == 1;
		
		String string = (String) TextType.INSTANCE.get(results, names[0], session);
		
		if(string == null || string.isEmpty())
			return null;

		List<X509Certificate> certificates = new ArrayList<X509Certificate>();
		
		PEMReader pemReader = new PEMReader(new StringReader(string));
		
		while(true) {
		
			X509Certificate certificate = null;
			
			try {
			
				certificate = (X509Certificate) pemReader.readObject();
			
				if(certificate == null) {
					
					pemReader.close();
					break;
				}
				
			} catch (IOException e) {
			
				throw new HibernateException(e);
			
			}
			
			certificates.add(certificate);
		}
		
		return certificates.toArray(new X509Certificate[certificates.size()]);
	}

	public void nullSafeSet(PreparedStatement statement, Object object, int index, SessionImplementor session) 
			throws HibernateException, SQLException {
		
		if(object == null) {
			
			TextType.INSTANCE.set(statement, null, index, session);
			
		} else {
			
			final X509Certificate[] certificates = (X509Certificate[]) object;

			StringWriter stringWriter = new StringWriter();
			
			PEMWriter pemWriter = new PEMWriter(stringWriter);

			String string = null;
			
			try {
				
				for(int i =0; i<certificates.length; ++i) {
				
					pemWriter.writeObject(certificates[i]); 
		
				}				
				
				pemWriter.close();
				
				string = stringWriter.toString();

			} catch (IOException e) {
				
				throw new HibernateException(e);
				
			}

			TextType.INSTANCE.set(statement, string, index, session);
		}
	}

	public Object replace(Object original, Object targer, Object arg2) throws HibernateException {
		
		return original;
	}

	public Class returnedClass() {

		return X509Certificate[].class;
	}

	public int[] sqlTypes() {

		return new int[] {TextType.INSTANCE.sqlType()};
	}

}
