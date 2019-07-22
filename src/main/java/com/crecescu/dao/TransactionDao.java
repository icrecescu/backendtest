package com.crecescu.dao;

import com.crecescu.model.Transaction;

public interface TransactionDao {

  /**
   * Transfers fund from a source to a destination account and logs
   * the info into transactions table.
   * @param transaction info about transaction
   * @return the created transaction
   */
  Transaction transferFunds(Transaction transaction);
}
