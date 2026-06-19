-- Migration V1: Password Hashing & Security Improvements
-- Run this ONCE after deploying the BCrypt changes

-- 1. Ensure created_at column exists in users table
ALTER TABLE `users`
  ADD COLUMN IF NOT EXISTS `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  ADD COLUMN IF NOT EXISTS `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  ADD COLUMN IF NOT EXISTS `role` varchar(20) DEFAULT 'customer',
  ADD COLUMN IF NOT EXISTS `is_active` tinyint(1) DEFAULT 1;

-- 2. Add indexes for performance
ALTER TABLE `addresses`
  ADD INDEX IF NOT EXISTS `idx_user_id` (`user_id`);
ALTER TABLE `orders`
  ADD INDEX IF NOT EXISTS `idx_user_id` (`user_id`);
ALTER TABLE `service_orders`
  ADD INDEX IF NOT EXISTS `idx_user_id` (`user_id`);
ALTER TABLE `order_items`
  ADD INDEX IF NOT EXISTS `idx_order_id` (`order_id`);
ALTER TABLE `booked_service`
  ADD INDEX IF NOT EXISTS `idx_booking_id` (`booking_id`);

-- 3. Add password_hash column to store BCrypt hashes
-- Note: Existing plain-text passwords will need to be reset or migrated via the Java PasswordMigration utility
