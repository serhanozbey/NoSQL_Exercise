package nosql.mongoDBExercise3.commands;

import nosql.mongoDBExercise3.Dao;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Scanner;

public class ViewPosts implements View {
    
    @Override
    public void execute() {
        while (true) {
            System.out.println("Enter the post number if you want to comment.");
            Dao.printAllPosts();
            ViewAddComment view = new ViewAddComment();
            view.execute();
            if (!view.isAdded()) break;
        }
    }
}
