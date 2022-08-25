package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Map;

public class createTasklistFrame extends JFrame {
    private JPanel mainPanel;
    private JTextField name;
    private JTextPane desc;
    private JTextField tags;
    private JLabel tagsLabel;
    private JLabel titleLabel;
    private JLabel descLabel;
    private JButton saveButton;
    private JTextPane batchTasks;
    private JLabel tasksLabel;
    private JLabel dropdownLabel;
    private JComboBox<contenttypes.Project> projectDropdown;
    private JTextArea tagError;
    private utils.Controller cont;
    private contenttypes.Project project;

    public createTasklistFrame(MainFrame frame) {
        cont = utils.Controller.getInstance();
        dropdownMenu();

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
                int id = cont.generateID("tasklist");

                contenttypes.Tasklist tasklist = new contenttypes.Tasklist(id, name.getText(), desc.getText(), tagged);
                tasklist.batchAddTasks(batchTasks.getText());

                if (project!=null){
                    tasklist.setProject(project.getID());
                    project.addTasklist(tasklist);
                }

                cont.addTasklist(tasklist);

                dispose();
                frame.reloadMain();
            }
        });

        setContentPane(mainPanel);

        setTitle("New tasklist");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        pack();
        setLocationRelativeTo(null);
    }

    public void dropdownMenu(){
        projectDropdown = new JComboBox<>();
        Map<Integer, contenttypes.Project> map =  cont.getProjectHashMap();
        for (int key:map.keySet()) {
            projectDropdown.addItem(cont.getProjectHashMap().get(key));
        }

        projectDropdown.setRenderer(new ComboBoxRenderer("- None -"));
        projectDropdown.setSelectedIndex(-1);
        projectDropdown.setEditable(false);

        projectDropdown.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if ((e.getStateChange() == ItemEvent.SELECTED)) {

                    if (projectDropdown.getSelectedItem().toString().equals("- None -")){
                        project = null;
                    } else {
                        project = (contenttypes.Project) projectDropdown.getSelectedItem();
                    }
                }
            }
        });
    }
}
