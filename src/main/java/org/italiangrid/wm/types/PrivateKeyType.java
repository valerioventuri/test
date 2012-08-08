package org.italiangrid.wm.types;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PEMWriter;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.TextType;
import org.hibernate.usertype.UserType;

/**
 * Hibernate custom type for persisting properties of type PrivateKey.
 * 
 */
public class PrivateKeyType implements UserType {

	public Object assemble(Serializable cached, Object arg1) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object deepCopy(Object object) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public Serializable disassemble(Object object) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean equals(Object anObject, Object anotherObject) throws HibernateException {
		// TODO Auto-generated method stub
		return false;
	}

	public int hashCode(Object object) throws HibernateException {
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
		
        PrivateKey privateKey = null;

        PEMReader reader = new PEMReader(new StringReader(string));
        
        try {
			
        	privateKey = ((KeyPair) reader.readObject()).getPrivate();
			
        	reader.close();
			
		} catch (IOException e) {

			throw new HibernateException(e);
		
		}
        
        return privateKey;
	}

	public void nullSafeSet(PreparedStatement statement, Object object, int index, SessionImplementor session) 
			throws HibernateException, SQLException {

		if(object == null) {
			
			TextType.INSTANCE.set(statement, null, index, session);

		} else {
			
			final PrivateKey privateKey = (PrivateKey) object;
			
			StringWriter stringWriter = new StringWriter();
			
			PEMWriter pemWriter = new PEMWriter(stringWriter);
			
			String string = null;
			
			try {
				
				pemWriter.writeObject(privateKey);

				pemWriter.close();
				
				string = stringWriter.toString();
				
			} catch (IOException e) {
			
				throw new HibernateException(e);
				
			}
			
			TextType.INSTANCE.set(statement, string, index, session);
			
		}

	}

	public Object replace(Object original, Object target, Object arg2) throws HibernateException {

		return original;
	}

	public Class returnedClass() {

		return PrivateKey.class;
	}

	public int[] sqlTypes() {
		
		return new int[] {TextType.INSTANCE.sqlType()};
	}

}
