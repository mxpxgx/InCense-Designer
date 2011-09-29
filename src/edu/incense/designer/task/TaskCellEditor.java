/**
 * 
 */
package edu.incense.designer.task;

import java.util.EventObject;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.view.mxICellEditor;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

import edu.incense.designer.task.session.SessionEditorPanel;
import edu.incense.designer.task.survey.SurveyEditorPanel;
import edu.incense.designer.task.trigger.TriggerEditorPanel;

/**
 * @author mxpxgx
 * 
 */
public class TaskCellEditor implements mxICellEditor {
    private static final long serialVersionUID = -1600414191378754772L;

    protected mxGraphComponent graphComponent;

    protected transient Object editingCell;
    protected transient EventObject trigger;

    private mxCellState state;
    private mxICell cell;
    private mxGraph graph;
    private EditorTask task;

    /**
     * 
     */
    public TaskCellEditor(mxGraphComponent graphComponent) {
        this.graphComponent = graphComponent;

    }

    public void saveNewTask(EditorTask task) {
        // TODO implement saving
        this.task = task;
        // graph.getModel().beginUpdate();
        // cell.setValue(task);
        // graph.getModel().endUpdate();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.mxgraph.swing.view.mxICellEditor#startEditing(java.lang.Object,
     * java.util.EventObject)
     */
    public void startEditing(Object cellObject, EventObject evt) {
        if (editingCell != null) {
            stopEditing(true);
        }

        graph = graphComponent.getGraph();
        state = graph.getView().getState(cellObject);

        if (state != null) {
            editingCell = cellObject;
            trigger = evt;

            cell = (mxICell) cellObject;
            if (cell.isVertex()) {
                EditorTask task = (EditorTask) cell.getValue();

                if (task != null) {
                    JFrame parentFrame = (JFrame) SwingUtilities
                            .windowForComponent(graphComponent);

                    JDialog dialog = new JDialog(parentFrame, "Task Editor");

                    this.task = task;
                    TaskEditorPanel taskEditorPanel;
                    // For survey
                    if (task.getTaskType() == TaskType.Survey) {
                        taskEditorPanel = new SurveyEditorPanel(dialog, task,
                                this);
                        dialog.setTitle("Survey Editor");
                    }
                    // For sink
                    else if (task.getTaskType() == TaskType.Sink) {
                        taskEditorPanel = new SinkEditorPanel(dialog, task,
                                this, graph, cell);
                        dialog.setTitle("Sink Editor");
                    }
                    // For trigger
                    else if (task.getTaskType() == TaskType.Trigger) {
                        taskEditorPanel = new TriggerEditorPanel(dialog, task,
                                this, graph, cell);
                        dialog.setTitle("Trigger Editor");
                    }
                    // For session
                    else if (task.getTaskType() == TaskType.Session) {
                        taskEditorPanel = new SessionEditorPanel(dialog, task,
                                this, graph, cell);
                        dialog.setTitle("Session Editor");
                    }
                    // For other tasks
                    else {
                        taskEditorPanel = new TaskEditorPanel(dialog, task,
                                this);
                        dialog.add(taskEditorPanel);
                    }
                    dialog.add(taskEditorPanel);

                    dialog.setModal(true);
                    dialog.pack();

                    // Centers inside the application frame
                    int x = parentFrame.getX()
                            + (parentFrame.getWidth() - dialog.getWidth()) / 2;
                    int y = parentFrame.getY()
                            + (parentFrame.getHeight() - dialog.getHeight())
                            / 2;
                    dialog.setLocation(x, y);

                    // Shows the modal dialog and waits
                    dialog.setVisible(true);
                    dialog.toFront();
                }
            }
        }
    }

    /**
     * 
     */
    protected boolean isHideLabel(mxCellState state) {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.mxgraph.swing.view.mxICellEditor#stopEditing(boolean)
     */
    public void stopEditing(boolean cancel) {
        if (editingCell != null) {
            // scrollPane.transferFocusUpCycle();
            Object cell = editingCell;
            editingCell = null;

            if (!cancel) {
                EventObject trig = trigger;
                trigger = null;
                graphComponent.labelChanged(cell, getCurrentValue(), trig);
            } else {
                mxCellState state = graphComponent.getGraph().getView()
                        .getState(cell);
                graphComponent.redraw(state);
            }

            graphComponent.requestFocusInWindow();
        }
    }

    /**
     * Gets the initial editing value for the given cell.
     */
    protected String getInitialValue(mxCellState state, EventObject trigger) {
        return graphComponent.getEditingValue(state.getCell(), trigger);
    }

    /**
     * Returns the current editing value.
     */
    public Task getCurrentValue() {
        return task;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.mxgraph.swing.view.mxICellEditor#getEditingCell()
     */
    public Object getEditingCell() {
        return editingCell;
    }

}
