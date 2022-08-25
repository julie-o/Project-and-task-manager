package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/** Use for writing to file **/
public class StateWriter {
    private static Controller cont;
    private static StateWriter instance;

    public static StateWriter getInstance(){
        if(instance == null){
            instance = new StateWriter();
            cont = Controller.getInstance();
        }
        return instance;
    }

    public void writeAll() throws IOException {
        taskWriter();
        activeWriter();
        tasklistWriter();
        projectWriter();
    }

    private void taskWriter() throws IOException {
        File csvFile = new File("data/tasks.csv");
        FileWriter fileWriter = new FileWriter(csvFile);

        Map<Integer, contenttypes.Task> map = cont.getTasksHashMap();
        for (int key:map.keySet()) {
            fileWriter.write(map.get(key).getAll());
        }

        fileWriter.close();
    }

    private void tasklistWriter() throws IOException {
        File csvFile = new File("data/tasklists.csv");
        FileWriter fileWriter = new FileWriter(csvFile);

        Map<Integer, contenttypes.Tasklist> map = cont.getTasklistsHashMap();
        for (int key:map.keySet()) {
            fileWriter.write(map.get(key).getAll());
        }

        fileWriter.close();
    }

    private void projectWriter() throws IOException {
        File csvFile = new File("data/projects.csv");
        FileWriter fileWriter = new FileWriter(csvFile);

        Map<Integer, contenttypes.Project> map = cont.getProjectHashMap();
        for (int key:map.keySet()) {
            fileWriter.write(map.get(key).getAll());
        }

        fileWriter.close();
    }

    private void activeWriter() throws IOException {
        File csvFile = new File("data/active.csv");
        FileWriter fileWriter = new FileWriter(csvFile);

        StringBuilder line = new StringBuilder();

        for (int key:cont.getActiveTasks().keySet()) {
            line.append(key+"\n");
        }

        fileWriter.write(line.toString());
        fileWriter.close();
    }
}
