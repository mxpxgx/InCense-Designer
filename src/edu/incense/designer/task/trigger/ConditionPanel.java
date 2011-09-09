/**
 * 
 */
package edu.incense.designer.task.trigger;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.incense.designer.task.Output;

/**
 * @author mxpxgx
 * 
 */
public class ConditionPanel extends JPanel {
    private static final long serialVersionUID = -6331776259845822699L;
    private static final int COMPONENT_WIDTH    = 160;
    private static final int SUBCOMPONENT_WIDTH = 65;
    private static final int COMPONENT_HEIGHT   = 28;

    public enum DataType {
        BOOLEAN, NUMERIC, TEXT, DATE, DATA, TYPE
    }

    private static final String[] booleanOperators = { "is true", "is false" };
    private static final String[] textOperators = { "contains",
            "does not contain", "is", "is not", "starts with", "ends with" };
    private static final String[] numericOperators = { "is", "is not",
            "is greater than", "is less than", "is in the range" };
    private static final String[] dateOperators = { "is", "is not", "is after",
            "is before", "is in the last", "is not in the last",
            "is in the range" };
    private DataType selectedType = null;
    private int selectedOperator = -1;
    private JComboBox dataComboBox;
    private JComboBox operatorComboBox;
    private JPanel flexiblePanel;
    private JTextField valueField1;
    private JLabel toLabel;
    private JTextField valueField2;
    private JComboBox dateComboBox;
//    private JButton removeButton;
    private boolean changing;

    public ConditionPanel(Map<String, Output> data) {
        changing = false;
        addComponents(data);
    }
    
    public void setCondition(Condition c){
        
//        String[] operators = getOperatorsFor(type);
//        int op = Arrays.binarySearch(operators, c.getOperator());
//        operatorComboBox.setSelectedIndex(op);
        
        
        if( c.getData() != null){
            dataComboBox.setSelectedItem(c.getData());
        }
        if( c.getOperator() != null){
            operatorComboBox.setSelectedItem(c.getOperator());
        }
        if( c.getValue1() != null){
            valueField1.setText(c.getValue1());
        }
        if( c.getValue2() != null){
            valueField2.setText(c.getValue2());
        }
        if( c.getDate() != null){
            dateComboBox.setSelectedItem(c.getDate());
        }
        
        
    }

    public void addComponents(final Map<String, Output> data) {
        setOpaque(true);
        GridLayout layout = new GridLayout(1, 3);
        setLayout(layout);
        dataComboBox = new JComboBox(data.keySet().toArray());
        // dataComboBox.setPreferredSize(new Dimension(COMPONENT_WIDTH,
        // COMPONENT_HEIGHT));
        Output output = data.get(dataComboBox
                .getSelectedItem().toString());
        DataType selectedType = DataType.valueOf(output.getType());
        dataComboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent ie) {
                String type = data.get((String) ie.getItem()).getType();
                changeType(DataType.valueOf(type), data.get((String) ie.getItem()));
                // changeOperator(selectedOperator);
                // changeOperator(operatorComboBox.getSelectedIndex());
            }

        });

        operatorComboBox = new JComboBox();// getOperatorsFor(selectedType));
        // operatorComboBox.setPreferredSize(new Dimension(COMPONENT_WIDTH,
        // COMPONENT_HEIGHT));
        // int selectedOperator = operatorComboBox.getSelectedIndex();
        operatorComboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent ie) {
                if (!changing) {
                    changeOperator(operatorComboBox.getSelectedIndex());
                }
            }

        });
        add(dataComboBox);
        add(operatorComboBox);
        // hGroup.addComponent(dataComboBox).addComponent(operatorComboBox);
        // vGroup.addComponent(dataComboBox).addComponent(operatorComboBox);

        flexiblePanel = new JPanel();
//        BoxLayout boxLayout = new BoxLayout(flexiblePanel, BoxLayout.Y_AXIS);
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setHgap(0);
        flowLayout.setVgap(0);
        flowLayout.setAlignment(FlowLayout.LEADING);
        flexiblePanel.setLayout(flowLayout);
        // flexiblePanel.setSize(30, 400);
        valueField1 = new JTextField();
        // flexiblePanel.add(valueField1);
        toLabel = new JLabel("to");
        valueField2 = new JTextField();
        // valueField2.setPreferredSize(new Dimension(SUBCOMPONENT_WIDTH,
        // COMPONENT_HEIGHT));
        dateComboBox = new JComboBox(new String[] { "days", "weeks", "years" });
        add(flexiblePanel);
        // hGroup.addComponent(flexiblePanel);
        // vGroup.addComponent(flexiblePanel);

        // ImageIcon icon = new ImageIcon(
        // GraphEditor.class
//         .getResource("/edu/incense/designer/images/remove-icon.png"));
//        removeButton = new JButton(icon);
        // removeButton.setBackground(Color.WHITE);
        // removeButton.setUI(new BasicButtonUI());
        // add(removeButton);
        // removeButton.setSize(icon.getIconWidth(), BUTTON_HEIGHT);

        if (data.isEmpty()) {
            dataComboBox.setEnabled(false);
            operatorComboBox.setEnabled(false);
        } else {
            changeType(selectedType, output);
            // changeOperator(selectedOperator);
        }

    }

//    private String[] getOperatorsFor(DataType type) {
//        if (type == null)
//            return null;
//        switch (type) {
//        case BOOLEAN:
//            return booleanOperators;
//        case NUMERIC:
//            return numericOperators;
//        case TEXT:
//            return textOperators;
//        case DATE:
//            return dateOperators;
//        }
//        return null;
//    }

    private void changeType(DataType selectedType, Output output) {
        // Check if needs a change
        if (this.selectedType == selectedType) {
            return;
        }
        changing = true;
        operatorComboBox.removeAllItems();
        flexiblePanel.removeAll();
        valueField1.setPreferredSize(new Dimension(COMPONENT_WIDTH,
                COMPONENT_HEIGHT));

        switch (selectedType) {
        case BOOLEAN:
            addItems(operatorComboBox, booleanOperators);
            break;
        case NUMERIC:
            addItems(operatorComboBox, numericOperators);
            valueField1.setPreferredSize(new Dimension(SUBCOMPONENT_WIDTH,
                    COMPONENT_HEIGHT));
            valueField2.setPreferredSize(new Dimension(SUBCOMPONENT_WIDTH,
                    COMPONENT_HEIGHT));
            flexiblePanel.add(valueField1);
            flexiblePanel.add(toLabel);
            flexiblePanel.add(valueField2);
            break;
        case TEXT:
            addItems(operatorComboBox, textOperators);
            flexiblePanel.add(valueField1);
            break;
        case DATE:
            addItems(operatorComboBox, dateOperators);
            flexiblePanel.add(valueField1);
            break;
        case TYPE:
            addItems(operatorComboBox, output.getTypes());
            break;
        }

        this.selectedType = selectedType;
        operatorComboBox.setSelectedIndex(0);
        changing = false;
        changeOperator(operatorComboBox.getSelectedIndex());
    }

    private void changeOperator(int selectedOperator) {
        if (this.selectedOperator == selectedOperator) {
            return;
        }

        changing = true;
        switch (this.selectedType) {
        case BOOLEAN:
            break;
        case NUMERIC:
            boolean inRange = numericOperators[selectedOperator]
                    .compareTo("is in the range") == 0;
            toLabel.setVisible(inRange);
            valueField2.setVisible(inRange);
            break;
        case TEXT:
            break;
        case DATE:
            flexiblePanel.add(dateComboBox);
            if (dateOperators[selectedOperator].compareTo("is in the range") == 0) {
                flexiblePanel.remove(dataComboBox);
                flexiblePanel.add(toLabel);
                flexiblePanel.add(valueField2);
            } else if (dateOperators[selectedOperator]
                    .compareTo("is in the last") == 0
                    || dateOperators[selectedOperator]
                            .compareTo("is not in the last") == 0) {
                flexiblePanel.remove(toLabel);
                flexiblePanel.remove(valueField2);
                flexiblePanel.add(dateComboBox);
            } else {
                flexiblePanel.remove(toLabel);
                flexiblePanel.remove(valueField2);
                flexiblePanel.remove(dateComboBox);
            }
            break;
        case TYPE:
            break;
        }

        this.selectedOperator = selectedOperator;
        changing = false;
    }

    private void addItems(JComboBox cb, String[] items) {
        for (int i = 0; i < items.length; i++) {
            cb.addItem(items[i]);
        }
    }

//    public void addRemoveListener(final JList conditionsList) {
//        ActionListener removeListener = new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                conditionsList.remove(ConditionPanel.this);
//            }
//
//        };
//        removeButton.addActionListener(removeListener);
//    }

    public Condition getCondition() {
        Condition condition = new Condition();
        condition.setData(dataComboBox.getSelectedItem().toString());
        String operator = operatorComboBox.getSelectedItem().toString();
        condition.setOperator(operator);
        if (operator.compareTo("is in the range") == 0) {
            condition.setValue1(valueField1.getText());
            condition.setValue2(valueField2.getText());
        } else if (operator.compareTo("is in the last") == 0
                || operator.compareTo("is not in the last") == 0) {
            condition.setValue1(valueField1.getText());
            condition.setDate(dateComboBox.getSelectedItem().toString());
        } else {
            condition.setValue1(valueField1.getText());
        }

        return condition;
        // List<String> condition = new ArrayList<String>();
        // condition.add(dataComboBox.getSelectedItem().toString());
        // String operator = operatorComboBox.getSelectedItem().toString();
        // condition.add(operator);
        // if (operator.compareTo("is in the range") == 0) {
        // condition.add(valueField1.getText());
        // condition.add(valueField2.getText());
        // } else if (operator.compareTo("is in the last") == 0
        // || operator.compareTo("is not in the last") == 0) {
        // condition.add(valueField1.getText());
        // condition.add(dateComboBox.getSelectedItem().toString());
        // } else {
        // condition.add(valueField1.getText());
        // }

        // String[] conditionArray = new String[condition.size()];
        // return condition.toArray(conditionArray);
    }

}