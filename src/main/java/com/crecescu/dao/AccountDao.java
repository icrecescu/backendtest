package com.crecescu.dao;

import com.crecescu.model.Account;
import java.math.BigDecimal;
import java.util.List;

/**
 * DAO class used for db manipulation related to accounts
 */
public interface AccountDao {
    /**
     * Creates a new account
     * @param account to be created
     */
    void createAccount(Account account);

    /**
     * Get account by account id
     * @param accountId to search for
     * @return null if nothing found account otherwise
     */
    Account getAccountById(String accountId);

    /**
     * Deletes an account by account id.
     *
     * @param accountId to be deleted
     */
    void deleteAccountById(String accountId);

    /**
     * Get all the accounts belonging to a particular user
     * @param userId user's accounts to search for
     * @return a list of accounts
     */
    List<Account> getAccountsByUserId(int userId);

    /**
     * Updates an account's balance with a specified amount
     * @param accountId to be updated
     * @param amount to be added
     * @return the account with updated balance
     */
    Account updateAccountBalance(String accountId, BigDecimal amount);

}
