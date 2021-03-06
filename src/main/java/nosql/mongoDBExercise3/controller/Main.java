package nosql.mongoDBExercise3.controller;

import nosql.mongoDBExercise3.commands.*;
import nosql.mongoDBExercise3.model.User;
import nosql.mongoDBExercise3.util.UserUtil;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    
    //TODO: create scanner method, argument with what it says, and says enter sth according to that. if = is entered, returns null for quit.
    
    private View command;
    private User user;
    //singleton constructor start.
    private static Main main = new Main();
    
    private Main() {
        command = new ViewNull();
    }
    
    public static Main getMain() {
        return main;
    }
    //singleton constructor end.
    
    public static void main(String[] args) {
        Main CLI = getMain();
        while (true) {
            System.out.println("Welcome to the NoSQL user-post-comment");
            CLI.mainMenu();
        }
    }
    
    private void mainMenu() throws InputMismatchException {
        if (user==null&&!login()) {
            return;
        }
        System.out.println("\nMAIN MENU\n");
        System.out.println("Select an operation: ");
        System.out.println("1- View Posts");
        System.out.println("2- View Posts and Comments");
        System.out.println("3- View Account");
        System.out.println("4- New Post");
        System.out.println("5- Logout");
    
        //TODO: Better Input snippet to be generated.
        int selection;
        while (true) {
            Scanner scanner = new Scanner(System.in);
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
                return;
        }
        command.execute();
    }
    
    private boolean login() {
        System.out.println("\nLOGIN MENU");
        user = UserUtil.login();
        if (user == null) {
            String selection = getScanner("User not found. Register? (y/n)");
            if (selection.equals("y")) {
                UserUtil.register();
            } else {
                System.out.println("Registration aborted.");
            }
            return false;
        } else {
            return true;
        }
    }
    
    public void setCommand(View command) {
        this.command = command;
    }
    
    public User getUser() {
        return user;
    }
    
    public String getScanner(String message){
        System.out.println(message);
        String input = new Scanner(System.in).nextLine();
        if(input.equals("=")) return null;
        return input;
    }
}
