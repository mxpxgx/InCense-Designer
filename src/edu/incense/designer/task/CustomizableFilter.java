/**
 * 
 */
package edu.incense.designer.task;

/**
 * @author mxpxgx
 *
 */
public class CustomizableFilter extends Task {
    private static final long serialVersionUID = -3002842093326677272L;
    private String userCode = "Empty";
    
    /**
     * @param userCode
     */
    public CustomizableFilter() {
        setType(TaskType.CustomizableFilter.toString());
    }

    /**
     * @return the userCode
     */
    public String getUserCode() {
        return userCode;
    }

    /**
     * @param userCode the userCode to set
     */
    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

}
