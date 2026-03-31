# Student Grade Management System

A comprehensive console-based Java application for managing student grades, calculating GPAs, tracking academic performance, and generating detailed reports. This system addresses the real-world challenge faced by educators in efficiently managing student data and performance metrics.

## 🎯 Problem Statement

Teachers and educational institutions often struggle with:
- Manually tracking student grades across multiple courses and assessment types
- Calculating weighted averages and GPAs accurately
- Identifying at-risk students who need additional support
- Generating performance reports and statistics
- Managing enrollment and course capacity
- Maintaining data consistency across academic terms

**This system solves these problems** by providing a centralized, efficient, and user-friendly platform for academic data management.

## ✨ Features

### Student Management
- Add, update, and remove student records
- Track student enrollment in multiple courses
- Store personal information (ID, name, email, enrollment date)
- Search students by name or email
- View individual student performance reports

### Course Management
- Create and manage course offerings
- Set course credits and capacity limits
- Track enrollment numbers
- Monitor course performance statistics
- Search courses by name or code

### Grade Management
- Record grades for multiple assessment types (Midterm, Final, Assignment, Quiz, Project)
- Support weighted grade calculations
- Update and remove grade records
- Calculate final course grades automatically
- View grades by student or by course

### GPA Calculation & Analytics
- Automatic GPA calculation on 4.0 scale
- Letter grade assignment (A, B, C, D, F)
- Identify at-risk students (GPA < 2.0)
- Generate class rankings
- Track top performers
- View GPA distribution across the student body

### Reporting
- Generate detailed student report cards
- Create course performance summaries
- Export reports to text files
- View overall system statistics
- Grade distribution analysis per course

### Data Persistence
- File-based data storage using CSV format
- Auto-save functionality
- Data validation and error handling
- Backup support
- Load data automatically on startup

## 🚀 Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Command line terminal (Terminal on macOS/Linux, Command Prompt on Windows)

### Installation

1. **Clone or download this repository**
   ```bash
   git clone <repository-url>
   cd harshita_java
   ```

2. **Verify the project structure**
   ```
   harshita_java/
   ├── src/
   │   └── main/
   │       ├── Main.java
   │       ├── models/
   │       ├── managers/
   │       ├── utils/
   │       └── ui/
   ├── data/            # Data files (CSV)
   ├── reports/         # Generated reports
   └── README.md
   ```

3. **Compile the project**
   ```bash
   # Create bin directory for compiled classes
   mkdir -p bin
   
   # Compile all Java files
   javac -d bin src/main/**/*.java src/main/*.java
   ```

4. **Run the application**
   ```bash
   java -cp bin main.Main
   ```

## 📖 Usage Guide

### First Time Setup

When you run the application for the first time, it will create empty data files. You can:
1. Manually add data through the menu system
2. Or use the sample data files provided in the `data/` directory

### Main Menu Navigation

The system uses a numbered menu interface. Simply enter the number corresponding to your desired action:

```
╔═══════════════════════════════════════════════════╗
║                   MAIN MENU                       ║
╠═══════════════════════════════════════════════════╣
║  1. Student Management                            ║
║  2. Course Management                             ║
║  3. Grade Management                              ║
║  4. View Reports                                  ║
║  5. View Statistics                               ║
║  6. Search                                        ║
║  7. Save Data                                     ║
║  8. Reload Data                                   ║
║  9. About System                                  ║
║  0. Exit                                          ║
╚═══════════════════════════════════════════════════╝
```

### Common Workflows

#### Adding a New Student
1. Select `1` (Student Management)
2. Select `1` (Add New Student)
3. Enter Student ID (alphanumeric, no spaces)
4. Enter Name
5. Enter Email (validated format)
6. Enter Enrollment Date (YYYY-MM-DD format)

#### Recording Grades
1. Select `3` (Grade Management)
2. Select `1` (Add Grade)
3. Enter Student ID
4. Enter Course Code
5. Select Assessment Type (Midterm, Final, Assignment, Quiz, Project, or Custom)
6. Enter Score (0-100)

#### Viewing Student Performance
1. Select `4` (View Reports)
2. Select `1` (Student Report Card)
3. Enter Student ID
4. View detailed report with all courses, grades, and GPA

#### Identifying At-Risk Students
1. Select `5` (View Statistics)
2. Select `2` (At-Risk Students)
3. View list of students with GPA < 2.0

### Sample Data

The project includes sample data with:
- 5 students (S001-S005)
- 5 courses (CS101, MATH201, ENG101, PHYS201, HIST101)
- 56 grade records across different assessments

## 📊 Data Structure

### Students (students.csv)
```csv
StudentID,Name,Email,EnrollmentDate,EnrolledCourses
S001,Alice Johnson,alice.johnson@example.com,2024-01-15,CS101;MATH201;ENG101
```

### Courses (courses.csv)
```csv
CourseCode,CourseName,Credits,MaxCapacity,EnrolledStudents
CS101,Introduction to Programming,4,50,S001;S002;S003;S004
```

### Grades (grades.csv)
```csv
StudentID,CourseCode,AssessmentType,Weight,Score,DateRecorded,Remarks
S001,CS101,Midterm,0.30,92.00,2024-03-15,Excellent work
```

## 🏗️ Architecture

### Project Structure

```
src/main/
├── Main.java                    # Entry point
├── models/                      # Data models
│   ├── Student.java            # Student entity
│   ├── Course.java             # Course entity
│   ├── Grade.java              # Grade record
│   └── Assessment.java         # Assessment type (Midterm, Final, etc.)
├── managers/                    # Business logic
│   ├── StudentManager.java    # Student CRUD operations
│   ├── CourseManager.java     # Course CRUD operations
│   ├── GradeManager.java      # Grade CRUD operations
│   └── FileManager.java       # Data persistence (CSV I/O)
├── utils/                       # Utilities
│   ├── GPACalculator.java     # GPA calculations & analytics
│   ├── ReportGenerator.java   # Report generation
│   └── InputValidator.java    # Input validation
└── ui/
    └── ConsoleUI.java          # User interface
```

### Key Data Structures Used

- **HashMap**: Fast O(1) lookup for students and courses by ID
- **ArrayList**: Flexible storage for grades with ordering
- **TreeMap**: Sorted data structures for rankings
- **Streams**: Efficient filtering and data processing

### Design Patterns

- **Singleton**: FileManager for centralized data access
- **Factory Methods**: Assessment types (midterm(), finalExam(), etc.)
- **Builder Pattern**: Complex object construction
- **Separation of Concerns**: Models, Managers, UI, Utils

## 🎓 Grade Calculation

### Weighted Grade System
Each assessment has a weight (percentage of final grade):
- Midterm: 30%
- Final: 40%
- Assignment: 20%
- Quiz: 10%

**Example Calculation:**
```
Midterm: 90/100 × 0.30 = 27.0
Final:   85/100 × 0.40 = 34.0
Assignment: 95/100 × 0.20 = 19.0
Quiz:    88/100 × 0.10 = 8.8
─────────────────────────────
Final Grade:           88.8/100 (B)
```

### GPA Scale (4.0 System)
- A (90-100): 4.0
- B (80-89):  3.0
- C (70-79):  2.0
- D (60-69):  1.0
- F (0-59):   0.0

## 📈 Statistics & Reports

### Available Reports
1. **Student Report Card**: Complete academic record for a student
2. **Course Report**: Performance summary for all students in a course
3. **Overall Statistics**: System-wide metrics and trends

### Analytics Features
- Top performers ranking
- At-risk student identification (GPA < 2.0)
- Class-wide GPA distribution
- Course average calculations
- Grade distribution per course
- Highest/lowest scores per course

## 🔧 Technical Details

### Technologies Used
- **Language**: Java 8+
- **Data Storage**: CSV files
- **I/O**: BufferedReader/BufferedWriter for file operations
- **Date Handling**: java.time.LocalDate
- **Collections**: HashMap, ArrayList, TreeMap

### Input Validation
- Email format validation using regex
- Date format validation (YYYY-MM-DD)
- ID format validation (alphanumeric only)
- Range validation for scores (0-100)
- Confirmation prompts for destructive operations

### Error Handling
- Try-catch blocks for all file I/O
- Validation at every user input point
- Graceful error messages
- Data integrity checks during CSV parsing

## 🐛 Troubleshooting

### Common Issues

**Q: "No students file found" message on startup**
- A: This is normal for first run. The system will create empty data files automatically.

**Q: Compilation errors**
- A: Ensure you're using JDK 8 or higher and compiling from the project root directory.

**Q: Data not persisting**
- A: Use option 7 (Save Data) from the main menu before exiting, or exit using option 0 which auto-saves.

**Q: Cannot find compiled classes**
- A: Make sure you compiled to the `bin` directory and are running from the project root.

## 🚀 Future Enhancements

Potential improvements for future versions:
- GUI using JavaFX or Swing
- Database integration (SQLite, MySQL)
- Multi-user support with authentication
- Email report generation
- Data visualization with charts
- Import/Export to Excel format
- Attendance tracking
- Custom grading schemes
- Semester/term management
- Grade history and trends

## 📝 License

This project is created as part of a Bring Your Own Project (BYOP) academic assignment.

## 👨‍💻 Author

Created as part of the BYOP course requirement to demonstrate practical application of:
- Object-Oriented Programming principles
- Data structures (HashMap, ArrayList, TreeMap)
- File I/O operations
- Real-world problem solving
- Software design and documentation

## 🙏 Acknowledgments

This project addresses real challenges faced in educational administration and demonstrates how software can simplify complex data management tasks while maintaining accuracy and providing valuable insights.

---

**Version**: 1.0.0  
**Last Updated**: March 2026

For questions or issues, please refer to the project documentation or contact the development team.
