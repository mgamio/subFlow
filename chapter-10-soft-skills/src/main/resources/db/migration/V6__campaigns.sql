-- Campaigns become data that marketing can manage (ADR 0006).
CREATE TABLE campaigns (
  name            VARCHAR(100) PRIMARY KEY,
  annual_discount DECIMAL(3,2) NOT NULL
                  CHECK (annual_discount > 0 AND annual_discount < 1),
  first_day       DATE         NOT NULL,
  last_day        DATE         NOT NULL,
  CHECK (last_day >= first_day)
);
