/**
 * 
 */
package edu.incense.designer.task.survey;

import javax.swing.JButton;

/**
 * @author mxpxgx
 *
 */
public interface ItemEditor<T> {
    public void start(T t, JButton sourceButton);
}
