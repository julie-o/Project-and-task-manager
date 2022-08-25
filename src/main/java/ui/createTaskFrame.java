package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Map;

public class createTaskFrame extends JFrame {

    private JPanel mainPanel;
    private JLabel nameLabel;
    private JLabel descLabel;
    private JLabel tagsLabel;
    private JTextField name;
    private JTextPane desc;
    private JTextField tags;
    private JButton saveButton;
    private JLabel listLabel;
    private JComboBox listDropdown;
    private JCheckBox activeMarker;
    private JLabel activeLabel;
    private JTextArea tagError;
    private boolean active;
    private utils.Controller cont;
    private contenttypes.Tasklist tasklist;

    public createTaskFrame(MainFrame frame) {
        cont = utils.Controller.getInstance();
        dropdownMenu(0);
        active = false;

        tagError.setVisible(false);
        tagError.setEditable(false);
        tagError.setWrapStyleWord(true);

        setResizable(false);

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

                ArrayList<String> tagged = cont.tagSplitter(tags.getText(),",");
                int id = cont.generateID("task");

                int listID=0;
                if (tasklist!=null){
                    listID = tasklist.getID();
                }

                contenttypes.Task task = new contenttypes.Task(id, name.getText(), desc.getText(), tagged, listID);

                if (tasklist==null||activeMarker.isSelected()){
                    cont.markTaskAsActive(task);
                }
                if (tasklist!=null) {
                    tasklist.addTask(task);
                }
                cont.addTask(task);

                frame.reloadMain();
                dispose();
            }
        });

        setContentPane(mainPanel);

        setTitle("New task");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        pack();
        setLocationRelativeTo(null);
    }

    public void setDropdownValue(contenttypes.Tasklist tl){
        listDropdown.setSelectedItem(tl);
        tasklist = tl;
    }

    private void dropdownMenu(int id){
        Map<Integer, contenttypes.Tasklist> map =  cont.getTasklistsHashMap();

        if (id!=0){
            for (int key:map.keySet()) {
                contenttypes.Tasklist tl = cont.getTasklistsHashMap().get(key);
                if (tl.getProject()==id){
                    listDropdown.addItem(tl);
                }
            }
        } else {
            for (int key:map.keySet()) {
                listDropdown.addItem(cont.getTasklistsHashMap().get(key));
            }
        }

        listDropdown.setRenderer(new ComboBoxRenderer("- None -"));
        listDropdown.setSelectedIndex(-1);
        listDropdown.setEditable(false);

        listDropdown.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if ((e.getStateChange() == ItemEvent.SELECTED)) {

                    if (listDropdown.getSelectedItem().toString().equals("- None -")){
                        tasklist = null;
                    } else {
                        tasklist = (contenttypes.Tasklist) listDropdown.getSelectedItem();
                    }
                }
            }
        });
    }

    public void hideTasklistsNotInProject(int projectID){
        dropdownMenu(projectID);
    }
}
