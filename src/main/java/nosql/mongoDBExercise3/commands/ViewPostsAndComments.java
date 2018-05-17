package nosql.mongoDBExercise3.commands;

import nosql.mongoDBExercise3.Dao;
import nosql.mongoDBExercise3.Main;
import nosql.mongoDBExercise3.Post;

import java.util.InputMismatchException;
import java.util.Scanner;


public class ViewPostsAndComments implements View {
    
    @Override
    public void execute() {
        System.out.println("Enter the post number if you want to comment.");
        Dao.printAllPostsAndComments();
        System.out.println("To quit, type = and ENTER");
    
        int selection = 0;
        Scanner scanner;
        while (true) {
            scanner = new Scanner(System.in);
            try {
                String sel = scanner.next();
                if(sel.equals("=")) return;
                selection = Integer.valueOf(sel);
                Dao.printAllPosts();
                Dao.savePostComment(Main.getUser(), Dao.getMorphia().fromDBObject(Dao.getDatastore(), Post.class, Dao.getAllPosts().get(selection - 1)));
                break;
            } catch (Exception ex) {
                System.out.println("\nWrong input");
                System.out.println("To quit, type = and ENTER");
    
            }
        }
    }
}

