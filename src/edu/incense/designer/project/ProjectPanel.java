/**
 * 
 */
package edu.incense.designer.project;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Panel to capture Project data: name, appkey
 * 
 * @author mxpxgx
 * 
 */
public class ProjectPanel extends JPanel {
    private static final long serialVersionUID = 3609815717552677711L;
    private Window window;
    
    public ProjectPanel(ProjectSignature project, Window window) {
        this.window = window;
        addComponents(project);
    }

    public void addComponents(ProjectSignature project) {
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        GroupLayout.ParallelGroup hGroup = layout
                .createParallelGroup(Alignment.CENTER);
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();

        GroupLayout.ParallelGroup hLabelGroup = layout.createParallelGroup();
        GroupLayout.ParallelGroup hFieldGroup = layout.createParallelGroup();

        JLabel lName = new JLabel("Project name: ");
        JTextField tfName = new JTextField(project.getName());
        vGroup.addGroup(layout.createParallelGroup().addComponent(lName)
                .addComponent(tfName));

        JLabel lAppKey = new JLabel("Project ID: ");
        final JTextField tfAppKey = new JTextField();
        tfAppKey.setPreferredSize(new Dimension(200, tfName.getHeight()));
        JButton bGenAppKey = new JButton("Generate");
        bGenAppKey.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Long uuid = UUID.randomUUID().getMostSignificantBits();
                uuid = Math.abs(uuid);
                tfAppKey.setText(String.valueOf(uuid));
            }
            
        });
        
        vGroup.addGroup(layout.createParallelGroup().addComponent(lAppKey)
                .addComponent(tfAppKey).addComponent(bGenAppKey));

        hLabelGroup.addComponent(lName).addComponent(lAppKey);
        hFieldGroup.addComponent(tfName).addGroup(
                layout.createSequentialGroup().addComponent(tfAppKey)
                        .addComponent(bGenAppKey));
        
        JButton okButton = new JButton("Send");
        okButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {

                window.dispose();
            }
        });
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                window.dispose();
            }
        });
        
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(okButton);
        buttonsPanel.add(cancelButton);
        
        vGroup.addComponent(buttonsPanel);
        
        layout.setVerticalGroup(vGroup);
        
        GroupLayout.SequentialGroup hLabelFieldGroup = layout.createSequentialGroup();
        hLabelFieldGroup.addGroup(hLabelGroup);
        hLabelFieldGroup.addGroup(hFieldGroup);
        hGroup.addGroup(hLabelFieldGroup);
        hGroup.addComponent(buttonsPanel);
        layout.setHorizontalGroup(hGroup);

    }

}
