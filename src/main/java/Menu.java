import Users.Admin;
import Users.Lecturer;
import Users.Student;
import Users.User;

import java.util.Scanner;

public class Menu {
    boolean runApp = true;
    Login app = new Login();
    DataWriter dataWriter = new DataWriter();
    DataStorage dataStorage = new DataStorage();
    Scanner sc = new Scanner(System.in);

    public void runWelcomeMenu() throws Exception {
        System.out.println("Welcome to MIVS");
        while (runApp && app.isLoginDataValid() == false) {
            System.out.println("1. Sign in");
            System.out.println("2. Exit");
            try {
                String userInput = sc.next();
                switch (Integer.parseInt(userInput)) {
                    case 1:
                        if (!app.isLoginDataValid()) {
                            app.login();
                        }
                        break;
                    case 2:
                        app.close();
                        runApp = false;
                        break;
                    default:
                        System.out.println("Wrong input");

                }
            } catch (Exception e) {
                System.out.println("\nWrong number format\n");
            }
        }
    }

    public void runUserMenu(User user) {

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


    private void runAdminMenu(User user) {
        System.out.println("1. Edit profile");
        System.out.println("2. View all Courses");
        // View info / Edit / Delete
        System.out.println("3. View all Users");
        // [firstName, lastName], [email], [role]
        System.out.println("4. Add new User");
        System.out.println("5. Add new Course");
        System.out.println("6. Exit");
        try {
            String userInput = sc.next();
            switch (Integer.parseInt(userInput)) {
                case 1:
                    editProfileMenu(user);
                    //Edit profile menu
                    break;
                case 2:
                    dataStorage.printCoursesTable();
                    coursesMenu();
                    break;
                case 3:
                    dataStorage.printUsersTable();
                    userTableMenu(user);
                    break;
                case 4:
                    System.out.println("Enter username");
                    System.out.println("Enter password");
                    System.out.println("Enter first name");
                    System.out.println("Enter last name");
                    dataStorage.setLastID();
                    System.out.println(dataStorage.getLastID() + 1);
                case 6:
                    app.close();
                    runApp = false;
                    break;
                default:
                    System.out.println("\nWrong input\n");
//                    runUserMenu(user);
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
                        editUserPropertiesMenu(userID);
                        break;
                    case 2:
                        if (!user.getID().equals(userID)) {
                            dataStorage.getUserProperties().remove(userID);
                            dataStorage.getLoginInfo().remove(userID);
                        } else {
                            System.out.println("You cannot delete yourself!");
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
            System.out.println("Course ");
        }
        dataWriter.updateCoursesInfo();
    }

    private void editUserPropertiesMenu(String userID) {
    }

    private void coursesMenu() {
        System.out.println("Choose Course (enter COURSE CODE)");
        String courseCode = sc.next();
        courseCode = courseCode.toUpperCase();
        if (dataStorage.getCoursesInfo().containsKey(courseCode)) {
            System.out.println("Chosen Course: " + dataStorage.getCoursesInfo().get(courseCode).split(";")[2]);
            System.out.println("1. Edit Course \n2. Delete Course");
            try {
                String userInput = sc.next();
                switch (Integer.parseInt(userInput)) {
                    case 1:
                        editCourseMenu(courseCode);
                        break;
                    case 2:
                        dataStorage.getCoursesInfo().remove(courseCode);
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
        dataWriter.updateCoursesInfo();
    }

    private void editCourseMenu(String courseCode) {
        //lecID;Credit;Title;Description;StartDate;
        System.out.println("\nChoose what you want to change:");
        System.out.println("1. lecturer   2. credit   3. title   4. description   5. start date");
        System.out.println("\n6. Back to main menu");
        try {
            String userInput = sc.next();
            switch (Integer.parseInt(userInput)) {
                case 1:
dataStorage.
                    break;
                case 2:

                    break;
                case 3:

                    break;
                case 4:

                    break;
                case 5:

                    break;
                case 6:

                    break;
                default:
                    System.out.println("\nWrong input\n");
                    break;
            }
        } catch (Exception e) {
            System.out.println("\nWrong number format\n");
        }
    }

    private void runLecturerMenu(User user) {
        System.out.println("1. Edit profile");
        System.out.println("2. My Courses");
        // View info / Edit / Delete
        System.out.println("3. My Students");
        System.out.println("4. Add new Course");
        System.out.println("5. Exit");
        try {
            String userInput = sc.next();
            switch (Integer.parseInt(userInput)) {
                case 1:
                    editProfileMenu(user);
                    break;
                case 2:
                    dataStorage.printLecturersCoursesTable(user.getID());
                    break;
                case 5:
                    app.close();
                    runApp = false;
                    break;
                default:
                    System.out.println("\nWrong input\n");
                    runUserMenu(user);
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
        //Check TotalNumberOfCredits
        //Print only those Courses with right StartDate
        System.out.println("5. Exit");
        try {
            String userInput = sc.next();
            switch (Integer.parseInt(userInput)) {
                case 1:
                    editProfileMenu(user);
                    break;
                case 5:
                    app.close();
                    runApp = false;
                    break;
                default:
                    System.out.println("\nWrong input\n");
                    runUserMenu(user);
                    break;
            }
        } catch (Exception e) {
            System.out.println("\nWrong number format\n");
        }
    }


    private void editProfileMenu(User user) {
        System.out.println(user.toString());
        System.out.println("\nChoose what you want to change:");
        System.out.println("1. username   2. password   3. first name   4. last name   5. date of birth   6. email address   7. mobile number   8. gender  9. address");
        System.out.println("\n10. Back to main menu");
        try {
            String userInput = sc.next();
            switch (Integer.parseInt(userInput)) {
                case 1:
                    System.out.println("Enter new username");
                    String newUsername = sc.next();
                    if (dataStorage.isUsernameUnique(newUsername)) {
                        user.setUserName(newUsername);
                        System.out.println("Your username has been changed successfully! Thank you.");
                    } else {
                        System.out.println("This username is already taken. Please choose another username.");
                    }
                    break;
                case 2:
                    System.out.println("Enter old password");
                    String oldPassword = sc.next();
                    System.out.println("Enter new password");
                    String newPassword = sc.next();
                    System.out.println("Confirm new password");
                    String confirmedPassword = sc.next();
                    if ((oldPassword.equals(user.getPassword())) && (newPassword.equals(confirmedPassword))) {
                        user.setPassword(newPassword);
                        System.out.println("Your password has been changed successfully! Thank you.");
                    } else {
                        System.out.println("Something went wrong. Please try again");
                    }
                    break;
                case 3:
                    System.out.println("Enter your first name");
                    user.setFirstName(sc.next());
                    System.out.println("Your first name has been changed successfully! Thank you.");
                    break;
                case 4:
                    System.out.println("Enter your last name");
                    user.setLastName(sc.next());
                    System.out.println("Your last name has been changed successfully! Thank you.");
                    break;
                case 5:
                    System.out.println("Enter your date of birth (yyyy-mm-dd");
                    user.setDateOfBirth(sc.next());
                    System.out.println("Your date of birth has been changed successfully! Thank you.");
                    break;
                case 6:
                    System.out.println("Enter your email address");
                    user.setEmail(sc.next());
                    System.out.println("Your email address has been changed successfully! Thank you.");
                    break;
                case 7:
                    System.out.println("Enter your mobile number");
                    sc.nextLine();
                    user.setMobileNumber(sc.nextLine());
                    break;
                case 8:
                    System.out.println("Choose gender: \n0. -- \n1. male\n2. female");
                    try {
                        switch (sc.nextInt()) {
                            case 0:
                                user.setGender("");
                                break;
                            case 1:
                                user.setGender("male");
                                break;
                            case 2:
                                user.setGender("female");
                                break;
                            default:
                                System.out.println("\nWrong input\n");
                                break;
                        }

                    } catch (Exception e) {
                        System.out.println("\nWrong number format\n");
                    }
                    System.out.println("Your gender has been changed successfully! Thank you.");
                    break;
                case 9:
                    System.out.println("Enter your address");
                    sc.nextLine();
                    user.setAddress(sc.nextLine());
                    System.out.println("Your address has been changed successfully! Thank you.");
                    break;
                case 10:
                    break;
                default:
                    System.out.println("\nWrong input\n");
                    editProfileMenu(user);
                    break;
            }
        } catch (Exception e) {
            System.out.println("\nWrong number format\n");
        }
        chooseNextMenu(user, "editProfileMenu");
        dataWriter.updateFiles(user);
    }

    private void chooseNextMenu(User user, String methodName) {
        System.out.println("Want to change something else? \n1.Yes   2.No");
        try {
            switch (Integer.parseInt(sc.next())) {
                case 1:
                    if (methodName.equals("editProfileMenu")) {
                        editProfileMenu(user);
                    } else if (methodName.equals("userTableMenu")) {
                        userTableMenu(user);
                    } else {
                        runUserMenu(user);
                    }
                    break;
                case 2:
                    runUserMenu(user);
                    break;
                default:
                    System.out.println("\nWrong input\n");
                    break;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

