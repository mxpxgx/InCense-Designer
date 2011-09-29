/**
 * 
 */
package edu.incense.designer.editor;

import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxResources;
import com.mxgraph.view.mxGraph;

import edu.incense.designer.project.Project;
import edu.incense.designer.project.ProjectPanel;
import edu.incense.designer.project.ProjectSignature;
import edu.incense.designer.project.Session;
import edu.incense.designer.task.EditorTask;
import edu.incense.designer.task.Task;
import edu.incense.designer.task.TaskRelation;
import edu.incense.designer.task.survey.JsonSurvey;
import edu.incense.designer.task.survey.Survey;

/**
 * @author mxpxgx
 * 
 */
public class InCenseEditorActions {

    /**
     * 
     * @param e
     * @return Returns the graph for the given action event.
     */
    public static final BasicGraphEditor getEditor(ActionEvent e) {
        if (e.getSource() instanceof Component) {
            Component component = (Component) e.getSource();

            while (component != null
                    && !(component instanceof BasicGraphEditor)) {
                component = component.getParent();
            }

            return (BasicGraphEditor) component;
        }

        return null;
    }

    /**
    *
    */
    @SuppressWarnings("serial")
    public static class SendAction extends AbstractAction {
        private final static String TAG = "SendAction";

        /**
        * 
        */
        public void actionPerformed(ActionEvent e) {
            BasicGraphEditor editor = getEditor(e);
            if (editor != null) {
                mxGraphComponent graphComponent = editor.getGraphComponent();
                mxGraph graph = graphComponent.getGraph();

                if (editor.getCurrentFile() == null) {
                    JOptionPane.showMessageDialog(graphComponent,
                            mxResources.get("pleaseSave"));
                } else {
                    String filename = editor.getCurrentFile().getAbsolutePath();
                    File file = new File(filename);
                    JFrame frame = (JFrame) SwingUtilities
                            .windowForComponent(editor);
                    jgraphToInCense(graph, file);
                    start(frame);
                }
            }
        }

        public void start(Window windowParent) {
            JDialog dialog = new JDialog(windowParent, "Question Editor");

            ProjectSignature signature = new ProjectSignature();
            ProjectPanel panel = new ProjectPanel(signature, dialog);
            dialog.add(panel);

            dialog.setModalityType(ModalityType.APPLICATION_MODAL);
            dialog.pack();

            // Centers inside the application frame
            int x = windowParent.getX()
                    + (windowParent.getWidth() - dialog.getWidth()) / 2;
            int y = windowParent.getY()
                    + (windowParent.getHeight() - dialog.getHeight()) / 2;
            dialog.setLocation(x, y);

            // Shows the modal dialog and waits
            dialog.setVisible(true);
            dialog.toFront();
        }

        /* *** Working directly with Graph Model *** */

        private enum SessionAttributes {
            durationMeasure, durationUnits, autoTriggered, startDate, endDate, repeatMeasure, repeatUnits, repeat, notices, sessionType
        }
        private List<Session> sessions;
        private List<Survey> surveys;
        private Map<String, String> verticesRegistry;
        private mxGraph graph;
        private ObjectMapper mapper;
        
        public void jgraphToInCense(mxGraph graph, File file){
            this.graph = graph;
            sessions = new ArrayList<Session>();
            surveys = new ArrayList<Survey>();
            verticesRegistry = new HashMap<String, String>();
            
            Object[] vertices = graph.getChildCells(graph.getDefaultParent(), true, false);
            List<Task> tasks = processChildVertices(vertices);
            Object[] edges = graph.getChildCells(graph.getDefaultParent(), false, true);
            ArrayList<TaskRelation> relations = processChildEdges(edges);
            
            Session mainSession = new Session();
            mainSession.setName("mainSession");
            mainSession.setTasks(tasks);
            mainSession.setRelations(relations);
            
            Project project = new Project();
            int sessionsSize = 0;
            if(mainSession.getTasks() != null && mainSession.getTasks().size()>0){
                project.put("mainSession", mainSession);
                sessionsSize++;
            }
            
            for(Session s: sessions){
                project.put(s.getName(), s);
            }
            sessionsSize+=sessions.size();
            
            for(Survey s: surveys){
                project.put(s.getTitle(), s);
            }
            
            project.setSessionsSize(sessionsSize);
            project.setSurveysSize(surveys.size());
            
            // Create file with .json extension
            StringBuilder sb = new StringBuilder(
                    file.getAbsolutePath());
            int i = file.getAbsolutePath().lastIndexOf(".");
            String extension = ".json";
            sb.replace(i, sb.length(), extension);
            File jsonFile = new File(sb.toString());
            
            // Write JSON object to file
            System.out.print("Writing [" + sb.toString() + "]...");
            try {
//                if(mapper == null){
//                    System.out.println("mapper is null");
//                }
//                if(jsonFile == null){
//                    System.out.println("jsonFile is null");
//                }
//                if(project == null){
//                    System.out.println("project is null");
//                }
                mapper = new ObjectMapper();
                mapper.writeValue(jsonFile, project);
            } catch (JsonGenerationException e) {
                System.err.println(TAG+": "+e);
            } catch (JsonMappingException e) {
                System.err.println(TAG+": "+e);
            } catch (IOException e) {
                System.err.println(TAG+": "+e);
            }
            System.out.println("complete.");
        }
        
        /**
         * 
         * @param cells
         */
        private ArrayList<Task> processChildVertices(Object[] cells){
            if(cells == null){
                return null;
            }
            ArrayList<Task> tasks = new ArrayList<Task>();
            for(int i=0; i<cells.length; i++){
                mxCell c = (mxCell)cells[i];
                Task t = Task.valueOf((EditorTask)c.getValue());
                switch (t.getTaskType()) {
                    default:
                        tasks.add(t);
                        break;
                    case Session:
                        Session newSession = nodeToSession(t);
                        
                        Object[] vertices = graph.getChildCells(c, true, false);
                        ArrayList<Task> newTasks = processChildVertices(vertices);
                        newSession.setTasks(newTasks);
                        
                        Object[] edges = graph.getChildCells(c, false, true);
                        ArrayList<TaskRelation> relations = processChildEdges(edges);
                        newSession.setRelations(relations);
                        
                        sessions.add(newSession);
                        break;
                    case Survey:
                        JsonSurvey jsonSurvey = new JsonSurvey(new ObjectMapper());
                        Survey survey = jsonSurvey.toSurvey(t.getExtra("survey"));
                        surveys.add(survey);
                        break;
                }
                verticesRegistry.put(c.getId(), t.getName());
            }
            return tasks;
        }
        
        private ArrayList<TaskRelation> processChildEdges(Object[] cells){
            if(cells == null){
                return null;
            }
            ArrayList<TaskRelation> relations = new ArrayList<TaskRelation>();
            for(int i=0; i<cells.length; i++){
                mxCell c = (mxCell)cells[i];
                String source = verticesRegistry.get(c.getSource().getId());
                String target = verticesRegistry.get(c.getTarget().getId());
                relations.add(new TaskRelation(source, target));
            }
            return relations;
        }
        
        private Session nodeToSession(Task t) {
            Session s = new Session();
            s.setName(t.getName());
            Map<String, String> extras = t.getExtras();
            for (Entry<String, String> entry : extras.entrySet()) {

                switch (SessionAttributes.valueOf(entry.getKey())) {
                    case durationMeasure:
                        s.setDurationMeasure(entry.getValue());
                        break;
                    case durationUnits:
                        s.setDurationUnits(Long.parseLong(entry.getValue()));
                        break;
                    case autoTriggered:
                        s.setAutoTriggered(Boolean.parseBoolean(entry.getValue()));
                        break;
                    case startDate:
                        s.setStartDate(Long.parseLong(entry.getValue()));
                        break;
                    case endDate:
                        s.setEndDate(Long.parseLong(entry.getValue()));
                        break;
                    case repeatMeasure:
                        s.setRepeatMeasure(entry.getValue());
                        break;
                    case repeatUnits:
                        s.setRepeatUnits(Integer.parseInt(entry.getValue()));
                        break;
                    case repeat:
                        s.setRepeat(Boolean.parseBoolean(entry.getValue()));
                        break;
                    case notices:
                        s.setNotices(Boolean.parseBoolean(entry.getValue()));
                        break;
                    case sessionType:
                        s.setSessionType(entry.getValue());
                        break;
                }
            }
            return s;
        }
        
        public boolean isRealParentOf(mxCell parent, mxCell child){
            return isRealParentOf(parent.getGeometry(), child.getGeometry());
        }
        
        public boolean isRealParentOf(mxGeometry parent, mxGeometry child){
            return parent.contains(child.getX(), child.getY());
            //&& parent.contains(child.getX()+child.getWidth(), child.getY()+child.getHeight());
        }

    }
}
