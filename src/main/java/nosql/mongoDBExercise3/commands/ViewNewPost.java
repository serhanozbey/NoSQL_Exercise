package nosql.mongoDBExercise3.commands;

import nosql.mongoDBExercise3.controller.Main;
import nosql.mongoDBExercise3.util.PostUtil;

public class ViewNewPost implements View {
    @Override
    public void execute() {
        PostUtil.savePost(Main.getMain().getUser());
    }
}
