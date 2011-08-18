/**
 * 
 */
package edu.incense.designer.task.survey;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicButtonUI;

import edu.incense.designer.GraphEditor;

/**
 * @author mxpxgx
 *
 */
public class DynamicList<T> extends JPanel{
    private static final long serialVersionUID = 4251774532292228263L;
    private static final int BUTTON_WIDTH = 400;
    private static final int BUTTON_HEIGHT = 30;
    private static final int ICON_WIDTH = 16;
    private Map<T,JPanel> items;
    private ItemEditor<T> editor;
    private JLabel emptyLabel;
    private Window containerWindow;

    public DynamicList(ItemEditor<T> editor, Window window) {
        this.editor = editor;
        containerWindow = window;
        GridLayout listLayout = new GridLayout(0,1);
        setLayout(listLayout);
        items = new LinkedHashMap<T,JPanel>();
        emptyLabel = new JLabel("Survey is empty.");
        showEmptyMessage();
        setSize(getPreferredSize());
        setOpaque(true);
        setBackground(Color.WHITE);
    }
    
    public void showEmptyMessage(){
        if(items.size()==0){
            add(emptyLabel);
        }
    }
    
    public void removeEmptyMessage(){
        remove(emptyLabel);
    }
    
    public JButton add(T key){
        return add(key, key.toString());
    }
    
    public JButton add(final T key, String buttonText){
        JButton editButton = new JButton(buttonText);
        editButton.setBackground(Color.WHITE);
        //editButton.setOpaque(true);
        editButton.setUI(new BasicButtonUI());
        editButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        editButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent event) {
                startEditor(key, (JButton)event.getSource());
            }
            
        });
        
        ImageIcon icon = new ImageIcon(GraphEditor.class.getResource("/edu/incense/designer/images/remove-icon.png"));
        JButton removeButton = new JButton(icon);
        removeButton.setBackground(Color.WHITE);
        removeButton.setUI(new BasicButtonUI());
        removeButton.setSize(ICON_WIDTH, BUTTON_HEIGHT);
        removeButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent event) {
                remove(key);
            }
            
        });
        
        JPanel itemPanel = new JPanel();
        SpringLayout layout = new SpringLayout();
        itemPanel.setLayout(layout);
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setBorder(new LineBorder(Color.DARK_GRAY));
        itemPanel.add(editButton);
        itemPanel.add(removeButton);
        layout.putConstraint(SpringLayout.WEST, editButton, 0, SpringLayout.WEST, itemPanel);
        layout.putConstraint(SpringLayout.EAST, editButton, 0, SpringLayout.WEST, removeButton);
        layout.putConstraint(SpringLayout.EAST, removeButton, 0, SpringLayout.EAST, itemPanel);
        
        if(items.isEmpty()) removeEmptyMessage();
        items.put(key, itemPanel);
        add(itemPanel);
        setSize(getPreferredSize());
        validate(); //Update container
        containerWindow.pack();
        return editButton;
    }
    
    public void remove(T key){
        remove(items.remove(key));
        showEmptyMessage();
        setSize(getPreferredSize());
        validate(); //Update container
        containerWindow.pack();
    }
    
    public void startEditor(T key, JButton source){
        editor.start(key, source);
    }
    
    public Dimension getPreferredSize(){
        int rows = items.size();
        return  getPreferredSize(rows);
    }
    
    public Dimension getPreferredSize(int rows){
        if(rows <= 0){
            rows = 1;
        }
        return new Dimension(BUTTON_WIDTH + ICON_WIDTH, BUTTON_HEIGHT * rows);
    }
    
    public List<T> getList(){
        List<T> list = new ArrayList<T>(items.keySet());
        return list;
    }
}
