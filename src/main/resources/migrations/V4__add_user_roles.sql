-- Migration V4: Add user roles and admin user
ALTER TABLE `users`
  ADD COLUMN IF NOT EXISTS `role` varchar(20) DEFAULT 'customer',
  ADD COLUMN IF NOT EXISTS `is_active` tinyint(1) DEFAULT 1,
  ADD COLUMN IF NOT EXISTS `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp();

-- Create default admin user (password: Admin123!)
-- The hash below corresponds to BCrypt hash of "Admin123!"
-- INSERT INTO `users` (`username`, `email`, `password`, `role`, `is_active`) VALUES
-- ('admin', 'admin@handyman.com', '$2a$12$LJ3m4ys3Lk0TSwHp7vKkeOjJ8mBKoMFK4NnTjR0GxvYRMfK5zXKGe', 'admin', 1);
