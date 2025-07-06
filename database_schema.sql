-- =============================================
-- SK Calendar Database Schema
-- =============================================

-- Create database
CREATE DATABASE IF NOT EXISTS sk_calendar;
USE sk_calendar;

-- Users table for authentication
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    position VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- SK Profiles table (create this first to avoid foreign key issues)
CREATE TABLE IF NOT EXISTS sk_profiles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    sex ENUM('Male', 'Female') NOT NULL,
    position VARCHAR(50) NOT NULL,
    position_number INT UNIQUE NOT NULL, -- Position number 1-10 for SK officials
    committee VARCHAR(100),
    sector VARCHAR(100),
    photo_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Events table
CREATE TABLE IF NOT EXISTS events (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    event_date DATE NOT NULL,
    event_time TIME,
    location VARCHAR(255),
    attending_officials_count INT DEFAULT 1,
    created_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(id)
);

-- Event assignments table (tracks which officials are assigned to events)
CREATE TABLE IF NOT EXISTS event_assignments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    event_id INT,
    sk_profile_id INT,
    position_number INT,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
    FOREIGN KEY (sk_profile_id) REFERENCES sk_profiles(id) ON DELETE CASCADE,
    UNIQUE KEY unique_event_profile (event_id, sk_profile_id)
);

-- Event attendance table (tracks actual attendance during events)
CREATE TABLE IF NOT EXISTS event_attendance (
    id INT AUTO_INCREMENT PRIMARY KEY,
    event_id INT,
    sk_profile_id INT,
    attendance_status ENUM('present', 'absent', 'excused') DEFAULT 'present',
    attendance_time TIMESTAMP,
    notes TEXT,
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
    FOREIGN KEY (sk_profile_id) REFERENCES sk_profiles(id) ON DELETE CASCADE,
    UNIQUE KEY unique_event_attendance (event_id, sk_profile_id)
);

-- Insert default admin user
INSERT IGNORE INTO users (username, password, full_name, position) 
VALUES ('admin', 'admin', 'System Administrator', 'Administrator');

-- Insert sample SK profiles with position numbers
INSERT IGNORE INTO sk_profiles (name, age, sex, position, position_number, committee, sector) VALUES
('Juan Dela Cruz', 22, 'Male', 'SK Chairman', 1, 'Education Committee', 'Out-of-School Youth'),
('Maria Santos', 20, 'Female', 'SK Kagawad 1', 2, 'Health Committee', 'Working Youth'),
('Carlos Mendoza', 21, 'Male', 'SK Kagawad 2', 3, 'Sports Committee', 'Students'),
('Ana Garcia', 19, 'Female', 'SK Kagawad 3', 4, 'Environment Committee', 'Students'),
('Roberto Silva', 23, 'Male', 'SK Kagawad 4', 5, 'Livelihood Committee', 'Working Youth'),
('Carmen Lopez', 20, 'Female', 'SK Kagawad 5', 6, 'Youth Development Committee', 'Students'),
('Miguel Torres', 22, 'Male', 'SK Kagawad 6', 7, 'Peace and Order Committee', 'Working Youth'),
('Isabella Reyes', 21, 'Female', 'SK Kagawad 7', 8, 'Cultural Affairs Committee', 'Students'),
('Diego Morales', 19, 'Male', 'Secretary', 9, 'Information Committee', 'Students'),
('Sofia Hernandez', 20, 'Female', 'Treasurer', 10, 'Finance Committee', 'Out-of-School Youth');

-- =============================================
-- Migration Scripts (if database already exists)
-- =============================================

-- Add position_number column to existing sk_profiles table
ALTER TABLE sk_profiles ADD COLUMN IF NOT EXISTS position_number INT UNIQUE;

-- Update existing profiles with position numbers (if any exist)
UPDATE sk_profiles SET position_number = 1 WHERE position = 'SK Chairman' AND position_number IS NULL;
UPDATE sk_profiles SET position_number = 9 WHERE position = 'Secretary' AND position_number IS NULL;
UPDATE sk_profiles SET position_number = 10 WHERE position = 'Treasurer' AND position_number IS NULL;

-- Update SK Secretary to Secretary
UPDATE sk_profiles SET position = 'Secretary' WHERE position = 'SK Secretary';
-- Update SK Treasurer to Treasurer  
UPDATE sk_profiles SET position = 'Treasurer' WHERE position = 'SK Treasurer';
-- Update SK Vice Chairman positions to SK Kagawad 1
UPDATE sk_profiles SET position = 'SK Kagawad 1' WHERE position = 'SK Vice Chairman';

-- Update remaining Kagawad positions with numbers 2-7
UPDATE sk_profiles SET position = 'SK Kagawad 1', position_number = 2 WHERE position = 'SK Kagawad' AND position_number IS NULL ORDER BY id LIMIT 1;
UPDATE sk_profiles SET position = 'SK Kagawad 2', position_number = 3 WHERE position = 'SK Kagawad' AND position_number IS NULL ORDER BY id LIMIT 1;
UPDATE sk_profiles SET position = 'SK Kagawad 3', position_number = 4 WHERE position = 'SK Kagawad' AND position_number IS NULL ORDER BY id LIMIT 1;
UPDATE sk_profiles SET position = 'SK Kagawad 4', position_number = 5 WHERE position = 'SK Kagawad' AND position_number IS NULL ORDER BY id LIMIT 1;
UPDATE sk_profiles SET position = 'SK Kagawad 5', position_number = 6 WHERE position = 'SK Kagawad' AND position_number IS NULL ORDER BY id LIMIT 1;
UPDATE sk_profiles SET position = 'SK Kagawad 6', position_number = 7 WHERE position = 'SK Kagawad' AND position_number IS NULL ORDER BY id LIMIT 1;
UPDATE sk_profiles SET position = 'SK Kagawad 7', position_number = 8 WHERE position = 'SK Kagawad' AND position_number IS NULL ORDER BY id LIMIT 1;

-- Add attending_officials_count column to existing events table
ALTER TABLE events ADD COLUMN IF NOT EXISTS attending_officials_count INT DEFAULT 1;

-- Drop old event_attendees table if it exists (backup data first!)
-- DROP TABLE IF EXISTS event_attendees; 