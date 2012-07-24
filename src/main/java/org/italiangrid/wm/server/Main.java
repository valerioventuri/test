package org.italiangrid.wm.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.italiangrid.utils.https.ServerFactory;

import com.sun.jersey.spi.container.servlet.ServletContainer;

public class Main {

	private static Server server;
	
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		server = ServerFactory.newServer("localhost", 
				Integer.parseInt("8443"));
		
		ServletContainer servlet = new ServletContainer();
		
		ServletHolder holder = new ServletHolder(servlet);
		holder.setInitParameter("com.sun.jersey.config.property.packages", 
				                "org.italiangrid.wm.resources");
		holder.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");

		ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		servletContextHandler.setContextPath("/");
		servletContextHandler.addServlet(holder, "/*");

		ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();
		contextHandlerCollection.addHandler(servletContextHandler);
		server.setHandler(contextHandlerCollection);
		
		server.start();
		server.join();

	}

}
