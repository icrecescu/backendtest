package com.crecescu.service;

import com.crecescu.dao.AccountDao;
import com.crecescu.model.Account;

import com.crecescu.utils.Utils;
import java.math.BigDecimal;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/api/v1/account")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountService {

  @Inject
  AccountDao accountDao;

  /**
   * Creates a new account
   *
   * @param account to be created
   * @return created account containing the generated account id
   */
  @POST
  @Path("/create")
  public Account createAccount(Account account) {
    Utils.validateAccount(account);
    account.setAccountId(Utils.generateAccountId(account));
    accountDao.createAccount(account);
    return account;
  }

  /**
   * Gets an account by account id. Return 404 HTTP code if no account was found
   *
   * @param accountId to search for
   * @return searched account
   */
  @GET
  @Path("/{accountId}")
  public Account getAccountById(@PathParam("accountId") String accountId) {
    return accountDao.getAccountById(accountId);
  }

  /**
   * Gets a list of all accounts related to a specific user
   *
   * @param userId of the users
   * @return a list of accounts
   */
  @GET
  @Path("/user/{userId}")
  public List<Account> getAccountsByUserId(@PathParam("userId") int userId) {
    return accountDao.getAccountsByUserId(userId);
  }

  /**
   * Deletes an account by account id. Returns HTTP code 200 if the account is found 404 otherwise
   *
   * @param accountId to be deleted
   * @return 200 if the account was deleted; 404 if the account was not found
   */
  @DELETE
  @Path("/{accountId}/delete")
  public Response deleteAccountById(@PathParam("accountId") String accountId) {
    accountDao.deleteAccountById(accountId);
    return Response.status(Status.OK).build();
  }

  /**
   * Updates an account's balance with a specified amount
   *
   * @param accountId to be updated
   * @param amount to be added
   * @return the account with updated balance
   */
  @PUT
  @Path("/{accountId}/balance/{amount}")
  public Account updateAccountBalance(@PathParam("accountId") String accountId,
      @PathParam("amount") BigDecimal amount) {
    return accountDao.updateAccountBalance(accountId, amount);
  }
}
