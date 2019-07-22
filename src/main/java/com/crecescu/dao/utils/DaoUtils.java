package com.crecescu.dao.utils;

import com.crecescu.model.Account;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DaoUtils {

  public final static String SQL_CREATE_ACCOUNT = "INSERT INTO accounts "
      + "(account_id, user_id, balance, currency, account_limit) VALUES (?, ?, ?, ?, ?)";
  public final static String SQL_GET_ACCOUNT_BY_ID = "SELECT account_id, user_id, balance, "
          + "currency, account_limit from accounts WHERE account_id = ? ";
  public final static String SQL_GET_ACCOUNT_BY_USER_ID = "SELECT account_id, user_id, balance, "
              + "currency, account_limit from accounts WHERE user_id = ? ";
  public final static String SQL_DELETE_ACCOUNT_BY_ID = "DELETE FROM accounts WHERE account_id = ?";
  public final static String SQL_GET_ACCOUNT_BY_ID_WITH_LOCK =
                  "SELECT account_id, user_id, balance, "
                      + "currency, account_limit from accounts WHERE account_id = ? FOR UPDATE";
  public final static String SQL_UPDATE_ACCOUNT_BALANCE = "UPDATE accounts SET balance = ? WHERE "
                          + "account_id = ?";
  public final static String SQL_CREATE_TRANSACTION = "INSERT INTO transactions "
      + "(tid, date_created, source, destination, balance) VALUES (?, ?, ?, ?, ?)";

  public static Account convertResultSetToAccount(ResultSet resultSet) throws SQLException {
    Account account = new Account();
    account.setAccountId(resultSet.getString("account_id"));
    account.setUserId(resultSet.getInt("user_id"));
    account.setBalance(resultSet.getBigDecimal("balance"));
    account.setCurrencyCode(resultSet.getString("currency"));
    account.setLimit(resultSet.getBigDecimal("account_limit"));
    return account;
  }

  public static String generateTransactionId(){
    return UUID.randomUUID().toString();
  }
}
