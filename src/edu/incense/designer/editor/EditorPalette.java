package edu.incense.designer.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.TransferHandler;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.util.mxGraphTransferable;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;

/**
 * Container of icons representing edges and vertices available to use in the
 * edu.incense.designer.editor.
 */

public class EditorPalette extends JPanel {
    private static final long serialVersionUID = 7771113885935187066L;
    protected JLabel selectedEntry = null;
    protected mxEventSource eventSource = new mxEventSource(this);
    protected Color gradientColor = new Color(250, 250, 250);
    protected PaletteSelectionListener selectionListener;

    @SuppressWarnings("serial")
    public EditorPalette() {
        setBackground(new Color(255, 255, 255));
        setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));

        // Clears the current selection when the background is clicked
        addMouseListener(new MouseListener() {

            public void mousePressed(MouseEvent e) {
                clearSelection();
            }

            public void mouseClicked(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

        });

        // Shows a nice icon for drag and drop but doesn't import anything
        setTransferHandler(new TransferHandler() {
            public boolean canImport(JComponent comp, DataFlavor[] flavors) {
                return true;
            }
        });
    }

    public EditorPalette(PaletteSelectionListener selectionListener) {
        this();
        this.selectionListener = selectionListener;
    }

    /**
	 * 
	 */
    public void setGradientColor(Color c) {
        gradientColor = c;
    }

    /**
	 * 
	 */
    public Color getGradientColor() {
        return gradientColor;
    }

    /**
	 * 
	 */
    public void paintComponent(Graphics g) {
        if (gradientColor == null) {
            super.paintComponent(g);
        } else {
            Rectangle rect = getVisibleRect();

            if (g.getClipBounds() != null) {
                rect = rect.intersection(g.getClipBounds());
            }

            Graphics2D g2 = (Graphics2D) g;

            g2.setPaint(new GradientPaint(0, 0, getBackground(), getWidth(), 0,
                    gradientColor));
            g2.fill(rect);
        }
    }

    /**
	 * 
	 */
    public void clearSelection() {
        setSelectionEntry(null, null);
    }

    /**
	 * 
	 */
    public void setSelectionEntry(JLabel entry, mxGraphTransferable t) {
        JLabel previous = selectedEntry;
        selectedEntry = entry;

        if (previous != null) {
            previous.setBorder(null);
            previous.setOpaque(false);
        }

        if (selectedEntry != null) {
            selectedEntry.setBorder(ShadowBorder.getSharedInstance());
            selectedEntry.setOpaque(true);
        }

        if (t != null && t.getCells() != null && t.getCells().length > 0) {
            mxCell cell = (mxCell) t.getCells()[0];
            if (cell.isVertex()) {
                selectionListener.selectionChanged(cell.getValue());
            }
        }

        eventSource.fireEvent(new mxEventObject(mxEvent.SELECT, "entry",
                selectedEntry, "transferable", t, "previous", previous));
    }

    /**
	 * 
	 */
    public void setPreferredWidth(int width) {
        int cols = Math.max(1, width / 55);
        setPreferredSize(new Dimension(width,
                (getComponentCount() * 55 / cols) + 30));
        revalidate();
    }

    /**
     * Add edge icon (lines, connectors and the like) to the Palette
     * 
     * @param name
     * @param icon
     * @param style
     * @param width
     * @param height
     * @param value
     */
    public void addEdgeTemplate(final String name, ImageIcon icon,
            String style, int width, int height, Object value) {
        mxGeometry geometry = new mxGeometry(0, 0, width, height);
        geometry.setTerminalPoint(new mxPoint(0, height), true); // source
        geometry.setTerminalPoint(new mxPoint(width, 0), false); // not source
                                                                 // (end?)
        geometry.setRelative(true);

        mxCell cell = new mxCell(value, geometry, style);
        cell.setEdge(true);

        addTemplate(name, icon, cell);
    }

    /**
     * Add vertex icon (figures, shapes) to the Palette
     * 
     * @param name
     * @param icon
     * @param style
     * @param width
     * @param height
     * @param value
     */
    public void addTemplate(final String name, ImageIcon icon, String style,
            int width, int height, Object value) {
        mxCell cell = new mxCell(value, new mxGeometry(0, 0, width, height),
                style);
        cell.setVertex(true);

        addTemplate(name, icon, cell);
    }

    /**
     * Add a mxCell to the Palette (once it's defined as an Edge or Vertex)
     * 
     * @param name
     * @param icon
     * @param cell
     */
    public void addTemplate(final String name, ImageIcon icon, mxCell cell) {
        mxRectangle bounds = (mxGeometry) cell.getGeometry().clone();
        final mxGraphTransferable t = new mxGraphTransferable(
                new Object[] { cell }, bounds);

        // Scales the image if it's too large for the library
        if (icon != null) {
            if (icon.getIconWidth() > 32 || icon.getIconHeight() > 32) {
                icon = new ImageIcon(icon.getImage().getScaledInstance(32, 32,
                        0));
            }
        }

        final JLabel entry = new JLabel(icon);
        entry.setPreferredSize(new Dimension(50, 50));
        entry.setBackground(EditorPalette.this.getBackground().brighter());
        entry.setFont(new Font(entry.getFont().getFamily(), 0, 10));

        entry.setVerticalTextPosition(JLabel.BOTTOM);
        entry.setHorizontalTextPosition(JLabel.CENTER);
        entry.setIconTextGap(0);

        entry.setToolTipText(name);
        entry.setText(name);

        entry.addMouseListener(new MouseListener() {

            public void mousePressed(MouseEvent e) {
                setSelectionEntry(entry, t);
            }

            public void mouseClicked(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

        });

        // Install the handler for dragging nodes into a graph
        DragGestureListener dragGestureListener = new DragGestureListener() {

            public void dragGestureRecognized(DragGestureEvent e) {
                e.startDrag(null, mxConstants.EMPTY_IMAGE, new Point(), t, null);
            }

        };

        DragSource dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(entry,
                DnDConstants.ACTION_COPY, dragGestureListener);

        add(entry);
    }

    /**
     * @param eventName
     * @param listener
     * @see com.mxgraph.util.mxEventSource#addListener(java.lang.String,
     *      com.mxgraph.util.mxEventSource.mxIEventListener)
     */
    public void addListener(String eventName, mxIEventListener listener) {
        eventSource.addListener(eventName, listener);
    }

    /**
     * @return whether or not event are enabled for this palette
     * @see com.mxgraph.util.mxEventSource#isEventsEnabled()
     */
    public boolean isEventsEnabled() {
        return eventSource.isEventsEnabled();
    }

    /**
     * @param listener
     * @see com.mxgraph.util.mxEventSource#removeListener(com.mxgraph.util.mxEventSource.mxIEventListener)
     */
    public void removeListener(mxIEventListener listener) {
        eventSource.removeListener(listener);
    }

    /**
     * @param eventName
     * @param listener
     * @see com.mxgraph.util.mxEventSource#removeListener(java.lang.String,
     *      com.mxgraph.util.mxEventSource.mxIEventListener)
     */
    public void removeListener(mxIEventListener listener, String eventName) {
        eventSource.removeListener(listener, eventName);
    }

    /**
     * @param eventsEnabled
     * @see com.mxgraph.util.mxEventSource#setEventsEnabled(boolean)
     */
    public void setEventsEnabled(boolean eventsEnabled) {
        eventSource.setEventsEnabled(eventsEnabled);
    }

}
