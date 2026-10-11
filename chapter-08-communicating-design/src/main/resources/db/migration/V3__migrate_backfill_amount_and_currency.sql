-- Migrate: copy existing data. From this release, the code writes both
-- the old and the new columns, and reads the new ones.
UPDATE payments
   SET amount = amount_usd, currency = 'USD'
 WHERE amount IS NULL;

ALTER TABLE payments ALTER COLUMN amount   SET NOT NULL;
ALTER TABLE payments ALTER COLUMN currency SET NOT NULL;
ALTER TABLE payments ADD CONSTRAINT payments_amount_positive CHECK (amount > 0);
