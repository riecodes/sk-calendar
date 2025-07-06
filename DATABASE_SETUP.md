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

### 4. Migration for Existing Databases
If you have an existing database with the old position structure, run the migration script:
1. Import `database_migration_position_update.sql` in phpMyAdmin
2. This will update your positions to the new structure
3. Converts old positions to the new format

### 5. Tables Created
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

#### `sk_profiles` - SK Member Profiles
- `id` (Primary Key)
- `name`, `age`, `sex`, `position`, `position_number` (1-10)
- `committee`, `sector`, `photo_path`
- `created_at`, `updated_at`

#### `event_assignments` - Event Official Assignments
- `id` (Primary Key)
- `event_id` (Foreign Key to events)
- `sk_profile_id` (Foreign Key to sk_profiles)
- `position_number`, `assigned_at`

#### `event_attendance` - Event Attendance Tracking
- `id` (Primary Key)
- `event_id` (Foreign Key to events)
- `sk_profile_id` (Foreign Key to sk_profiles)
- `attendance_status` (present/absent/excused)
- `attendance_time`, `notes`

### 6. Position Structure
The new SK position structure follows the official hierarchy:
1. **SK Chairman** (Position 1)
2. **SK Kagawad 1** (Position 2)
3. **SK Kagawad 2** (Position 3)
4. **SK Kagawad 3** (Position 4)
5. **SK Kagawad 4** (Position 5)
6. **SK Kagawad 5** (Position 6)
7. **SK Kagawad 6** (Position 7)
8. **SK Kagawad 7** (Position 8)
9. **Secretary** (Position 9)
10. **Treasurer** (Position 10)

### 7. Default Data
The schema includes:
- Default admin user (username: admin, password: admin)
- 10 sample SK profiles for all positions (1-10)

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
- **Position number system** (1-10) for proper hierarchy

### ✅ Event Management System
- **Add Event** with validation and placeholders
- **Edit Event** with double-click functionality
- **Delete Event** with confirmation
- **Assign Officials** to events
- **Attendance Tracking** with status management
- **Time format validation** (12-hour format)

### ✅ Official Assignment & Attendance
- **Duty Assignment Dialog** for assigning officials to events
- **Attendance Log Dialog** for tracking actual attendance
- **Present/Absent/Excused** status tracking
- **Notes and timestamps** for attendance records
- **Position-based assignment** (1-10 hierarchy)

### ✅ Manual Database Management
- No automatic table creation
- Full control over database structure
- Manual data management via SQL
- Use provided schema file for setup
- **Migration scripts** for database updates

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