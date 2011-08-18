/**
 * 
 */
package edu.incense.designer.task.trigger;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.util.mxResources;
import com.mxgraph.view.mxGraph;

import edu.incense.designer.GraphEditor;
import edu.incense.designer.task.Output;
import edu.incense.designer.task.Task;
import edu.incense.designer.task.TaskCellEditor;
import edu.incense.designer.task.TaskEditorPanel;

/**
 * @author mxpxgx
 * 
 */
public class TriggerEditorPanel extends TaskEditorPanel {
    private static final long serialVersionUID = -7235791171081830109L;
    // private static final int MAX_LIST_WIDTH = 250;
    // private static final int MAX_ITEM_HEIGHT = 30;
    // private static final int MAX_ROWS = 6;
    private mxGraph graph;
    private String matches;

    public TriggerEditorPanel(Window windowContainer, Task task,
            TaskCellEditor editor, mxGraph graph, mxICell cell) {
        super(windowContainer, task, editor);
        this.graph = graph;
        matches = "all";
        // this.cell = (mxCell)cell;
        addComponents(task, (mxCell) cell);
    }

    protected void addComponents(final Task task, mxCell cell) {
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

        // Trigger specific data
        // final JList conditionsList = new JList();
        final Map<String, Output> dataMap = getDataAvailableFor(cell);
        final ConditionList conditionList = new ConditionList(windowContainer,
                dataMap);
        List<Condition> conditions = loadConditions(task);
        if (!dataMap.isEmpty()) {
            if (conditions != null) {
                conditionList.addList(conditions);
                // If the conditions saved aren't applicable anymore, add one
                // default condition
                if (conditionList.getListSize() <= 0) {
                    conditionList.addItem();
                }
            } else {
                conditionList.addItem();
            }

            ImageIcon icon = new ImageIcon(
                    GraphEditor.class
                            .getResource("/edu/incense/designer/images/add-icon.png"));
            JButton addButton = new JButton("Add a rule", icon);
            // hGroup.addComponent(addButton);
            // vGroup.addComponent(addButton);

            JLabel matchLabel1 = new JLabel("Match");
            JComboBox matchesComboBox = new JComboBox(new String[] { "all",
                    "any" });
            matchesComboBox.setMaximumSize(new Dimension(80, 30));
            matchesComboBox.setSelectedItem(matches);

            matchesComboBox.addItemListener(new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent ie) {
                    matches = (String) ie.getItem();
                }

            });

            JLabel matchLabel2 = new JLabel("of the following rules:");

            vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(matchLabel1).addComponent(matchesComboBox)
                    .addComponent(matchLabel2).addComponent(addButton));
            hGroup.addGroup(layout.createSequentialGroup()
                    .addComponent(addButton).addComponent(matchLabel1)
                    .addComponent(matchesComboBox).addComponent(matchLabel2));

            JScrollPane scrollPane = new JScrollPane(conditionList);
            Dimension d = conditionList.getPreferredSize(8);
            d.setSize(d.getWidth() + 50, d.getHeight() + 5);
            scrollPane.setMaximumSize(d);
            scrollPane
                    .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

            hGroup.addComponent(scrollPane);
            vGroup.addComponent(scrollPane);
            // conditionsList.setLayoutOrientation(JList.VERTICAL);
            // conditionsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            // conditionsList.setVisibleRowCount(6);
            // conditionsList.setPreferredSize(new Dimension(MAX_LIST_WIDTH,
            // MAX_ITEM_HEIGHT * MAX_ROWS));
            //
            // ConditionPanel condition = new ConditionPanel(dataMap);
            // conditionsList.add(condition);
            // conditionsList.add(new JButton("Item 2"));
            //
            // hGroup.addComponent(conditionsList);
            // vGroup.addComponent(conditionsList);

            addButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent event) {
                    conditionList.addItem();
                }

            });
        }

        // Ok, Cancel buttons

        JButton okButton = new JButton("Ok");
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // TODO
                // String[] selectedValues =
                // (String[])conditionsList.getSelectedValues()
                // saveTrigger(task, nameTextField.getText(), conditionsList);
                saveTrigger(task, nameTextField.getText(),
                        conditionList.getList());
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

    private Map<String, Output> getDataAvailableFor(mxICell cell) {
        Map<String, Output> data = new HashMap<String, Output>();

        Object[] edges = graph.getIncomingEdges(cell);
        if (edges == null) {
            return data;
        }
        mxCell source;
        Task task;
        for (int i = 0; i < edges.length; i++) {
            source = (mxCell) ((mxCell) edges[i]).getSource();
            task = (Task) source.getValue();
            System.out.println("Found outputs:" + task.getOutputs().size());
            data.putAll(task.getOutputs());
            data.putAll(getDataAvailableFor(source));
        }
        return data;
    }

    private final static String CONDITIONS = "conditions";
    private final static String MATCHES = "matches";

    private void saveTrigger(Task task, String name, List<Condition> conditions) {
        task.setName(name);

        ObjectMapper mapper = new ObjectMapper();
        try {
            if (conditions != null && conditions.size() > 0) {
                task.putExtra(MATCHES, matches);
                String conditionsJson = mapper.writeValueAsString(conditions);
                task.putExtra(CONDITIONS, conditionsJson);
            }
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Save new task
        editor.saveNewTask(task);
    }

    private List<Condition> loadConditions(Task task) {
        ObjectMapper mapper = new ObjectMapper();
        List<Condition> conditions = null;
        try {
            if (task.getExtra(MATCHES) != null){
                matches = task.getExtra(MATCHES);
            }
            String conditionsJson = task.getExtra(CONDITIONS);
            if (conditionsJson == null) {
                return null;
            }

            conditions = mapper.readValue(conditionsJson,
                    new TypeReference<List<Condition>>() {
                    });
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return conditions;
    }
}
