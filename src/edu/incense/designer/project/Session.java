package edu.incense.designer.project;

import java.util.List;

import edu.incense.designer.task.Task;
import edu.incense.designer.task.TaskRelation;

public class Session {
    private List<Task> tasks;
    private List<TaskRelation> relations;
    private long duration; // Time length of recording session
    private boolean autoTriggered; // automatically triggered
    private long startDate; // The date when this session will be executed for
                            // the first time
    private long endDate; // If it's repeating, the date it will stop repeating.
    private String type; // Type of repeating units (eg. Hours)
    private int repeatUnits; // The repeating units length (eg. 8)
                             // If repeatType = hours and repeatUnits = 8, then
                             // this session will repeat every 8 hours
    private boolean repeat;
    private boolean notices;

    public enum RepeatType {
        NOT_REPEATABLE, MINUTES, HOURS, DAYS, WEEKS, MONTHS
    };

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<TaskRelation> getRelations() {
        return relations;
    }

    public void setRelations(List<TaskRelation> relations) {
        this.relations = relations;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    /**
     * @param autoTriggered
     *            the autoTriggered to set
     */
    public void setAutoTriggered(boolean autoTriggered) {
        this.autoTriggered = autoTriggered;
    }

    /**
     * @return the autoTriggered
     */
    public boolean isAutoTriggered() {
        return autoTriggered;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the startDate
     */
    public long getStartDate() {
        return startDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the endDate
     */
    public long getEndDate() {
        return endDate;
    }

    /**
     * @param repeatType the repeatType to set
     */
    public void setRepeatType(String type) {
        this.type = type;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }
    
    /**
     * @return the repeatType
     */
    public RepeatType getRepeatType() {
        return RepeatType.valueOf(type);
    }

    /**
     * @param repeatUnits the repeatUnits to set
     */
    public void setRepeatUnits(int repeatUnits) {
        this.repeatUnits = repeatUnits;
    }

    /**
     * @return the repeatUnits
     */
    public int getRepeatUnits() {
        return repeatUnits;
    }

    /**
     * @return the repeat
     */
    public boolean isRepeat() {
        return repeat;
    }

    /**
     * @param repeat the repeat to set
     */
    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    /**
     * @return the notices
     */
    public boolean isNotices() {
        return notices;
    }

    /**
     * @param notices the notices to set
     */
    public void setNotices(boolean notices) {
        this.notices = notices;
    }

}
