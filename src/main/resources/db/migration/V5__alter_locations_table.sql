-- alter latitude and longitude precision

ALTER TABLE locations ALTER COLUMN latitude TYPE decimal(10,6);
ALTER TABLE locations ALTER COLUMN longitude TYPE decimal(10,6);
