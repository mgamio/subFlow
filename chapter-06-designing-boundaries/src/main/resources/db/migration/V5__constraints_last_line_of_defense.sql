-- Rules the database can guarantee even if application code has a bug.
CREATE UNIQUE INDEX customers_email_unique ON customers (email);

ALTER TABLE subscriptions ADD CONSTRAINT subscriptions_dates
  CHECK (end_date IS NULL OR end_date >= start_date);

ALTER TABLE subscriptions ADD CONSTRAINT subscriptions_status
  CHECK (status IN ('TRIAL', 'ACTIVE', 'CANCELED'));

CREATE TABLE processed_events (
  event_id     VARCHAR(128) PRIMARY KEY,
  processed_at TIMESTAMP    NOT NULL
);
