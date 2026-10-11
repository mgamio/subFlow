package com.subflow.service;

/** Remembers which events or messages were already handled. */
public interface ProcessedEvents {
  /** Records the id; returns false if it had already been recorded. */
  boolean markProcessed(String eventId);
}
