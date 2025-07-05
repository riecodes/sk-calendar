# SK Calendar - Development TODO List

## Project Overview
Sangguniang Kabataan Calendar Action Planner for event management and scheduling

## Phase 1: Project Setup & Configuration
- [ ] **1.1** Update Maven configuration (pom.xml)
  - Change Java version from 24 to 8 (as per requirements)
  - Add JCalendar dependency
  - Add SQLite/H2 database dependency
  - Add required Maven plugins
- [ ] **1.2** Create database schema design
- [ ] **1.3** Set up project package structure

## Phase 2: Database Layer
- [ ] **2.1** Create database connection utility class
- [ ] **2.2** Create SK Members table and model
  - Fields: ID, Name, Position, Contact Details
- [ ] **2.3** Create Events table and model
  - Fields: ID, Title, Date, Time, Place, Attending Officials
- [ ] **2.4** Create DAO (Data Access Object) classes
  - SKMemberDAO (CRUD operations)
  - EventDAO (CRUD operations)
- [ ] **2.5** Database initialization script

## Phase 3: Core Models & Entities
- [ ] **3.1** Create SKMember model class
- [ ] **3.2** Create Event model class
- [ ] **3.3** Create Position enum (Chairman, SK1-7, Treasurer, Secretary)
- [ ] **3.4** Create validation utilities

## Phase 4: Main Application Window
- [ ] **4.1** Create MainForm JFrame
  - Window title: "SK Calendar Action Planner"
  - Set appropriate size and center on screen
  - Add application icon
- [ ] **4.2** Add main buttons
  - Calendar button (navigate to calendar view)
  - SK Profiles button (navigate to profiles management)
- [ ] **4.3** Apply consistent styling and layout
- [ ] **4.4** Add menu bar with basic options

## Phase 5: SK Profiles Management
- [ ] **5.1** Create SKProfilesForm JFrame
  - Table to display all SK members
  - Add/Edit/Delete buttons
- [ ] **5.2** Create AddEditSKMemberDialog
  - Name field
  - Position combo box (10 positions)
  - Contact details field
  - Form validation
- [ ] **5.3** Implement CRUD operations
  - Add new SK member
  - Edit existing member
  - Delete member
  - View all members
- [ ] **5.4** Add search/filter functionality

## Phase 6: Calendar Interface
- [ ] **6.1** Create CalendarForm JFrame
  - Integrate JCalendar component
  - Event display panel
  - Navigation controls
- [ ] **6.2** Create AddEditEventDialog
  - Event title field
  - Date picker
  - Time picker
  - Place field
  - Attending officials multi-select (combo box)
  - Form validation
- [ ] **6.3** Implement event display on calendar
  - Show events as markers on calendar dates
  - Handle multiple events on same date
  - Click to view event details
- [ ] **6.4** Event management features
  - Add new event
  - Edit existing event
  - Delete event
  - View event details

## Phase 7: Event Management Logic
- [ ] **7.1** Event creation workflow
  - Validate required fields
  - Check for scheduling conflicts
  - Save to database
- [ ] **7.2** Attending officials management
  - Populate combo box from SK members
  - Allow selection of multiple officials
  - Validate maximum 10 attendees
- [ ] **7.3** Event editing and viewing
  - Load event details from database
  - Edit event information
  - Update database
- [ ] **7.4** Event deletion with confirmation

## Phase 8: UI/UX Enhancements
- [ ] **8.1** Add icons to buttons and forms
  - Use provided asset images (back.png, next.png, close.png)
- [ ] **8.2** Implement navigation between forms
  - Back buttons on secondary forms
  - Proper form closing behavior
- [ ] **8.3** Add loading indicators
- [ ] **8.4** Error handling and user feedback
  - Success messages
  - Error dialogs
  - Input validation messages

## Phase 9: Data Management
- [ ] **9.1** Implement data persistence
  - Save events to database
  - Load events from database
- [ ] **9.2** Add data backup functionality
- [ ] **9.3** Import/Export features (optional)
- [ ] **9.4** Data validation and integrity checks

## Phase 10: Testing & Refinement
- [ ] **10.1** Unit testing for DAO classes
- [ ] **10.2** Integration testing
- [ ] **10.3** UI testing with mock data
- [ ] **10.4** Performance optimization
- [ ] **10.5** Bug fixes and improvements

## Phase 11: Documentation & Deployment
- [ ] **11.1** Create user manual
- [ ] **11.2** Add code documentation
- [ ] **11.3** Create deployment package
- [ ] **11.4** Installation guide

## Key Technical Decisions Made:
1. **Database**: SQLite for local storage (lightweight, no server required)
2. **UI Framework**: Java Swing with JCalendar library
3. **Architecture**: MVC pattern with DAO layer
4. **Officials Limit**: Maximum 10 attendees per event
5. **Positions**: Fixed 10 positions (Chairman, SK1-7, Treasurer, Secretary)

## Immediate Next Steps:
1. Update Maven configuration (pom.xml)
2. Create database schema
3. Build main application window
4. Implement SK profiles management
5. Add calendar functionality

## Notes:
- Focus on core functionality first, then enhance UI/UX
- Keep forms simple and user-friendly
- Implement proper error handling throughout
- Test each component thoroughly before moving to next phase 