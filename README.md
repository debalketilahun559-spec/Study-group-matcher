# Study Group Matcher

A Java console application that helps students find study partners based on their course, free time, and preferred study place.

## Features

- Create a student profile
- Log in using a student ID
- Search for study partners in the same course
- Match partners by free time or study place
- Prevent duplicate student IDs
- Update and delete a profile
- Simple menu-based console interface

## Technologies

- Java
- JDBC
- SQL
- SQLite

## How It Works

1. A student creates a profile with their name, ID, course, free time, study place, and Telegram.
2. The student logs in using their ID.
3. The app searches for other students in the same course.
4. A match is shown if the other student has the same free time **or** the same study place.
5. The student can contact a match using the Telegram handle shown in the results.

## How to Run

1. Install Java on your computer.
2. Open the project in IntelliJ IDEA.
3. Run the main class.
4. Choose an option from the menu.

Example menu:

```text
=== WELCOME ===
1. Log in with ID
2. Create profile
3. Exit
Choose an option:
```

## Database

The app uses an SQL database to store student profiles.

Each profile includes:

- Name
- ID number
- Course
- Free time
- Study place
- Telegram handle

## What I Learned

- Using JDBC and `PreparedStatement` safely
- Writing SQL queries with `WHERE`, `AND`, `OR`, and parentheses
- Reading results with `ResultSet`
- Preventing duplicate IDs
- Organizing repeated database code into helper methods

## Future Improvements

- Add a graphical interface using JavaFX
- Allow matching by multiple free-time slots
- Add password protection for profiles
- Save and load the database more securely