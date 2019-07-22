package com.crecescu.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Objects;

public class Account {

  @JsonProperty()
  private String accountId;

  @JsonProperty(required = true)
  private BigDecimal balance;

  @JsonProperty(required = true)
  private int userId;

  @JsonProperty(required = true)
  private String currencyCode;

  @JsonProperty(required = true)
  private BigDecimal limit;

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

  public BigDecimal getLimit() {
    return limit;
  }

  public void setLimit(BigDecimal limit) {
    this.limit = limit;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Account account = (Account) o;
    return Objects.equals(accountId, account.accountId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId);
  }

  @Override
  public String toString() {
    return "Account{" +
        "accountId='" + accountId + '\'' +
        ", balance=" + balance +
        ", userId=" + userId +
        ", currencyCode='" + currencyCode + '\'' +
        ", limit=" + limit +
        '}';
  }

}
