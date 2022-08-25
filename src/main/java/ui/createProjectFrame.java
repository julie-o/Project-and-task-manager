package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class createProjectFrame extends JFrame {
    private JLabel titleLabel;
    private JLabel descLabel;
    private JLabel tagsLabel;
    private JTextField name;
    private JTextPane desc;
    private JTextField tags;
    private JButton saveButton;
    private JPanel mainPanel;
    private JTextArea tagError;

    public createProjectFrame(MainFrame frame) {

        tagError = new JTextArea();
        tagError.setVisible(false);
        tagError.setEditable(false);
        tagError.setWrapStyleWord(true);

        setResizable(false);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                utils.Controller cont = utils.Controller.getInstance();

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
                int id = cont.generateID("project");

                contenttypes.Project project = new contenttypes.Project(id, name.getText(), desc.getText(), tagged);

                cont.addProject(project);

                frame.reloadMain();
                dispose();
            }
        });

        setContentPane(mainPanel);

        setTitle("New project");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        pack();
        setLocationRelativeTo(null);
    }
}
