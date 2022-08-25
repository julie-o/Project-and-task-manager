package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class StateReader {
    private static Controller cont;
    private static ControlUtils tools;
    private static StateReader instance;

    public static StateReader getInstance(){
        if(instance == null){
            instance = new StateReader();
            cont = Controller.getInstance();
            tools = ControlUtils.getInstance();
        }
        return instance;
    }

    // note that the order here is important, eg. active parser needs to have all tasks read
    public void parseAll() throws IOException {
        fileCreator();

        taskReader();
        activeReader();
        tasklistReader();
        projectReader();
    }

    private void fileCreator() throws IOException {
        String[] filenames = {"data/active.csv","data/tasks.csv","data/tasklists.csv","data/projects.csv"};

        for (String name:filenames){
            File file = new File(name);
            if (!file.exists()){
                file.createNewFile();
            }
        }
    }

    // helper for checking if a line is empty or blank
    private boolean lineEmpty(String line) {
        return line.trim().isEmpty() || line.trim().isBlank();
    }

    private void activeReader() {
        String task = "";
        try
        {
            BufferedReader buffer = new BufferedReader(new FileReader("data/active.csv"));
            while ((task = buffer.readLine()) != null)
            {
                if (lineEmpty(task)){
                    continue;
                }
                String[] taskAsArray = task.split(",");

                contenttypes.Task activeTask = cont.getTasksHashMap().get(Integer.parseInt(taskAsArray[0]));
                cont.markTaskAsActive(activeTask);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void taskReader() {
        String task = "";
        try
        {
            BufferedReader buffer = new BufferedReader(new FileReader("data/tasks.csv"));
            while ((task = buffer.readLine()) != null)
            {
                if (lineEmpty(task)){
                    continue;
                }


                int count = 0;

                for(int i=0; i < task.length(); i++)
                {    if(task.charAt(i) == ',')
                    count++;
                }

                String[] taskAsArray= task.split(",");

                contenttypes.Task newTask = new contenttypes.Task(Integer.parseInt(taskAsArray[0]),
                        tools.removeQuotes(taskAsArray[1]),
                        tools.removeQuotes(taskAsArray[2]),
                        cont.tagSplitter(tools.removeQuotes(taskAsArray[3]),";"),
                        Integer.parseInt(taskAsArray[4]));
                cont.addTask(newTask);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void tasklistReader() {
        String tasklist = "";
        try
        {
            BufferedReader buffer = new BufferedReader(new FileReader("data/tasklists.csv"));
            while ((tasklist = buffer.readLine()) != null)
            {
                if (lineEmpty(tasklist)){
                    continue;
                }
                String[] tasklistAsArray = tasklist.split(",");

                contenttypes.Tasklist newTasklist = new contenttypes.Tasklist(Integer.parseInt(tasklistAsArray[0]),
                        tools.removeQuotes(tasklistAsArray[1]),
                        tools.removeQuotes(tasklistAsArray[2]),
                        cont.tagSplitter(tools.removeQuotes(tasklistAsArray[3]),";"));

                String[] taskIDs = (tools.removeQuotes(tasklistAsArray[4])).split(";");
                for (String taskID:taskIDs){
                    if (!taskID.isBlank()&&!taskID.isEmpty()){
                        newTasklist.addTask(cont.getTasksHashMap().get(Integer.parseInt(taskID)));
                    }
                }

                newTasklist.setProject(Integer.parseInt(tasklistAsArray[5]));

                cont.addTasklist(newTasklist);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void projectReader() {
        String project = "";
        try
        {
            BufferedReader buffer = new BufferedReader(new FileReader("data/projects.csv"));
            while ((project = buffer.readLine()) != null)
            {
                if (lineEmpty(project)){
                    continue;
                }
                String[] projectAsArray = project.split(",");

                contenttypes.Project newProject = new contenttypes.Project(Integer.parseInt(projectAsArray[0]),
                        tools.removeQuotes(projectAsArray[1]),
                        tools.removeQuotes(projectAsArray[2]),
                        cont.tagSplitter(tools.removeQuotes(projectAsArray[3]),";"));


                String[] tasklistIDs = (tools.removeQuotes(projectAsArray[4])).split(";");
                for (String tasklistID:tasklistIDs){
                    if (!tasklistID.isBlank()&&!tasklistID.isEmpty()){
                        newProject.addTasklist(cont.getTasklistsHashMap().get(Integer.parseInt(tasklistID)));
                    }
                }

                cont.addProject(newProject);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


}
