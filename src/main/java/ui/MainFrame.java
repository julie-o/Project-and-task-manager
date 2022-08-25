package ui;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Map;

public class MainFrame extends JFrame {
    private JPanel mainPanel;
    private JPanel activePanel;
    private JPanel buttonPanel;
    private JPanel tasklistPanel;
    private JPanel projectsPanel;

    private JComboBox createNew;
    private JTextField search;

    private JPanel pins;
    private JList<String> pin;

    private JList<contenttypes.Tasklist> tasklists;
    private JList<contenttypes.Project> projects;
    private JList<contenttypes.Task> activeTasks;

    private JScrollPane taskscroll;
    private JScrollPane projectscroll;
    private JScrollPane activescroll;

    private utils.Controller cont;
    private MainFrame frame;

    public MainFrame() throws IOException {
        cont = utils.Controller.getInstance();
        frame = this;

        createButton();
        buttonPanel.add(createNew);

        pinnedProjects();
        getTasklists();
        getProjects();
        getActiveTasks();

        openTasklist();
        openTask();
        openProject();

        mainPanel.setFocusable(true);

        activePanel.setBorder(BorderFactory.createTitledBorder("Active tasks"));

        setTitle("Whamdo");
        setContentPane(mainPanel);

        closer();
        setVisible(true);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
    }

    public void createButton() {
        createNew = new JComboBox();

        createNew.addItem("Task");
        createNew.addItem("Tasklist");
        createNew.addItem("Project");

        createNew.setRenderer(new ComboBoxRenderer("Create New"));
        createNew.setSelectedIndex(-1);

        createNew.setEditable(false);

        createNew.addActionListener(e -> {
            JComboBox cbox = (JComboBox) e.getSource();
            String selected = cbox.getSelectedItem().toString();
            if (selected.equals("Task")){
                createTaskFrame newtask = new createTaskFrame(frame);
            } else if (selected.equals("Tasklist")) {
                createTasklistFrame newtasklist = new createTasklistFrame(frame);
            } else if (selected.equals("Project")) {
                createProjectFrame newproject = new createProjectFrame(frame);
            } else {}
        });
    }

    private void pinnedProjects(){
        DefaultListModel<String> model = new DefaultListModel<>();
        pin.setModel(model);

        String buttons[]= { "Button1","Button2"};
        for (String button:buttons){
            model.addElement(button);
        }

        pin.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pin.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        pin.setVisibleRowCount(1);
        pins.add(pin);
    }

    public void getTasklists(){
        DefaultListModel<contenttypes.Tasklist> list = new DefaultListModel<>();
        tasklists.setModel(list);

        Map<Integer, contenttypes.Tasklist> map = cont.getTasklistsHashMap();
        for (int key:map.keySet()) {
            list.addElement(cont.getTasklistsHashMap().get(key));
        }

        tasklists.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tasklists.setLayoutOrientation(JList.VERTICAL);
        taskscroll.setViewportView(tasklists);

        tasklistPanel.setBorder(BorderFactory.createTitledBorder("Tasklists"));
    }

    public void getProjects(){
        DefaultListModel<contenttypes.Project> list = new DefaultListModel<>();
        projects.setModel(list);

        Map<Integer, contenttypes.Project> map = cont.getProjectHashMap();
        for (int key:map.keySet()) {
            list.addElement(cont.getProjectHashMap().get(key));
        }

        projects.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        projects.setLayoutOrientation(JList.VERTICAL);
        projectscroll.setViewportView(projects);

        projectsPanel.setBorder(BorderFactory.createTitledBorder("Projects"));
    }

    public void getActiveTasks(){
        DefaultListModel<contenttypes.Task> list = new DefaultListModel<>();
        activeTasks.setModel(list);

        Map<Integer, contenttypes.Task> map = cont.getActiveTasks();
        for (int key:map.keySet()) {
            list.addElement(cont.getActiveTasks().get(key));
        }

        activeTasks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        activeTasks.setLayoutOrientation(JList.VERTICAL);
        activescroll.setViewportView(activeTasks);

        activePanel.setBorder(BorderFactory.createTitledBorder("Tasklists"));
    }

    public void openProject(){
        projects.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    new openProjectFrame(frame,projects.getSelectedValue());
                }
            }
        });
        // catch enter-key events
        projects.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ke) {
                if(ke.getKeyCode() == KeyEvent.VK_ENTER) {
                    new openProjectFrame(frame,projects.getSelectedValue());
                }
            }
        });
    }

    public void openTasklist(){
        tasklists.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    new openTasklistFrame(frame,tasklists.getSelectedValue());
                }
            }
        });
        // catch enter-key events
        tasklists.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ke) {
                if(ke.getKeyCode() == KeyEvent.VK_ENTER) {
                    new openTasklistFrame(frame,tasklists.getSelectedValue());
                }
            }
        });
    }

    public void openTask(){
        activeTasks.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    new openTaskFrame(frame,activeTasks.getSelectedValue());
                }
            }
        });
        // catch enter-key events
        activeTasks.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ke) {
                if(ke.getKeyCode() == KeyEvent.VK_ENTER) {
                    new openTaskFrame(frame,activeTasks.getSelectedValue());
                }
            }
        });
    }

    public void reloadMain(){
        getTasklists();
        getProjects();
        getActiveTasks();
    }

    public void closer(){
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    utils.StateWriter.getInstance().writeAll();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                System.out.println("System exit");
                System.exit(0);
            }
        });
    }
}
