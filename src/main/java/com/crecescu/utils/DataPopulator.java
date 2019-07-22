package com.crecescu.utils;

import java.io.InputStreamReader;
import org.apache.commons.dbutils.DbUtils;
import org.h2.tools.RunScript;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataPopulator {

  private static final String h2_driver = Utils.getStringProperty("h2_driver");
  private static final String h2_connection_url = Utils.getStringProperty("h2_connection_url");
  private static final String h2_user = Utils.getStringProperty("h2_user");
  private static final String h2_password = Utils.getStringProperty("h2_password");

  static {
    DbUtils.loadDriver(h2_driver);
  }

  public static void createDummyDb() {
    Connection conn = null;
    try {
      conn = getConnection();
      RunScript.execute(conn, new InputStreamReader(DataPopulator.class.getClassLoader()
          .getResourceAsStream("demo.sql")));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      DbUtils.closeQuietly(conn);
    }
  }

  public static Connection getConnection() {
    try {
      return DriverManager.getConnection(h2_connection_url, h2_user, h2_password);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

}
