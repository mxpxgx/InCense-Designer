/**
 * 
 */
package edu.incense.designer.task;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.util.mxResources;
import com.mxgraph.view.mxGraph;

import edu.incense.designer.task.trigger.ConditionPanel.DataType;

/**
 * @author mxpxgx
 * 
 */
public class SinkEditorPanel extends TaskEditorPanel {
    private static final long serialVersionUID = -7235791171081830109L;
//    private static final int MAX_LIST_WIDTH = 250;
//    private static final int MAX_ITEM_HEIGHT = 30;
//    private static final int MAX_ROWS = 6;
    private mxGraph graph;
    // private mxCell cell;
    private static final String[] FILE_FORMATS = { "JSON", "XML", "CVS" };
    private Map<String, JCheckBox> checkList;

    public SinkEditorPanel(Window windowContainer, Task task,
            TaskCellEditor editor, mxGraph graph, mxICell cell) {
        super(windowContainer, task, editor);
        this.graph = graph;
        // this.cell = (mxCell)cell;
        addComponents(task, (mxCell) cell);
    }

    @Override
    protected void addComponents(final Task task) {

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

        JLabel formatLabel = new JLabel(mxResources.get("fileFormat") + ":");
        final JComboBox formatComboBox = new JComboBox(FILE_FORMATS);
        formatComboBox.setSelectedItem(task.getExtra(ATT_SELECTED_FORMAT,
                FILE_FORMATS[0]));

        vGroup.addGroup(layout.createParallelGroup().addComponent(formatLabel)
                .addComponent(formatComboBox));
        hLeftGroup.addComponent(formatLabel);
        hRightGroup.addComponent(formatComboBox);

        hGroup.addGroup(layout.createSequentialGroup().addGroup(hLeftGroup)
                .addGroup(hRightGroup));

        // Sink specfic data

        Set<String> data = getDataAvailableFor(cell).keySet();
        // Vector<Object> values = new Vector<Object>();
        List<Object> objects = new ArrayList<Object>();
        Collections.addAll(objects, data.toArray());
        List<String> values = new ArrayList<String>();
        for (Object o : objects) {
            values.add(o.toString());
        }
        Collections.sort(values);

        checkList = new HashMap<String, JCheckBox>();
        // for (String s : values) {
        // checkList.put(s, new JCheckBox(s));
        // }

        int n = values.size();
        String name;
        JCheckBox cb;
        GroupLayout.Group rowGroup;
        GroupLayout.Group colGroup1 = layout.createParallelGroup();
        GroupLayout.Group colGroup2 = layout.createParallelGroup();
        GroupLayout.Group colGroup3 = layout.createParallelGroup();
        for (int i = 0; i < n; i = i + 3) {
            rowGroup = layout.createParallelGroup();
            if (i < n) {
                name = values.get(i).toString();
                cb = new JCheckBox(name);
                checkList.put(name, cb);
                rowGroup.addComponent(cb);
                colGroup1.addComponent(cb);
            }
            if (i + 1 < n) {
                name = values.get(i + 1).toString();
                cb = new JCheckBox(name);
                checkList.put(name, cb);
                rowGroup.addComponent(cb);
                colGroup2.addComponent(cb);
            }
            if (i + 2 < n) {
                name = values.get(i + 2).toString();
                cb = new JCheckBox(name);
                checkList.put(name, cb);
                rowGroup.addComponent(cb);
                colGroup3.addComponent(cb);
            }
            vGroup.addGroup(rowGroup);
        }
        hGroup.addGroup(layout.createSequentialGroup().addGroup(colGroup1)
                .addGroup(colGroup2).addGroup(colGroup3));

        // final JList dataList = new JList(values);
        // dataList.setLayoutOrientation(JList.VERTICAL);
        // dataList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        // dataList.setVisibleRowCount(6);
        // dataList.setPreferredSize(new Dimension(MAX_LIST_WIDTH,
        // MAX_ITEM_HEIGHT
        // * MAX_ROWS));
        //
        // String[] selectedData = loadSelectedData(task);
        // if (selectedData != null) {
        // int[] indices = new int[selectedData.length];
        // // DefaultListModel model = (DefaultListModel)(dataList.getModel());
        // for (int i = 0; i < selectedData.length; i++) {
        // indices[i] = values.indexOf(selectedData[i]);
        // }
        // dataList.setSelectedIndices(indices);
        // } else {
        // System.out.println("Array was null");
        // }

        String[] selectedData = loadSelectedData(task);
        if (selectedData != null) {
            for (int i = 0; i < selectedData.length; i++) {
                if (checkList.containsKey(selectedData[i]))
                    checkList.get(selectedData[i]).setSelected(true);
            }
        } else {
            System.out.println("Array was null");
        }

        // hGroup.addComponent(dataList);
        // vGroup.addComponent(dataList);

        JButton okButton = new JButton("Ok");
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // Object[] values = dataList.getSelectedValues();
                // String[] stringValues = new String[values.length];
                // for (int i = 0; i < values.length; i++) {
                // stringValues[i] = (String) values[i];
                // }
                List<String> selectedList = new ArrayList<String>();
                for (JCheckBox cb : checkList.values()) {
                    if (cb.isSelected()) {
                        selectedList.add(cb.getText());
                    }
                }
                String[] selectedValues = new String[selectedList.size()];
                selectedValues = selectedList.toArray(selectedValues);

                saveSink(task, nameTextField.getText(),
                        (String)formatComboBox.getSelectedItem(),
                        selectedValues);
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
            data.putAll(task.getOutputs());
            data.putAll(getDataAvailableFor(source));
        }
        return data;
    }

    private static final String ATT_SELECTED_DATA = "selectedData";
    private static final String ATT_SELECTED_FORMAT = "selectedFormat";

    private String[] loadSelectedData(Task task) {
        String sdString = task.getExtra(ATT_SELECTED_DATA, "");
        ObjectMapper mapper = new ObjectMapper();
        if (sdString == null || sdString.length() <= 0) {
            return null;
        }
        String[] stringList = null;
        try {
            JsonNode node = mapper.readTree(sdString);
            List<String> list = mapper.readValue(node,
                    new TypeReference<List<String>>() {
                    });
            if (list == null)
                return null;
            stringList = new String[list.size()];
            stringList = list.toArray(stringList);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringList;
    }

    private void saveSink(Task task, String name, String format,
            String[] selectedData) {
        task.setName(name);
        task.putExtra(ATT_SELECTED_FORMAT, format);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.valueToTree(selectedData);
        // JsonNode jsonNode = mapper.createArrayNode();
        // for(int i=0; i<selectedData.length; i++){
        // ((ArrayNode) jsonNode).add(selectedData[i]);
        // }
        String json;
        try {
            json = mapper.writeValueAsString(node);
            task.putExtra(ATT_SELECTED_DATA, json);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Add outputs
        HashMap<String, Output> outputs = new HashMap<String, Output>();
        for (int i = 0; i < selectedData.length; i++) {
            outputs.put(selectedData[i], new Output(selectedData[i],DataType.NUMERIC.toString()));
        }
        task.setOutputs(outputs);

        // Save new task
        editor.saveNewTask(task);
    }
}
