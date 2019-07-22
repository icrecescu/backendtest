package com.crecescu.dao.impl;

import static com.crecescu.dao.utils.DaoUtils.SQL_CREATE_TRANSACTION;
import static com.crecescu.dao.utils.DaoUtils.SQL_GET_ACCOUNT_BY_ID_WITH_LOCK;
import static com.crecescu.dao.utils.DaoUtils.SQL_UPDATE_ACCOUNT_BALANCE;

import com.crecescu.dao.TransactionDao;
import com.crecescu.dao.utils.DaoUtils;
import com.crecescu.exception.AccountException;
import com.crecescu.model.Account;
import com.crecescu.model.Transaction;
import com.crecescu.utils.DataPopulator;
import com.crecescu.utils.Utils;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.dbutils.DbUtils;

public class TransactionDaoImpl implements TransactionDao {

  @Override
  public Transaction transferFunds(Transaction transaction) {
    Connection conn = null;
    PreparedStatement lockStmt = null;
    ResultSet rs = null;
    Account source = null;
    Account destination = null;

    transaction.setId(DaoUtils.generateTransactionId());
    transaction.setDateCreated(System.currentTimeMillis());

    try {
      conn = DataPopulator.getConnection();
      conn.setAutoCommit(false);
      // lock accounts
      lockStmt = conn.prepareStatement(SQL_GET_ACCOUNT_BY_ID_WITH_LOCK);
      lockStmt.setString(1, transaction.getSource());
      rs = lockStmt.executeQuery();
      if (rs.next()) {
        source = DaoUtils.convertResultSetToAccount(rs);
      }
      lockStmt = conn.prepareStatement(SQL_GET_ACCOUNT_BY_ID_WITH_LOCK);
      lockStmt.setString(1, transaction.getDestination());
      rs = lockStmt.executeQuery();
      if (rs.next()) {
        destination = DaoUtils.convertResultSetToAccount(rs);
      }

      if (source == null || destination == null) {
        throw new AccountException("Transfer failed");
      }

      // check there are enough funds in source account
      BigDecimal fromAccountLeftOver = source.getBalance().subtract(transaction.getAmount());
      if (fromAccountLeftOver.compareTo(source.getLimit().negate()) < 0) {
        throw new AccountException("Not enough Fund from source Account ");
      }

      BigDecimal amountToAddToDestination = getAmountToAdd(source, destination,
          transaction.getAmount());

      //update balance of source and destination accounts
      updateAccountsBalance(conn, fromAccountLeftOver, destination.getBalance()
          .add(amountToAddToDestination), source.getAccountId(), destination.getAccountId());

      createTransaction(conn, transaction);

      conn.commit();
    } catch (SQLException se) {
      // rollback transaction if exception occurs
      try {
        conn.rollback();
      } catch (SQLException re) {
        throw new AccountException("Fail to rollback transaction", re);
      }
    } finally {
      DbUtils.closeQuietly(conn);
      DbUtils.closeQuietly(rs);
      DbUtils.closeQuietly(lockStmt);
    }
    return transaction;
  }

  private BigDecimal getAmountToAdd(Account source, Account destination, BigDecimal initial) {
    BigDecimal amountToAddToDestination = initial;

    if (!source.getCurrencyCode().equals(destination.getCurrencyCode())) {
      BigDecimal exchangeRate =
          Utils.getExchangeRate(source.getCurrencyCode(), destination.getCurrencyCode());

      if (exchangeRate == null) {
        throw new AccountException("Transaction can not be created");
      }

      amountToAddToDestination = amountToAddToDestination.multiply(exchangeRate);
    }

    return amountToAddToDestination;
  }

  private void updateAccountsBalance(Connection conn,
      BigDecimal fromAccountLeftOver, BigDecimal amountToAddToDestination,
      String source, String destination) throws SQLException {
    PreparedStatement updateStmt = conn.prepareStatement(SQL_UPDATE_ACCOUNT_BALANCE);
    updateStmt.setBigDecimal(1, fromAccountLeftOver);
    updateStmt.setString(2, source);
    updateStmt.addBatch();
    updateStmt.setBigDecimal(1, amountToAddToDestination);
    updateStmt.setString(2, destination);
    updateStmt.addBatch();
    updateStmt.executeBatch();
  }

  private void createTransaction(Connection conn, Transaction transaction)
      throws SQLException {
    PreparedStatement transStmt = conn.prepareStatement(SQL_CREATE_TRANSACTION);
    transStmt.setString(1, transaction.getId());
    transStmt.setLong(2, transaction.getDateCreated());
    transStmt.setString(3, transaction.getSource());
    transStmt.setString(4, transaction.getDestination());
    transStmt.setBigDecimal(5, transaction.getAmount());
    int affectedRows = transStmt.executeUpdate();
    if (affectedRows == 0) {
      throw new AccountException("Transaction can not be created");
    }
  }

}
