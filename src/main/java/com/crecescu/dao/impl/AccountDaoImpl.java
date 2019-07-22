package com.crecescu.dao.impl;

import com.crecescu.dao.AccountDao;
import com.crecescu.dao.utils.DaoUtils;
import com.crecescu.exception.AccountException;
import com.crecescu.exception.AccountNotFoundException;
import com.crecescu.model.Account;
import com.crecescu.utils.DataPopulator;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.dbutils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.SQLException;

public class AccountDaoImpl implements AccountDao {

  @Override
  public void createAccount(Account account){
    Connection conn = null;
    PreparedStatement stmt;
    try {
      conn = DataPopulator.getConnection();
      stmt = conn.prepareStatement(DaoUtils.SQL_CREATE_ACCOUNT);
      stmt.setString(1, account.getAccountId());
      stmt.setInt(2, account.getUserId());
      stmt.setBigDecimal(3, account.getBalance());
      stmt.setString(4, account.getCurrencyCode());
      stmt.setBigDecimal(5, account.getLimit());
      int affectedRows = stmt.executeUpdate();
      if (affectedRows == 0) {
        throw new AccountException("Account can not be created");
      }
    } catch (SQLException e) {
      throw new AccountException("Error creating user account " + account, e);
    } finally {
      DbUtils.closeQuietly(conn);
    }
  }

  @Override
  public Account getAccountById(String accountId){
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    Account acc = null;
    try {
      conn = DataPopulator.getConnection();
      stmt = conn.prepareStatement(DaoUtils.SQL_GET_ACCOUNT_BY_ID);
      stmt.setString(1, accountId);
      rs = stmt.executeQuery();
      if (rs.next()) {
        acc = DaoUtils.convertResultSetToAccount(rs);
      }
      if(acc == null){
        throw new AccountNotFoundException("Account not found");
      }
      return acc;
    } catch (SQLException e) {
      throw new AccountException("Error reading account data", e);
    } finally {
      DbUtils.closeQuietly(conn, stmt, rs);
    }
  }

  @Override
  public void deleteAccountById(String accountId){
    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = DataPopulator.getConnection();
      stmt = conn.prepareStatement(DaoUtils.SQL_DELETE_ACCOUNT_BY_ID);
      stmt.setString(1, accountId);
      int updateCount = stmt.executeUpdate();
      if(updateCount != 1){
        throw new AccountNotFoundException("Account to be deleted not found");
      }
    } catch (SQLException e) {
      throw new AccountException("Error deleting user account", e);
    } finally {
      DbUtils.closeQuietly(conn);
      DbUtils.closeQuietly(stmt);
    }
  }

  @Override
  public List<Account> getAccountsByUserId(int userId){
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    List<Account> allAccounts = new ArrayList<>();
    try {
      conn = DataPopulator.getConnection();
      stmt = conn.prepareStatement(DaoUtils.SQL_GET_ACCOUNT_BY_USER_ID);
      stmt.setInt(1, userId);
      rs = stmt.executeQuery();
      while (rs.next()) {
        allAccounts.add(DaoUtils.convertResultSetToAccount(rs));
      }
      return allAccounts;
    } catch (SQLException e) {
      throw new AccountException("Error reading account data", e);
    } finally {
      DbUtils.closeQuietly(conn, stmt, rs);
    }
  }

  @Override
  public Account updateAccountBalance(String accountId, BigDecimal amount){
    Connection conn = null;
    PreparedStatement lockStmt = null;
    PreparedStatement updateStmt = null;
    ResultSet rs = null;
    Account targetAccount = null;
    try {
      conn = DataPopulator.getConnection();
      conn.setAutoCommit(false);

      lockStmt = conn.prepareStatement(DaoUtils.SQL_GET_ACCOUNT_BY_ID_WITH_LOCK);
      lockStmt.setString(1, accountId);

      rs = lockStmt.executeQuery();
      if (rs.next()) {
        targetAccount = DaoUtils.convertResultSetToAccount(rs);
      }

      if (targetAccount == null) {
        throw new AccountNotFoundException("Account to be updated not found");
      }

      BigDecimal balance = targetAccount.getBalance().add(amount);
      if (balance.compareTo(targetAccount.getLimit().negate()) < 0) {
        throw new AccountException("Insufficient funds for account: " + accountId);
      }

      targetAccount.setBalance(balance);
      updateStmt = conn.prepareStatement(DaoUtils.SQL_UPDATE_ACCOUNT_BALANCE);
      updateStmt.setBigDecimal(1, targetAccount.getBalance());
      updateStmt.setString(2, targetAccount.getAccountId());
      updateStmt.executeUpdate();
      conn.commit();
      return targetAccount;

    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException re) {
        throw new AccountException("Fail to rollback transaction", re);
      }
    } finally {
      DbUtils.closeQuietly(conn);
      DbUtils.closeQuietly(rs);
      DbUtils.closeQuietly(lockStmt);
      DbUtils.closeQuietly(updateStmt);
    }
    throw new AccountException("Account's balance couldn't be updated");
  }

}
