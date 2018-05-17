package nosql.mongoDBExercise3;

import nosql.mongoDBExercise3.commands.*;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    
    private static View command;
    private static User user;
    private static Scanner scanner = new Scanner(System.in);
    
    //TODO: Change the command pattern (state pattern?)
    //TODO: CLI program lifecycle
    
    static {
        command = new ViewNull();
    }
    
    public static void main(String[] args) {
        
        while (true) {
            mainMenu();
        }
    }
    
    private static void mainMenu() throws InputMismatchException {
        System.out.println("Welcome to the NoSQL user-post-comment");
        if (user==null&&!login()) {
            return;
        }
        System.out.println("\nMAIN MENU\n");
        System.out.println("\nSelect an operation: ");
        System.out.println("1- View Posts");
        System.out.println("2- View Posts and Comments");
        System.out.println("3- View Account");
        System.out.println("4- New Post");
        System.out.println("5- Logout");
    
        //TODO: Input snippet
        int selection;
        while (true) {
            scanner = new Scanner(System.in);
            try {
                selection = scanner.nextInt();
                break;
            } catch (Exception e) {
                System.out.println("wrong input");
            }
        }
        switch (selection) {
            case 1:
                setCommand(new ViewPosts());
                break;
            case 2:
                setCommand(new ViewPostsAndComments());
                break;
            case 3:
                setCommand(new ViewAccount());
                break;
            case 4:
                setCommand(new ViewNewPost());
                break;
            case 5:
                user = null;
                break;
        }
        command.execute();
    }
    
    private static boolean login() {
        System.out.println("\nLOGIN MENU");
        user = Dao.loadUser();
        if (user == null) {
            System.out.println("User not found. Register? (y/n)");
            Scanner scanner = new Scanner(System.in);
            String selection = scanner.nextLine();
            if (selection.equals("y")) {
                Dao.registerUser();
            } else {
                System.out.println("Registration aborted.");
            }
            return false;
        } else {
            return true;
        }
    }
    
    public static void setCommand(View command) {
        Main.command = command;
    }
    
    public static User getUser() {
        return user;
    }
}
