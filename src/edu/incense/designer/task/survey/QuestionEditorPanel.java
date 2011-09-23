package edu.incense.designer.task.survey;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 * @author mxpxgx
 * 
 */
public class QuestionEditorPanel extends JPanel{
    private static final long serialVersionUID = 6117307860714832118L;
    private static final int MAX_OPTIONS = 7;
    private static final int FIELDS_MAX_WIDTH = 300;
    private Window window;
    private JButton sourceButton;

    public QuestionEditorPanel(Question question, Window window, JButton sourceButton) {
        this.window = window;
        this.sourceButton = sourceButton;
        addComponents(question);
    }

    private void addComponents(final Question question) {
        // Add layout
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        GroupLayout.ParallelGroup hGroup = layout.createParallelGroup(Alignment.CENTER);
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        
        GroupLayout.ParallelGroup hLabelGroup = layout.createParallelGroup();
        GroupLayout.ParallelGroup hFieldGroup = layout.createParallelGroup();
        
        JLabel questionLabel = new JLabel("Question: ");
        final JTextField questionTextField = new JTextField(
                question.getQuestion());
        setFieldSize(questionTextField);
        vGroup.addGroup(layout.createParallelGroup().
                addComponent(questionLabel).addComponent(questionTextField));
        
        JLabel typeLabel = new JLabel("Type: ");
        final JComboBox typeComboBox = new JComboBox(QuestionType.values());
        vGroup.addGroup(layout.createParallelGroup().
                addComponent(typeLabel).addComponent(typeComboBox));
        typeComboBox.setSelectedItem(question.getType());

        //JLabel skipLabel = new JLabel("Skippable: ");
        final JCheckBox skipCheckBox = new JCheckBox("Skippable", false);
        vGroup.addComponent(skipCheckBox);
        skipCheckBox.setSelected(question.isSkippable());
        
        hLabelGroup.addComponent(questionLabel).addComponent(typeLabel);
        hFieldGroup.addComponent(questionTextField).addComponent(typeComboBox).addComponent(skipCheckBox);
        
        // Add options
        List<JLabel> labels = new ArrayList<JLabel>();
        final List<JTextField> fields = new ArrayList<JTextField>();
        
        String[] savedOptions = question.getOptions();
        
        JLabel label;
        JTextField field;
        for (int i = 1; i <= MAX_OPTIONS; i++) {
            label = new JLabel("Option " + i + ": ");
            labels.add(label);
            
            field = new JTextField();
            if (savedOptions!=null && (i - 1) < savedOptions.length) {
                field.setText(savedOptions[i - 1]);
            }
            fields.add(field);
            
            vGroup.addGroup(layout.createParallelGroup().
                    addComponent(label).addComponent(field));
            hLabelGroup.addComponent(label);
            hFieldGroup.addComponent(field);
        }
        
        JButton okButton = new JButton("Ok");
        okButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                List<String> optionsList = new ArrayList<String>();
                String option;
                
                for (JTextField field : fields) {
                    option = field.getText();
                    if (option.length() > 0) {
                        optionsList.add(option);
                    }
                }
                
                String[] options = new String[optionsList.size()];
                options = optionsList.toArray(options);
                question.setQuestion(questionTextField.getText());
                question.setType(typeComboBox.getSelectedItem().toString());
                question.setSkippable(skipCheckBox.isSelected());
                if (question.obtainQuestionType() == QuestionType.CHECKBOXES
                        || question.obtainQuestionType() == QuestionType.RADIOBUTTONS) {
                    question.setOptions(options);
                } else {
                    question.setOptions(null);
                }
                sourceButton.setText(question.toString());
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
    
    public void setFieldSize(JTextField field){
        field.setPreferredSize(new Dimension(FIELDS_MAX_WIDTH, field.getHeight()));
    }
}
