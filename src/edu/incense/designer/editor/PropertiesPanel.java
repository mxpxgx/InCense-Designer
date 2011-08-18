/**
 * 
 */
package edu.incense.designer.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.incense.designer.task.Output;
import edu.incense.designer.task.Task;
import edu.incense.designer.task.TaskType;

/**
 * Panel that displays the properties of the selected component in a palette.
 * These include: task type, outputs. Implements the PaletteSelectionListener
 * interface; the events of this listener are triggered in EditorPalette class.
 * 
 * @author mxpxgx
 * @version 1.0 (2011/08/15)
 */
public class PropertiesPanel extends JPanel implements PaletteSelectionListener {

    /**
     * 
     */
    private static final long serialVersionUID = 2835920409856391701L;

    private JLabel titleLabel, typeLabel, outputsLabel;
    private JLabel descriptionArea;
    private boolean instantiated = false;
    private GroupLayout layout;
    private GroupLayout.Group hGroup;
    private GroupLayout.Group vGroup;

    public PropertiesPanel(Task task) {
        setBackground(Color.WHITE);
        setTask(task);
        instantiated = true;
    }

    public void setTask(Task task) {
        if (instantiated) {
            this.removeAll();
        }

        layout = new GroupLayout(this);
        setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        hGroup = layout.createParallelGroup(Alignment.LEADING);
        vGroup = layout.createSequentialGroup();

        setTitle();
        setType(task.getTaskType());
        setDescription(task.getDescription());
        setOutputs(task.getOutputs());

        layout.setVerticalGroup(vGroup);
        layout.setHorizontalGroup(hGroup);
    }

    private void setTitle() {
        if (titleLabel == null) {
            titleLabel = new JLabel();
        }
        titleLabel.setText("Properties");
        titleLabel.setFont(new Font("Properties Title", Font.BOLD, 14));

        hGroup.addComponent(titleLabel);
        vGroup.addComponent(titleLabel);
    }

    private void setType(TaskType type) {
        if (typeLabel == null) {
            typeLabel = new JLabel();
        }
        typeLabel.setText("<html><b>Type:</b> " + type + "</html>");

        hGroup.addComponent(typeLabel);
        vGroup.addComponent(typeLabel);
    }

    private void setDescription(String description) {
        if (description != null) {
            if (descriptionArea == null) {
                descriptionArea = new JLabel();
                descriptionArea.setPreferredSize(new Dimension(getWidth()-25, descriptionArea.getHeight()));
//                this.addPropertyChangeListener(new PropertyChangeListener(){
//
//                    @Override
//                    public void propertyChange(PropertyChangeEvent pce) {
//                        if(pce.getPropertyName() != null && pce.getPropertyName().compareTo("size")==0){
//                            descriptionArea.setSize(getWidth()-10, descriptionArea.getHeight());
//                        }
//                    }
//                    
//                });
                //descriptionArea.setContentType("text/html");
            }
            descriptionArea.setText("<html><b>Description:</b> <p align=\"justify\">" + description
                    + "</p></html>");

            hGroup.addComponent(descriptionArea);
            vGroup.addComponent(descriptionArea);
        }
    }

    private void setOutputs(Map<String, Output> outputs) {
        if (!outputs.isEmpty()) {
            if (outputsLabel == null) {
                outputsLabel = new JLabel("<html><b>Outputs:</b> </html>");
            }
            hGroup.addComponent(outputsLabel);
            vGroup.addComponent(outputsLabel);

            JLabel fieldLabel, valueLabel;
            GroupLayout.Group vOutput;
            GroupLayout.Group hOutputLeft = layout.createParallelGroup();
            GroupLayout.Group hOutputRight = layout.createParallelGroup();
            for (String field : outputs.keySet()) {
                vOutput = layout.createParallelGroup();

                fieldLabel = new JLabel("  " + field + ": ");
                fieldLabel.setToolTipText("<html><p>"+outputs.get(field).getDescription() + "</p><p>" + outputs.get(field).getExample()+"</p></html>");
                valueLabel = new JLabel(outputs.get(field).getType());
                valueLabel.setToolTipText(fieldLabel.getToolTipText());

                vOutput.addComponent(fieldLabel).addComponent(valueLabel);
                vGroup.addGroup(vOutput);
                hOutputLeft.addComponent(fieldLabel);
                hOutputRight.addComponent(valueLabel);
            }
            GroupLayout.Group hOutput = layout.createSequentialGroup();
            hOutput.addGroup(hOutputLeft).addGroup(hOutputRight);
            hGroup.addGroup(hOutput);
        }
    }

    /**
     * @see edu.incense.designer.editor.PaletteSelectionListener#selectionChanged(edu.incense.designer.task.Task)
     */
    @Override
    public void selectionChanged(Object value) {
        Task task = (Task) value;
        setTask(task);
    }

}
