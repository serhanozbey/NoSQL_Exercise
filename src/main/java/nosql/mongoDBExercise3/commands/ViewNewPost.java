package nosql.mongoDBExercise3.commands;

import nosql.mongoDBExercise3.Dao;
import nosql.mongoDBExercise3.Main;

public class ViewNewPost implements View {
    @Override
    public void execute() {
        Dao.savePost(Main.getUser());
    }
}
