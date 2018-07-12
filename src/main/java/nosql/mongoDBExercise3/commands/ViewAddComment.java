package nosql.mongoDBExercise3.commands;

import nosql.mongoDBExercise3.controller.Main;
import nosql.mongoDBExercise3.model.Post;
import nosql.mongoDBExercise3.util.CommentUtil;
import nosql.mongoDBExercise3.util.MorphiaUtil;
import nosql.mongoDBExercise3.util.PostUtil;

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
                PostUtil.printAllPosts();
                CommentUtil.saveComment(Main.getMain().getUser(), MorphiaUtil.getInstance().getMorphia().fromDBObject(MorphiaUtil.getInstance().getDatastore(), Post.class, PostUtil.getAllPosts().get(selection - 1)));
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
