package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class openTasklistFrame extends JFrame {
    private utils.Controller cont;
    private MainFrame frame;
    private contenttypes.Tasklist tasklist;

    private JPanel mainPanel;
    private JTabbedPane tabsPanel;
    private JPanel tasksPanel;
    private JPanel detailsPanel;
    private JButton saveButton;
    private JList<contenttypes.Task> tasksList;
    private JTextField tags;
    private JTextField name;
    private JComboBox projectDropBox;
    private JLabel projectLabel;
    private JLabel tagsLabel;
    private JLabel titleLabel;
    private JTextPane desc;
    private JLabel descLabel;
    private JButton newTask;
    private JScrollPane tasksScroll;
    private JList<contenttypes.Task> activeTasksList;
    private JScrollPane activeTasksScroll;
    private JTextArea tagError;
    private JLabel listName;
    private contenttypes.Project project;

    public openTasklistFrame(MainFrame frame, contenttypes.Tasklist tasklist) {
        this.cont = utils.Controller.getInstance();
        this.frame = frame;
        this.tasklist = tasklist;

        setResizable(false);

        tabsPanel.setTitleAt(0,"Tasks");
        tabsPanel.setTitleAt(1,"Details");

        desc.setText(tasklist.getDescription());
        name.setText(tasklist.getName());

        listName.setText(tasklist.getName().toUpperCase(Locale.ROOT));
        listName.setFont(new Font("Arial", Font.BOLD, 14));

        setProjectValue(tasklist.getProject());
        setTags();

        existingTaskPopup();

        tagError.setVisible(false);
        tagError.setEditable(false);
        tagError.setWrapStyleWord(true);

        getTasks();
        openTasks();

        newTask.addActionListener(e -> {
            createTaskFrame newtask = new createTaskFrame(frame);
            newtask.setDropdownValue(tasklist);
            newtask.addWindowListener(
                    new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            super.windowClosed(e);
                            reload();
                        }
                    }
            );
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String findSpecials = utils.ControlUtils.getInstance().includesSpecialCharacter(tags.getText()).replace(",","");
                if (findSpecials.length()!=0){
                    tagError.setText("Tags cannot contain special characters.\nTags contain: " + findSpecials);
                    tagError.setForeground(Color.RED);
                    tagError.setVisible(true);
                    mainPanel.setVisible(true);
                    throw new IllegalArgumentException("contenttypes.Tag contains special character");
                }
                tagError.setVisible(false);

                updateDetails();
                frame.reloadMain();
                dispose();
            }
        });

        setContentPane(mainPanel);
        setTitle(tasklist.getName());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        pack();
        setLocationRelativeTo(null);
    }

    private void openTasks() {
        activeTasksList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    taskWindowClosed(new openTaskFrame(frame,activeTasksList.getSelectedValue()));
                }
            }
        });
        activeTasksList.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ke) {
                if(ke.getKeyCode() == KeyEvent.VK_ENTER) {
                    taskWindowClosed(new openTaskFrame(frame,activeTasksList.getSelectedValue()));
                }
            }
        });
        tasksList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    taskWindowClosed(new openTaskFrame(frame,tasksList.getSelectedValue()));
                }
            }
        });
        tasksList.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ke) {
                if(ke.getKeyCode() == KeyEvent.VK_ENTER) {
                    taskWindowClosed(new openTaskFrame(frame,tasksList.getSelectedValue()));
                }
            }
        });
    }

    public void taskWindowClosed(openTaskFrame window){
        window.addWindowListener(
                new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        super.windowClosed(e);
                        reload();
                    }
                }
        );
    }

    public void getTasks(){
        ArrayList<contenttypes.Task> allTasks = tasklist.getTasks();

        DefaultListModel<contenttypes.Task> list = new DefaultListModel<>();
        DefaultListModel<contenttypes.Task> activelist = new DefaultListModel<>();
        tasksList.setModel(list);
        activeTasksList.setModel(activelist);

        for (contenttypes.Task task:allTasks) {
            list.addElement(task);
            if (cont.getActiveTasks().containsKey(task.getID())){
                activelist.addElement(task);
            }
        }

        tasksList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tasksList.setLayoutOrientation(JList.VERTICAL);
        tasksScroll.setViewportView(tasksList);

        activeTasksList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        activeTasksList.setLayoutOrientation(JList.VERTICAL);
        activeTasksScroll.setViewportView(activeTasksList);

        activeTasksScroll.setBorder(BorderFactory.createTitledBorder("Active tasks"));
        tasksScroll.setBorder(BorderFactory.createTitledBorder("All tasks"));
    }

    public void setProjectValue(int projectKey){
        Map<Integer, contenttypes.Project> map =  cont.getProjectHashMap();
        projectDropBox.addItem("- None -");
        for (int key:map.keySet()) {
            projectDropBox.addItem(cont.getProjectHashMap().get(key));
        }

        projectDropBox.setRenderer(new ComboBoxRenderer("- None -"));
        projectDropBox.setSelectedIndex(-1);
        projectDropBox.setEditable(false);

        if (projectKey!=0) {
            projectDropBox.setSelectedItem(map.get(projectKey));
        }

        projectDropBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if ((e.getStateChange() == ItemEvent.SELECTED)) {

                    if (projectDropBox.getSelectedItem().toString().equals("- None -")){
                        project = null;
                    } else {
                        project = (contenttypes.Project) projectDropBox.getSelectedItem();
                    }
                }
            }
        });
    }

    public void setTags(){
        String alltags = "";
        for (String tag: tasklist.getTags()){
            alltags = alltags+tag+",";
        }
        tags.setText(alltags);
    }

    public void existingTaskPopup(){
        JPopupMenu popupList = new JPopupMenu();
        JScrollPane scroller = new JScrollPane();
        JList tasks = new JList();
        JButton cancel = new JButton();

        DefaultListModel<contenttypes.Task> list = new DefaultListModel<>();
        tasks.setModel(list);

        Map<Integer, contenttypes.Task> map =  cont.getActiveTasks();
        for (int key:map.keySet()) {
            if (map.get(key).getTasklist()==0){
                list.addElement(map.get(key));
            }
        }

        popupList.add(scroller);

        tasks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tasks.setLayoutOrientation(JList.VERTICAL);
        scroller.setViewportView(tasks);

        cancel.setText("Cancel");
        popupList.add(cancel);

        newTask.setComponentPopupMenu(popupList);

        cancel.addActionListener(e -> {
            popupList.setVisible(false);
            reload();
        });

        tasks.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    tasklist.addTask((contenttypes.Task) tasks.getSelectedValue());
                    popupList.setVisible(false);
                    reload();
                }
            }
        });
        // catch enter-key events
        tasks.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ke) {
                if(ke.getKeyCode() == KeyEvent.VK_ENTER) {
                    tasklist.addTask((contenttypes.Task) tasks.getSelectedValue());
                    popupList.setVisible(false);
                    reload();
                }
            }
        });
    }

    public void updateDetails(){
        if (project==null) {
            tasklist.setProject(0);
        } else {
            tasklist.setProject(((contenttypes.Project) projectDropBox.getSelectedItem()).getID());
        }
        tasklist.setName(name.getText());
        tasklist.setDescription(desc.getText());

        ArrayList<String> tagged = cont.tagSplitter(tags.getText(),",");
        tasklist.setTags(tagged);
    }

    public void reload(){
        getTasks();
    }
}
