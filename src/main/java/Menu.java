import Users.Admin;
import Users.Lecturer;
import Users.Student;
import Users.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Menu {
    boolean runApp = true;
    Login app = new Login();
    DataWriter dataWriter = new DataWriter();
    DataStorage dataStorage = new DataStorage();
    Scanner sc = new Scanner(System.in);
    LoggerMIVS logger = new LoggerMIVS();

    public void runWelcomeMenu() throws Exception {
        System.out.println("Welcome to MIVS");
        while (runApp && !app.isLoginDataValid()) {
            System.out.println("1. Sign in");
            System.out.println("2. Exit");
            try {
                String userInput = sc.next();
                switch (Integer.parseInt(userInput)) {
                    case 1:
                        if (!app.isLoginDataValid()) {
                            app.login();
                            break;
                        }
                        break;
                    case 2:
                        app.close();
                        runApp = false;
                        break;
                    default:
                        System.out.println("\nWrong input\n");
                        break;

                }
            } catch (Exception e) {
                System.out.println("\nWrong number format\n");
            }
        }
    }

    public void runUserMenu(User user) {
        while (runApp) {
            System.out.println();
            if (user instanceof Admin) {
                runAdminMenu(user);
            } else if (user instanceof Lecturer) {
                runLecturerMenu(user);
            } else if (user instanceof Student) {
                runStudentMenu(user);
            } else {
                return;
            }
        }
    }

    private void runAdminMenu(User user) {
        System.out.println("1. Edit profile");
        System.out.println("2. View all Courses");
        System.out.println("3. View all Users");
        System.out.println("4. Add new User");
        System.out.println("5. Add new Course");
        System.out.println("6. Exit");
        try {
            String userInput = sc.next();
            switch (Integer.parseInt(userInput)) {
                case 1:
                    editUserPropertiesMenu(user, user.getID());
                    user.setUserProperties(user.getID(), dataStorage.getUserInfoByID(user.getID()));
                    break;
                case 2:
                    dataStorage.printCoursesTable();
                    makeChangesToCourseTable(user);
                    break;
                case 3:
                    dataStorage.printUsersTable();
                    System.out.println("Do you want to make changes? \n1. yes   2. no");
                    try {
                        switch (Integer.parseInt(sc.next())) {
                            case 1:
                                userTableMenu(user);
                                break;
                            case 2:
                                break;
                            default:
                                System.out.println("\nWrong input\n");
                                break;
                        }
                    } catch (Exception e) {
                        System.out.println("\nWrong number format\n");
                        break;
                    }
                    break;
                case 4:
                    dataStorage.setUserAdded(false);
                    if (!dataStorage.userAdded) {
                        addNewUser(user);
                    }
                    break;

                case 5:
                    dataStorage.printAllLecturers();
                    int wrongInput = 0;
                    while (wrongInput < 3) {
                        System.out.print("\nChose Course lecturer (Enter ID): ");
                        String lecID = sc.next();
                        lecID = lecID.toUpperCase();
                        if ((lecID.split("-")[0].equals("LEC")) && dataStorage.getUserProperties().containsKey(lecID)) {
                            addNewCourse(user,lecID);
                            return;
                        } else if ((!lecID.split("-")[0].equals("LEC")) && (dataStorage.getUserProperties().containsKey(lecID))) {
                            System.out.println("\nUser is not a lecturer.");
                            wrongInput++;
                        } else if (!dataStorage.getUserProperties().containsKey(lecID)) {
                            System.out.println("\nUser doesn't exist");
                            wrongInput++;
                        }
                    }
                    break;
                case 6:
                    app.close();
                    runApp = false;
                    break;
                default:
                    System.out.println("\nWrong input\n");
                    break;
            }
        } catch (Exception e) {
            System.out.println("\nWrong number format\n");
        }
    }


    private void addNewCourse(User user, String lecID) {
        dataStorage.storeCoursesInfo();
        sc.nextLine();
        System.out.print("Course Title: ");
        String courseTitle = sc.nextLine();
        courseTitle = courseTitle.replace(";",",");
        System.out.print("Description: ");
        String description = sc.nextLine();
        description = description.replace(";",",");
        System.out.print("Start Date (yyyy-mm-dd): ");
        String startDate = String.valueOf(LocalDate.now());
        try {
            LocalDate checkStartDate = LocalDate.parse(sc.next(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            startDate = String.valueOf(checkStartDate);
        } catch (Exception e) {
            System.out.println("Wrong date format. Default Start date is Today");
        }
        System.out.print("Credit: ");
        int credit = 2;
        try {
            credit = Integer.parseInt(sc.next());
            if(credit>0&&credit<5){
                }else {
                System.out.println(credit+" is not allowed Amount of Credit. Default Course Credit is 2");
            }

        } catch (Exception e) {
            System.out.println("Wrong number format. Default Course Credit is 2");
        }
        String courseData = lecID + ";" + credit + ";" + courseTitle + ";" + description + ";" + startDate + ";";
        String courseCode = "CRS-" + (dataStorage.getLastCourseCode() + 1);
        dataStorage.addNewCourseToHashMaps(courseCode, courseData);
        dataWriter.updateCoursesInfo();
        System.out.println("\nNew Course has been added");
        logger.addMessageToLogFile("User "+user.getName()+" added Course "+courseTitle);
        dataStorage.printCurrentCourseTable(courseCode);
    }

    private void makeChangesToCourseTable(User user) {
        System.out.println("Do you want to make changes? \n1. yes   2. no");
        try {
            switch (Integer.parseInt(sc.next())) {
                case 1:
                    coursesMenu(user);
                    break;
                case 2:
                    break;
                default:
                    System.out.println("\nWrong input\n");
                    break;
            }
        } catch (Exception e) {
            System.out.println("\nWrong number format\n");
        }
    }

    private void addNewUser(User user) {
        System.out.print("Username: ");
        String username = sc.next();
        if (!dataStorage.isUsernameUnique(username)) {
            System.out.println("This username is already taken. Please choose another username");
            addNewUser(user);
            return;
        }
        System.out.print("Password: ");
        String password = sc.next();
        System.out.print("First name: ");
        sc.nextLine();
        String firstName = sc.nextLine();
        firstName=firstName.replace(";",",");
        System.out.print("Last name: ");
        String lastName = sc.nextLine();
        lastName=lastName.replace(";",",");
        System.out.println("Choose User role: \n1.admin 2. lecturer 3. student");
        String role = sc.next();
        try {
            switch (Integer.parseInt(role)) {
                case 1:
                    role = "ADM";
                    break;
                case 2:
                    role = "LEC";
                    break;
                case 3:
                    role = "STU";
                    break;
                default:
                    System.out.println("\nWrong input! Default User role - Student");
                    role = "STU";
                    break;
            }
        } catch (Exception e) {
            role = "STU";
            System.out.println(e);
        }
        dataStorage.setLastID();
        String ID = role + "-" + (dataStorage.getLastID() + 1);
        String loginData = username + ";" + password + ";";
        String userPropertiesData = firstName + ";" + lastName + "; ; ; ; ; ;";
        dataStorage.addNewUserToHashMaps(ID, loginData, userPropertiesData);
        dataWriter.updateUserFiles();
        System.out.println("Do you want to add User properties? \n1. yes 2.no");
        makeMoreChanges(user, ID);
    }

    private void runLecturerMenu(User user) {
        System.out.println("1. Edit profile");
        System.out.println("2. My Courses");
        System.out.println("3. My Students");
        System.out.println("4. Add new Course");
        System.out.println("5. Exit");
        try {
            String userInput = sc.next();
            switch (Integer.parseInt(userInput)) {
                case 1:
                    editUserPropertiesMenu(user, user.getID());
                    user.setUserProperties(user.getID(), dataStorage.getUserInfoByID(user.getID()));
                    break;
                case 2:
                    dataStorage.printLecturersCoursesTable(user.getID());
                    makeChangesToCourseTable(user);
                    break;
                case 3:
                    dataStorage.printLecturersStudents(user.getID());
                    break;
                case 4:
                    addNewCourse(user, user.getID());
                    break;
                case 5:
                    app.close();
                    runApp = false;
                    break;
                default:
                    System.out.println("\nWrong input\n");
                    break;
            }
        } catch (Exception e) {
            System.out.println("\nWrong number format\n");
        }

    }

    private void runStudentMenu(User user) {
        System.out.println("1. Edit profile");
        System.out.println("2. My Courses");
        System.out.println("3. View all Courses");
        System.out.println("4. Enroll in a New Course");

        System.out.println("5. Exit");
        try {
            String userInput = sc.next();
            switch (Integer.parseInt(userInput)) {
                case 1:
                    editUserPropertiesMenu(user, user.getID());
                    user.setUserProperties(user.getID(), dataStorage.getUserInfoByID(user.getID()));
                    break;
                case 2:
                    dataStorage.printStudentsCoursesTable(user.getID());

                    break;
                case 3:
                    dataStorage.printCoursesTable();
                    break;
                case 4:
                    int totalNumberOfCredits = dataStorage.countStudentsCredits(user.getID());
                    int allowedCredits = 12 - totalNumberOfCredits;
                    if (allowedCredits <= 0) {
                        System.out.println("You can't enroll in any Course. You have " + allowedCredits + " Credits left.");
                        leaveCourse(user);
                    } else {
                        if (dataStorage.countAllowedCourses(user.getID(), allowedCredits) > 0) {
                            dataStorage.printAllowedCoursesTable();
                            System.out.println("\nYou have " + allowedCredits + " Credits left.\nChoose course (Enter COURSE CODE): ");
                            String courseCode = sc.next();
                            courseCode = courseCode.toUpperCase();
                            if (dataStorage.getCoursesInfo().containsKey(courseCode)) {
                                if (dataStorage.getAllowedCourses().contains(courseCode)) {
                                    String studentCourse = user.getID() + ";" + courseCode + ";";
                                    dataStorage.addStudentsCourse(studentCourse);
                                    dataWriter.updateStudentCourses();
                                } else {
                                    System.out.println("You can't enroll in this course");
                                }
                            } else {
                                System.out.println("Course " + courseCode + " doesn't exist");
                            }
                        } else {
                            System.out.println("There are no Courses for you right now");
                        }
                    }
                    break;
                case 5:
                    app.close();
                    runApp = false;
                    break;
                default:
                    System.out.println("\nWrong input\n");
                    break;
            }
        } catch (Exception e) {
            System.out.println("\nWrong number format\n");
        }
    }

    private void leaveCourse(User user) {
        System.out.println("Do you want to leave any of your Courses?\n1. yes   2. no");
        try {
            String userInput = sc.next();
            switch (Integer.parseInt(userInput)) {
                case 1:
                    dataStorage.printStudentsCoursesTable(user.getID());
                    System.out.print("Choose course (enter COURSE CODE): ");
                    String courseCode = sc.next();
                    courseCode = courseCode.toUpperCase();
                    if (dataStorage.getCoursesInfo().containsKey(courseCode)) {
                        String studentsCourse = user.getID() + ";" + courseCode + ";";
                        dataStorage.deleteStudentsCourse(studentsCourse);
                        dataWriter.updateStudentCourses();
                        String courseTitle = dataStorage.getCoursesInfo().get(courseCode).split(";")[2];
                        System.out.println("You left " + courseTitle + " Course");
                        logger.addMessageToLogFile("User "+user.getName()+" left Course "+courseTitle);
                    } else {
                        System.out.println("Course " + courseCode + " doesn't exist");
                    }
                    break;
                case 2:
                    break;
                default:
                    System.out.println("\nWrong input\n");
                    break;
            }
        } catch (Exception e) {
            System.out.println("\nWrong number format\n");
        }
    }

    private void userTableMenu(User user) {
        System.out.println("Choose User (enter USER ID)");
        String userID = sc.next();
        userID = userID.toUpperCase();
        if (dataStorage.getUserProperties().containsKey(userID)) {
            System.out.println("Chosen User: " + (dataStorage.getUserProperties().get(userID).split(";")[0]) + " " + (dataStorage.getUserProperties().get(userID).split(";")[1]));
            System.out.println("1. Change User Properties \n2. Delete User");
            try {
                String userInput = sc.next();
                switch (Integer.parseInt(userInput)) {
                    case 1:
                        editUserPropertiesMenu(user, userID);
                        break;
                    case 2:
                        if (!user.getID().equals(userID)) {
                            dataStorage.deleteUser(userID);
                            System.out.println("\nUser has been deleted\n");
                            dataWriter.updateStudentCourses();
                            dataWriter.updateCoursesInfo();
                        } else {
                            System.out.println("\nYou cannot delete yourself!\n");
                        }
                        break;
                    default:
                        System.out.println("\nWrong input\n");
                        break;
                }
            } catch (Exception e) {
                System.out.println("\nWrong number format\n");
            }
        } else {
            System.out.println("\nUser " + userID + " doesn't exist\n");
        }
        dataWriter.updateUserFiles();
    }

    private void editUserPropertiesMenu(User user, String userID) {
        dataStorage.printCurrentUsersTable(userID);
        String username = dataStorage.getUserInfoByID(userID)[0][0];
        String password = dataStorage.getUserInfoByID(userID)[0][1];
        String oldPassword = password;
        String firstName = dataStorage.getUserInfoByID(userID)[1][0];
        String lastName = dataStorage.getUserInfoByID(userID)[1][1];
        String dateOfBirth = dataStorage.getUserInfoByID(userID)[1][2];
        String email = dataStorage.getUserInfoByID(userID)[1][3];
        String mobileNumber = dataStorage.getUserInfoByID(userID)[1][4];
        String gender = dataStorage.getUserInfoByID(userID)[1][5];
        String address = dataStorage.getUserInfoByID(userID)[1][6];

        System.out.println("\nChoose what you want to change:");
        System.out.println("1. username   2. password   3. first name   4. last name   5. date of birth   6. email address   7. mobile number   8. gender  9. address");
        System.out.println("\n10. Back to main menu");
        try {
            String userInput = sc.next();
            switch (Integer.parseInt(userInput)) {
                case 1:
                    System.out.println("Username: " + username);
                    System.out.print("Enter new username:  ");
                    String newUsername = sc.next();
                    if (dataStorage.isUsernameUnique(newUsername)) {
                        username = newUsername;
                        System.out.println("\nUsername has been changed successfully.");
                        logger.addMessageToLogFile("User "+user.getName()+" changed User's "+firstName+" "+lastName+" username");
                    } else {
                        System.out.println("\nThis username is already taken. Please choose another username.");
                    }
                    break;
                case 2:
                    if (user.getID().equals(userID)) {
                        System.out.print("Enter old password: ");
                        oldPassword = sc.next();
                    }
                    System.out.print("Enter new password: ");
                    String newPassword = sc.next();
                    System.out.print("Confirm new password: ");
                    String confirmedPassword = sc.next();
                    if ((oldPassword.equals(password)) && (newPassword.equals(confirmedPassword))) {
                        password = newPassword;
                        System.out.println("Password has been changed successfully.");
                        logger.addMessageToLogFile("User "+user.getName()+" changed User's "+firstName+" "+lastName+" password");
                    } else if ((oldPassword.equals(password)) && (!newPassword.equals(confirmedPassword))) {
                        System.out.println("\nPassword confirmation doesn't match Password. Please try again. ");
                    } else {
                        System.out.println("\nSomething went wrong. Please try again. ");
                    }
                    break;
                case 3:
                    System.out.println("First name: " + firstName);
                    System.out.print("Enter first name: ");
                    sc.nextLine();
                    firstName = sc.nextLine().replaceAll(";", ",");
                    System.out.println("\nFirst name has been changed successfully.");
                    logger.addMessageToLogFile("User "+user.getName()+" changed User's "+firstName+" "+lastName+" first name");
                    break;
                case 4:
                    System.out.println("Last name: " + lastName);
                    System.out.print("Enter last name: ");
                    sc.nextLine();
                    lastName = sc.nextLine().replaceAll(";", ",");
                    System.out.println("\nLast name has been changed successfully.");
                    logger.addMessageToLogFile("User "+user.getName()+" changed User's "+firstName+" "+lastName+" last name");
                    break;
                case 5:
                    System.out.println("Date of Birth: " + dateOfBirth);
                    System.out.print("Enter date of Birth (yyyy-mm-dd): ");
                    try {
                        LocalDate checkDate = LocalDate.parse(sc.next(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        dateOfBirth = String.valueOf(checkDate);
                        System.out.println("\nDate of Birth has been changed successfully.");
                        logger.addMessageToLogFile("User "+user.getName()+" changed User's "+firstName+" "+lastName+" date of birth");
                    } catch (Exception e) {
                        System.out.println("\nWrong date format. Please try again\n");
                    }
                    break;
                case 6:
                    System.out.println("Email address: " + email);
                    System.out.print("Enter email address: ");
                    sc.nextLine();
                    email = sc.nextLine().replaceAll(";", ",");
                    System.out.println("\nEmail address has been changed successfully.");
                    logger.addMessageToLogFile("User "+user.getName()+" changed User's "+firstName+" "+lastName+" email address");
                    break;
                case 7:
                    System.out.println("Mobile number: " + mobileNumber);
                    System.out.print("Enter mobile number: ");
                    sc.nextLine();
                    mobileNumber = sc.nextLine().replaceAll(";", ",");
                    System.out.println("\nMobile number has been changed successfully.");
                    logger.addMessageToLogFile("User "+user.getName()+" changed User's "+firstName+" "+lastName+" mobile number");
                    break;
                case 8:
                    System.out.println("Gender: " + gender);
                    System.out.println("Choose gender: \n0. -- \n1. male\n2. female");
                    try {
                        switch (sc.nextInt()) {
                            case 0:
                                gender = "";
                                System.out.println("\nGender has been changed successfully.");
                                logger.addMessageToLogFile("User "+user.getName()+" removed User's "+firstName+" "+lastName+" gender");
                                break;
                            case 1:
                                gender = "male";
                                System.out.println("\nGender has been changed successfully.");
                                logger.addMessageToLogFile("User "+user.getName()+" changed User's "+firstName+" "+lastName+" gender");
                                break;
                            case 2:
                                gender = "female";
                                System.out.println("\nGender has been changed successfully.");
                                logger.addMessageToLogFile("User "+user.getName()+" changed User's "+firstName+" "+lastName+" gender");
                                break;
                            default:
                                System.out.println("\nWrong input\n");
                                break;
                        }

                    } catch (Exception e) {
                        System.out.println("\nWrong number format\n");
                    }

                    break;
                case 9:
                    System.out.println("Address: " + address);
                    System.out.println("Enter address: ");
                    sc.nextLine();
                    address = sc.nextLine().replaceAll(";", ",");
                    System.out.println("\nAddress has been changed successfully.");
                    logger.addMessageToLogFile("User "+user.getName()+" changed User's "+firstName+" "+lastName+" address");
                    break;
                case 10:
                    return;
                default:
                    System.out.println("\nWrong input\n");
                    return;
            }
        } catch (Exception e) {
            System.out.println("\nWrong number format\n");
            return;
        }
        String userPropertiesToFile = (firstName + ";" + lastName + ";" + dateOfBirth + ";" + email + ";" + mobileNumber + ";" + gender + ";" + address + ";");
        String userLoginInfoToFile = (username + ";" + password + ";");
        dataStorage.updateUserInfoHashMap(userID, userPropertiesToFile, userLoginInfoToFile);
        dataWriter.updateUserFiles();
        System.out.println("\nDo you want to make more changes? \n1. yes   2. no");
        makeMoreChanges(user, userID);
    }

    private void makeMoreChanges(User user, String userID) {
        try {
            switch (Integer.parseInt(sc.next())) {
                case 1:
                    editUserPropertiesMenu(user, userID);
                    break;
                case 2:
                    break;
                default:
                    System.out.println("\nWrong input\n");
                    break;
            }
        } catch (Exception e) {
            System.out.println("\nWrong number format\n");
        }
    }

    private void coursesMenu(User user) {
        System.out.println("Choose Course (enter COURSE CODE)");
        String courseCode = sc.next();
        courseCode = courseCode.toUpperCase();
        if (dataStorage.getCoursesInfo().containsKey(courseCode)) {
            String courseTitle = dataStorage.getCoursesInfo().get(courseCode).split(";")[2];
            System.out.println("Chosen Course: " + courseTitle);
            System.out.println("1. Edit Course \n2. Delete Course");
            try {
                String userInput = sc.next();
                switch (Integer.parseInt(userInput)) {
                    case 1:
                        if ((user.getID().equals(dataStorage.getCoursesInfo().get(courseCode).split(";")[0])) || (user.getID().split("-")[0].equals("ADM"))) {
                            editCourseMenu(user, courseCode);
                        } else {
                            System.out.println("\nCannot edit Course. You need permission to perform this action\n");
                        }
                        break;
                    case 2:
                        if ((user.getID().equals(dataStorage.getCoursesInfo().get(courseCode).split(";")[0])) || (user.getID().split("-")[0].equals("ADM"))) {
                            dataStorage.getCoursesInfo().remove(courseCode);
                            System.out.println("Course \"" + courseTitle + "\" has been deleted");
                            logger.addMessageToLogFile("User "+user.getName()+" deleted Course "+courseTitle);

                        } else {
                            System.out.println("\nCannot delete Course. You need permission to perform this action\n");
                        }
                        dataWriter.updateCoursesInfo();
                        break;
                    default:
                        System.out.println("\nWrong input\n");
                        break;
                }
            } catch (Exception e) {
                System.out.println("\nWrong number format\n");
            }
        } else {
            System.out.println("Course " + courseCode + " doesn't exist");
        }
    }

    private void editCourseMenu(User user, String courseCode) {
        dataStorage.printCurrentCourseTable(courseCode);
        String lecID = dataStorage.getCurrentCourseInfo(courseCode)[0];
        int credit = 2;
        try {
            credit = Integer.parseInt(dataStorage.getCurrentCourseInfo(courseCode)[1]);
        } catch (Exception e) {
            System.out.println("Wrong number format");
        }
        String courseTitle = dataStorage.getCurrentCourseInfo(courseCode)[2];
        String description = dataStorage.getCurrentCourseInfo(courseCode)[3];
        String startDate = dataStorage.getCurrentCourseInfo(courseCode)[4];
        System.out.println("\nChoose what you want to change:");
        System.out.println("1. lecturer (admin-only)   2. credit   3. title   4. description   5. start date");
        System.out.println("\n6. Back to main menu");
        try {
            String userInput = sc.next();
            switch (Integer.parseInt(userInput)) {
                case 1:
                    System.out.println("Lecturer: " + (dataStorage.getUserProperties().get(lecID).split(";")[0]) + " " + (dataStorage.getUserProperties().get(lecID).split(";")[1]) + "\n");
                    if (user.getID().split("-")[0].equals("ADM")) {
                        dataStorage.printAllLecturers();
                        System.out.print("Choose Lecturer (enter USER ID): ");
                        lecID = sc.next();
                        System.out.println("\nLecturer has been changed successfully.\n");
                        logger.addMessageToLogFile("User "+user.getName()+" changed Course "+courseTitle+" lecturer");
                    } else {
                        System.out.println("Cannot change Lecturer. You need permission to perform this action. ");
                    }
                    break;
                case 2:
                    System.out.println("Number of credints: " + credit);
                    System.out.print("Enter Nubmer of Credits (1-5): ");
                    try {
                        credit = Integer.parseInt(sc.next());
                        if(credit>0&&credit<5){


                        System.out.println("\nNumber of Credits has been changed successfully.\n");
                        logger.addMessageToLogFile("User "+user.getName()+" changed Course "+courseTitle+" credits");}else {
                            System.out.println(credit+" is not allowed Amount of Credit");
                        }
                    } catch (Exception e) {
                        System.out.println("\nWrong number format\n");
                    }
                    break;
                case 3:
                    System.out.println("Course Title: " + courseTitle);
                    System.out.print("Enter Course Title: ");
                    sc.nextLine();
                    courseTitle = sc.nextLine();
                    courseTitle=courseTitle.replace(";",",");
                    System.out.println("\nCourse Title has been changed successfully.\n");
                    logger.addMessageToLogFile("User "+user.getName()+" changed Course "+courseTitle+" title");
                    break;
                case 4:
                    System.out.println("Course Description: " + description);
                    System.out.print("Enter Course Description: ");
                    sc.nextLine();
                    description = sc.nextLine();
                    description=description.replace(";",",");
                    System.out.println("\nDescription has been changed successfully.\n");
                    logger.addMessageToLogFile("User "+user.getName()+" changed Course "+courseTitle+" description");
                    break;
                case 5:
                    System.out.println("Start date: " + startDate);
                    System.out.print("Enter Start Date (yyyy-mm-dd): ");
                    try {
                        LocalDate checkStartDate = LocalDate.parse(sc.next(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        startDate = String.valueOf(checkStartDate);
                        System.out.println("\nStart Date has been changed successfully.\n");
                        logger.addMessageToLogFile("User "+user.getName()+" changed Course "+courseTitle+" start date");
                    } catch (Exception e) {
                        System.out.println("Wrong date format");
                    }
                    break;
                case 6:
                    return;
                default:
                    System.out.println("\nWrong input\n");
                    return;
            }
        } catch (Exception e) {
            System.out.println("\nWrong number format\n");
        }
        String courseData = lecID + ";" + String.valueOf(credit) + ";" + courseTitle + ";" + description + ";" + startDate + ";";
        dataStorage.changeCourseInfoHashMap(courseCode, courseData);
        dataWriter.updateCoursesInfo();
        System.out.println("Do you want to change something else?\n1. yes   2. no");
        try {
            switch (Integer.parseInt(sc.next())) {
                case 1:
                    editCourseMenu(user, courseCode);
                    break;
                case 2:
                    break;
                default:
                    System.out.println("\nWrong input\n");
                    break;
            }
        } catch (Exception e) {
            System.out.println("\nWrong number format\n");
        }
    }
}

