# SK Calendar Database Setup

## Overview
The SK Calendar system now uses **manual database management** to give you full control over the database structure and data.

## Database Setup Instructions

### 1. Prerequisites
- XAMPP installed and running
- MySQL server active on localhost:3306
- MySQL root access (default XAMPP setup)

### 2. Database Creation
1. Open phpMyAdmin (http://localhost/phpmyadmin)
2. Create a new database named `sk_calendar`
3. Import the provided `database_schema.sql` file:
   - Click on the `sk_calendar` database
   - Go to "Import" tab
   - Choose `database_schema.sql` file
   - Click "Go" to execute

### 3. Database Configuration
The application connects using these default settings:
- **Host**: localhost:3306
- **Database**: sk_calendar
- **Username**: root
- **Password**: (empty - default XAMPP)

### 4. Tables Created
The schema creates the following tables:

#### `users` - Authentication
- `id` (Primary Key)
- `username`, `password`, `full_name`, `position`
- `created_at`

#### `events` - Calendar Events
- `id` (Primary Key)
- `title`, `description`, `event_date`, `event_time`, `location`
- `created_by` (Foreign Key to users)
- `created_at`

#### `event_attendees` - Event Attendance
- `id` (Primary Key)
- `event_id` (Foreign Key to events)
- `user_id` (Foreign Key to users)
- `attendance_status` (invited/attending/not_attending)

#### `sk_profiles` - SK Member Profiles
- `id` (Primary Key)
- `name`, `age`, `sex`, `position`
- `committee`, `sector`, `photo_path`
- `created_at`, `updated_at`

### 5. Default Data
The schema includes:
- Default admin user (username: admin, password: admin)
- 5 sample SK profiles for testing

### 6. Image Upload Directory
Create an `uploads` folder in your project root for profile photos:
```
sk-calendar/
├── uploads/           ← Create this folder
├── src/
├── target/
└── database_schema.sql
```

## Features Implemented

### ✅ Database Operations
- Full CRUD operations for SK profiles
- Image upload and storage
- Data persistence between sessions
- No automatic table creation/dropping

### ✅ SK Profile Enhancements
- **ADD NEW** button for creating profiles
- **Image upload** functionality with file chooser
- **Photo display** in profile cards
- All form fields (committee, sector as text field)
- Rounded text field borders

### ✅ Manual Database Management
- No automatic table creation
- Full control over database structure
- Manual data management via SQL
- Use provided schema file for setup

## Usage Notes
1. **Always backup your database** before making changes
2. **Run the schema file only once** during initial setup
3. **Profile images** are stored in `uploads/` directory
4. **Database changes** persist between application runs
5. **Manual control** over all database operations

## Troubleshooting
- Ensure XAMPP MySQL is running
- Check database name is exactly `sk_calendar`
- Verify uploads folder exists and is writable
- Check MySQL connection settings in DatabaseUtil.java 