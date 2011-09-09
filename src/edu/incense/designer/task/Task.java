package edu.incense.designer.task;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Task model, contains a HashMap for extras (specific data needed by different
 * task types) and another one for outputs (description of the data generated by
 * the task).
 * 
 * @author mxpxgx
 * @version 1.0, 2011/07/15
 */
public class Task implements Serializable {
    private static final long serialVersionUID = -2705547375579083074L;
    private static final String TAG = "Task";
    private TaskType taskType = null;
    private String name = "Untitled";
    private String description = null;
    private Map<String, String> extras;
    private Map<String, Output> outputs;

    public Task() {
        extras = new HashMap<String, String>();
        outputs = new HashMap<String, Output>();
    }

    public Task(TaskType taskType) {
        this();
        this.taskType = taskType;
    }

    public Task(TaskType taskType, String name) {
        this(taskType);
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    @Override
    public String toString() {
        String prefix = "";
        switch(getTaskType()){
        case Survey:
            return name;
        case Trigger:
            return name;
        case Sink:
            return "Si";
        case Session:
            return name;
        case Stop:
            return "Stop";
        }
        if(getTaskType().toString().toLowerCase().contains("filter")){
            return name;
        }
        if(name.length() < 8){
            return prefix+name;
        } else {
            return prefix+name.substring(0, 7)+".";
        }
    }

    /* EXTRAS METHODS */

    public void setExtras(Map<String, String> extras) {
        this.extras = extras;
    }

    public Map<String, String> getExtras() {
        return extras;
    }

    public void putExtra(String key, String value) {
        extras.put(key, value);
    }

    public void putExtra(String key, boolean value) {
        putExtra(key, String.valueOf(value));
    }

    public void putExtra(String key, int value) {
        putExtra(key, String.valueOf(value));
    }

    public void putExtra(String key, float value) {
        putExtra(key, String.valueOf(value));
    }

    public void putExtra(String key, long value) {
        putExtra(key, String.valueOf(value));
    }

    public String getExtra(String key) {
        return extras.get(key);
    }
    
    public String getExtra(String key, String defValue) {
        if (extras == null || !extras.containsKey(key)) {
            return defValue;
        }
        return extras.get(key);
    }

    public boolean getExtra(String key, boolean defValue) {
        return Boolean.parseBoolean(getExtra(key, String.valueOf(defValue)));
    }

    public int getExtra(String key, int defValue) {
        return Integer.parseInt(getExtra(key, String.valueOf(defValue)));
    }

    public float getExtra(String key, float defValue) {
        return Float.parseFloat(getExtra(key, String.valueOf(defValue)));
    }
    
    public long getExtra(String key, long defValue) {
        return Long.parseLong(getExtra(key, String.valueOf(defValue)));
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

    public Output getOutput(String key) {
        return outputs.get(key);
    }

    /* JSON METHODS */

    public JsonNode toJsonNode() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.valueToTree(this);
        return node;
    }

    public String toJsonString() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.valueToTree(this);
        String json = null;
        try {
            json = mapper.writeValueAsString(node);
        } catch (JsonGenerationException e) {
            System.err.println(TAG + ": " + e);
        } catch (JsonMappingException e) {
            System.err.println(TAG + ": " + e);
        } catch (IOException e) {
            System.err.println(TAG + ": " + e);
        }
        return json;
    }

}