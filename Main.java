import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


import java.util.Locale;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean appRunning=true;

        // Profile details
        String id = "";
        String name = "";
        String course = "";
        String freeTime = "";
        String place = "";
        String telegram = "";
         boolean hasProfile = false;
         boolean running = true;
            while(appRunning) {

                System.out.println("=== WELCOME ===");
                System.out.println("1. Log in with ID");
                System.out.println("2. Create profile");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");

                String startChoice = input.nextLine().trim();

                if (startChoice.equals("1")) {
                    id = askForId(input);


                    String verifySql = "SELECT id_number FROM users WHERE id_number = ?;";
                    try (Connection connection = DatabaseConnection.connect()) {
                        PreparedStatement statement = connection.prepareStatement(verifySql);
                        statement.setString(1, id);

                        ResultSet result = statement.executeQuery();

                        if (!result.next()) {
                            System.out.println("STUDENT NOT FOUND!");
                            continue;
                        }
                        hasProfile = true;

                    } catch (SQLException e) {
                        System.out.println("INVALID ID: " + e.getMessage());
                    }

                } else if (startChoice.equals("2")) {
                    id = askForId(input);


                    String checkSql = "SELECT id_number FROM users WHERE id_number = ?;";

                    try (Connection connection = DatabaseConnection.connect()) {
                        PreparedStatement statement = connection.prepareStatement(checkSql);
                        statement.setString(1, id);

                        ResultSet result = statement.executeQuery();

                        if (result.next()) {
                            System.out.println("ID number already exists!");
                            continue;
                        }

                    } catch (SQLException e) {
                        System.out.println("Could not check ID: " + e.getMessage());
                        return;
                    }

                } else if (startChoice.equals("3")) {

                    System.out.println("Goodbye!");
                    appRunning = false;
                    break;
                } else {
                    System.out.println("Invalid Entry!\n");
                    continue;


                }
                running=true;


                while (running) {
                    showMenu();

                    String choice = input.nextLine();

                    if (choice.equals("1")) {

                        if (hasProfile) {
                            System.out.println(
                                    "You already have created a profile."
                            );

                        } else {
                            System.out.println("\n=== CREATE PROFILE ===");

                            System.out.print("Full name: ");
                            name = input.nextLine().trim();

                            if (isNameEmpty(name)) {
                                System.out.println(
                                        "You did not enter a name.\n Choose option 1 to try again."
                                );

                            } else {
                                // users input
                                id = askForId(input);


                                // where we block a duplicate ID
                                String checkSql = "SELECT id_number FROM users WHERE id_number = ?;";
                                try (Connection connection = DatabaseConnection.connect()) {
                                    PreparedStatement statement = connection.prepareStatement(checkSql);
                                    statement.setString(1, id);

                                    ResultSet result = statement.executeQuery();

                                    if (result.next()) {
                                        System.out.println("ID number already exists!");
                                        continue;
                                    }

                                } catch (SQLException e) {
                                    System.out.println("INVALID ID: " + e.getMessage());
                                    continue;

                                }
                                // validating course
                                course = askForcourse(input);
                                freeTime = askFreeTime(input);
                                place = askForPlace(input);
                                telegram = askForTelegarm(input);


                                // the sql that stores the data in the database


                                String insertSql = "INSERT INTO users (name, course, id_number, free_time, telegram, study_place) "
                                        + "VALUES (?, ?, ?, ?, ?, ?)";

                                try (Connection connection = DatabaseConnection.connect();
                                     PreparedStatement statement = connection.prepareStatement(insertSql)) {

                                    statement.setString(1, name);
                                    statement.setString(2, course);
                                    statement.setString(3, id);
                                    statement.setString(4, freeTime);
                                    statement.setString(5, telegram);
                                    statement.setString(6, place);

                                    statement.executeUpdate();

                                    System.out.println("Profile saved to database!");
                                    // THE SQL THAT VIEWS CREATED PROFILE
                                    String createSql = "SELECT * FROM users WHERE id_number = ?;";
                                    try (Connection createConnection = DatabaseConnection.connect()) {
                                        PreparedStatement createStatement = createConnection.prepareStatement(createSql);

                                        createStatement.setString(1, id);

                                        ResultSet result = createStatement.executeQuery();

                                        if (result.next()) {

                                            String crName = result.getString("name");
                                            String crCourse = result.getString("course");
                                            String crFreeTime = result.getString("free_time");
                                            String crStudyPlace = result.getString("study_place");
                                            String crTelegram = result.getString("telegram");
                                            showProfile(
                                                    crName,
                                                    crCourse,
                                                    crFreeTime,
                                                    id,
                                                    crStudyPlace,
                                                    crTelegram
                                            );

                                        }

                                    }


                                } catch (SQLException e) {

                                    System.out.println("Could not SAVE profile: " + e.getMessage());
                                }


                                hasProfile = true;

                                System.out.println("Profile created!");
                            }
                        }

                    } else if (choice.equals("2")) {


                        String sql = "SELECT * FROM users WHERE id_number = ?;";
                        try (Connection connection = DatabaseConnection.connect()) {


                            PreparedStatement statement = connection.prepareStatement(sql);
                            statement.setString(1, id);
                            ResultSet result = statement.executeQuery();

                            if (result.next()) {
                                System.out.println("Profile found!");
                                String dbName = result.getString("name");
                                String dbCourse = result.getString("course");
                                String dbFreeTime = result.getString("free_time");
                                String dbStudyPlace = result.getString("study_place");
                                String dbTelegram = result.getString("telegram");


                                showProfile(
                                        dbName,
                                        dbCourse,
                                        dbFreeTime,
                                        id,
                                        dbStudyPlace,
                                        dbTelegram
                                );
                                hasProfile = true;

                            } else {
                                System.out.println("No profile found with that ID!");


                            }
                        } catch (SQLException e) {
                            System.out.println("Could not load profile: " + e.getMessage());


                        }


                    } else if (choice.equals("3")) {
                        String upvalsql = "SELECT id_number FROM users WHERE id_number = ?;";
                        try (Connection connection = DatabaseConnection.connect()) {
                            PreparedStatement statement = connection.prepareStatement(upvalsql);
                            statement.setString(1, id);
                            ResultSet result = statement.executeQuery();
                            if (!result.next()) {
                                System.out.println("No profile found!");
                                continue;
                            }
                        } catch (SQLException e) {
                            System.out.println("Could not load profile: " + e.getMessage());

                        }
                        course = askForcourse(input);
                        place = askForPlace(input);
                        freeTime = askFreeTime(input);
                        telegram = askForTelegarm(input);

                        // the sql for option 3

                        String sql = "UPDATE users SET course = ?, study_place = ?, free_time = ?, telegram = ? WHERE id_number = ?;";
                        try (Connection connection = DatabaseConnection.connect()) {
                            PreparedStatement statement = connection.prepareStatement(sql);

                            statement.setString(1, course);
                            statement.setString(2, place);
                            statement.setString(3, freeTime);
                            statement.setString(4, telegram);
                            statement.setString(5, id);

                            int rowsUpdate = statement.executeUpdate();

                            if (rowsUpdate > 0) {
                                System.out.println("Profile updated!");

                                String viewerSql = "SELECT * FROM users WHERE id_number= ?;";
                                try (Connection viewConnection = DatabaseConnection.connect()) {
                                    PreparedStatement viewStatement = viewConnection.prepareStatement(viewerSql);
                                    viewStatement.setString(1, id);
                                    ResultSet result = viewStatement.executeQuery();
                                    if (result.next()) {

                                        String upName = result.getString("name");
                                        String upCourse = result.getString("course");
                                        String upFreeTime = result.getString("free_time");
                                        String upStudyPlace = result.getString("study_place");
                                        String upTelegram = result.getString("telegram");


                                        showProfile(
                                                upName,
                                                upCourse,
                                                upFreeTime,
                                                id,
                                                upStudyPlace,
                                                upTelegram
                                        );

                                    }
                                } catch (SQLException e) {
                                    System.out.println("Could not profile view updated profile: " + e.getMessage());
                                }


                                hasProfile = true;
                            } else {
                                System.out.println("No profile found with that ID.");
                            }


                        } catch (SQLException e) {

                            System.out.println("Could not update your profile");
                        }


                    } else if (choice.equals("5")) {
                        String deletionChoice = "";
                        System.out.println("Are you sure you want to delete your profile?");
                        System.out.println("1.Yes, delete it.");
                        System.out.println("2.No, cancel.");
                        deletionChoice = input.nextLine();


                        if (deletionChoice.equals("2")) {

                            System.out.println("DELETION CANCELED!");


                        } else if (deletionChoice.equals("1")) {


                            String sql = "DELETE FROM users WHERE id_number = ?;";

                            try (Connection connection = DatabaseConnection.connect()) {
                                PreparedStatement statement = connection.prepareStatement(sql);

                                statement.setString(1, id);
                                int rowsDeleted = statement.executeUpdate();

                                if (rowsDeleted > 0) {
                                    System.out.println("Profile deleted from database!");
                                } else {
                                    System.out.println("No profile found with that ID.");
                                }


                            } catch (SQLException e) {
                                System.out.println("Could not delete profile: " + e.getMessage());

                            }

                            id = "";
                            name = "";
                            course = "";
                            freeTime = "";
                            place = "";
                            telegram = "";
                            hasProfile = false;
                        } else {
                            System.out.println("INVALID ENTRY!");
                        }


                    } else if (choice.equals("4")) {
                        System.out.println("\n====FIND STUDY PARTNERS!====");
                        course = askForcourse(input);

                        if (course.trim().isEmpty()) {
                            System.out.println("Please enter a course name.");
                            continue;
                        }
                        // SQL for free time

                        String myFreeTime = getUserColumn(id, "free_time");
                        String myStudyPlace = getUserColumn(id, "study_place");

                        // sql for the find partners

                        String sql = "SELECT * FROM users WHERE LOWER(course) = ? AND id_number <> ? AND (LOWER(free_time) = ? OR LOWER(study_place) = ?);";

                        boolean foundPartner = false;

                        try (Connection connection = DatabaseConnection.connect()) {
                            PreparedStatement statement = connection.prepareStatement(sql);

                            statement.setString(1, course.trim().toLowerCase());
                            statement.setString(2, id);
                            statement.setString(3, myFreeTime);
                            statement.setString(4, myStudyPlace);


                            ResultSet result = statement.executeQuery();

                            while (result.next()) {

                                String matchName = result.getString("name");
                                String matchCourse = result.getString("course");
                                String matchFreeTime = result.getString("free_time");
                                String matchStudyPlace = result.getString("study_place");
                                String matchTelegram = result.getString("telegram");
                                showClassmates(
                                        matchName,
                                        matchCourse,
                                        matchFreeTime,
                                        matchStudyPlace,
                                        matchTelegram
                                );
                                foundPartner = true;


                            }
                            if (!foundPartner) {
                                System.out.println("No study partner found for your course and free time.");
                            }

                        } catch (SQLException e) {
                            System.out.println("Could not find a study partner: " + e.getMessage());


                        }


                    }else if(choice.equals("7")){

                        id = "";
                        name = "";
                        course = "";
                        freeTime = "";
                        place = "";
                        telegram = "";
                        hasProfile = false;
                        running = false;

                    }



                    else if (choice.equals("6")){
                        running = false;
                    }
                    else {
                        System.out.println(
                                "Invalid choice. Enter 1, 2, 3, 4, 5, 6 ,or 7."
                        );
                    }
                }//this whr
            }//ye outer loop mecheresha


        System.out.println("Goodbye!");
        input.close();
    }


    // Displays the menu
    public static void showMenu() {
        System.out.println("\n=== STUDY GROUP MATCHER ===");
        System.out.println("1. Create study profile");
        System.out.println("2. View my profile");
        System.out.println("3. Update my profile");
        System.out.println("4. Find study partners" );
        System.out.println("5.Delete my profile");
        System.out.println("6.EXIT");
        System.out.println("7. Log out");
        System.out.print("Choose an option: ");
    }

    // Receives and displays the profile details
    public static void showProfile(
            String studentName,
            String studentCourse,
            String studentFreeTime,
            String studentId,
            String studentPlace,
            String studentTelegram) {

        System.out.println("\n===== YOUR PROFILE =====");
        System.out.println("ID: " + studentId);
        System.out.println("Name: " + studentName);
        System.out.println("Course: " + studentCourse);
        System.out.println("Free time: " + studentFreeTime);
        System.out.println("Study place: " + studentPlace);
        System.out.println("Telegram: " + studentTelegram);
        System.out.println("========================");
    }
    public static void showClassmates(String classmateName,
                                       String classmateCourse,
                                       String classmateFreeTime,
                                       String classmatePlace,
                                       String classmateTelegram) {

        System.out.println("\n=== STUDY PARTNER FOUND ===");
        System.out.println("Name: " + classmateName);
        System.out.println("Course: " + classmateCourse);
        System.out.println("Free time: " + classmateFreeTime);
        System.out.println("Study place: " + classmatePlace);
        System.out.println("Telegram: " + classmateTelegram);
        System.out.println("==========================");
    }



    public static String askForcourse(Scanner input){



        String courseChoice = "";

        boolean validCourse = false;
        while(!validCourse){
            System.out.print("\nAVAILABLE COURSES");
            System.out.println();
            System.out.println("1,Digital Logic Design");
            System.out.println("2,Fundamental of Database Systems");
            System.out.println("3,Linear Algebra");
            System.out.println("4,Statistics");

            System.out.print("Choose a course (1-4): ");
            courseChoice = input.nextLine().trim();

            if(courseChoice.isEmpty()){
                System.out.print("Choose course!");

            }else if(courseChoice.equals("1")){

                courseChoice = "Digital Logic Design" ;
                validCourse=true;
            }else if(courseChoice.equals("2")){

                courseChoice = "Fundamental of Database Systems" ;
                validCourse=true;

            }else if(courseChoice.equals("3")){

                courseChoice = "Linear Algebra";
                validCourse=true;

            }else if(courseChoice.equals("4")) {

                courseChoice = "Statistics";
                validCourse=true;
            }else{
                System.out.print("INVALID ENTRY!\n");
            }

        }
        return courseChoice;
    }
public static String askForPlace(Scanner input){


    boolean validPlace=false;
    String choosePlace= "";
    while(!validPlace) {
        System.out.print("\nAVAILABLE PLACES");
        System.out.println();
        System.out.println("1,Library");
        System.out.println("2,Dorm");
        System.out.println("3,Classroom");
        System.out.print("Choose a place (1-3): ");
        choosePlace = input.nextLine().trim();
        if(choosePlace.isEmpty()){
            System.out.print("Please enter a study place");

        }else if(choosePlace.equals("1")) {

            choosePlace = "Library";
            validPlace = true;
        }else if(choosePlace.equals("2")) {

            choosePlace = "Dorm";
            validPlace = true;
        }else if(choosePlace.equals("3")) {

            choosePlace = "Classroom";
            validPlace = true;
        }else{
            System.out.print("INVALID ENTRY!\n");
        }
    }
        return choosePlace;

}
public static String askFreeTime(Scanner input){

    boolean hasFreeTime=false;
    String freeTime = "";

    while(!hasFreeTime) {


        System.out.println("Free time: ");
        freeTime = input.nextLine().trim();
        if(freeTime.isEmpty()){
            System.out.print("Tell us your free time.\n");
        }
        else{
            hasFreeTime = true;
        }
    }

        return freeTime;
}
public static String askForTelegarm(Scanner input){

    String telegram = "";
    boolean validTelegram = false;
  while(!validTelegram){

      System.out.println("Telegram account(optional) : ");
      telegram = input.nextLine().trim();

      if((!telegram.startsWith("@"))&&(!telegram.isEmpty())){
          System.out.println("Invalid Telegram account");

      }else{
          validTelegram = true;
      }

  }
        return telegram;
}
public static String askForId(Scanner input){

    String id= "";
    boolean validId = false;
    while(!validId){

        System.out.println("Enter your ID: ");
       id = input.nextLine().trim();

        if((!id.matches("(Ugr|Hgr)\\\\\\d+\\\\\\d{2}"))||(id.isEmpty())){
            System.out.println("Invalid Id\n");

        }else{
            validId = true;
        }

    }
        return id;
}
private static String getUserColumn(String id, String columnName) {
        String value = "";

        String sql = "SELECT " + columnName + " FROM users WHERE id_number = ?";

        try (Connection connection = DatabaseConnection.connect()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                value = result.getString(columnName);
            }
        } catch (SQLException e) {
            System.out.println("Could not load user information: " + e.getMessage());
        }

        return value;
    }

    // Returns true if the name contains no characters
    public static boolean isNameEmpty(String studentName) {
        return studentName.isEmpty();
    }
    public static boolean isIdEmpty(String studentId){
            return studentId.isEmpty();

    }

}


