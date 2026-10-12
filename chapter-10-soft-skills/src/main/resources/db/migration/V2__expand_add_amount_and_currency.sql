-- Expand: add the new columns next to the old one. Old code keeps working.
ALTER TABLE payments ADD COLUMN amount   DECIMAL(12,2);
ALTER TABLE payments ADD COLUMN currency CHAR(3);
