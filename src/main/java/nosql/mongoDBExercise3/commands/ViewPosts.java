package nosql.mongoDBExercise3.commands;

import nosql.mongoDBExercise3.Dao;

public class ViewPosts implements View {
    
    @Override
    public void execute() {
        System.out.println("Enter the post number if you want to comment.");
        Dao.printAllPosts();
        System.out.println("To quit, press escape\n");
        
    }
}
