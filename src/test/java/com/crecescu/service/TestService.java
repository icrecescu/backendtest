package com.crecescu.service;

import com.crecescu.app.Hk2Config;
import com.crecescu.utils.DataPopulator;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;


public abstract class TestService {

  protected static Server server = null;
  protected static PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();

  protected static HttpClient client;
  protected ObjectMapper mapper = new ObjectMapper();
  protected URIBuilder builder = new URIBuilder().setScheme("http").setHost("localhost:8080");


  @BeforeAll
  public static void setup() throws Exception {
    DataPopulator.createDummyDb();
    startServer();
    connManager.setDefaultMaxPerRoute(100);
    connManager.setMaxTotal(200);
    client = HttpClients.custom()
        .setConnectionManager(connManager)
        .setConnectionManagerShared(true)
        .build();

  }

  @AfterAll
  public static void closeClient(){
    HttpClientUtils.closeQuietly(client);
  }


  private static void startServer() throws Exception {
    if (server == null) {
      ResourceConfig rs = new Hk2Config();
      ServletHolder holder = new ServletHolder(new ServletContainer(rs));
      Server server = new Server(8080);
      ServletContextHandler context = new ServletContextHandler(server, "/");
      context.addServlet(holder, "/*");
      try {
        server.start();
        //server.join();
      } catch (Exception ignored) {
        server.stop();
        server.destroy();
      }
    }
  }
}
