package contenttypes;

import java.util.ArrayList;

public class Project {
    private int projectID;
    private String name;
    private String description;
    private ArrayList<String> tags;
    private ArrayList<Tasklist> tasklists;

    public Project(int id, String name, String description, ArrayList<String> tags) {
        this.projectID = id;
        this.name = name;
        this.description = description;
        this.tags = tags;
        tasklists = new ArrayList<>();
    }

    public void addTasklist(Tasklist tasklist) {
        tasklists.add(tasklist);
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return projectID;
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

    public ArrayList<Tasklist> getTasklists() {
        return tasklists;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getAll() {
        StringBuilder line = new StringBuilder();
        StringBuilder tagString = new StringBuilder();
        StringBuilder tasklistString = new StringBuilder();

        line.append(projectID + ",");
        line.append("\"" + name + "\"" + ",");
        line.append("\"" + description + "\"" + ",");

        for (String tag : tags) {
            tagString.append(tag + ";");
        }
        line.append("\"" + tagString + "\"" + ",");

        if (tasklists!=null){
            for (Tasklist tasklist : tasklists) {
                tasklistString.append(tasklist.getID() + ";");
            }
            line.append("\"" + tasklistString + "\"" + ",");
        } else {
            line.append("\"\",");
        }

        line.append("\n");
        return line.toString();
    }
}