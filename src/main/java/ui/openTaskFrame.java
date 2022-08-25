package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Map;

public class openTaskFrame extends JFrame {
    private JPanel mainPanel;
    private JTextPane desc;
    private JTextField name;
    private JTextField tags;
    private JComboBox tasklistDropdown;
    private JCheckBox activeMarker;
    private JButton saveButton;
    private JPanel detailsPanel;
    private JLabel nameLabel;
    private JLabel descLabel;
    private JLabel tagsLabel;
    private JLabel dropdownLabel;
    private JLabel activeLabel;
    private JTextArea tagError;

    private contenttypes.Tasklist tasklist;
    private utils.Controller cont;
    private contenttypes.Task task;

    public openTaskFrame(MainFrame frame, contenttypes.Task task) {
        initAll();

        cont = utils.Controller.getInstance();
        this.task = task;

        desc.setText(task.getDescription());
        name.setText(task.getName());
        tasklist = cont.getTasklistsHashMap().get(task.getTasklist());

        setTasklistValue(task.getTasklist());
        setTags();

        activeMarker.setSelected(cont.getActiveTasks().containsKey(task.getID()));

        tagError.setVisible(false);
        tagError.setEditable(false);
        tagError.setWrapStyleWord(true);

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

        setResizable(false);
        setContentPane(mainPanel);
        setTitle(task.getName());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        pack();
        setLocationRelativeTo(null);
    }

    private void initAll() {
        desc = new JTextPane();
        name = new JTextField();
        tags = new JTextField();
        tasklistDropdown = new JComboBox();
        activeMarker = new JCheckBox();
        saveButton = new JButton();
        detailsPanel = new JPanel();
        tagError = new JTextArea();
    }

    public void setTags(){
        String alltags = "";
        for (String tag: task.getTags()){
            alltags = alltags+tag+",";
        }
        tags.setText(alltags);
    }

    private void setTasklistValue(int tasklistID) {
        Map<Integer, contenttypes.Tasklist> map =  cont.getTasklistsHashMap();
        tasklistDropdown.addItem("- None -");

        if (tasklistID!=0){
            int projectID = cont.getTasklistsHashMap().get(tasklistID).getProject();

            if (projectID!=0){
                for (int key:map.keySet()) {
                    contenttypes.Tasklist tl = cont.getTasklistsHashMap().get(key);
                    if (tl.getProject()==projectID||tl.getProject()==0){
                        tasklistDropdown.addItem(tl);
                    }
                }
            } else {
                for (int key:map.keySet()) {
                    tasklistDropdown.addItem(cont.getTasklistsHashMap().get(key));
                }
            }
        } else {
            for (int key:map.keySet()) {
                tasklistDropdown.addItem(cont.getTasklistsHashMap().get(key));
            }
        }

        tasklistDropdown.setRenderer(new ComboBoxRenderer("- None -"));
        tasklistDropdown.setSelectedIndex(-1);
        tasklistDropdown.setEditable(false);

        if (tasklistID!=0) {
            tasklistDropdown.setSelectedItem(map.get(tasklistID));
        }

        tasklistDropdown.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if ((e.getStateChange() == ItemEvent.SELECTED)) {

                    if (tasklistDropdown.getSelectedItem().toString().equals("- None -")){
                        tasklist = null;
                    } else {
                        tasklist = (contenttypes.Tasklist) tasklistDropdown.getSelectedItem();
                    }
                }
            }
        });
    }

    public void updateDetails(){
        if (tasklist==null) {
            task.setTasklist(0);
        } else {
            task.setTasklist(((contenttypes.Tasklist) tasklistDropdown.getSelectedItem()).getID());
        }
        task.setName(name.getText());
        task.setDescription(desc.getText());

        ArrayList<String> tagged = cont.tagSplitter(tags.getText(),",");
        task.setTags(tagged);

        if (activeMarker.isSelected()){
            cont.markTaskAsActive(task);
        } else {
            cont.getActiveTasks().remove(task.getID());
        }
    }
}
