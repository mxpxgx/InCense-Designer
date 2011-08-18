/**
 * 
 */
package edu.incense.designer.task;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.mxgraph.model.mxICell;
import com.mxgraph.util.mxResources;

/**
 * @author mxpxgx
 *
 */
public class EditTaskDialog extends JDialog{
    private static final long serialVersionUID = -9165724986948581250L;
    private mxICell cell;
    private Task task;
    

    public EditTaskDialog(mxICell cell, Frame owner){
        super(owner);
        setTitle(mxResources.get("taskInfoTitle"));
        setLayout(new BorderLayout());
        this.cell = cell;
        task = (Task)cell.getValue();

        // Creates the gradient panel
        JPanel panel = new JPanel(new BorderLayout())
        {
            private static final long serialVersionUID = -749785276524596120L;

            public void paintComponent(Graphics g)
            {
                super.paintComponent(g);

                // Paint gradient background
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new GradientPaint(0, 0, Color.WHITE, getWidth(),
                        0, getBackground()));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }

        };
        
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createMatteBorder(0, 0, 1, 0, Color.GRAY), BorderFactory
                .createEmptyBorder(8, 8, 12, 8)));

        // Adds title
        JLabel titleLabel = new JLabel(mxResources.get("taskInfoTitle"));
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        titleLabel.setOpaque(false);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Adds optional subtitle
        JLabel subtitleLabel = new JLabel(
                "");
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(4, 18, 0, 0));
        subtitleLabel.setOpaque(false);
        panel.add(subtitleLabel, BorderLayout.CENTER);

        getContentPane().add(panel, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        content.add(new JLabel("Type: "+task.getTaskType()));
        content.add(new JLabel(" "));

        content.add(new JLabel("Name: "));
        final JTextField nameTextField = new JTextField(task.getName());
        if(task.getTaskType() == TaskType.Sensor){
            nameTextField.setEditable(false);
        }
        content.add(nameTextField);
        
        //content.add(new JLabel("Freq. sample: "));
        //final JSpinner freqSpinner= new JSpinner(new SpinnerNumberModel(task.getSampleFrequency(),0,100,1));
        //content.add(freqSpinner);

        getContentPane().add(content, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createMatteBorder(1, 0, 0, 0, Color.GRAY), BorderFactory
                .createEmptyBorder(16, 8, 8, 8)));
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        // Adds OK button to close window
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                setVisible(false);
            }
        });

        buttonPanel.add(cancelButton);
        
        // Adds OK button to close window
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                Task modTask = getTask();
                modTask.setName(nameTextField.getText());
                //modTask.setSampleFrequency(((Double)freqSpinner.getValue()).floatValue());
                save(task);
                setVisible(false);
            }
        });

        buttonPanel.add(saveButton);
        

        // Sets default button for enter key
        getRootPane().setDefaultButton(cancelButton);

        setResizable(false);
        setSize(400, 400);
        
    }
    
    private Task getTask(){
        return task;
    }
    
    private void save(Task task){
        
        cell.setValue(task);
    }
}
