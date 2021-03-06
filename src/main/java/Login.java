import Users.Admin;
import Users.Lecturer;
import Users.Student;
import Users.User;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class Login {
    DataStorage dataStorage = new DataStorage();
    private boolean foundUser;
    private boolean loginDataValid;
    LoggerMIVS logger = new LoggerMIVS();

    public void setFoundUser(boolean foundUser) {
        this.foundUser = foundUser;
    }

    public boolean isLoginDataValid() {
        return loginDataValid;
    }

    public void setLoginDataValid(boolean loginDataValid) {
        this.loginDataValid = loginDataValid;
    }

    public void close() {
        this.loginDataValid = false;
    }

    public void login() {
        String userID = "";
        dataStorage.storeLoginInfo();

        Scanner sc = new Scanner(System.in);
        int wrongUsername = 0;
        //
        int wrongPassword = 0;
        while (!foundUser && wrongUsername < 3) {
            System.out.println("Enter login");
            String login = sc.next();
            for (String ID : dataStorage.getLoginInfo().keySet()) {
                String[] loginInfo = dataStorage.getLoginInfo().get(ID).split(";");
                for (int i = 0; i < loginInfo.length; i++) {
                    if (loginInfo[0].equals(login)) {
                        foundUser = true;
                        System.out.println("Enter password");
                        String password = sc.next();
                        if (loginInfo[1].equals(password)) {
                            setLoginDataValid(true);
                            userID = ID;
                            break;
                        } else {
//                            wrongUsername++;
                            int wrongInput = 0;
                            while ((wrongInput < 3) || (loginDataValid)) {
                                System.out.println("Incorrect password. \n\nPlease try again.");
                                if (loginInfo[1].equals(sc.next())) {
                                    setLoginDataValid(true);
                                    userID = ID;
                                    break;
                                } else {
                                    wrongInput++;
                                    break;
                                }
                            }
                            if (!loginDataValid) {
                                System.out.println("Wrong username. \n\nPlease try again after 5 sec.");
                                setFoundUser(false);

                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                return;
                            }
                        }
                    }
                }}
        }
         if (!foundUser) {
            System.out.println("Wrong username. \n\nPlease try again.\n");
            wrongUsername++;
        }
        if (loginDataValid) {
            loggedUser(userID);
        }
    }


    private void loggedUser(String userID) {
        String[] userRole = userID.split("-");
        User user = null;
        switch (userRole[0]) {
            case "ADM":
                user = new Admin();
                break;
            case "LEC":
                user = new Lecturer();
                break;
            case "STU":
                user = new Student();
                break;
            default:
                break;
        }
        dataStorage.getUserInfoByID(userID);
        user.setUserProperties(userID, dataStorage.getUserInfoByID(userID));
        logger.addMessageToLogFile("User "+user.getName()+" logged in");
        user.printUserTable();
        Menu menu = new Menu();
        menu.runUserMenu(user);
    }

}
