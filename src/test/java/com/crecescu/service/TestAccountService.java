package com.crecescu.service;

import com.crecescu.model.Account;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestAccountService extends TestService {

  @Test
  public void testCreateAccount() throws URISyntaxException, IOException {
    Account account = createAccount();

    Assertions.assertNotNull(account.getAccountId());
  }

  @Test
  public void testCreateAccountWithWrongCurrency() throws URISyntaxException, IOException {
    URI uri = builder.setPath("/api/v1/account/create").build();

    Account account = getMockedAccount();
    account.setCurrencyCode("NaN");
    String jsonAccount = mapper.writeValueAsString(account);
    StringEntity accountEntity = new StringEntity(jsonAccount);

    HttpPost request = new HttpPost(uri);
    request.setHeader("Content-type", "application/json");
    request.setEntity(accountEntity);

    HttpResponse response = client.execute(request);
    int statusCode = response.getStatusLine().getStatusCode();

    Assertions.assertEquals(statusCode, 400);
  }

  @Test
  public void testDeleteAccount() throws URISyntaxException, IOException {
    Account account = createAccount();

    Account createdAccount = getAccountById(account.getAccountId(), 200);
    Assertions.assertNotNull(createdAccount);

    deleteAccount(createdAccount.getAccountId());

    getAccountById(createdAccount.getAccountId(), 404);
  }

  @Test
  public void testUpdateAccount() throws IOException, URISyntaxException {
    Account account = createAccount();
    BigDecimal amount = new BigDecimal("100.00");

    URI uri = builder.setPath("/api/v1/account/" + account.getAccountId() + "/balance/" + amount.toString()).build();

    HttpPut request = new HttpPut(uri);
    request.setHeader("Content-type", "application/json");

    HttpResponse response = client.execute(request);
    int statusCode = response.getStatusLine().getStatusCode();
    Assertions.assertEquals(statusCode, 200);

    Account updateAccount = getAccountById(account.getAccountId(), 200);

    Assertions.assertEquals(updateAccount.getBalance(),
        account.getBalance().add(amount).setScale(4, RoundingMode.HALF_EVEN));
  }

  private Account createAccount()
      throws IOException, URISyntaxException {
    URI uri = builder.setPath("/api/v1/account/create").build();

    Account account = getMockedAccount();
    String jsonAccount = mapper.writeValueAsString(account);
    StringEntity accountEntity = new StringEntity(jsonAccount);

    HttpPost request = new HttpPost(uri);
    request.setHeader("Content-type", "application/json");
    request.setEntity(accountEntity);

    HttpResponse response = client.execute(request);
    int statusCode = response.getStatusLine().getStatusCode();

    Assertions.assertEquals(statusCode, 200);

    String jsonString = EntityUtils.toString(response.getEntity());
    return mapper.readValue(jsonString, Account.class);
  }

  private Account getAccountById(String accountId, int expectedStatusCode)
      throws IOException, URISyntaxException {
    URI uri = builder.setPath("/api/v1/account/" + accountId).build();


    HttpGet request = new HttpGet(uri);
    request.setHeader("Content-type", "application/json");

    HttpResponse response = client.execute(request);
    int statusCode = response.getStatusLine().getStatusCode();

    Assertions.assertEquals(statusCode, expectedStatusCode);

    if(expectedStatusCode != 200){
      return null;
    }

    String jsonString = EntityUtils.toString(response.getEntity());
    return mapper.readValue(jsonString, Account.class);
  }

  private void deleteAccount(String accountId)
      throws IOException, URISyntaxException {
    URI uri = builder.setPath("/api/v1/account/" + accountId + "/delete").build();


    HttpDelete request = new HttpDelete(uri);
    request.setHeader("Content-type", "application/json");

    HttpResponse response = client.execute(request);
    int statusCode = response.getStatusLine().getStatusCode();

    Assertions.assertEquals(statusCode, 200);
  }

  private Account getMockedAccount(){
    Account account = new Account();
    account.setBalance(new BigDecimal("45.50000"));
    account.setCurrencyCode("EUR");
    account.setLimit(new BigDecimal("-10.0000"));
    account.setUserId(5);
    return account;
  }

}
