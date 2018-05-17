package nosql.mongoDBExercise3.commands;

import nosql.mongoDBExercise3.Dao;
import nosql.mongoDBExercise3.Main;
import nosql.mongoDBExercise3.Post;

import java.util.InputMismatchException;
import java.util.Scanner;


public class ViewPostsAndComments implements View {
    
    @Override
    public void execute() {
        while (true) {
            System.out.println("Enter the post number if you want to comment.");
            Dao.printAllPostsAndComments();
            ViewAddComment view = new ViewAddComment();
            view.execute();
            if(!view.isAdded()) break;
        }
        
    }
    
}

