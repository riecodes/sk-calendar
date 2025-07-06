# SK Calendar System

A desktop application for managing SK (Sangguniang Kabataan) events, officials, and attendance, designed with a modern, user-friendly interface.

## Features

- **Monthly Calendar View:** Visualize and navigate events by month and day.
- **Event Management:**  
  - Add, edit, and delete events.
  - Assign SK officials to each event (up to 10).
  - Specify event details: title, time, location, and number of attending officials.
- **Duty Assignment:**  
  - Assign specific SK officials to event duties.
  - Easy dropdown selection for each official.
- **Attendance Logging:**  
  - Record attendance for each official per event.
  - Mark as Present, Absent, or Excused.
  - View and update attendance logs.
- **Modern UI:**  
  - Consistent color scheme and custom fonts.
  - Responsive dialogs with scroll support for large forms.
  - Clean agenda view showing only event titles.

## Getting Started

### Prerequisites

- **Java 8 or higher**
- **Maven** (for building the project)
- **SQLite** (used as the database, no separate server required)

### Setup

1. **Clone the repository:**
   ```sh
   git clone <your-repo-url>
   cd sk-calendar
   ```

2. **Database Setup:**
   - The database schema is defined in `database_schema.sql`.
   - On first run, the application will create and initialize the SQLite database if it does not exist.

3. **Build the project:**
   ```sh
   mvn clean package
   ```

4. **Run the application:**
   ```sh
   mvn exec:java -Dexec.mainClass="com.mycompany.sk.calendar.SkCalendar"
   ```
   Or run the generated JAR from the `target/` directory.

### Directory Structure

- `src/main/java/com/mycompany/sk/calendar/` — Main application source code
- `src/main/java/com/mycompany/sk/assets/` — Fonts, images, and other assets
- `database_schema.sql` — Database schema
- `sk_calendar.sql` — Example database file

### Main Classes

- `SkCalendar.java` — Application entry point
- `CalendarScreen.java` — Main calendar UI
- `AddEventDialog.java` — Add/edit event dialog
- `DutyAssignmentDialog.java` — Assign officials to events
- `AttendanceLogDialog.java` — Attendance management
- `DatabaseUtil.java` — Database operations

## Usage

1. **Launch the application.**
2. **Navigate the calendar** using the month navigation buttons.
3. **Add events** by selecting a date and clicking "ADD EVENT".
4. **Assign officials** and manage attendance via the event dialogs.
5. **View the agenda** for each day in the right-hand panel.

## Customization

- **Fonts and Images:**  
  Located in `assets/` — you can replace or add new assets as needed.
- **Database:**  
  The SQLite database file is created in the project directory. You can back it up or inspect it with any SQLite tool.

## Troubleshooting

- If the UI appears cut off or scrollbars are missing, ensure your Java version is up to date.
- For database errors, check that you have write permissions in the project directory.

## License

This project is for educational and demonstration purposes. 