package nosql.mongoDBExercise3.commands;

import nosql.mongoDBExercise3.Dao;
import nosql.mongoDBExercise3.Main;
import nosql.mongoDBExercise3.Post;

import java.util.Scanner;

public class ViewAddComment implements View {
    
    private boolean isAdded;
    
    @Override
    public void execute() {
        System.out.println("To quit, press = and ENTER");
        int selection = 0;
        Scanner scanner;
        while (true) {
            scanner = new Scanner(System.in);
            try {
                String sel = scanner.nextLine();
                if (sel.equals("=")) return;
                selection = Integer.valueOf(sel);
                Dao.printAllPosts();
                Dao.savePostComment(Main.getUser(), Dao.getMorphia().fromDBObject(Dao.getDatastore(), Post.class, Dao.getAllPosts().get(selection - 1)));
                isAdded = true;
                break;
            } catch (Exception ex) {
                System.out.println("\nWrong input");
                System.out.println("To quit, type = and ENTER");
                
            }
        }
    }
    
    public boolean isAdded() {
        return isAdded;
    }
}
