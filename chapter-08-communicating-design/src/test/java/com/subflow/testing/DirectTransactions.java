package com.subflow.testing;

import com.subflow.service.TransactionRunner;

/** Runs the work directly. In-memory fakes have nothing to roll back. */
public class DirectTransactions implements TransactionRunner {
  private int count;

  @Override
  public void inTransaction(Runnable work) {
    count++;
    work.run();
  }

  public int count() {
    return count;
  }
}
