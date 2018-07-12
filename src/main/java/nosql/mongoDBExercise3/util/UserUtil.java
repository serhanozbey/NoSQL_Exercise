package nosql.mongoDBExercise3.util;

import nosql.mongoDBExercise3.dao.UserDaoImpl;
import nosql.mongoDBExercise3.model.User;
import org.apache.commons.validator.routines.EmailValidator;
import org.mongodb.morphia.query.Query;

import java.util.Scanner;

public class UserUtil {
    
    //USER
    
    //User - login
    public static User login() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nEnter username");
        String username = scanner.nextLine();
        System.out.println("Enter password");
        //TODO: Following to be added for production, as intellij starts Java from javaw instead javac: String password = new String(console.readPassword("password: "));
        String password = scanner.nextLine();
        if (userValidation(username,password)) {
            return new UserDaoImpl().get(username);
        }
        return null;
    }
    
    //User - username-password validation
    public static boolean userValidation(String username, String password) {
        Query<User> q = MorphiaUtil.getInstance().getDatastore().find(User.class);
        if (q.field("username").equal(username).count() != 0) {
            String stored_hash = q.project("password", true).get().password;
            if (PassHasher.checkPassword(password, stored_hash)) {
                return true;
            }
        }
        return false;
    }
    
    //User - registration
    public static void register() {
        User userNew;
        String username,password,email;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("Enter username");
                username = scanner.nextLine();
                System.out.println("Enter password");
                password = scanner.nextLine().trim();
                break;
            } catch (Exception e) {
                System.out.println("wrong input, try again");
            }
        }
        while (true) {
            try {
                System.out.println("Enter email");
                email = scanner.nextLine();
                if (!EmailValidator.getInstance().isValid(email)) throw new Exception();
                break;
            } catch (Exception e) {
                System.out.println("wrong input, try again");
            }
        }
        userNew = new User(username, password, email);
        new UserDaoImpl().add(userNew);
        System.out.println("Successfully registered.");
    }
    
   
}
