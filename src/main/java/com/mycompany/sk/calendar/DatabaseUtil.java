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
            String query = "SELECT * FROM sk_profiles ORDER BY id";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                SKProfile profile = new SKProfile();
                profile.setId(rs.getInt("id"));
                profile.setName(rs.getString("name"));
                profile.setAge(rs.getInt("age"));
                profile.setSex(rs.getString("sex"));
                profile.setPosition(rs.getString("position"));
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
            String query = "INSERT INTO sk_profiles (name, age, sex, position, committee, sector, photo_path) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setString(1, profile.getName());
            stmt.setInt(2, profile.getAge());
            stmt.setString(3, profile.getSex());
            stmt.setString(4, profile.getPosition());
            stmt.setString(5, profile.getCommittee());
            stmt.setString(6, profile.getSector());
            stmt.setString(7, profile.getPhotoPath());
            
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
            String query = "UPDATE sk_profiles SET name=?, age=?, sex=?, position=?, committee=?, sector=?, photo_path=? WHERE id=?";
            PreparedStatement stmt = connection.prepareStatement(query);
            
            stmt.setString(1, profile.getName());
            stmt.setInt(2, profile.getAge());
            stmt.setString(3, profile.getSex());
            stmt.setString(4, profile.getPosition());
            stmt.setString(5, profile.getCommittee());
            stmt.setString(6, profile.getSector());
            stmt.setString(7, profile.getPhotoPath());
            stmt.setInt(8, profile.getId());
            
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
                event.setDescription(rs.getString("description"));
                
                // Convert SQL Date to LocalDate
                java.sql.Date sqlDate = rs.getDate("event_date");
                if (sqlDate != null) {
                    event.setDate(sqlDate.toLocalDate());
                }
                
                event.setTime(rs.getString("event_time"));
                event.setLocation(rs.getString("location"));
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
     * Save a new event to database
     * @param event Event to save
     * @return Generated event ID, or 0 if failed
     */
    public static int saveEvent(CalendarScreen.Event event) {
        try {
            String query = "INSERT INTO events (title, description, event_date, event_time, location, created_by) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setString(1, event.getTitle());
            stmt.setString(2, event.getDescription());
            stmt.setDate(3, java.sql.Date.valueOf(event.getDate()));
            stmt.setString(4, event.getTime());
            stmt.setString(5, event.getLocation());
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
            String query = "UPDATE events SET title = ?, description = ?, event_date = ?, event_time = ?, location = ? WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            
            stmt.setString(1, event.getTitle());
            stmt.setString(2, event.getDescription());
            stmt.setDate(3, java.sql.Date.valueOf(event.getDate()));
            stmt.setString(4, event.getTime());
            stmt.setString(5, event.getLocation());
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
                event.setDescription(rs.getString("description"));
                event.setDate(rs.getDate("event_date").toLocalDate());
                event.setTime(rs.getString("event_time"));
                event.setLocation(rs.getString("location"));
                event.setCreatedBy(rs.getInt("created_by"));
                
                events.add(event);
            }
            
            stmt.close();
            
        } catch (SQLException e) {
            System.err.println("Error loading events for date: " + e.getMessage());
        }
        
        return events;
    }
} 