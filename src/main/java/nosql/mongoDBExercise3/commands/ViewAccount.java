package nosql.mongoDBExercise3.commands;

import nosql.mongoDBExercise3.controller.Main;

public class ViewAccount implements View {
    @Override
    public void execute() {
        System.out.println("Account details:");
        System.out.println(Main.getMain().getUser()+"\n");
    }
}
