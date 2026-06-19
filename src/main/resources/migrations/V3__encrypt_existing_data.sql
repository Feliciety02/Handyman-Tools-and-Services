-- Migration V3: Encrypt existing credit card data
-- Run this after deploying the EncryptionUtils changes
-- This will be run by the Java MigrationRunner utility

-- Note: Existing credit card data must be migrated via the Java utility
-- because encryption requires the application's secret key.
-- The Java MigrationRunner class handles this automatically.
