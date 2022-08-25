package contenttypes;

import utils.Controller;

import java.util.ArrayList;

public class Tasklist {
    private final int listID;
    private String name;
    private String description;
    private ArrayList<String> tags;
    private ArrayList<Task> tasks;
    private int project;

    public Tasklist(int id, String name, String description, ArrayList<String> tags) {
        this.listID = id;
        this.name = name;
        this.description = description;
        this.tags = tags;
        this.tasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void batchAddTasks (String tasks){
        String[] splitTasks = tasks.lines().toArray(String[]::new);
        for (String task:splitTasks){
            if (task.trim().isBlank()||task.isEmpty()){
                continue;
            }

            Task batchedTask = new Task((Controller.getInstance()).generateID("task"), task, "", new ArrayList<>(), listID);
            addTask(batchedTask);
            Controller.getInstance().addTask(batchedTask);
        }
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return listID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public void setProject(int project) {
        this.project = project;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public int getProject() {
        return project;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getAll(){
        StringBuilder line = new StringBuilder();
        StringBuilder tagString = new StringBuilder();
        StringBuilder taskString = new StringBuilder();

        line.append(listID +",");
        line.append("\""+name+"\""+",");
        line.append("\""+description+"\""+",");

        for (String tag:tags){
            tagString.append(tag+";");
        }
        line.append("\""+tagString+"\""+",");

        if (tasks!=null){
            for (Task task:tasks){
                taskString.append(task.getID()+";");
            }
            line.append("\""+taskString+"\""+",");
        } else {
            line.append("\"\""+",");
        }


        line.append(project+",");

        line.append("\n");

        return line.toString();
    }
}
