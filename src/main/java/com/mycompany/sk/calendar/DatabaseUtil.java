package com.mycompany.sk.calendar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

/**
 * Database utility class for handling MySQL connections and operations
 */
public class DatabaseUtil {
    
    // Database configuration for XAMPP
    private static final String DB_URL = "jdbc:mysql://localhost:3306/sk_calendar";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = ""; // Default XAMPP password is empty
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    
    private static Connection connection = null;
    
    /**
     * Get database connection
     * @return Connection object or null if connection fails
     */
    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName(DB_DRIVER);
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("Database connected successfully!");
            } catch (ClassNotFoundException e) {
                System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
            } catch (SQLException e) {
                System.err.println("Database connection failed: " + e.getMessage());
                System.err.println("Make sure XAMPP is running and the database 'sk_calendar' exists.");
            }
        }
        return connection;
    }
    
    /**
     * Initialize database connection (tables must be created manually)
     */
    public static void initializeDatabase() {
        try {
            // Connect to the sk_calendar database
            connection = getConnection();
            if (connection != null) {
                System.out.println("Database connection established successfully!");
                System.out.println("Note: Please ensure all required tables exist in the database.");
                System.out.println("Use the provided database_schema.sql file to create tables manually.");
            }
            
        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
    
    // NOTE: Table creation is now handled manually via database_schema.sql
    // This method is commented out to prevent automatic table creation
    /*
    private static void createTables() {
        // Tables must be created manually using database_schema.sql
        // This ensures better control over database structure
    }
    */
    
    // NOTE: Default data insertion is now handled manually via database_schema.sql
    /*
    private static void insertDefaultUser() {
        // Default admin user must be created manually using database_schema.sql
    }
    */
    
    /**
     * Authenticate user login
     * @param username the username
     * @param password the password
     * @return User object if authentication successful, null otherwise
     */
    public static User authenticateUser(String username, String password) {
        try {
            String query = "SELECT id, username, full_name, position FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password); // In production, compare with hashed password
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("full_name"),
                    rs.getString("position")
                );
                stmt.close();
                return user;
            }
            stmt.close();
            
        } catch (SQLException e) {
            System.err.println("Error authenticating user: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Close database connection
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
    
    /**
     * Test database connection
     * @return true if connection is successful, false otherwise
     */
    public static boolean testConnection() {
        Connection testConn = getConnection();
        return testConn != null;
    }
    
    // ========== SK PROFILE DATABASE OPERATIONS ==========
    
    /**
     * Get all SK profiles from database
     * @return List of SKProfile objects
     */
    public static java.util.List<SKProfile> getAllProfiles() {
        java.util.List<SKProfile> profiles = new java.util.ArrayList<>();
        try {
            String query = "SELECT * FROM sk_profiles ORDER BY position_number";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                SKProfile profile = new SKProfile();
                profile.setId(rs.getInt("id"));
                profile.setName(rs.getString("name"));
                profile.setAge(rs.getInt("age"));
                profile.setSex(rs.getString("sex"));
                profile.setPosition(rs.getString("position"));
                profile.setPositionNumber(rs.getInt("position_number"));
                profile.setCommittee(rs.getString("committee"));
                profile.setSector(rs.getString("sector"));
                profile.setPhotoPath(rs.getString("photo_path"));
                profiles.add(profile);
            }
            
            stmt.close();
            
        } catch (SQLException e) {
            System.err.println("Error fetching profiles: " + e.getMessage());
        }
        return profiles;
    }
    
    /**
     * Save a new SK profile to database
     * @param profile SKProfile object to save
     * @return generated profile ID, or -1 if failed
     */
    public static int saveProfile(SKProfile profile) {
        try {
            String query = "INSERT INTO sk_profiles (name, age, sex, position, position_number, committee, sector, photo_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setString(1, profile.getName());
            stmt.setInt(2, profile.getAge());
            stmt.setString(3, profile.getSex());
            stmt.setString(4, profile.getPosition());
            stmt.setInt(5, profile.getPositionNumber());
            stmt.setString(6, profile.getCommittee());
            stmt.setString(7, profile.getSector());
            stmt.setString(8, profile.getPhotoPath());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    profile.setId(generatedId);
                    stmt.close();
                    return generatedId;
                }
            }
            stmt.close();
            
        } catch (SQLException e) {
            System.err.println("Error saving profile: " + e.getMessage());
        }
        return -1;
    }
    
    /**
     * Update an existing SK profile in database
     * @param profile SKProfile object to update
     * @return true if update successful, false otherwise
     */
    public static boolean updateProfile(SKProfile profile) {
        try {
            String query = "UPDATE sk_profiles SET name=?, age=?, sex=?, position=?, position_number=?, committee=?, sector=?, photo_path=? WHERE id=?";
            PreparedStatement stmt = connection.prepareStatement(query);
            
            stmt.setString(1, profile.getName());
            stmt.setInt(2, profile.getAge());
            stmt.setString(3, profile.getSex());
            stmt.setString(4, profile.getPosition());
            stmt.setInt(5, profile.getPositionNumber());
            stmt.setString(6, profile.getCommittee());
            stmt.setString(7, profile.getSector());
            stmt.setString(8, profile.getPhotoPath());
            stmt.setInt(9, profile.getId());
            
            int rowsAffected = stmt.executeUpdate();
            stmt.close();
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating profile: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Delete an SK profile from database
     * @param profileId ID of profile to delete
     * @return true if deletion successful, false otherwise
     */
    public static boolean deleteProfile(int profileId) {
        try {
            String query = "DELETE FROM sk_profiles WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, profileId);
            
            int rowsAffected = stmt.executeUpdate();
            stmt.close();
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting profile: " + e.getMessage());
        }
        return false;
    }
    
    // NOTE: Sample data insertion is now handled manually via database_schema.sql
    /*
    public static void insertSampleProfiles() {
        // Sample profiles must be created manually using database_schema.sql
    }
    */
    
    /**
     * Check if a position is already taken by another SK member
     * @param position The SK position to check
     * @return true if position is already taken, false otherwise
     */
    public static boolean isPositionTaken(String position) {
        if (position == null || position.trim().isEmpty()) {
            return false;
        }
        
        try {
            String query = "SELECT COUNT(*) FROM sk_profiles WHERE position = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, position.trim());
            
            ResultSet rs = stmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            
            stmt.close();
            
            return count > 0;
            
        } catch (SQLException e) {
            System.err.println("Error checking position availability: " + e.getMessage());
            return false; // Assume position is available if there's an error
        }
    }
    
    /**
     * Check if a position is already taken by another SK member (excluding specific profile)
     * @param position The SK position to check
     * @param excludeProfileId Profile ID to exclude from the check
     * @return true if position is already taken by someone else, false otherwise
     */
    public static boolean isPositionTakenByOther(String position, int excludeProfileId) {
        if (position == null || position.trim().isEmpty()) {
            return false;
        }
        
        try {
            String query = "SELECT COUNT(*) FROM sk_profiles WHERE position = ? AND id != ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, position.trim());
            stmt.setInt(2, excludeProfileId);
            
            ResultSet rs = stmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            
            stmt.close();
            
            return count > 0;
            
        } catch (SQLException e) {
            System.err.println("Error checking position availability: " + e.getMessage());
            return false; // Assume position is available if there's an error
        }
    }
    
    // ===========================
    // EVENT MANAGEMENT METHODS
    // ===========================
    
    /**
     * Get all events from database
     * @return List of events
     */
    public static List<CalendarScreen.Event> getAllEvents() {
        List<CalendarScreen.Event> events = new ArrayList<>();
        
        try {
            String query = "SELECT * FROM events ORDER BY event_date, event_time";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                CalendarScreen.Event event = new CalendarScreen.Event();
                event.setId(rs.getInt("id"));
                event.setTitle(rs.getString("title"));
                
                // Convert SQL Date to LocalDate
                java.sql.Date sqlDate = rs.getDate("event_date");
                if (sqlDate != null) {
                    event.setDate(sqlDate.toLocalDate());
                }
                
                // Convert time from 24-hour to 12-hour format for display
                java.sql.Time sqlTime = rs.getTime("event_time");
                if (sqlTime != null) {
                    event.setTime(convertTo12HourFormat(sqlTime.toString()));
                } else {
                    event.setTime("");
                }
                
                event.setLocation(rs.getString("location"));
                event.setAttendingOfficialsCount(rs.getInt("attending_officials_count"));
                event.setCreatedBy(rs.getInt("created_by"));
                
                events.add(event);
            }
            
            stmt.close();
            
        } catch (SQLException e) {
            System.err.println("Error loading events: " + e.getMessage());
        }
        
        return events;
    }
    
    /**
     * Convert 12-hour format time to 24-hour format for database storage
     * @param time12Hour Time in 12-hour format (e.g., "2:00 PM", "10:30 AM")
     * @return Time in 24-hour format (e.g., "14:00:00", "10:30:00") or null if invalid
     */
    private static String convertTo24HourFormat(String time12Hour) {
        if (time12Hour == null || time12Hour.trim().isEmpty()) {
            return null;
        }
        
        try {
            // Remove extra spaces and convert to uppercase
            String cleanTime = time12Hour.trim().toUpperCase();
            
            // Check if it's already in 24-hour format (no AM/PM)
            if (!cleanTime.contains("AM") && !cleanTime.contains("PM")) {
                // If it contains colons and numbers, assume it's already 24-hour format
                if (cleanTime.matches("^\\d{1,2}:\\d{2}(:\\d{2})?$")) {
                    // Add seconds if missing
                    if (!cleanTime.contains(":")) {
                        return cleanTime + ":00";
                    }
                    String[] parts = cleanTime.split(":");
                    if (parts.length == 2) {
                        return cleanTime + ":00";
                    }
                    return cleanTime;
                }
                return null;
            }
            
            // Parse 12-hour format
            boolean isPM = cleanTime.contains("PM");
            String timeOnly = cleanTime.replace("AM", "").replace("PM", "").trim();
            
            String[] parts = timeOnly.split(":");
            if (parts.length != 2) {
                return null;
            }
            
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            
            // Validate time values
            if (hours < 1 || hours > 12 || minutes < 0 || minutes > 59) {
                return null;
            }
            
            // Convert to 24-hour format
            if (isPM && hours != 12) {
                hours += 12;
            } else if (!isPM && hours == 12) {
                hours = 0;
            }
            
            return String.format("%02d:%02d:00", hours, minutes);
            
        } catch (NumberFormatException e) {
            System.err.println("Invalid time format: " + time12Hour);
            return null;
        }
    }
    
    /**
     * Convert 24-hour format time to 12-hour format for display
     * @param time24Hour Time in 24-hour format (e.g., "14:00:00")
     * @return Time in 12-hour format (e.g., "2:00 PM") or original string if invalid
     */
    private static String convertTo12HourFormat(String time24Hour) {
        if (time24Hour == null || time24Hour.trim().isEmpty()) {
            return "";
        }
        
        try {
            String[] parts = time24Hour.split(":");
            if (parts.length < 2) {
                return time24Hour; // Return original if can't parse
            }
            
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            
            // Validate time values
            if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59) {
                return time24Hour;
            }
            
            String period = hours >= 12 ? "PM" : "AM";
            int displayHours = hours;
            
            if (hours == 0) {
                displayHours = 12;
            } else if (hours > 12) {
                displayHours = hours - 12;
            }
            
            return String.format("%d:%02d %s", displayHours, minutes, period);
            
        } catch (NumberFormatException e) {
            return time24Hour; // Return original if can't parse
        }
    }
    
    /**
     * Save a new event to database
     * @param event Event to save
     * @return Generated event ID, or 0 if failed
     */
    public static int saveEvent(CalendarScreen.Event event) {
        try {
            String query = "INSERT INTO events (title, event_date, event_time, location, attending_officials_count, created_by) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setString(1, event.getTitle());
            stmt.setDate(2, java.sql.Date.valueOf(event.getDate()));
            
            // Convert time to 24-hour format for database storage
            String time24Hour = convertTo24HourFormat(event.getTime());
            if (time24Hour != null) {
                stmt.setTime(3, java.sql.Time.valueOf(time24Hour));
            } else {
                stmt.setTime(3, null);
            }
            
            stmt.setString(4, event.getLocation());
            stmt.setInt(5, event.getAttendingOfficialsCount());
            stmt.setInt(6, event.getCreatedBy());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int eventId = generatedKeys.getInt(1);
                    stmt.close();
                    return eventId;
                }
            }
            
            stmt.close();
            
        } catch (SQLException e) {
            System.err.println("Error saving event: " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * Update an existing event
     * @param event Event to update
     * @return true if successful, false otherwise
     */
    public static boolean updateEvent(CalendarScreen.Event event) {
        try {
            String query = "UPDATE events SET title = ?, event_date = ?, event_time = ?, location = ?, attending_officials_count = ? WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            
            stmt.setString(1, event.getTitle());
            stmt.setDate(2, java.sql.Date.valueOf(event.getDate()));
            
            // Convert time to 24-hour format for database storage
            String time24Hour = convertTo24HourFormat(event.getTime());
            if (time24Hour != null) {
                stmt.setTime(3, java.sql.Time.valueOf(time24Hour));
            } else {
                stmt.setTime(3, null);
            }
            
            stmt.setString(4, event.getLocation());
            stmt.setInt(5, event.getAttendingOfficialsCount());
            stmt.setInt(6, event.getId());
            
            int affectedRows = stmt.executeUpdate();
            stmt.close();
            
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating event: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete an event from database
     * @param eventId ID of event to delete
     * @return true if successful, false otherwise
     */
    public static boolean deleteEvent(int eventId) {
        try {
            String query = "DELETE FROM events WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, eventId);
            
            int affectedRows = stmt.executeUpdate();
            stmt.close();
            
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting event: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get events for a specific date
     * @param date The date to get events for
     * @return List of events for the specified date
     */
    public static List<CalendarScreen.Event> getEventsForDate(java.time.LocalDate date) {
        List<CalendarScreen.Event> events = new ArrayList<>();
        
        try {
            String query = "SELECT * FROM events WHERE event_date = ? ORDER BY event_time";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setDate(1, java.sql.Date.valueOf(date));
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                CalendarScreen.Event event = new CalendarScreen.Event();
                event.setId(rs.getInt("id"));
                event.setTitle(rs.getString("title"));
                event.setDate(rs.getDate("event_date").toLocalDate());
                
                // Convert time from 24-hour to 12-hour format for display
                java.sql.Time sqlTime = rs.getTime("event_time");
                if (sqlTime != null) {
                    event.setTime(convertTo12HourFormat(sqlTime.toString()));
                } else {
                    event.setTime("");
                }
                
                event.setLocation(rs.getString("location"));
                event.setAttendingOfficialsCount(rs.getInt("attending_officials_count"));
                event.setCreatedBy(rs.getInt("created_by"));
                
                events.add(event);
            }
            
            stmt.close();
            
        } catch (SQLException e) {
            System.err.println("Error loading events for date: " + e.getMessage());
        }
        
        return events;
    }
    
    // ===========================
    // EVENT ASSIGNMENT METHODS
    // ===========================
    
    /**
     * Get SK profiles by position numbers
     * @param positionNumbers List of position numbers to retrieve
     * @return List of SKProfile objects
     */
    public static List<SKProfile> getProfilesByPositionNumbers(List<Integer> positionNumbers) {
        List<SKProfile> profiles = new ArrayList<>();
        
        if (positionNumbers == null || positionNumbers.isEmpty()) {
            return profiles;
        }
        
        try {
            // Create placeholders for IN clause
            String placeholders = String.join(",", java.util.Collections.nCopies(positionNumbers.size(), "?"));
            String query = "SELECT * FROM sk_profiles WHERE position_number IN (" + placeholders + ") ORDER BY position_number";
            
            PreparedStatement stmt = connection.prepareStatement(query);
            for (int i = 0; i < positionNumbers.size(); i++) {
                stmt.setInt(i + 1, positionNumbers.get(i));
            }
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                SKProfile profile = new SKProfile();
                profile.setId(rs.getInt("id"));
                profile.setName(rs.getString("name"));
                profile.setAge(rs.getInt("age"));
                profile.setSex(rs.getString("sex"));
                profile.setPosition(rs.getString("position"));
                profile.setPositionNumber(rs.getInt("position_number"));
                profile.setCommittee(rs.getString("committee"));
                profile.setSector(rs.getString("sector"));
                profile.setPhotoPath(rs.getString("photo_path"));
                profiles.add(profile);
            }
            
            stmt.close();
            
        } catch (SQLException e) {
            System.err.println("Error fetching profiles by position numbers: " + e.getMessage());
        }
        
        return profiles;
    }
    
    /**
     * Assign officials to an event
     * @param eventId ID of the event
     * @param profileIds List of SK profile IDs to assign
     * @return true if successful, false otherwise
     */
    public static boolean assignOfficialsToEvent(int eventId, List<Integer> profileIds) {
        if (profileIds == null || profileIds.isEmpty()) {
            return true; // No assignments needed
        }
        
        try {
            // First, remove any existing assignments for this event
            String deleteQuery = "DELETE FROM event_assignments WHERE event_id = ?";
            PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery);
            deleteStmt.setInt(1, eventId);
            deleteStmt.executeUpdate();
            deleteStmt.close();
            
            // Then add new assignments
            String insertQuery = "INSERT INTO event_assignments (event_id, sk_profile_id, position_number) VALUES (?, ?, (SELECT position_number FROM sk_profiles WHERE id = ?))";
            PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
            
            for (Integer profileId : profileIds) {
                insertStmt.setInt(1, eventId);
                insertStmt.setInt(2, profileId);
                insertStmt.setInt(3, profileId);
                insertStmt.addBatch();
            }
            
            int[] results = insertStmt.executeBatch();
            insertStmt.close();
            
            // Check if all assignments were successful
            for (int result : results) {
                if (result <= 0) {
                    return false;
                }
            }
            
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error assigning officials to event: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get assigned officials for an event
     * @param eventId ID of the event
     * @return List of assigned SKProfile objects
     */
    public static List<SKProfile> getAssignedOfficials(int eventId) {
        List<SKProfile> assignedOfficials = new ArrayList<>();
        
        try {
            String query = "SELECT sp.* FROM sk_profiles sp " +
                          "JOIN event_assignments ea ON sp.id = ea.sk_profile_id " +
                          "WHERE ea.event_id = ? ORDER BY sp.position_number";
            
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, eventId);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                SKProfile profile = new SKProfile();
                profile.setId(rs.getInt("id"));
                profile.setName(rs.getString("name"));
                profile.setAge(rs.getInt("age"));
                profile.setSex(rs.getString("sex"));
                profile.setPosition(rs.getString("position"));
                profile.setPositionNumber(rs.getInt("position_number"));
                profile.setCommittee(rs.getString("committee"));
                profile.setSector(rs.getString("sector"));
                profile.setPhotoPath(rs.getString("photo_path"));
                assignedOfficials.add(profile);
            }
            
            stmt.close();
            
        } catch (SQLException e) {
            System.err.println("Error fetching assigned officials: " + e.getMessage());
        }
        
        return assignedOfficials;
    }
    
    /**
     * Get assigned officials names for an event (for display purposes)
     * @param eventId ID of the event
     * @return List of official names
     */
    public static List<String> getAssignedOfficialNames(int eventId) {
        List<String> officialNames = new ArrayList<>();
        
        try {
            String query = "SELECT sp.name, sp.position FROM sk_profiles sp " +
                          "JOIN event_assignments ea ON sp.id = ea.sk_profile_id " +
                          "WHERE ea.event_id = ? ORDER BY sp.position_number";
            
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, eventId);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String name = rs.getString("name");
                String position = rs.getString("position");
                officialNames.add(name + " (" + position + ")");
            }
            
            stmt.close();
            
        } catch (SQLException e) {
            System.err.println("Error fetching assigned official names: " + e.getMessage());
        }
        
        return officialNames;
    }
    
    // ===========================
    // ATTENDANCE TRACKING METHODS
    // ===========================
    
    /**
     * Record attendance for an event
     * @param eventId ID of the event
     * @param profileId ID of the SK profile
     * @param status Attendance status (present, absent, excused)
     * @param notes Optional notes
     * @return true if successful, false otherwise
     */
    public static boolean recordAttendance(int eventId, int profileId, String status, String notes) {
        try {
            String query = "INSERT INTO event_attendance (event_id, sk_profile_id, attendance_status, attendance_time, notes) " +
                          "VALUES (?, ?, ?, NOW(), ?) " +
                          "ON DUPLICATE KEY UPDATE attendance_status = ?, attendance_time = NOW(), notes = ?";
            
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, eventId);
            stmt.setInt(2, profileId);
            stmt.setString(3, status);
            stmt.setString(4, notes);
            stmt.setString(5, status);
            stmt.setString(6, notes);
            
            int affectedRows = stmt.executeUpdate();
            stmt.close();
            
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error recording attendance: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get attendance records for an event
     * @param eventId ID of the event
     * @return List of attendance records with official details
     */
    public static List<AttendanceRecord> getEventAttendance(int eventId) {
        List<AttendanceRecord> attendanceRecords = new ArrayList<>();
        
        try {
            String query = "SELECT sp.name, sp.position, ea.attendance_status, ea.attendance_time, ea.notes " +
                          "FROM sk_profiles sp " +
                          "JOIN event_attendance ea ON sp.id = ea.sk_profile_id " +
                          "WHERE ea.event_id = ? ORDER BY sp.position_number";
            
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, eventId);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                AttendanceRecord record = new AttendanceRecord();
                record.setOfficialName(rs.getString("name"));
                record.setPosition(rs.getString("position"));
                record.setAttendanceStatus(rs.getString("attendance_status"));
                record.setAttendanceTime(rs.getTimestamp("attendance_time"));
                record.setNotes(rs.getString("notes"));
                attendanceRecords.add(record);
            }
            
            stmt.close();
            
        } catch (SQLException e) {
            System.err.println("Error fetching event attendance: " + e.getMessage());
        }
        
        return attendanceRecords;
    }
    
    /**
     * Simple attendance record class
     */
    public static class AttendanceRecord {
        private String officialName;
        private String position;
        private String attendanceStatus;
        private java.sql.Timestamp attendanceTime;
        private String notes;
        
        // Getters and setters
        public String getOfficialName() { return officialName; }
        public void setOfficialName(String officialName) { this.officialName = officialName; }
        
        public String getPosition() { return position; }
        public void setPosition(String position) { this.position = position; }
        
        public String getAttendanceStatus() { return attendanceStatus; }
        public void setAttendanceStatus(String attendanceStatus) { this.attendanceStatus = attendanceStatus; }
        
        public java.sql.Timestamp getAttendanceTime() { return attendanceTime; }
        public void setAttendanceTime(java.sql.Timestamp attendanceTime) { this.attendanceTime = attendanceTime; }
        
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }
} 