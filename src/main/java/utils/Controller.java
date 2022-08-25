package utils;

import ui.MainFrame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Controller {
    private static Controller instance;

    private final HashMap<Integer, contenttypes.Task> activeTasks;
    private final HashMap<Integer, contenttypes.Task> tasksHashMap;
    private final HashMap<Integer, contenttypes.Tasklist> tasklistsHashMap;
    private final HashMap<Integer, contenttypes.Project> projectHashMap;

    private final ArrayList<String> allTags;

    private boolean running;

    public static Controller getInstance(){
        if(instance == null){
            instance = new Controller();
        }
        return instance;
    }

    public Controller() {
        this.activeTasks = new HashMap<>();
        this.tasksHashMap = new HashMap<>();
        this.tasklistsHashMap = new HashMap<>();
        this.projectHashMap = new HashMap<>();
        this.allTags = new ArrayList<>();
    }

    public void run() throws IOException {
        running = true;
        StateReader.getInstance().parseAll();
        new MainFrame();
    }

    public HashMap<Integer, contenttypes.Task> getActiveTasks() {
        return activeTasks;
    }

    public HashMap<Integer, contenttypes.Task> getTasksHashMap() {
        return tasksHashMap;
    }

    public HashMap<Integer, contenttypes.Tasklist> getTasklistsHashMap() {
        return tasklistsHashMap;
    }

    public HashMap<Integer, contenttypes.Project> getProjectHashMap() {
        return projectHashMap;
    }

    public void markTaskAsActive(contenttypes.Task task){
        if (!activeTasks.containsKey(task.getID())){
            activeTasks.put(task.getID(),task);
        }
    }

    public void addTask(contenttypes.Task task){
        if (!tasksHashMap.containsKey(task.getID())){
            tasksHashMap.put(task.getID(),task);
        }
    }

    public void addTasklist(contenttypes.Tasklist tasklist){
        if (!tasklistsHashMap.containsKey(tasklist.getID())){
            tasklistsHashMap.put(tasklist.getID(),tasklist);
        }
    }

    public void addProject(contenttypes.Project project){
        if (!projectHashMap.containsKey(project.getID())){
            projectHashMap.put(project.getID(),project);
        }
    }

    public ArrayList<String> tagSplitter(String tags, String splitter){
        String[] splitTags = tags.split(splitter);
        ArrayList<String> tagList = new ArrayList<>();

        for (String tag:splitTags){
            if(tag.isBlank()){
                continue;
            }

            String trimmed = tag.trim();

            String foundSpecial = ControlUtils.getInstance().includesSpecialCharacter(trimmed);
            if (foundSpecial.length()!=0){
                if (!foundSpecial.isBlank()){
                    throw new IllegalArgumentException("Found character: " + foundSpecial);
                }
            }

            if (!allTags.contains(trimmed)){
                allTags.add(trimmed);
            }

            if (!tagList.contains(trimmed)){
                tagList.add(trimmed);
            }
        }

        return tagList;
    }

    private int randomGen(){
        return (int) (Math.random()*(99999 - 10000 + 1) + 10000);
    }

    public int generateID(String type) {
        int id = 0;
        switch (type){
            case "task":
                while (tasksHashMap.containsKey(id)||id==0) {
                    id = randomGen();
                }
                break;
            case "tasklist":
                while (tasklistsHashMap.containsKey(id)||id==0) {
                    id = randomGen();
                }
                break;
            case "project":
                while (projectHashMap.containsKey(id)||id==0) {
                    id = randomGen();
                }
                break;
            default:
                throw new RuntimeException();
        }
        return id;
    }
}
