package com.crecescu.utils;

import com.crecescu.exception.AccountException;
import com.crecescu.model.Account;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;
import java.math.BigDecimal;
import java.util.Random;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Utils {

  private static Properties properties = new Properties();
  private static Logger log = Logger.getLogger(Utils.class);

  private enum Currency {
    USD,
    EUR
  }

  private static Table<Currency, Currency, BigDecimal> currencyTable = HashBasedTable.create();

  static {
    String configFileName = System.getProperty("app.properties");

    if (configFileName == null) {
      configFileName = "app.properties";
    }
    loadConfig(configFileName);

    initializeCurrencyTable();
  }

  private static void initializeCurrencyTable() {
    currencyTable.put(Currency.EUR, Currency.USD, new BigDecimal("1.44"));
    currencyTable.put(Currency.USD, Currency.EUR, new BigDecimal("0.80"));
  }

  public static void loadConfig(String fileName) {
    if (fileName == null) {
      log.warn("loadConfig: config file name cannot be null");
    } else {
      try {
        log.info("loadConfig(): Loading config file: " + fileName);
        final InputStream fis = Utils.class.getClassLoader().getResourceAsStream(fileName);
        properties.load(fis);

      } catch (FileNotFoundException fne) {
        log.error("loadConfig(): file name not found " + fileName, fne);
      } catch (IOException ioe) {
        log.error("loadConfig(): error when reading the config " + fileName, ioe);
      }
    }

  }

  public static String getStringProperty(String key) {
    String value = properties.getProperty(key);
    if (value == null) {
      value = System.getProperty(key);
    }
    return value;
  }

  /**
   * Mock method used to validate an incoming account. In a real world scenario more validations
   * would be involved
   */
  public static boolean validateAccount(Account account) {
    for(Currency c : Currency.values()){
      if(c.name().equalsIgnoreCase(account.getCurrencyCode()))
        return true;
    }
    throw new AccountException("Invalid currency for account");
  }

  /**
   * Mock method not used in a real world scenario for generating account ids. It generates an
   * account id similar to an IBAN formed as currency + userId + random alphanumeric string
   */
  public static String generateAccountId(Account account) {
    String randomAccountId = account.getCurrencyCode();
    randomAccountId += account.getUserId();
    randomAccountId += RandomStringUtils.random(10, true, true);
    return randomAccountId.toUpperCase();
  }

  public static BigDecimal getExchangeRate(String cur1, String cur2) {
    return currencyTable.get(Currency.valueOf(cur1), Currency.valueOf(cur2));
  }

}
