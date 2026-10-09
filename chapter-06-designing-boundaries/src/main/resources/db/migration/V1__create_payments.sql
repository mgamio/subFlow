-- The first version: amounts in dollars only.
CREATE TABLE payments (
  id              VARCHAR(64)   PRIMARY KEY,
  subscription_id BIGINT        NOT NULL REFERENCES subscriptions (id),
  provider        VARCHAR(16)   NOT NULL,
  amount_usd      DECIMAL(12,2) NOT NULL CHECK (amount_usd > 0),
  payment_date    DATE          NOT NULL
);
