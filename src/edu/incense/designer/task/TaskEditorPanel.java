package edu.incense.designer.task;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.mxgraph.util.mxResources;


/**
 * @author mxpxgx
 * 
 */
public class TaskEditorPanel extends JPanel {
    private static final long serialVersionUID = 7340943810863690202L;
    protected Task task;
    protected TaskCellEditor editor;
    protected Window windowContainer;

    public TaskEditorPanel(Window windowContainer, Task task, TaskCellEditor editor) {
        this.editor = editor;
        this.windowContainer = windowContainer;

        addComponents(task);
    }

    /**
     * @return the task
     */
    private Task getTask() {
        return task;
    }

    /**
     * Add Swing elements to modify a Task class
     * 
     * @param task
     */
    protected void addComponents(Task task) {
        this.task = task;

        final Map<String, String> extras = task.getExtras();

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        GroupLayout.Group hGroup = layout.createParallelGroup(Alignment.CENTER);
        GroupLayout.Group vGroup = layout.createSequentialGroup();

        GroupLayout.Group hLeftGroup = layout.createParallelGroup();
        GroupLayout.Group hRightGroup = layout.createParallelGroup();

        // First 2 fields are always the same: type and name
        JLabel typeLabel = new JLabel(mxResources.get("taskType") + ":");
        JLabel typeValue = new JLabel(task.getTaskType().toString());

        vGroup.addGroup(layout.createParallelGroup().addComponent(typeLabel)
                .addComponent(typeValue));
        hLeftGroup.addComponent(typeLabel);
        hRightGroup.addComponent(typeValue);

        JLabel nameLabel = new JLabel(mxResources.get("taskName") + ":");
        final JTextField nameTextField = new JTextField(10);
        nameTextField.setText(task.getName());
        if (task.getTaskType() == TaskType.Sensor) {
            nameTextField.setEnabled(false);
        }

        vGroup.addGroup(layout.createParallelGroup().addComponent(nameLabel)
                .addComponent(nameTextField));
        hLeftGroup.addComponent(nameLabel);
        hRightGroup.addComponent(nameTextField);

        // Add fields for extras
        final List<JTextField> textFields = new ArrayList<JTextField>();
        if (extras != null && task.getTaskType() != TaskType.Survey) {
            JLabel extraLabel;
            String labelText;
            for (Map.Entry<String, String> entry : extras.entrySet()) {
                labelText = prettyAttribute(entry.getKey());
                extraLabel = new JLabel(labelText + ":");
                final JTextField extraTextField = new JTextField(10);
                extraTextField.setText(entry.getValue());
                textFields.add(extraTextField);

                vGroup.addGroup(layout.createParallelGroup()
                        .addComponent(extraLabel).addComponent(extraTextField));
                hLeftGroup.addComponent(extraLabel);
                hRightGroup.addComponent(extraTextField);
            }
        }

        hGroup.addGroup(layout.createSequentialGroup().addGroup(hLeftGroup)
                .addGroup(hRightGroup));
        
        //Ok button
        
        JButton okButton = new JButton("Ok");
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Task task = getTask();
                if (task.getTaskType() != TaskType.Sensor) {
                    task.setName(nameTextField.getText());
                }

                if (extras != null && task.getTaskType() != TaskType.Survey) {
                    // Set extras
                    String[] keys = new String[extras.keySet().size()];
                    keys = extras.keySet().toArray(keys);
                    int size = Math.min(keys.length, textFields.size());
                    for (int i = 0; i < size; i++) {
                        extras.put(keys[i], textFields.get(i).getText());
                    }
                    task.setExtras(extras);
                }

                // Save new task
                editor.saveNewTask(task);

                // setVisible(false);
                editor.stopEditing(false);
                windowContainer.dispose();
            }
        });
        
        // Cancel button

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // setVisible(false);
                editor.stopEditing(true);
                windowContainer.dispose();
            }
        });

        hGroup.addGroup(layout.createSequentialGroup().addComponent(okButton)
                .addComponent(cancelButton));
        vGroup.addGroup(layout.createParallelGroup().addComponent(okButton)
                .addComponent(cancelButton));
        layout.setVerticalGroup(vGroup);
        layout.setHorizontalGroup(hGroup);

    }

    /**
     * Formats the name of a field adding spacing between words to be easier to
     * read.
     * 
     * @param attName
     * @return
     */
    protected String prettyAttribute(String attName) {
        Pattern p = Pattern.compile("[0-9]*[A-Z]*[a-z]*");
        Matcher m = p.matcher(attName);
        StringBuilder builder = new StringBuilder();
        while (m.find()) {
            String piece = m.group();
            if (builder.length() != 0 && piece.length() != 0)
                builder.append(" ");
            builder.append(piece);
        }

        // First char is always uppercase
        char firstLetter = builder.charAt(0);
        if (Character.isLetter(firstLetter)) {
            firstLetter = Character.toUpperCase(firstLetter);
            builder.setCharAt(0, firstLetter);
        }

        return builder.toString();
    }

}
