package edu.incense.designer.task.session;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.util.mxResources;
import com.mxgraph.view.mxGraph;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JSpinnerDateEditor;

import edu.incense.designer.task.Task;
import edu.incense.designer.task.TaskCellEditor;
import edu.incense.designer.task.TaskEditorPanel;

/**
 * @author mxpxgx
 * 
 */
public class SessionEditorPanel extends TaskEditorPanel {
    private static final long serialVersionUID = -7235791171081830109L;
    private static final String[] SESSION_TYPES = { "User", "Automatic" };
    private static final String[] DURATION_MEASURES = { "minutes", "hours" };
    private static final String[] REPEAT_MEASURES = { "minutes", "hours",
            "days", "weeks", "months" };
    public static final String ATT_TYPE = "sessionType";
    public static final String ATT_NOTICES = "notices";
    public static final String ATT_START = "startDate";
    public static final String ATT_DURATION_UNITS = "durationUnits";
    public static final String ATT_DURATION_MEASURE = "durationMeasure";
    public static final String ATT_REPEAT = "repeat";
    public static final String ATT_REPEAT_UNITS = "repeatUnits";
    public static final String ATT_REPEAT_MEASURE = "repeatMeasure";
    public static final String ATT_END = "endDate";
//    private mxGraph graph;
//    private String matches;
    private JComboBox typeBox;
    private JCheckBox noticesCheckBox;
    private JDateChooser startChooser;
    private JSpinner durationSpinner;
    private JComboBox durationBox;
    private JCheckBox repeatCheckBox;
    private JSpinner repeatSpinner;
    private JDateChooser endChooser;
    private JComboBox repeatBox;

    public SessionEditorPanel(Window windowContainer, Task task,
            TaskCellEditor editor, mxGraph graph, mxICell cell) {
        super(windowContainer, task, editor);
//        this.graph = graph;
//        matches = "all";
        // this.cell = (mxCell)cell;
        addComponents(task, (mxCell) cell);
    }

    protected void addComponents(final Task task, mxCell cell) {
        final Map<String, String> extras = task.getExtras();

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        GroupLayout.ParallelGroup hGroup = layout
                .createParallelGroup(Alignment.CENTER);
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();

        // First 2 fields are always the same: type and name
        GroupLayout.Group hLeftGroup = layout.createParallelGroup();
        GroupLayout.Group hRightGroup = layout.createParallelGroup();

        JLabel typeLabel = new JLabel(mxResources.get("taskType") + ":");
        JLabel typeValue = new JLabel(task.getTaskType().toString());

        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
                .addComponent(typeLabel).addComponent(typeValue));
        hLeftGroup.addComponent(typeLabel);
        hRightGroup.addComponent(typeValue);

        JLabel nameLabel = new JLabel(mxResources.get("taskName") + ":");
        final JTextField nameTextField = new JTextField(10);
        nameTextField.setText(task.getName());

        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
                .addComponent(nameLabel).addComponent(nameTextField));
        hLeftGroup.addComponent(nameLabel);
        hRightGroup.addComponent(nameTextField);

        // Session specific data
        if (extras != null) {
            // Type
            JLabel sessionTypeLabel = new JLabel("Triggered by:");
            typeBox = new JComboBox(SESSION_TYPES);
            if (extras.get(ATT_TYPE) != null
                    && extras.get(ATT_TYPE).length() > 0) {
                typeBox.setSelectedItem(extras.get(ATT_TYPE));
            } else {
                typeBox.setSelectedItem(SESSION_TYPES[0]);
            }
            noticesCheckBox = new JCheckBox("Notices/Reminders");
            if (extras.get(ATT_NOTICES) != null
                    && extras.get(ATT_NOTICES).length() > 0) {
                noticesCheckBox.setSelected(Boolean.valueOf(extras
                        .get(ATT_NOTICES)));
            }

            typeBox.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    if ("User".compareTo((String) typeBox.getSelectedItem()) == 0) {
                        noticesCheckBox.setEnabled(true);
                    } else {
                        noticesCheckBox.setEnabled(false);
                    }
                }

            });

            vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(sessionTypeLabel).addComponent(typeBox)
                    .addComponent(noticesCheckBox));

            hLeftGroup.addComponent(sessionTypeLabel);
            hRightGroup.addGroup(layout.createSequentialGroup()
                    .addComponent(typeBox).addComponent(noticesCheckBox));

            // Start
            JLabel startLabel = new JLabel("Start:");
            Date date = new Date();
            startChooser = new JDateChooser(null, null, null,
                    new JSpinnerDateEditor());
            startChooser.setDateFormatString("hh:mm aa, "
                    + startChooser.getDateFormatString());
            if (extras.get(ATT_START) != null
                    && extras.get(ATT_START).length() > 0) {
                startChooser.setDate(new Date(Long.valueOf(extras
                        .get(ATT_START))));
            } else {
                startChooser.setDate(date);
            }
//            startChooser.getDateEditor().addPropertyChangeListener(
//                    new PropertyChangeListener() {
//                        @Override
//                        public void propertyChange(PropertyChangeEvent e) {
//                            if ("date".equals(e.getPropertyName())) {
//                                System.out.println(e.getPropertyName() + ": "
//                                        + (Date) e.getNewValue());
//                            }
//                        }
//                    });

            vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(startLabel).addComponent(startChooser));
            hLeftGroup.addComponent(startLabel);
            hRightGroup.addGroup(layout.createSequentialGroup().addComponent(
                    startChooser));

            // Time length (duration)
            JLabel durationLabel = new JLabel("Time length:");
            durationSpinner = new JSpinner(
                    new SpinnerNumberModel(0, 0, null, 1));
            if (extras.get(ATT_DURATION_UNITS) != null
                    && extras.get(ATT_DURATION_UNITS).length() > 0) {
                durationSpinner.setValue(Integer.valueOf(extras
                        .get(ATT_DURATION_UNITS)));
            }
            durationBox = new JComboBox(DURATION_MEASURES);
            if (extras.get(ATT_DURATION_MEASURE) != null
                    && extras.get(ATT_DURATION_MEASURE).length() > 0) {
                durationBox.setSelectedItem(extras.get(ATT_DURATION_MEASURE));
            } else {
                durationBox.setSelectedItem(DURATION_MEASURES[0]);
            }

            vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(durationLabel).addComponent(durationSpinner)
                    .addComponent(durationBox));
            hLeftGroup.addComponent(durationLabel);
            hRightGroup.addGroup(layout.createSequentialGroup()
                    .addComponent(durationSpinner).addComponent(durationBox));

            // Repeat
            repeatCheckBox = new JCheckBox("Repeat");
            final JLabel repeatLabel = new JLabel("Repeat every:");
            repeatLabel.setEnabled(false);
            repeatSpinner = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
            repeatSpinner.setEnabled(false);
            if (extras.get(ATT_REPEAT_UNITS) != null
                    && extras.get(ATT_REPEAT_UNITS).length() > 0) {
                repeatSpinner.setValue(Integer.valueOf(extras
                        .get(ATT_REPEAT_UNITS)));
            }

            repeatBox = new JComboBox(REPEAT_MEASURES);
            repeatBox.setEnabled(false);
            if (extras.get(ATT_REPEAT_MEASURE) != null
                    && extras.get(ATT_REPEAT_MEASURE).length() > 0) {
                repeatBox.setSelectedItem(extras.get(ATT_REPEAT_MEASURE));
            } else {
                repeatBox.setSelectedItem(REPEAT_MEASURES[0]);
            }

            vGroup.addComponent(repeatCheckBox);
            hLeftGroup.addComponent(repeatCheckBox);
            vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(repeatCheckBox).addComponent(repeatLabel)
                    .addComponent(repeatSpinner).addComponent(repeatBox));

            // End
            final JLabel endLabel = new JLabel("End:");
            endLabel.setEnabled(false);
            endChooser = new JDateChooser(null, null, null,
                    new JSpinnerDateEditor());
            endChooser.setDateFormatString("hh:mm aa, "
                    + endChooser.getDateFormatString());
            if (extras.get(ATT_END) != null && extras.get(ATT_END).length() > 0) {
                endChooser.setDate(new Date(Long.valueOf(extras.get(ATT_END))));
            } else {
                endChooser.setDate(date);
            }
//            endChooser.getDateEditor().addPropertyChangeListener(
//                    new PropertyChangeListener() {
//                        @Override
//                        public void propertyChange(PropertyChangeEvent e) {
//                            if ("date".equals(e.getPropertyName())) {
//                                System.out.println(e.getPropertyName() + ": "
//                                        + (Date) e.getNewValue());
//                            }
//                        }
//                    });
            endChooser.setEnabled(false);

            vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(endLabel).addComponent(endChooser));
            hLeftGroup.addComponent(endLabel);
            hRightGroup.addGroup(layout.createSequentialGroup().addComponent(
                    endChooser));

            repeatCheckBox.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    boolean isSelected = repeatCheckBox.isSelected();
                    repeatLabel.setEnabled(isSelected);
                    repeatSpinner.setEnabled(isSelected);
                    repeatBox.setEnabled(isSelected);
                    endLabel.setEnabled(isSelected);
                    endChooser.setEnabled(isSelected);
                }

            });
            if (extras.get(ATT_REPEAT) != null
                    && extras.get(ATT_REPEAT).length() > 0) {
                repeatCheckBox.setSelected(Boolean.valueOf(extras
                        .get(ATT_REPEAT)));
            } else {
                repeatCheckBox.setSelected(false);
            }

            hLeftGroup.addComponent(repeatLabel);
            hRightGroup.addGroup(layout.createSequentialGroup()
                    .addComponent(repeatSpinner).addComponent(repeatBox));
        }

        hGroup.addGroup(layout.createSequentialGroup().addGroup(hLeftGroup)
                .addGroup(hRightGroup));

        // Ok, Cancel buttons
        JButton okButton = new JButton("Ok");
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                extras.put(ATT_TYPE, String.valueOf(typeBox.getSelectedItem()));
                extras.put(ATT_NOTICES,
                        String.valueOf(noticesCheckBox.isSelected()));
                extras.put(ATT_START,
                        String.valueOf(startChooser.getDate().getTime()));
                extras.put(ATT_DURATION_UNITS,
                        String.valueOf(durationSpinner.getValue()));
                extras.put(ATT_DURATION_MEASURE,
                        String.valueOf(durationBox.getSelectedItem()));
                extras.put(ATT_REPEAT,
                        String.valueOf(repeatCheckBox.isSelected()));
                extras.put(ATT_REPEAT_UNITS,
                        String.valueOf(repeatSpinner.getValue()));
                extras.put(ATT_REPEAT_MEASURE,
                        String.valueOf(repeatBox.getSelectedItem()));
                extras.put(ATT_END,
                        String.valueOf(endChooser.getDate().getTime()));
                // task.set
                saveTrigger(task, nameTextField.getText());
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

//    private Map<String, Output> getDataAvailableFor(mxICell cell) {
//        Map<String, Output> data = new HashMap<String, Output>();
//
//        Object[] edges = graph.getIncomingEdges(cell);
//        if (edges == null) {
//            return data;
//        }
//        mxCell source;
//        Task task;
//        for (int i = 0; i < edges.length; i++) {
//            source = (mxCell) ((mxCell) edges[i]).getSource();
//            task = (Task) source.getValue();
//            data.putAll(task.getOutputs());
//            data.putAll(getDataAvailableFor(source));
//        }
//        return data;
//    }

    private void saveTrigger(Task task, String name) {
        task.setName(name);

//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode jsonNode = mapper.createObjectNode();

        // Save new task
        editor.saveNewTask(task);
    }
}
