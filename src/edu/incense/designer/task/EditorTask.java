/**
 * 
 */
package edu.incense.designer.task;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mxpxgx
 *
 */
public class EditorTask extends Task{
    private static final long serialVersionUID = 8327697317653665668L;
//    private static final String TAG = "EditorTask";
    private String description = null;
    private Map<String, Output> outputs;

    public EditorTask() {
        setExtras(new HashMap<String, String>());
        outputs = new HashMap<String, Output>();
    }

    public EditorTask(String type) {
        this();
        setType(type);
    }

    public EditorTask(String taskType, String name) {
        this(taskType);
        setName(name);
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /* OUTPUTS METHODS */

    public void setOutputs(Map<String, Output> outputs) {
        this.outputs = outputs;
    }

    public Map<String, Output> getOutputs() {
        return outputs;
    }

    public void putOutput(String key, String value, String description, String example) {
        Output output = new Output(key, value);
        output.setDescription(description);
        output.setExample(example);
        outputs.put(key, output);
    }
    
    public void putOutput(String key, Output value) {
        outputs.put(key, value);
    }

    public Output obtainOutput(String key) {
        return outputs.get(key);
    }

}
