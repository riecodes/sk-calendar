package com.mycompany.sk.calendar;

/**
 * Model class representing an SK (Sangguniang Kabataan) member profile
 */
public class SKProfile {
    
    private int id;
    private String name;
    private int age;
    private String sex;
    private String position;
    private String committee;
    private String sector;
    private String photoPath;
    private boolean isActive;
    
    // SK Positions Constants
    public static final String[] SK_POSITIONS = {
        "SK Chairman",
        "SK Kagawad 1",
        "SK Kagawad 2", 
        "SK Kagawad 3",
        "SK Kagawad 4",
        "SK Kagawad 5",
        "SK Kagawad 6",
        "SK Kagawad 7",
        "SK Secretary",
        "SK Treasurer"
    };
    
    // Committee Options
    public static final String[] COMMITTEES = {
        "Education Committee",
        "Health and Wellness Committee",
        "Sports and Recreation Committee",
        "Environment Committee",
        "Peace and Order Committee",
        "Livelihood Committee",
        "Arts and Culture Committee"
    };
    
    // Sector Options
    public static final String[] SECTORS = {
        "In-School Youth",
        "Out-of-School Youth",
        "Working Youth",
        "Indigenous Youth",
        "Youth with Disabilities"
    };
    
    /**
     * Default constructor
     */
    public SKProfile() {
        this.isActive = true;
    }
    
    /**
     * Constructor with basic information
     */
    public SKProfile(String name, int age, String sex, String position) {
        this();
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.position = position;
    }
    
    /**
     * Full constructor
     */
    public SKProfile(int id, String name, int age, String sex, String position, 
                     String committee, String sector, String photoPath) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.position = position;
        this.committee = committee;
        this.sector = sector;
        this.photoPath = photoPath;
        this.isActive = true;
    }
    
    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getSex() { return sex; }
    public String getPosition() { return position; }
    public String getCommittee() { return committee; }
    public String getSector() { return sector; }
    public String getPhotoPath() { return photoPath; }
    public boolean isActive() { return isActive; }
    
    // Setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setSex(String sex) { this.sex = sex; }
    public void setPosition(String position) { this.position = position; }
    public void setCommittee(String committee) { this.committee = committee; }
    public void setSector(String sector) { this.sector = sector; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }
    public void setActive(boolean active) { this.isActive = active; }
    
    /**
     * Check if this profile represents a leadership position
     */
    public boolean isLeadershipPosition() {
        return position != null && (
            position.equals("SK Chairman") ||
            position.equals("SK Secretary") ||
            position.equals("SK Treasurer")
        );
    }
    
    /**
     * Get display name with position
     */
    public String getDisplayName() {
        if (name != null && position != null) {
            return name + " (" + position + ")";
        }
        return name != null ? name : "Unknown";
    }
    
    @Override
    public String toString() {
        return getDisplayName();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SKProfile profile = (SKProfile) obj;
        return id == profile.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
} 