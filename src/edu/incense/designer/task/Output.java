/**
 * 
 */
package edu.incense.designer.task;

import java.io.Serializable;

/**
 * @author mxpxgx
 *
 */
public class Output implements Serializable {
    private static final long serialVersionUID = 9183426165336681363L;
    private String name;
    private String type;
    private String description;
    private String example;
    private String[] types;
    
    public Output(){
        setName(null);
        setType(null);
    }
    
    public Output(String name, String type){
        setName(name);
        setType(type);
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the type
     */
    public String getType() {
        return type;
    }
    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * @return the example
     */
    public String getExample() {
        return example;
    }
    /**
     * @param example the example to set
     */
    public void setExample(String example) {
        this.example = example;
    }
    
    public String toString(){
        return type;
    }

    /**
     * @param types the types to set
     */
    public void setTypes(String[] types) {
        this.types = types;
    }

    /**
     * @return the types
     */
    public String[] getTypes() {
        return types;
    }
}
