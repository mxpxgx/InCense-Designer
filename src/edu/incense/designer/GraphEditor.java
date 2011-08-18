/**
 * $Id: GraphEditor.java,v 1.8 2011-02-14 15:45:58 gaudenz Exp $
 * Copyright (c) 2006-2010, Gaudenz Alder, David Benson
 */
package edu.incense.designer;

import java.awt.Color;
import java.awt.Point;
import java.net.URL;
import java.text.NumberFormat;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import org.w3c.dom.Document;

import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxGraphTransferable;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;

import edu.incense.designer.editor.BasicGraphEditor;
import edu.incense.designer.editor.EditorMenuBar;
import edu.incense.designer.editor.EditorPalette;
import edu.incense.designer.task.Task;
import edu.incense.designer.task.TaskCellEditor;
import edu.incense.designer.task.TaskType;
import edu.incense.designer.task.survey.Question;
import edu.incense.designer.task.survey.QuestionType;
import edu.incense.designer.task.survey.Survey;
import edu.incense.designer.task.trigger.ConditionPanel.DataType;

public class GraphEditor extends BasicGraphEditor {
    private static final long serialVersionUID = -4601740824088314699L;

    /**
     * Holds the shared number formatter.
     * 
     * @see NumberFormat#getInstance()
     */
    public static final NumberFormat numberFormat = NumberFormat.getInstance();

    /**
     * Holds the URL for the icon to be used as a handle for creating new
     * connections. This is currently unused.
     */
    public static URL url = null;

    // GraphEditor.class.getResource("/edu/incense/designer/images/connector.gif");

    public GraphEditor() {
        this("InCense Editor", new CustomGraphComponent(new mxGraph()));
    }

    /**
	 * 
	 */
    public GraphEditor(String appTitle, mxGraphComponent component) {
        super(appTitle, component);
        component.setAutoExtend(true);
        final mxGraph graph = graphComponent.getGraph();

        // Creates the shapes palette
        EditorPalette componentsPalette = insertPalette(mxResources
                .get("components"));
        EditorPalette filtersPalette = insertPalette(mxResources.get("filters"));
        EditorPalette sensorsPalette = insertPalette(mxResources.get("sensors"));
        EditorPalette surveysPalette = insertPalette(mxResources.get("surveys"));

        // Set straight line as default connector
        // mxGeometry geometry = new mxGeometry(0, 0, 120, 120);
        // geometry.setTerminalPoint(new mxPoint(0, 120), true); // source
        // geometry.setTerminalPoint(new mxPoint(120, 0), false); // not source
        // geometry.setRelative(true);
        // mxCell cell = new mxCell("", geometry, "straight");
        // cell.setEdge(true);

        // Sets the edge template to be used for creating new edges if an edge
        // is clicked in the shape palette
        componentsPalette.addListener(mxEvent.SELECT, new mxIEventListener() {
            public void invoke(Object sender, mxEventObject evt) {
                Object tmp = evt.getProperty("transferable");

                if (tmp instanceof mxGraphTransferable) {
                    mxGraphTransferable t = (mxGraphTransferable) tmp;
                    Object cell = t.getCells()[0];

                    if (graph.getModel().isEdge(cell)) {
                    }
                }
            }

        });

        /* COMPONENTS */
        Task task = new Task(TaskType.Session, "New session");
        task.putExtra("sessionType", "User");

        componentsPalette
                .addTemplate(
                        "Session",
                        new ImageIcon(
                                GraphEditor.class
                                        .getResource("/edu/incense/designer/images/swimlane.png")),
                        "swimlane", 280, 280, task);

        // task = new Task(TaskType.CustomizableFilter, "Filter");
        //
        // componentsPalette
        // .addTemplate(
        // "Filter",
        // new ImageIcon(
        // GraphEditor.class
        // .getResource("/edu/incense/designer/images/rectangle.png")),
        // null, 160, 120, task);

        task = new Task(TaskType.Stop, "Stop");

        componentsPalette
                .addTemplate(
                        "Stop",
                        new ImageIcon(
                                GraphEditor.class
                                        .getResource("/edu/incense/designer/images/cancel_end.png")),
                        "roundImage;image=/edu/incense/designer/images/cancel_end.png",
                        80, 80, task);

        task = new Task(TaskType.Sink, "Sink");

        componentsPalette
                .addTemplate(
                        "Sink",
                        new ImageIcon(
                                GraphEditor.class
                                        .getResource("/edu/incense/designer/images/doubleellipse.png")),
                        "ellipse;shape=doubleEllipse", 120, 120, task);

        task = new Task(TaskType.Trigger, "Trigger");
        task.setDescription("Triggers can receive raw data from sensors or other types of data coming from components as input. Triggers look for certain conditions in their inputs. When these conditions are met, they start programmed sessions or surveys.");

        componentsPalette
                .addTemplate(
                        "Trigger",
                        new ImageIcon(
                                GraphEditor.class
                                        .getResource("/edu/incense/designer/images/triangle.png")),
                        "triangle", 120, 120, task);

        // task = new Task(TaskType.Sensor, "Sensor");

        // componentsPalette
        // .addTemplate(
        // "Sensor",
        // new ImageIcon(
        // GraphEditor.class
        // .getResource("/edu/incense/designer/images/rhombus.png")),
        // "rhombus", 160, 160, task);

        componentsPalette
                .addEdgeTemplate(
                        "Connector",
                        new ImageIcon(
                                GraphEditor.class
                                        .getResource("/edu/incense/designer/images/straight.png")),
                        "straight", 120, 120, "");

        /* FILTERS */

        task = new Task(TaskType.HomeLocationFilter, "Home Location");
        task.putOutput("isHome", DataType.BOOLEAN.toString(), "Indicates if the user is at home or not", "<i>i.e.</i> \"true\" or \"false\"");

        filtersPalette
                .addTemplate(
                        "Home Location",
                        new ImageIcon(
                                GraphEditor.class
                                        .getResource("/edu/incense/designer/images/rectangle.png")),
                        "rectangle", 120, 100, task);

        task = new Task(TaskType.ShakeFilter, "Shake");
        task.putOutput("isShake", DataType.BOOLEAN.toString(), "Indicates if a shake is detected", "<i>i.e.</i> \"true\" or \"false\"");
        task.putOutput("velocity", DataType.NUMERIC.toString(), "Velocity measured by accelerometers", "Type: integer value");

        filtersPalette
                .addTemplate(
                        "Shake",
                        new ImageIcon(
                                GraphEditor.class
                                        .getResource("/edu/incense/designer/images/rectangle.png")),
                        "rectangle", 120, 100, task);

        task = new Task(TaskType.TransportationModeFilter, "Transportation Mode");
        task.putOutput("transportationMode", DataType.TEXT.toString(), "Tells the transportation mode infered", "<i>e.g.</i> \"car\", \"bus\", \"walking\"");

        filtersPalette
                .addTemplate(
                        "Transportation Mode",
                        new ImageIcon(
                                GraphEditor.class
                                        .getResource("/edu/incense/designer/images/rectangle.png")),
                        "rectangle", 120, 100, task);

        task = new Task(TaskType.StepsFilter, "Steps");
        task.putOutput("steps", DataType.NUMERIC.toString(), "number of steps infered", "Type: integer value");

        filtersPalette
                .addTemplate(
                        "Steps",
                        new ImageIcon(
                                GraphEditor.class
                                        .getResource("/edu/incense/designer/images/rectangle.png")),
                        "rectangle", 120, 100, task);

        /* SENSORS */
        task = new Task(TaskType.Sensor, mxResources.get("sensorAcc"));
        task.putOutput("xAxis", DataType.NUMERIC.toString(), "Value in axis X", "Type: float values from 0 to 1");
        task.putOutput("yAxis", DataType.NUMERIC.toString(), "Value in axis Y (float numbers from 0 to 1)", "Type: float values from 0 to 1");
        task.putOutput("zAxis", DataType.NUMERIC.toString(), "Value in axis Z (float numbers from 0 to 1)", "Type: float values from 0 to 1");

        sensorsPalette
                .addTemplate(
                        mxResources.get("sensorAcc"),
                        new ImageIcon(
                                GraphEditor.class
                                        .getResource("/edu/incense/designer/images/rhombus_acc.png")),
                        "rhombusAcc", 160, 160, task);

        task = new Task(TaskType.Sensor, mxResources.get("sensorAudio"));
        task.putExtra("sampleFrequency", "40");
        task.putOutput("audioFrame", DataType.DATA.toString(), "Frames in bytes", "<i>e.g.</i> N/A");

        sensorsPalette
                .addTemplate(
                        mxResources.get("sensorAudio"),
                        new ImageIcon(
                                GraphEditor.class
                                        .getResource("/edu/incense/designer/images/rhombus_audio.png")),
                        "rhombusAudio", 160, 160, task);

        task = new Task(TaskType.Sensor, mxResources.get("sensorBluetooth"));
        task.putOutput("address", DataType.TEXT.toString(), "Hardware address", "<i>e.g.</i> \"00:11:22:AA:BB:CC\"");
        task.putOutput("state", DataType.NUMERIC.toString(), "The bond state of the remote device", "<i>e.g.</i> BOND_NONE (10), BOND_BONDING(11) or BOND_BONDED(12)");
        task.putOutput("name", DataType.TEXT.toString(), "The friendly Bluetooth name of the remote device", "e.g. \"MyBluetoothDevice\"");
        task.putOutput("class", DataType.NUMERIC.toString(), "The Bluetooth class of the remote device", "<i>e.g.</i> \"260\" (computer desktop)");
        task.putOutput("majorClass", DataType.NUMERIC.toString(), "The major device class component", "<i>e.g.</i> \"256\" (computer)");

        sensorsPalette
                .addTemplate(
                        mxResources.get("sensorBluetooth"),
                        new ImageIcon(
                                GraphEditor.class
                                        .getResource("/edu/incense/designer/images/rhombus_bluetooth.png")),
                        "rhombusBluetooth", 160, 160, task);

        task = new Task(TaskType.Sensor, mxResources.get("sensorGps"));
        task.putOutput("altitude", DataType.NUMERIC.toString(), "Altitude of the fix", "<i>e.g.</i> \"31.873254\"");
        task.putOutput("latitude", DataType.NUMERIC.toString(), "Latitude of the fix", "<i>e.g.</i> \"-116.633913\"");
        task.putOutput("longitude", DataType.NUMERIC.toString(), "Longiturde of the fix", "<i>e.g.</i> \"12.406\"");
        task.putOutput("accuracy", DataType.NUMERIC.toString(), "The accuracy of the fix in meters", "Type: integer value");
        task.putOutput("satellites", DataType.NUMERIC.toString(), "Satellites used for the fix", "Type: integer value");

        sensorsPalette
                .addTemplate(
                        mxResources.get("sensorGps"),
                        new ImageIcon(
                                GraphEditor.class
                                        .getResource("/edu/incense/designer/images/rhombus_gps.png")),
                        "rhombusGps", 140, 140, task);

        task = new Task(TaskType.Sensor, mxResources.get("sensorGyro"));

        sensorsPalette
                .addTemplate(
                        mxResources.get("sensorGyro"),
                        new ImageIcon(
                                GraphEditor.class
                                        .getResource("/edu/incense/designer/images/rhombus_gyro.png")),
                        "rhombusGyro", 160, 160, task);

        task = new Task(TaskType.Sensor, mxResources.get("sensorWifi"));
        task.putOutput("address", DataType.TEXT.toString(), "The address of the access point (bssid).", "<i>e.g.</i> \"02:00:01:02:03:04\"");
        task.putOutput("name", DataType.TEXT.toString(), "The network name (ssid)", "<i>e.g.</i> \"myNetwork\"");
        task.putOutput("capabilities", DataType.TEXT.toString(), "Describes the authentication, key management, and encryption schemes supported by the access point.", "<i>e.g.</i> N/A");
        task.putOutput("frequency", DataType.NUMERIC.toString(), "The frequency in MHz of the channel over which the client is communicating with the access point.", "<i>e.g.</i> \"1200\" Mhz");
        task.putOutput("level", DataType.NUMERIC.toString(), "The detected signal level in dBm.", "<i>e.g.</i> \"23\" dBm");

        sensorsPalette
                .addTemplate(
                        mxResources.get("sensorWifi"),
                        new ImageIcon(
                                GraphEditor.class
                                        .getResource("/edu/incense/designer/images/rhombus_wifi.png")),
                        "rhombusWifi", 160, 160, task);

        task = new Task(TaskType.Sensor, mxResources.get("sensorCalls"));
        task.putOutput("phoneNumber", DataType.NUMERIC.toString(), "Called/caller phone number", "<i>e.g.</i> \"6461808080\"");

        sensorsPalette
                .addTemplate(
                        mxResources.get("sensorCalls"),
                        new ImageIcon(
                                GraphEditor.class
                                        .getResource("/edu/incense/designer/images/rhombus_calls.png")),
                        "rhombusCalls", 160, 160, task);

        task = new Task(TaskType.Sensor, mxResources.get("sensorStates"));
        task.putOutput("phoneState", DataType.TEXT.toString(), "Phone current state", "<i>e.g.</i> \"charging\"");

        sensorsPalette
                .addTemplate(
                        mxResources.get("sensorStates"),
                        new ImageIcon(
                                GraphEditor.class
                                        .getResource("/edu/incense/designer/images/rhombus_states.png")),
                        "rhombusStates", 160, 160, task);

        /* SURVEYS */

        task = new Task(TaskType.Survey, "Wandering Mind");
        Survey survey = getTestSurvey();
        task.putExtra("survey", survey.toJsonString());
        task.putOutput("answers", DataType.TEXT.toString(), "Answers from a survey", "Type: JSON content");

        surveysPalette
                .addTemplate(
                        "Wandering",
                        new ImageIcon(
                                GraphEditor.class
                                        .getResource("/edu/incense/designer/images/rounded.png")),
                        "rounded=1", 160, 120, task);

    }

    /**
	* 
	*/
    public static class CustomGraphComponent extends mxGraphComponent {
        private static final long serialVersionUID = -6833603133512882012L;

        /**
         * 
         * @param graph
         */
        public CustomGraphComponent(mxGraph graph) {
            super(graph);
            this.setCellEditor(new TaskCellEditor(this));

            getGraph().setSwimlaneNesting(false);
            getGraph().setAllowLoops(false);
            // Sets switches typically used in an editor
            // setPageVisible(true);
            // getPageFormat().setOrientation(PageFormat.LANDSCAPE);
            // setPreferPageSize(true);
            // setPanning(true);
            // setAutoExtend(true);
            // setGridVisible(true);
            setToolTips(true);
            getConnectionHandler().setCreateTarget(false); // DONT AUTOGENERATE
                                                           // NEW CELLS WHEN
                                                           // CONNECTION DOESN'T
                                                           // HAVE AND END!

            // Loads the defalt stylesheet from an external file
            mxCodec codec = new mxCodec();
            Document doc = mxUtils.loadDocument(GraphEditor.class.getResource(
                    "/edu/incense/designer/resources/default-style.xml")
                    .toString());
            codec.decode(doc.getDocumentElement(), graph.getStylesheet());

            // Sets the background to white
            getViewport().setOpaque(true);
            getViewport().setBackground(Color.WHITE);
        }

        /**
         * Overrides drop behaviour to set the cell style if the target is not a
         * valid drop target and the cells are of the same type (eg. both
         * vertices or both edges).
         */
        public Object[] importCells(Object[] cells, double dx, double dy,
                Object target, Point location) {
            if (target == null && cells.length == 1 && location != null) {
                target = getCellAt(location.x, location.y);

                if (target instanceof mxICell && cells[0] instanceof mxICell) {
                    mxICell targetCell = (mxICell) target;
                    mxICell dropCell = (mxICell) cells[0];

                    if (targetCell.isVertex() == dropCell.isVertex()
                            || targetCell.isEdge() == dropCell.isEdge()) {
                        mxIGraphModel model = graph.getModel();
                        model.setStyle(target, model.getStyle(cells[0]));
                        graph.setSelectionCell(target);

                        return null;
                    }
                }
            }

            return super.importCells(cells, dx, dy, target, location);
        }

    }

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        mxConstants.SHADOW_COLOR = Color.LIGHT_GRAY;
        mxConstants.W3C_SHADOWCOLOR = "#D3D3D3";

        GraphEditor editor = new GraphEditor();
        editor.createFrame(new EditorMenuBar(editor)).setVisible(true);
    }

    private Survey getTestSurvey() {
        // Survey
        Survey survey = new Survey();
        survey.setId(101);
        survey.setTitle("Demo: Avance 2");

        // String[] options = { "Strongly agree", "Agree", "Neutral",
        // "Disagree",
        // "Strongly disagree" };

        Question question = new Question();
        question.setQuestion("How are you feeling right now?");
        question.setType(QuestionType.SEEKBAR);
        question.setSkippable(false);
        String[] options = { "Bad", "Good" };
        question.setOptions(options);
        int[] nextQuestions1 = { 1 };
        question.setNextQuestions(nextQuestions1);
        survey.add(question);

        question = new Question();
        question.setQuestion("What are you doing right now?");
        question.setType(QuestionType.OPENTEXT);
        question.setSkippable(false);
        int[] nextQuestions2 = { 2 };
        question.setNextQuestions(nextQuestions2);
        survey.add(question);

        question = new Question();
        question.setQuestion("Are you thinking about something other than what you’re currently doing?");
        question.setType(QuestionType.RADIOBUTTONS);
        question.setSkippable(false);
        String[] options2 = { "No", "Yes, something pleasant",
                "Yes, something neutral", "Yes, something unpleasant" };
        question.setOptions(options2);
        int[] nextQuestions3 = { 0, 0, 0, 0 };
        question.setNextQuestions(nextQuestions3);
        survey.add(question);
        return survey;
    }
}
