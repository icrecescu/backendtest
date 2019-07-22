package com.crecescu.service;

import com.crecescu.dao.TransactionDao;
import com.crecescu.model.Transaction;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/api/v1/transaction")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TransactionService {

  @Inject
  private TransactionDao transactionDao;

  @POST
  public Transaction transferFunds(Transaction transaction) {
    return transactionDao.transferFunds(transaction);
  }
}
