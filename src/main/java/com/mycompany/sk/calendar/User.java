package com.mycompany.sk.calendar;

/**
 * User model class representing SK officials in the system
 */
public class User {
    
    private int id;
    private String username;
    private String fullName;
    private String position;
    
    /**
     * Constructor for User
     * @param id user ID
     * @param username username
     * @param fullName full name of the user
     * @param position SK position (Chairman, Kagawad, Secretary, Treasurer, etc.)
     */
    public User(int id, String username, String fullName, String position) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.position = position;
    }
    
    // Getters
    public int getId() {
        return id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public String getPosition() {
        return position;
    }
    
    // Setters
    public void setId(int id) {
        this.id = id;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    @Override
    public String toString() {
        return fullName + " (" + position + ")";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return id == user.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
} 