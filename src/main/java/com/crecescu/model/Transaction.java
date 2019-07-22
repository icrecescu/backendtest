package com.crecescu.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Objects;

public class Transaction {

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String id;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long dateCreated;

  @JsonProperty(required = true)
  private String source;

  @JsonProperty(required = true)
  private String destination;

  @JsonProperty(required = true)
  private BigDecimal amount;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Long getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(Long dateCreated) {
    this.dateCreated = dateCreated;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Transaction that = (Transaction) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "Transaction{" +
        "id='" + id + '\'' +
        ", dateCreated=" + dateCreated +
        ", source='" + source + '\'' +
        ", destination='" + destination + '\'' +
        ", amount=" + amount +
        '}';
  }

}
