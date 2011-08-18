package edu.incense.designer.task.survey;

import java.awt.Dialog.ModalityType;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;


/**
 * @author mxpxgx
 * 
 */
public class QuestionEditor implements ItemEditor<Question> {

    private Window windowParent;

    public QuestionEditor(Window windowParent) {
        this.windowParent = windowParent;
    }

    @Override
    public void start(Question t, JButton sourceButton) {
        JDialog dialog = new JDialog(windowParent, "Question Editor");
        
        QuestionEditorPanel questionEditorPanel = new QuestionEditorPanel(t, dialog, sourceButton);
        dialog.add(questionEditorPanel);
        
        dialog.setModalityType(ModalityType.APPLICATION_MODAL);
        dialog.pack();

        // Centers inside the application frame
        int x = windowParent.getX() + (windowParent.getWidth() - dialog.getWidth()) / 2;
        int y = windowParent.getY() + (windowParent.getHeight() - dialog.getHeight()) / 2;
        dialog.setLocation(x, y);

        // Shows the modal dialog and waits
        dialog.setVisible(true);
        dialog.toFront();
    }

}
