package com.crecescu.app;

import com.crecescu.utils.DataPopulator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public class Main {

  public static void main(String[] args) throws Exception {
    DataPopulator.createDummyDb();
    startService();
  }

  private static void startService() throws Exception {
    ResourceConfig rs = new Hk2Config();
    ServletHolder holder = new ServletHolder(new ServletContainer(rs));
    Server server = new Server(8080);
    ServletContextHandler context = new ServletContextHandler(server, "/");
    context.addServlet(holder, "/*");
    try {
      server.start();
      server.join();
    } catch (Exception ignored) {
      server.stop();
      server.destroy();
    }
  }

}
