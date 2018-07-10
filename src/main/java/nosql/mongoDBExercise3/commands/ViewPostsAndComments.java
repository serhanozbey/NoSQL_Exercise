package nosql.mongoDBExercise3.commands;


import nosql.mongoDBExercise3.util.PostUtil;

public class ViewPostsAndComments implements View {
    
    @Override
    public void execute() {
        while (true) {
            System.out.println("Enter the post number if you want to comment.");
            PostUtil.printAllPostsAndComments();
            ViewAddComment view = new ViewAddComment();
            view.execute();
            if(!view.isAdded()) break;
        }
        
    }
    
}

