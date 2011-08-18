/**
 * 
 */
package edu.incense.designer.task.survey;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import edu.incense.designer.GraphEditor;

/**
 * @author mxpxgx
 *
 */
public class SurveyEditorDialog extends JDialog {
    private static final long serialVersionUID = -9136393672800574520L;
    private DynamicList<Question> dynamicList;
    private Window windowParent;
    
    public SurveyEditorDialog(final Survey survey, Window windowParent){
        this.windowParent = windowParent;
        //BoxLayout listLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        
        ImageIcon icon = new ImageIcon(GraphEditor.class.getResource("/edu/incense/designer/images/add-icon.png"));
        JButton addButton = new JButton("Add a question", icon);
        addButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent event) {
                addItem();
            }
            
        });
        add(addButton);
        
        dynamicList = new DynamicList<Question>(new QuestionEditor(this),this);
        for(Question q: survey.getQuestions()){
            
            dynamicList.add(q.clone());
        }
        add(dynamicList);
        
        JButton okButton = new JButton("Ok");
        okButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                survey.setQuestions(dynamicList.getList());
                dispose();
            }
        });
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(okButton);
        buttonsPanel.add(cancelButton);
        add(buttonsPanel);
        
        showDialog();
    }
    
    private void showDialog() {

        if (windowParent != null) {
            setModal(true);
            this.pack();

            // Centers inside the application frame
            int x = windowParent.getX() + (windowParent.getWidth() - getWidth()) / 2;
            int y = windowParent.getY() + (windowParent.getHeight() - getHeight()) / 2;
            setLocation(x, y);

            // Shows the modal dialog and waits
            setVisible(true);
        }
    }
    
    public void addItem(){
        Question question = new Question();
        dynamicList.add(question);
    }
    
    
    
}
