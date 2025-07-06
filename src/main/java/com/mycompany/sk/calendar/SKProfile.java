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
    private int positionNumber; // Position number 1-10 for SK officials
    private String committee;
    private String sector;
    private String photoPath;
    private boolean isActive;
    
    // SK Positions Constants (ordered by position number)
    public static final String[] SK_POSITIONS = {
        "SK Chairman",        // Position 1
        "SK Kagawad 1",       // Position 2
        "SK Kagawad 2",       // Position 3
        "SK Kagawad 3",       // Position 4
        "SK Kagawad 4",       // Position 5
        "SK Kagawad 5",       // Position 6
        "SK Kagawad 6",       // Position 7
        "SK Kagawad 7",       // Position 8
        "Secretary",          // Position 9
        "Treasurer"           // Position 10
    };
    
    // Position number to position title mapping
    public static final String[] POSITION_TITLES = {
        "",                   // Position 0 (unused)
        "SK Chairman",        // Position 1
        "SK Kagawad 1",       // Position 2
        "SK Kagawad 2",       // Position 3
        "SK Kagawad 3",       // Position 4
        "SK Kagawad 4",       // Position 5
        "SK Kagawad 5",       // Position 6
        "SK Kagawad 6",       // Position 7
        "SK Kagawad 7",       // Position 8
        "Secretary",          // Position 9
        "Treasurer"           // Position 10
    };
    
    // Position Constants
    public static final String CHAIRMAN = "SK Chairman";
    public static final String KAGAWAD_1 = "SK Kagawad 1";
    public static final String KAGAWAD_2 = "SK Kagawad 2";
    public static final String KAGAWAD_3 = "SK Kagawad 3";
    public static final String KAGAWAD_4 = "SK Kagawad 4";
    public static final String KAGAWAD_5 = "SK Kagawad 5";
    public static final String KAGAWAD_6 = "SK Kagawad 6";
    public static final String KAGAWAD_7 = "SK Kagawad 7";
    public static final String SECRETARY = "Secretary";
    public static final String TREASURER = "Treasurer";
    
    // Position Number Constants
    public static final int CHAIRMAN_NUMBER = 1;
    public static final int KAGAWAD_1_NUMBER = 2;
    public static final int KAGAWAD_2_NUMBER = 3;
    public static final int KAGAWAD_3_NUMBER = 4;
    public static final int KAGAWAD_4_NUMBER = 5;
    public static final int KAGAWAD_5_NUMBER = 6;
    public static final int KAGAWAD_6_NUMBER = 7;
    public static final int KAGAWAD_7_NUMBER = 8;
    public static final int SECRETARY_NUMBER = 9;
    public static final int TREASURER_NUMBER = 10;
    
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
    public SKProfile(String name, int age, String sex, String position, int positionNumber) {
        this();
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.position = position;
        this.positionNumber = positionNumber;
    }
    
    /**
     * Full constructor
     */
    public SKProfile(int id, String name, int age, String sex, String position, int positionNumber,
                     String committee, String sector, String photoPath) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.position = position;
        this.positionNumber = positionNumber;
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
    public int getPositionNumber() { return positionNumber; }
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
    public void setPositionNumber(int positionNumber) { this.positionNumber = positionNumber; }
    public void setCommittee(String committee) { this.committee = committee; }
    public void setSector(String sector) { this.sector = sector; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }
    public void setActive(boolean active) { this.isActive = active; }
    
    /**
     * Check if this profile represents a leadership position
     */
    public boolean isLeadershipPosition() {
        return positionNumber == 1 || positionNumber == 9 || positionNumber == 10; // Chairman, Secretary, Treasurer
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
    
    /**
     * Get position title by position number
     * @param positionNumber Position number (1-10)
     * @return Position title or empty string if invalid
     */
    public static String getPositionTitle(int positionNumber) {
        if (positionNumber >= 1 && positionNumber < POSITION_TITLES.length) {
            return POSITION_TITLES[positionNumber];
        }
        return "";
    }
    
    /**
     * Check if position number is valid
     * @param positionNumber Position number to check
     * @return true if valid (1-10), false otherwise
     */
    public static boolean isValidPositionNumber(int positionNumber) {
        return positionNumber >= 1 && positionNumber <= 10;
    }
    
    /**
     * Get position number display text
     * @return Position number with title (e.g., "1 - SK Chairman")
     */
    public String getPositionNumberDisplay() {
        if (isValidPositionNumber(positionNumber)) {
            return positionNumber + " - " + getPositionTitle(positionNumber);
        }
        return "Invalid Position";
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