package com.subflow.service;

/** Runs work so that all its database changes succeed or fail together. */
@FunctionalInterface
public interface TransactionRunner {
  void inTransaction(Runnable work);
}
