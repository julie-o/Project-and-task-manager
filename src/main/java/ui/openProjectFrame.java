package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class openProjectFrame extends JFrame {
    private JTabbedPane tabsPanel;
    private JPanel mainPanel;
    private JPanel tasksPanel;
    private JPanel detailsPanel;
    private JScrollPane tasklistScroll;
    private JScrollPane activeTaskScroll;
    private JList<contenttypes.Tasklist> tasklists;
    private JList<contenttypes.Task> activeTasks;
    private JButton saveButton;
    private JPanel menuButtons;
    private JButton newList;
    private JLabel projectName;
    private JTextField name;
    private JTextPane desc;
    private JTextField tags;
    private JTextArea tagError;
    private JLabel nameLabel;
    private JLabel descLabel;
    private JLabel tagsLabel;
    private JButton newTask;

    private utils.Controller cont;
    private MainFrame frame;
    private contenttypes.Project project;

    public openProjectFrame(MainFrame frame, contenttypes.Project project) {
        this.cont = utils.Controller.getInstance();
        this.frame = frame;
        this.project = project;

        setResizable(false);

        tabsPanel.setTitleAt(0,"Tasks");
        tabsPanel.setTitleAt(1,"Details");

        desc.setText(project.getDescription());
        name.setText(project.getName());

        projectName.setText(project.getName().toUpperCase(Locale.ROOT));
        projectName.setFont(new Font("Arial", Font.BOLD, 14));

        getTasklists();
        getActiveTasks();

        openItem();

        setTags();
        existingListPopup();

        tagError.setVisible(false);
        tagError.setEditable(false);
        tagError.setWrapStyleWord(true);

        newTask.addActionListener(e -> {
            createTaskFrame newtask = (new createTaskFrame(frame));
            newtask.hideTasklistsNotInProject(project.getID());
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
        setTitle(project.getName());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        pack();
        setLocationRelativeTo(null);
    }

    public void getTasklists(){
        ArrayList<contenttypes.Tasklist> allTasklists = project.getTasklists();

        DefaultListModel<contenttypes.Tasklist> list = new DefaultListModel<>();
        tasklists.setModel(list);
        for (contenttypes.Tasklist tl:allTasklists) {
            list.addElement(tl);
        }

        tasklists.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tasklists.setLayoutOrientation(JList.VERTICAL);
        tasklistScroll.setViewportView(tasklists);

        tasklistScroll.setBorder(BorderFactory.createTitledBorder("Tasklists"));
    }

    public void getActiveTasks(){
        Map<Integer, contenttypes.Task> map = cont.getActiveTasks();

        DefaultListModel<contenttypes.Task> list = new DefaultListModel<>();
        activeTasks.setModel(list);

        for (int key:map.keySet()) {
            boolean isInProject = project.getTasklists().contains(cont.getTasklistsHashMap().get(map.get(key).getTasklist()));
            if (isInProject) {
                list.addElement(map.get(key));
            }
        }

        activeTasks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        activeTasks.setLayoutOrientation(JList.VERTICAL);
        activeTaskScroll.setViewportView(activeTasks);

        activeTaskScroll.setBorder(BorderFactory.createTitledBorder("Active tasks"));
    }

    private void openItem() {
        tasklists.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    taskWindowClosed(new openTasklistFrame(frame,tasklists.getSelectedValue()));
                }
            }
        });
        tasklists.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ke) {
                if(ke.getKeyCode() == KeyEvent.VK_ENTER) {
                    taskWindowClosed(new openTasklistFrame(frame,tasklists.getSelectedValue()));
                }
            }
        });
        activeTasks.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    taskWindowClosed(new openTaskFrame(frame,activeTasks.getSelectedValue()));
                }
            }
        });
        activeTasks.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ke) {
                if(ke.getKeyCode() == KeyEvent.VK_ENTER) {
                    taskWindowClosed(new openTaskFrame(frame,activeTasks.getSelectedValue()));
                }
            }
        });
    }

    public void taskWindowClosed(JFrame window){
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

    public void setTags(){
        String alltags = "";
        for (String tag: project.getTags()){
            alltags = alltags+tag+",";
        }
        tags.setText(alltags);
    }

    public void existingListPopup(){
        JPopupMenu popupList = new JPopupMenu();
        JScrollPane scroller = new JScrollPane();
        JList tasklists = new JList();
        JButton cancel = new JButton();

        DefaultListModel<contenttypes.Tasklist> list = new DefaultListModel<>();
        tasklists.setModel(list);

        Map<Integer, contenttypes.Tasklist> map =  cont.getTasklistsHashMap();
        for (int key:map.keySet()) {
            if (map.get(key).getProject()==0){
                list.addElement(map.get(key));
            }
        }

        popupList.add(scroller);

        tasklists.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tasklists.setLayoutOrientation(JList.VERTICAL);
        scroller.setViewportView(tasklists);

        cancel.setText("Cancel");
        popupList.add(cancel);

        newList.setComponentPopupMenu(popupList);

        cancel.addActionListener(e -> {
            popupList.setVisible(false);
            reload();
        });

        tasklists.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    contenttypes.Tasklist tl = (contenttypes.Tasklist) tasklists.getSelectedValue();
                    project.addTasklist(tl);
                    tl.setProject(project.getID());
                    popupList.setVisible(false);
                    reload();
                }
            }
        });
        // catch enter-key events
        tasklists.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ke) {
                if(ke.getKeyCode() == KeyEvent.VK_ENTER) {
                    contenttypes.Tasklist tl = (contenttypes.Tasklist) tasklists.getSelectedValue();
                    project.addTasklist(tl);
                    tl.setProject(project.getID());
                    popupList.setVisible(false);
                    reload();
                }
            }
        });
    }

    public void updateDetails(){
        project.setName(name.getText());
        project.setDescription(desc.getText());

        ArrayList<String> tagged = cont.tagSplitter(tags.getText(),",");
        project.setTags(tagged);
    }

    public void reload(){
        getActiveTasks();
        getTasklists();
    }
}
