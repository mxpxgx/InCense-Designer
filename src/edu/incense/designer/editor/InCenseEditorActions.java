/**
 * 
 */
package edu.incense.designer.editor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxResources;

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
                // mxGraph graph = graphComponent.getGraph();

                if (editor.getCurrentFile() == null) {
                    JOptionPane.showMessageDialog(graphComponent,
                            mxResources.get("pleaseSave"));
                } else {
                    String filename = editor.getCurrentFile().getAbsolutePath();
                    File file = new File(filename);
                    File jsonFile = null;
                    try {
                        //Read content from file
                        String content = toString(file);
                        System.out.println("XML: "+content);
                        //Convert to JSON object
                        JSONObject json = (JSONObject)XML.toJSONObject(content);
                        System.out.println("JSON: "+json.toString() + "\n");
                        
                        //Create file with .json extension
                        StringBuilder sb = new StringBuilder(file.getAbsolutePath());
                        int i = file.getAbsolutePath().lastIndexOf(".");
                        String extension = ".json";
                        sb.replace(i, sb.length(), extension);
                        jsonFile = new File(sb.toString());
                        
                        //Write JSON object to file
                        System.out.print("Writing ["+sb.toString()+"]...");
                        BufferedWriter writer = new BufferedWriter(new FileWriter(jsonFile));
                        json.write(writer);
                        writer.close();
                        System.out.println("complete.");
                        
                        
                    } catch (JSONException ex) {
                        System.err.println(TAG+": [" + file + "] XML file couldn't be converted to a JSON object. \n" + ex);
                    } catch (IOException ex) {
                        System.err.println(TAG+": Writer for [" + jsonFile + "] couldn't be created. \n" + ex);
                    }

                }
            }
        }
        
        
        public String toString(File file){
            String ls = System.getProperty("line.separator");
            
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                
                String line  = null;
                StringBuilder stringBuilder = new StringBuilder();
                while( ( line = reader.readLine() ) != null ) {
                    stringBuilder.append( line );
                    stringBuilder.append( ls );
                }
                reader.close();
                return stringBuilder.toString();
                
            } catch (FileNotFoundException e) {
                System.err.println(TAG+": [" + file + "] file wasn't found. " + ls + e);
                return null;
            } catch (IOException e) {
                System.err.println(TAG+": [" + file + "] file line couldn't be read. " + ls + e);
                return null;
            }
        }
        
        public String formatToInCenseJson(String json){
            //TODO
            return "";
        }

    }
}
