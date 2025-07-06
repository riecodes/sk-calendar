-- Migration Script: Remove Description Column from Events Table
-- Run this script if you have an existing database with the description column

-- Remove description column from events table
ALTER TABLE events DROP COLUMN IF EXISTS description;

-- Verify the change
DESCRIBE events; 