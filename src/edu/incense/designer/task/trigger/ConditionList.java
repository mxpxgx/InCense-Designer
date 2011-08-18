/**
 * 
 */
package edu.incense.designer.task.trigger;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicButtonUI;

import edu.incense.designer.GraphEditor;
import edu.incense.designer.task.Output;

/**
 * @author mxpxgx
 *
 */
public class ConditionList extends JPanel{
    private static final long serialVersionUID = 4251774532292228263L;
    private static final int ITEM_WIDTH = 500;
    private static final int ITEM_HEIGHT = 30;
    private static final int ICON_WIDTH = 16;
    private List<ConditionPanel> items;
    private Window containerWindow;
    private Map<String, Output> data;

    public ConditionList(Window window, Map<String, Output> data) {
        containerWindow = window;
        this.data = data;
        GridLayout listLayout = new GridLayout(0,1);
        setLayout(listLayout);
        items = new ArrayList<ConditionPanel>();
        //setSize(getPreferredSize());
        //addItem();
    }

    private void addItem(final ConditionPanel condition){
        //final ConditionPanel condition = new ConditionPanel(data);
        
        ImageIcon icon = new ImageIcon(GraphEditor.class.getResource("/edu/incense/designer/images/remove-icon.png"));
        JButton removeButton = new JButton(icon);
//        removeButton.setBackground(Color.WHITE);
        removeButton.setUI(new BasicButtonUI());
        removeButton.setSize(ICON_WIDTH, ITEM_HEIGHT);
        
        final JPanel itemPanel = new JPanel();
        SpringLayout layout = new SpringLayout();
        itemPanel.setLayout(layout);
//        itemPanel.setBackground(Color.WHITE);
        itemPanel.setBorder(new LineBorder(Color.DARK_GRAY));
        itemPanel.add(condition);
        itemPanel.add(removeButton);
        layout.putConstraint(SpringLayout.WEST, condition, 0, SpringLayout.WEST, itemPanel);
        layout.putConstraint(SpringLayout.EAST, condition, 0, SpringLayout.WEST, removeButton);
        layout.putConstraint(SpringLayout.EAST, removeButton, 0, SpringLayout.EAST, itemPanel);

        removeButton.addActionListener(new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent event) {
                if(items.size()>1){
                    remove(itemPanel, condition);
                }
            }
            
        });
        
        items.add(condition);
        add(itemPanel);
        //setSize(getPreferredSize());
        validate(); //Update container
        containerWindow.pack();
    }
    
    public void addItem(){
        ConditionPanel condition = new ConditionPanel(data);
        addItem(condition);
    }
    
    public void addItem(Condition c){
        if(data.containsKey(c.getData())){
            final ConditionPanel condition = new ConditionPanel(data);
            if(c != null){
                condition.setCondition(c);
            }
            addItem(condition);
        }
    }
    
    public void remove(JPanel component, ConditionPanel condition){
        remove(component);
        items.remove(condition);
        setSize(getPreferredSize());
        validate(); //Update container
        containerWindow.pack();
    }
    
    public Dimension getPreferredSize(){
        int rows = items.size();
        return  getPreferredSize(rows);
    }
    
    public Dimension getPreferredSize(int rows){
        if(rows <= 0){
            rows = 1;
        }
        return new Dimension(ITEM_WIDTH + ICON_WIDTH, ITEM_HEIGHT * rows);
    }
    
    public List<Condition> getList(){
        List<Condition> conditions = new ArrayList<Condition>();
        for(ConditionPanel cp: items){
            conditions.add(cp.getCondition());
        }
        return conditions;
    }

    public void addList(List<Condition> conditions){
        for(Condition c: conditions){
            addItem(c);
        }
    }
    
    public int getListSize(){
        return items.size();
    }
}
