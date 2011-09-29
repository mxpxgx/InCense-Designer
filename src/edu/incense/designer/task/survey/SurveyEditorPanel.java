/**
 * 
 */
package edu.incense.designer.task.survey;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.mxgraph.util.mxResources;

import edu.incense.designer.GraphEditor;
import edu.incense.designer.task.EditorTask;
import edu.incense.designer.task.TaskCellEditor;
import edu.incense.designer.task.TaskEditorPanel;

/**
 * @author mxpxgx
 * 
 */
public class SurveyEditorPanel extends TaskEditorPanel {
    private static final long serialVersionUID = -9136393672800574520L;
    private DynamicList<Question> dynamicList;
    private QuestionEditor questionEditor;

    public SurveyEditorPanel(Window windowContainer, EditorTask task,
            TaskCellEditor editor) {
        super(windowContainer, task, editor);
    }

    protected void addComponents(final EditorTask task) {
        this.task = task;
        
        JsonSurvey jsonSurvey = new JsonSurvey(new ObjectMapper());
        final Survey survey = jsonSurvey.toSurvey(task.getExtra("survey"));

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        GroupLayout.Group hGroup = layout.createParallelGroup(Alignment.CENTER);
        GroupLayout.Group vGroup = layout.createSequentialGroup();

        // First 2 fields are always the same: type and name
        GroupLayout.Group hLeftGroup = layout.createParallelGroup();
        GroupLayout.Group hRightGroup = layout.createParallelGroup();

        JLabel typeLabel = new JLabel(mxResources.get("taskType") + ":");
        JLabel typeValue = new JLabel(task.getType());

        vGroup.addGroup(layout.createParallelGroup().addComponent(typeLabel)
                .addComponent(typeValue));
        hLeftGroup.addComponent(typeLabel);
        hRightGroup.addComponent(typeValue);

        JLabel nameLabel = new JLabel(mxResources.get("taskName") + ":");
        final JTextField nameTextField = new JTextField(10);
        nameTextField.setText(task.getName());
        
        vGroup.addGroup(layout.createParallelGroup().addComponent(nameLabel)
                .addComponent(nameTextField));
        hLeftGroup.addComponent(nameLabel);
        hRightGroup.addComponent(nameTextField);
        
        hGroup.addGroup(layout.createSequentialGroup().addGroup(hLeftGroup)
                .addGroup(hRightGroup));

        // Survey specific data
        ImageIcon icon = new ImageIcon(
                GraphEditor.class
                        .getResource("/edu/incense/designer/images/add-icon.png"));
        JButton addButton = new JButton("Add a question", icon);
        addButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                addItem();
            }

        });
        hGroup.addGroup(layout.createParallelGroup(Alignment.LEADING)
                .addComponent(addButton));
        vGroup.addComponent(addButton);

        questionEditor = new QuestionEditor(windowContainer);
        dynamicList = new DynamicList<Question>(questionEditor, windowContainer);
        for (Question q : survey.getQuestions()) {

            dynamicList.add(q.clone());
        }
        JScrollPane scrollPane = new JScrollPane(dynamicList);
        Dimension d = dynamicList.getPreferredSize(8);
        d.setSize(d.getWidth() + 50, d.getHeight() + 5);
        scrollPane.setMaximumSize(d);
        scrollPane
                .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        hGroup.addComponent(scrollPane);
        vGroup.addComponent(scrollPane);

        JButton okButton = new JButton("Ok");
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                saveSurvey(nameTextField.getText(), survey);
                windowContainer.dispose();
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                windowContainer.dispose();
            }
        });
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(okButton);
        buttonsPanel.add(cancelButton);
        hGroup.addComponent(buttonsPanel);
        vGroup.addComponent(buttonsPanel);

        layout.setVerticalGroup(vGroup);
        layout.setHorizontalGroup(hGroup);
    }

    private void addItem() {
        Question question = new Question();
        JButton item = dynamicList.add(question);
        questionEditor.start(question, item);
    }

    private void saveSurvey(String name, Survey survey) {
        task.setName(name);
        survey.setTitle(name);
        survey.setQuestions(dynamicList.getList());

        ObjectMapper mapper = new ObjectMapper();
        String json=null;
        try {
            json = mapper.writeValueAsString(survey);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        task.putExtra("survey",json);

        // Save new task
        editor.saveNewTask(task);
    }

}
