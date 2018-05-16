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
        System.out.println("Welcome to the NoSQL user-post-comment");
        command = new ViewNull();
    }
    
    public static void main(String[] args) {
        start();
        while (true) {
            mainMenu();
        }
    }
    
    private static void mainMenu() throws InputMismatchException {
        if(user==null) start();
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
    
    private static void start() {
        user = Dao.saveOrGetUser();
        mainMenu();
    }
    
    public static void setCommand(View command) {
        Main.command = command;
    }
    
    public static User getUser() {
        return user;
    }
}
