package org.italiangrid.wm.repositories;

import java.io.InputStream;
import java.security.Security;
import java.util.List;

import org.italiangrid.wm.model.Job;
import org.italiangrid.wm.model.User;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.yaml.snakeyaml.Yaml;

public class UserRepositoryTest {

	private static UserRepository userRepository = null;

	@BeforeClass
	public static void setUp() {
	
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		
		ApplicationContext context = 
				new ClassPathXmlApplicationContext(new String[] {"repository-context.xml", 
						                                         "infrastructure.xml"});
		
		userRepository = context.getBean(UserRepository.class);

		Yaml yaml = new Yaml();

		InputStream inputStream = ClassLoader.getSystemResourceAsStream("data.yml");
		
		List<Object> objects = (List<Object>) yaml.load(inputStream);
		
		for(Object object : objects) {
			
			Assert.assertTrue(object instanceof User || object instanceof Job);
			
			if(object instanceof User) {
				
				User user = (User) object;
				
				userRepository.save(user);
			} 
		}
	}

	@Test
	public void find() {
		
		User user = userRepository.findByDn("CN=Gastone Paperone");
		
		Assert.assertNotNull(user);
		Assert.assertEquals("CN=Gastone Paperone", user.getDn());
	}
	
	
	@Test
	public void create() {
		
		{
			User user = new User();
			user.setDn("CN=Paolino Paperino");
		
			userRepository.save(user);
		}
		
		{
			User user = userRepository.findByDn("CN=Paolino Paperino");

			Assert.assertNotNull(user);
			Assert.assertEquals("CN=Paolino Paperino", user.getDn());
		}
		
	}

	@AfterClass
	public static void tearDown() {
		
		userRepository.deleteAll();
	}
	
}
