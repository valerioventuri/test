package org.italiangrid.wm.server;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.italiangrid.utils.https.SSLOptions;
import org.italiangrid.utils.https.ServerFactory;
import org.italiangrid.utils.voms.VOMSSecurityContextHandler;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

import com.sun.jersey.spi.spring.container.servlet.SpringServlet;

public class Main {

	private static Server server;
	
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		new ClassPathXmlApplicationContext(new String[] {"repository-context.xml", "infrastructure.xml"});
		
		SSLOptions sslOptions = new SSLOptions();
		sslOptions.setCertificateFile("/etc/grid-security/hostcert.pem");
		sslOptions.setKeyFile("/etc/grid-security/hostkey.pem");
		sslOptions.setTrustStoreDirectory("/etc/grid-security/certificates");
		sslOptions.setTrustStoreRefreshIntervalInMsec(60000L);
		sslOptions.setNeedClientAuth(true);
		
		server = ServerFactory.newServer("localhost", Integer.parseInt("8443"), sslOptions);
		
		SpringServlet servlet = new SpringServlet();
		
		ServletHolder holder = new ServletHolder(servlet);
		
		holder.setInitParameter("com.sun.jersey.config.property.packages",
				"org.italiangrid.wm.resources, org.italiangrid.wm.providers");
		holder.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");

		ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		servletContextHandler.setContextPath("/");
		servletContextHandler.addServlet(holder, "/*");
		
		servletContextHandler.getInitParams().put("contextConfigLocation",
		          "classpath:infrastructure.xml, classpath:repository-context.xml");
		servletContextHandler.addEventListener(new ContextLoaderListener());

		HandlerCollection handlerCollection = new HandlerCollection();
		handlerCollection.setHandlers(new Handler[]{ new VOMSSecurityContextHandler(), servletContextHandler});
		server.setHandler(handlerCollection);
		
		server.start();
		server.join();

	}

}
