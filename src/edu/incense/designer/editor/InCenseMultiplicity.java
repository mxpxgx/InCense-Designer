/**
 * 
 */
package edu.incense.designer.editor;

import java.util.Collection;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxMultiplicity;

import edu.incense.designer.task.EditorTask;
import edu.incense.designer.task.TaskType;

/**
 * @author mxpxgx
 *
 */
public class InCenseMultiplicity extends mxMultiplicity {

    /**
     * @param source
     * @param type
     * @param attr
     * @param value
     * @param min
     * @param max
     * @param validNeighbors
     * @param countError
     * @param typeError
     * @param validNeighborsAllowed
     */
    public InCenseMultiplicity(boolean source, String type, String attr,
            String value, int min, String max,
            Collection<String> validNeighbors, String countError,
            String typeError, boolean validNeighborsAllowed) {
        super(source, type, attr, value, min, max, validNeighbors, countError,
                typeError, validNeighborsAllowed);
        
        
    }

    /**
     * @see com.mxgraph.view.mxMultiplicity#checkNeighbors(com.mxgraph.view.mxGraph, java.lang.Object, java.lang.Object, java.lang.Object)
     */
    @Override
    public boolean checkNeighbors(mxGraph graph, Object edge, Object source,
            Object target) {
        mxCell s = (mxCell)source;
        mxCell t = (mxCell)target;
        TaskType targetType = ((EditorTask)t.getValue()).getTaskType();
        
        boolean sameParent = s.getParent().equals(t.getParent());
        boolean toSession = targetType == TaskType.Session;
        boolean toParent = s.getParent().equals(t);
        
        if(toParent){
            this.typeError = "You can't connect to the containing session.";
            return false;
        }
        
        if(sameParent || toSession){
            return super.checkNeighbors(graph, edge, source, target);
        } else {
            this.typeError = "You can't connect to task in a different session.";
            return false;
        }
    }


}
