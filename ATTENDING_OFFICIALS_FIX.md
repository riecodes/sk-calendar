# Fix for Attending Officials Data Not Being Saved

## Problem
The `attendingOfficialsField` in the `AddEventDialog` was collecting the number of attending officials from the user, but this data was not being saved to the database. This happened because:

1. The `events` table didn't have a column for `attending_officials_count`
2. The `Event` class didn't have a field for `attendingOfficialsCount`
3. The database methods weren't handling this field

## Solution
I've made the following changes to fix this issue:

### 1. Database Schema Changes
- **File**: `database_schema.sql`
- **Changes**:
  - Added `attending_officials_count INT DEFAULT 1` column to the `events` table
  - Added migration script to add the column to existing databases

### 2. Event Class Changes
- **File**: `src/main/java/com/mycompany/sk/calendar/CalendarScreen.java`
- **Changes**:
  - Added `attendingOfficialsCount` field to the `Event` class
  - Added getter and setter methods: `getAttendingOfficialsCount()` and `setAttendingOfficialsCount()`
  - Set default value to 1 in constructor

### 3. Database Utility Changes
- **File**: `src/main/java/com/mycompany/sk/calendar/DatabaseUtil.java`
- **Changes**:
  - Updated `saveEvent()` method to save `attending_officials_count`
  - Updated `updateEvent()` method to update `attending_officials_count`
  - Updated `getAllEvents()` method to load `attending_officials_count`
  - Updated `getEventsForDate()` method to load `attending_officials_count`

### 4. AddEventDialog Changes
- **File**: `src/main/java/com/mycompany/sk/calendar/AddEventDialog.java`
- **Changes**:
  - Updated `saveEvent()` method to set attending officials count from the field
  - Updated `updateEvent()` method to set attending officials count from the field
  - Updated `populateFields()` method to load attending officials count from database
  - Updated `openDutyAssignmentDialog()` method to use event's attending officials count

## How to Apply the Fix

### For New Databases
1. Run the updated `database_schema.sql` file - it now includes the `attending_officials_count` column

### For Existing Databases
1. The migration is already included in the `database_schema.sql` file - look for the line:
   ```sql
   ALTER TABLE events ADD COLUMN IF NOT EXISTS attending_officials_count INT DEFAULT 1;
   ```
2. Or manually run: `ALTER TABLE events ADD COLUMN IF NOT EXISTS attending_officials_count INT DEFAULT 1;`

## Testing
After applying the fix:
1. Create a new event and set the number of attending officials
2. Save the event
3. Edit the event - the attending officials count should be preserved
4. The attending officials count will be used when assigning officials to events

## Benefits
- Attending officials data is now properly saved to the database
- The data persists between edit sessions
- The duty assignment dialog can use the saved count
- Better data integrity and consistency 