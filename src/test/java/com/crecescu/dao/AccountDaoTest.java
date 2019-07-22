package com.crecescu.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.crecescu.dao.impl.AccountDaoImpl;
import com.crecescu.exception.AccountNotFoundException;
import com.crecescu.model.Account;
import com.crecescu.utils.DataPopulator;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountDaoTest {

  private AccountDao accountDao;

  @BeforeEach
  public void before() {
    DataPopulator.createDummyDb();
    accountDao = new AccountDaoImpl();
  }

  @Test
  public void testGetAccountById() {
    Account account = getMockedAccount();
    accountDao.createAccount(account);
    Account createdAccount = accountDao.getAccountById(account.getAccountId());

    assertEquals(account.getAccountId(), createdAccount.getAccountId());
    assertEquals(account.getCurrencyCode(), createdAccount.getCurrencyCode());
    assertEquals(account.getBalance().setScale(4, RoundingMode.HALF_EVEN), createdAccount.getBalance());
    assertEquals(account.getLimit(), createdAccount.getLimit());
    assertEquals(account.getUserId(), createdAccount.getUserId());
  }

  @Test
  public void testGetAllUserAccounts(){
    Account account = getMockedAccount();
    accountDao.createAccount(account);
    List<Account> createdAccounts = accountDao.getAccountsByUserId(account.getUserId());

    assertEquals(1, createdAccounts.size());
    Account createdAccount = createdAccounts.get(0);

    assertEquals(account.getAccountId(), createdAccount.getAccountId());
    assertEquals(account.getCurrencyCode(), createdAccount.getCurrencyCode());
    assertEquals(account.getBalance().setScale(4, RoundingMode.HALF_EVEN), createdAccount.getBalance());
    assertEquals(account.getLimit(), createdAccount.getLimit());
    assertEquals(account.getUserId(), createdAccount.getUserId());
  }

  @Test
  public void testDeleteAccount(){
    Account account = getMockedAccount();
    accountDao.createAccount(account);

    Account createdAccount = accountDao.getAccountById(account.getAccountId());
    assertNotNull(createdAccount);

    accountDao.deleteAccountById(account.getAccountId());
    assertThrows(AccountNotFoundException.class,
        () -> accountDao.getAccountById(account.getAccountId()));
  }

  @Test
  public void testUpdateAccount(){
    Account account = getMockedAccount();
    accountDao.createAccount(account);

    Account createdAccount = accountDao.getAccountById(account.getAccountId());
    assertEquals(account.getBalance().setScale(4, RoundingMode.HALF_EVEN), createdAccount.getBalance());

    BigDecimal amountToAdd = new BigDecimal("1000.5000000");
    account.setBalance(amountToAdd.add(account.getBalance()));
    accountDao.updateAccountBalance(account.getAccountId(), amountToAdd);

    Account updatedAccount = accountDao.getAccountById(account.getAccountId());
    assertEquals(account.getBalance().setScale(4, RoundingMode.HALF_EVEN), updatedAccount.getBalance());
  }

  private Account getMockedAccount(){
    Account account = new Account();
    account.setBalance(new BigDecimal("45.50000"));
    account.setCurrencyCode("EUR");
    account.setLimit(new BigDecimal("-10.0000"));
    account.setUserId(5);
    account.setAccountId("0TESTID");
    return account;
  }

}
