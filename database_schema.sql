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

-- Events table
CREATE TABLE IF NOT EXISTS events (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    event_date DATE NOT NULL,
    event_time TIME,
    location VARCHAR(255),
    created_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(id)
);

-- Event attendees table (many-to-many relationship)
CREATE TABLE IF NOT EXISTS event_attendees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    event_id INT,
    user_id INT,
    attendance_status ENUM('invited', 'attending', 'not_attending') DEFAULT 'invited',
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_event_user (event_id, user_id)
);

-- SK Profiles table (already has committee, sector, photo_path columns)
CREATE TABLE IF NOT EXISTS sk_profiles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    sex ENUM('Male', 'Female') NOT NULL,
    position VARCHAR(50) NOT NULL,
    committee VARCHAR(100),
    sector VARCHAR(100),
    photo_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert default admin user
INSERT IGNORE INTO users (username, password, full_name, position) 
VALUES ('admin', 'admin', 'System Administrator', 'Administrator');

-- Insert sample SK profiles
INSERT IGNORE INTO sk_profiles (name, age, sex, position, committee, sector) VALUES
('Juan Dela Cruz', 22, 'Male', 'SK Chairman', 'Education Committee', 'Out-of-School Youth'),
('Maria Santos', 20, 'Female', 'SK Kagawad', 'Health Committee', 'Working Youth'),
('Carlos Mendoza', 21, 'Male', 'SK Secretary', 'Sports Committee', 'Students'),
('Ana Garcia', 19, 'Female', 'SK Treasurer', 'Environment Committee', 'Students'),
('Roberto Silva', 23, 'Male', 'SK Kagawad', 'Livelihood Committee', 'Working Youth'); 