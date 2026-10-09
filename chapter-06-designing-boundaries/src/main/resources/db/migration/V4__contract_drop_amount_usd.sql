-- Contract: once no running version reads amount_usd, remove it.
ALTER TABLE payments DROP COLUMN amount_usd;
