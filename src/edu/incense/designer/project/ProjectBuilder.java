/**
 * 
 */
package edu.incense.designer.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import edu.incense.designer.editor.BasicGraphEditor;
import edu.incense.designer.task.Task;
import edu.incense.designer.task.TaskRelation;
import edu.incense.designer.task.TaskType;
import edu.incense.designer.task.session.SessionEditorPanel;

/**
 * @author mxpxgx
 *
 */
public class ProjectBuilder {
    
    
    public void saveProject(Project project, ProjectSignature signature){

    }
    
    public ProjectSignature buildSignature(String name, List<String> tasks){
        ProjectSignature signature = new ProjectSignature();
        signature.setName(name);
        signature.setTasks(tasks);
        Date date = new Date();
        signature.setTimestamp(date.getTime());
        return signature;
    }
    
    public Project buildProject(BasicGraphEditor editor){
        mxGraphComponent graphComponent = editor.getGraphComponent();
        mxGraph graph = graphComponent.getGraph();
        mxGraphModel model = (mxGraphModel)graph.getModel();
        
        //Search for the Session tasks
        List<Object> cells = new ArrayList<Object>(model.getCells().values());
//        List<Session> sessions = new ArrayList<Session>();
        Session session;
        for(Object o: cells){
            Task task = (Task)model.getValue(o);
            if(task.getTaskType() == TaskType.Session){
                session = createSession(o, task);
                List<Task> tasks = getTasksFor(o);
                List<TaskRelation> relations = getRelationsFor(o);
                session.setTasks(tasks);
                session.setRelations(relations);
            }
        }
        
        //
        
        
        
        //Generate Session object (get vertices and connections)
        
        Project project = new Project();
        
        
        return project;
    }
    
    private Session createSession(Object cell, Task task){        
        
        if(task.getTaskType() != TaskType.Session){
            return null;
        }
        
        Session session = new Session();
        
        // Session type
        String type = task.getExtra(SessionEditorPanel.ATT_TYPE, "User");
        if(type.compareTo("User") == 0) {
            session.setAutoTriggered(false);
        } else if (type.compareTo("Automatic") == 0) {
            session.setAutoTriggered(true);
        }
        
        //Notices
        boolean notices = task.getExtra(SessionEditorPanel.ATT_NOTICES, false);
        session.setNotices(notices);  
        
        // Duration (in milliseconds)
        long duration = 0;
        int units = task.getExtra(SessionEditorPanel.ATT_DURATION_UNITS, 0);
        String measure = task.getExtra(SessionEditorPanel.ATT_DURATION_MEASURE, "minutes");
        if(measure.compareTo("minutes") == 0){
            duration = units * 1000L * 60; //milliseconds
        } else if(measure.compareTo("hours") == 0){
            duration = units * 1000L * 60 * 60; //milliseconds
        }
//        session.setDurationMeasure(duration);
        
        //Start date
        long startDate = task.getExtra(SessionEditorPanel.ATT_START, 0L);
        session.setStartDate(startDate);
        
        //End date
        long endDate = task.getExtra(SessionEditorPanel.ATT_END, 0L);
        session.setEndDate(endDate);
        
        //Repeat
        boolean repeat = task.getExtra(SessionEditorPanel.ATT_REPEAT, false);
        session.setRepeat(repeat);       
        
        int repeatUnits = task.getExtra(SessionEditorPanel.ATT_REPEAT_UNITS, 0);
        session.setRepeatUnits(repeatUnits);
        
        String repeatType = task.getExtra(SessionEditorPanel.ATT_DURATION_MEASURE, "minutes");
        repeatType = measure.toUpperCase();
        session.setRepeatMeasure(repeatType);
        
        //Tasks and relations TODO
//        session.setTasks(tasks)
//        session.setRelations(relations)
        
        return session;
    }
    
    private List<Task> getTasksFor(Object cell){
        List<Task> tasks = new ArrayList<Task>();
//         cell TODO
        
        
        return tasks;
    }
    
    private List<TaskRelation> getRelationsFor(Object cell){
        List<TaskRelation> tasks = new ArrayList<TaskRelation>();
        return tasks;
    }
}
