package contenttypes;

import java.util.ArrayList;

public class Task {
    private int taskID;
    private String name;
    private String description;
    private ArrayList<String> tags;
    private int tasklist;

    public Task(int id, String name, String description, ArrayList<String> tags, int tasklist) {
        this.taskID = id;
        this.name = name;
        this.description = description;
        this.tasklist = tasklist;
        this.tags = tags;
    }

    public void setTasklist(int tasklist){
        this.tasklist = tasklist;
    }

    public int getID() {
        return taskID;
    }

    public int getTasklist() {
        return tasklist;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getAll(){
        StringBuilder line = new StringBuilder();
        StringBuilder tagString = new StringBuilder();

        line.append(taskID +",");
        line.append("\""+name+"\""+",");
        line.append("\""+description+"\""+",");

        for (String tag:tags){
            tagString.append(tag+";");
        }
        line.append("\""+tagString+"\""+",");

        line.append(tasklist+",");

        line.append("\n");

        return line.toString();
    }
}