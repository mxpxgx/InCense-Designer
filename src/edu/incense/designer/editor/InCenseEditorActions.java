/**
 * 
 */
package edu.incense.designer.editor;

import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxIGraphModel;
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
import edu.incense.designer.task.TaskType;
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
                mxIGraphModel model = graph.getModel();

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

        public String toString(File file) {
            String ls = System.getProperty("line.separator");

            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));

                String line = null;
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append(ls);
                }
                reader.close();
                return stringBuilder.toString();

            } catch (FileNotFoundException e) {
                System.err.println(TAG + ": [" + file + "] file wasn't found. "
                        + ls + e);
                return null;
            } catch (IOException e) {
                System.err.println(TAG + ": [" + file
                        + "] file line couldn't be read. " + ls + e);
                return null;
            }
        }

        private final static String ROOT_NODE = "root";
        private final static String TASK_NODE = "edu.incense.designer.task.Task";
        private final static String ATT_ID = "id";
        private final static String ATT_TYPE = "type";
        private final static String ATT_VERTEX = "vertex";
        private final static String ATT_EDGE = "edge";
        private final static String ATT_ARRAY = "Array";
        private final static String ATT_ADD = "add";
        private final static String ATT_AS = "as";
        private final static String ATT_VALUE = "value";
        private ObjectMapper mapper;

        public String formatToInCenseJson(String json) {
            mapper = new ObjectMapper();
            List<JsonNode> nodes = null;
            try {
                JsonNode node = mapper.readTree(json);
                nodes = mapper.readValue(node.findValue(ROOT_NODE),
                        new TypeReference<List<JsonNode>>() {
                        });
            } catch (JsonProcessingException e) {
                System.err.println(getClass().getName()
                        + ": Parsing JSON file failed, " + e);
                return null;
            } catch (IOException e) {
                System.err.println(getClass().getName()
                        + ": Parsing JSON file failed, " + e);
                return null;
            }

            List<Session> sessions = new ArrayList<Session>();
            Map<Integer, Task> tasks = new HashMap<Integer, Task>();
            List<JsonNode> edges = new ArrayList<JsonNode>();

            for (JsonNode node : nodes) {
                // Get the id of this task in JGraph, this would be useful to
                // create relations (edges) between tasks
                int id = node.get(ATT_ID).getValueAsInt();

                boolean vertex = node.get(ATT_VERTEX).getValueAsBoolean(false);
                boolean edge = node.get(ATT_EDGE).getValueAsBoolean(false);

                if (vertex) {
                    JsonNode task = node.get(TASK_NODE);
                    String type = task.get(ATT_TYPE).getValueAsText();
                    switch (TaskType.valueOf(type)) {
                    default:
                        System.err.println("Unknown vertex/task type: \n"
                                + task.getValueAsText());
                        break;
                    case Session:
                        Session session = nodeToSession(task);
                        sessions.add(session);
                        // tasks.put(Integer.valueOf(task.get(ATT_ID).getValueAsInt()),
                        // session);
                        break;
                    }
                } else if (edge) {
                    edges.add(node);
                }
            }

            Project project = new Project();

            // Check session have valid tasks

            // Check every relation is valid
            Map<Integer, TaskRelation> relations = new HashMap<Integer, TaskRelation>();

            return "";
        }

        private enum SessionAttributes {
            durationMeasure, durationUnits, autoTriggered, startDate, endDate, repeatMeasure, repeatUnits, repeat, notices, sessionType
        }

        /**
         * @param sn
         *            (Session Node)
         * @return
         */
        private Session nodeToSession(JsonNode sn) {
            List<JsonNode> attributes = null;
            try {
                attributes = mapper.readValue(sn.get(ATT_ARRAY).get(ATT_ADD),
                        new TypeReference<List<JsonNode>>() {
                        });
            } catch (JsonParseException e) {
                System.err.println(getClass().getName()
                        + ": Parsing JSON file failed, " + e);
                return null;
            } catch (JsonMappingException e) {
                System.err.println(getClass().getName()
                        + ": Parsing JSON file failed, " + e);
                return null;
            } catch (IOException e) {
                System.err.println(getClass().getName()
                        + ": Parsing JSON file failed, " + e);
                return null;
            }

            Session s = new Session();
            for (JsonNode a : attributes) {
                String value = a.get(ATT_VALUE).getValueAsText();
                String as = a.get(ATT_AS).getValueAsText();

                switch (SessionAttributes.valueOf(as)) {
                    case durationMeasure:
                        s.setDurationMeasure(value);
                        break;
                    case autoTriggered:
                        s.setAutoTriggered(Boolean.parseBoolean(value));
                        break;
                    case startDate:
                        s.setStartDate(Long.parseLong(value));
                        break;
                    case endDate:
                        s.setEndDate(Long.parseLong(value));
                        break;
                    case repeatMeasure:
                        s.setRepeatMeasure(value);
                        break;
                    case repeatUnits:
                        s.setRepeatUnits(Integer.parseInt(value));
                        break;
                    case repeat:
                        s.setRepeat(Boolean.parseBoolean(value));
                        break;
                    case notices:
                        s.setNotices(Boolean.parseBoolean(value));
                        break;
                }

            }

            return s;
        }
        
        /* *** Working directly with Graph Model *** */
        
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
        
        private List<Session> sessions;
        private List<Survey> surveys;
//        private List<Task> tasks;
        private Map<String, String> verticesRegistry;
//        private List<mxCell> edges;
        private mxGraph graph;
        
        public void jgraphToInCense(mxGraph graph, File file){
            this.graph = graph;
            sessions = new ArrayList<Session>();
            surveys = new ArrayList<Survey>();
//            tasks = new ArrayList<Task>();
            verticesRegistry = new HashMap<String, String>();
//            edges = new ArrayList<mxCell>();
            
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
                System.err.println(e);
            } catch (JsonMappingException e) {
                System.err.println(e);
            } catch (IOException e) {
                System.err.println(e);
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
        
        
        public boolean isRealParentOf(mxCell parent, mxCell child){
            return isRealParentOf(parent.getGeometry(), child.getGeometry());
        }
        
        public boolean isRealParentOf(mxGeometry parent, mxGeometry child){
            return parent.contains(child.getX(), child.getY());
            //&& parent.contains(child.getX()+child.getWidth(), child.getY()+child.getHeight());
        }

    }
}
