package edu.incense.designer.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxResources;

import edu.incense.designer.editor.EditorActions.AlignCellsAction;
import edu.incense.designer.editor.EditorActions.AutosizeAction;
import edu.incense.designer.editor.EditorActions.ColorAction;
import edu.incense.designer.editor.EditorActions.ExitAction;
import edu.incense.designer.editor.EditorActions.HistoryAction;
import edu.incense.designer.editor.EditorActions.KeyValueAction;
import edu.incense.designer.editor.EditorActions.NewAction;
import edu.incense.designer.editor.EditorActions.OpenAction;
import edu.incense.designer.editor.EditorActions.PageSetupAction;
import edu.incense.designer.editor.EditorActions.PrintAction;
import edu.incense.designer.editor.EditorActions.PromptValueAction;
import edu.incense.designer.editor.EditorActions.SaveAction;
import edu.incense.designer.editor.EditorActions.ScaleAction;
import edu.incense.designer.editor.EditorActions.SetLabelPositionAction;
import edu.incense.designer.editor.EditorActions.SetStyleAction;
import edu.incense.designer.editor.EditorActions.StyleAction;
import edu.incense.designer.editor.EditorActions.ToggleAction;
import edu.incense.designer.editor.EditorActions.ToggleGridItem;
import edu.incense.designer.editor.EditorActions.TogglePropertyItem;
import edu.incense.designer.editor.EditorActions.ToggleRulersItem;
import edu.incense.designer.editor.EditorActions.ZoomPolicyAction;
import edu.incense.designer.editor.InCenseEditorActions.SendAction;

public class EditorMenuBar extends JMenuBar {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4060203894740766714L;

//    @SuppressWarnings("serial")
    public EditorMenuBar(final BasicGraphEditor editor) {
        final mxGraphComponent graphComponent = editor.getGraphComponent();
//        final mxGraph graph = graphComponent.getGraph();
        JMenu menu = null;
        JMenu submenu = null;

        // Creates the file menu
        menu = add(new JMenu(mxResources.get("file")));

        menu.add(editor.bind(mxResources.get("new"), new NewAction(),
                "/edu/incense/designer/images/new.gif"));
        menu.add(editor.bind(mxResources.get("openFile"), new OpenAction(),
                "/edu/incense/designer/images/open.gif"));
//        menu.add(editor.bind(mxResources.get("importStencil"),
//                new ImportAction(), "/edu/incense/designer/images/open.gif"));

        menu.addSeparator();

        menu.add(editor.bind(mxResources.get("send"), new SendAction()));
        
        menu.addSeparator();

        menu.add(editor.bind(mxResources.get("save"), new SaveAction(false),
                "/edu/incense/designer/images/save.gif"));
        menu.add(editor.bind(mxResources.get("saveAs"), new SaveAction(true),
                "/edu/incense/designer/images/saveas.gif"));

        menu.addSeparator();

        menu.add(editor.bind(mxResources.get("pageSetup"),
                new PageSetupAction(),
                "/edu/incense/designer/images/pagesetup.gif"));
        menu.add(editor.bind(mxResources.get("print"), new PrintAction(),
                "/edu/incense/designer/images/print.gif"));

        menu.addSeparator();

        menu.add(editor.bind(mxResources.get("exit"), new ExitAction()));

        // Creates the edit menu
        menu = add(new JMenu(mxResources.get("edit")));

        menu.add(editor.bind(mxResources.get("undo"), new HistoryAction(true),
                "/edu/incense/designer/images/undo.gif"));
        menu.add(editor.bind(mxResources.get("redo"), new HistoryAction(false),
                "/edu/incense/designer/images/redo.gif"));

        menu.addSeparator();

        menu.add(editor.bind(mxResources.get("cut"),
                TransferHandler.getCutAction(),
                "/edu/incense/designer/images/cut.gif"));
        menu.add(editor.bind(mxResources.get("copy"),
                TransferHandler.getCopyAction(),
                "/edu/incense/designer/images/copy.gif"));
        menu.add(editor.bind(mxResources.get("paste"),
                TransferHandler.getPasteAction(),
                "/edu/incense/designer/images/paste.gif"));

        menu.addSeparator();

        menu.add(editor.bind(mxResources.get("delete"),
                mxGraphActions.getDeleteAction(),
                "/edu/incense/designer/images/delete.gif"));

        menu.addSeparator();

        menu.add(editor.bind(mxResources.get("selectAll"),
                mxGraphActions.getSelectAllAction()));
        menu.add(editor.bind(mxResources.get("selectNone"),
                mxGraphActions.getSelectNoneAction()));

        menu.addSeparator();

//        menu.add(editor.bind(mxResources.get("warning"), new WarningAction()));
        menu.add(editor.bind(mxResources.get("edit"),
                mxGraphActions.getEditAction()));

        // Creates the view menu
        menu = add(new JMenu(mxResources.get("view")));

        JMenuItem item = menu.add(new TogglePropertyItem(graphComponent,
                mxResources.get("pageLayout"), "PageVisible", true,
                new ActionListener() {
                    /**
					 * 
					 */
                    public void actionPerformed(ActionEvent e) {
                        if (graphComponent.isPageVisible()
                                && graphComponent.isCenterPage()) {
                            graphComponent.zoomAndCenter();
                        } else {
                            graphComponent.getGraphControl()
                                    .updatePreferredSize();
                        }
                    }
                }));

        item.addActionListener(new ActionListener() {
            /*
             * (non-Javadoc)
             * 
             * @see
             * java.awt.event.ActionListener#actionPerformed(java.awt.event.
             * ActionEvent)
             */
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof TogglePropertyItem) {
                    final mxGraphComponent graphComponent = editor
                            .getGraphComponent();
                    TogglePropertyItem toggleItem = (TogglePropertyItem) e
                            .getSource();

                    if (toggleItem.isSelected()) {
                        // Scrolls the view to the center
                        SwingUtilities.invokeLater(new Runnable() {
                            /*
                             * (non-Javadoc)
                             * 
                             * @see java.lang.Runnable#run()
                             */
                            public void run() {
                                graphComponent.scrollToCenter(true);
                                graphComponent.scrollToCenter(false);
                            }
                        });
                    } else {
                        // Resets the translation of the view
                        mxPoint tr = graphComponent.getGraph().getView()
                                .getTranslate();

                        if (tr.getX() != 0 || tr.getY() != 0) {
                            graphComponent.getGraph().getView()
                                    .setTranslate(new mxPoint());
                        }
                    }
                }
            }
        });

        menu.add(new TogglePropertyItem(graphComponent, mxResources
                .get("antialias"), "AntiAlias", true));

        menu.addSeparator();

        menu.add(new ToggleGridItem(editor, mxResources.get("grid")));
        menu.add(new ToggleRulersItem(editor, mxResources.get("rulers")));

        menu.addSeparator();

        submenu = (JMenu) menu.add(new JMenu(mxResources.get("zoom")));

        submenu.add(editor.bind("400%", new ScaleAction(4)));
        submenu.add(editor.bind("200%", new ScaleAction(2)));
        submenu.add(editor.bind("150%", new ScaleAction(1.5)));
        submenu.add(editor.bind("100%", new ScaleAction(1)));
        submenu.add(editor.bind("75%", new ScaleAction(0.75)));
        submenu.add(editor.bind("50%", new ScaleAction(0.5)));

        submenu.addSeparator();

        submenu.add(editor.bind(mxResources.get("custom"), new ScaleAction(0)));

        menu.addSeparator();

        menu.add(editor.bind(mxResources.get("zoomIn"),
                mxGraphActions.getZoomInAction()));
        menu.add(editor.bind(mxResources.get("zoomOut"),
                mxGraphActions.getZoomOutAction()));

        menu.addSeparator();

        menu.add(editor.bind(mxResources.get("page"), new ZoomPolicyAction(
                mxGraphComponent.ZOOM_POLICY_PAGE)));
        menu.add(editor.bind(mxResources.get("width"), new ZoomPolicyAction(
                mxGraphComponent.ZOOM_POLICY_WIDTH)));

        menu.addSeparator();

        menu.add(editor.bind(mxResources.get("actualSize"),
                mxGraphActions.getZoomActualAction()));

//        // Creates the format menu
//        menu = add(new JMenu(mxResources.get("format")));
//
//        populateFormatMenu(menu, editor);

//        // Creates the shape menu
//        menu = add(new JMenu(mxResources.get("shape")));
//
//        populateShapeMenu(menu, editor);
//
//        // Creates the diagram menu
//        menu = add(new JMenu(mxResources.get("diagram")));
//
//        menu.add(new ToggleOutlineItem(editor, mxResources.get("outline")));
//
//        menu.addSeparator();
//
//        submenu = (JMenu) menu.add(new JMenu(mxResources.get("background")));
//
//        submenu.add(editor.bind(mxResources.get("backgroundColor"),
//                new BackgroundAction()));
//        submenu.add(editor.bind(mxResources.get("backgroundImage"),
//                new BackgroundImageAction()));
//
//        submenu.addSeparator();
//
//        submenu.add(editor.bind(mxResources.get("pageBackground"),
//                new PageBackgroundAction()));
//
//        submenu = (JMenu) menu.add(new JMenu(mxResources.get("grid")));
//
//        submenu.add(editor.bind(mxResources.get("gridSize"),
//                new PromptPropertyAction(graph, "Grid Size", "GridSize")));
//        submenu.add(editor.bind(mxResources.get("gridColor"),
//                new GridColorAction()));
//
//        submenu.addSeparator();
//
//        submenu.add(editor.bind(mxResources.get("dashed"), new GridStyleAction(
//                mxGraphComponent.GRID_STYLE_DASHED)));
//        submenu.add(editor.bind(mxResources.get("dot"), new GridStyleAction(
//                mxGraphComponent.GRID_STYLE_DOT)));
//        submenu.add(editor.bind(mxResources.get("line"), new GridStyleAction(
//                mxGraphComponent.GRID_STYLE_LINE)));
//        submenu.add(editor.bind(mxResources.get("cross"), new GridStyleAction(
//                mxGraphComponent.GRID_STYLE_CROSS)));
//
//        menu.addSeparator();
//
//        submenu = (JMenu) menu.add(new JMenu(mxResources.get("layout")));
//
//        submenu.add(editor.graphLayout("verticalHierarchical", true));
//        submenu.add(editor.graphLayout("horizontalHierarchical", true));
//
//        submenu.addSeparator();
//
//        submenu.add(editor.graphLayout("verticalPartition", false));
//        submenu.add(editor.graphLayout("horizontalPartition", false));
//
//        submenu.addSeparator();
//
//        submenu.add(editor.graphLayout("verticalStack", false));
//        submenu.add(editor.graphLayout("horizontalStack", false));
//
//        submenu.addSeparator();
//
//        submenu.add(editor.graphLayout("verticalTree", true));
//        submenu.add(editor.graphLayout("horizontalTree", true));
//
//        submenu.addSeparator();
//
//        submenu.add(editor.graphLayout("placeEdgeLabels", false));
//        submenu.add(editor.graphLayout("parallelEdges", false));
//
//        submenu.addSeparator();
//
//        submenu.add(editor.graphLayout("organicLayout", true));
//        submenu.add(editor.graphLayout("circleLayout", true));
//
//        submenu = (JMenu) menu.add(new JMenu(mxResources.get("selection")));
//
//        submenu.add(editor.bind(mxResources.get("selectPath"),
//                new SelectShortestPathAction(false)));
//        submenu.add(editor.bind(mxResources.get("selectDirectedPath"),
//                new SelectShortestPathAction(true)));
//
//        submenu.addSeparator();
//
//        submenu.add(editor.bind(mxResources.get("selectTree"),
//                new SelectSpanningTreeAction(false)));
//        submenu.add(editor.bind(mxResources.get("selectDirectedTree"),
//                new SelectSpanningTreeAction(true)));
//
//        menu.addSeparator();
//
//        submenu = (JMenu) menu.add(new JMenu(mxResources.get("stylesheet")));
//
//        submenu.add(editor.bind(mxResources.get("basicStyle"),
//                new StylesheetAction(
//                        "/edu/incense/designer/resources/basic-style.xml")));
//        submenu.add(editor.bind(mxResources.get("defaultStyle"),
//                new StylesheetAction(
//                        "/edu/incense/designer/resources/default-style.xml")));
//
//        // Creates the options menu
//        menu = add(new JMenu(mxResources.get("options")));
//
//        submenu = (JMenu) menu.add(new JMenu(mxResources.get("display")));
//        submenu.add(new TogglePropertyItem(graphComponent, mxResources
//                .get("buffering"), "TripleBuffered", true));
//
//        submenu.add(new TogglePropertyItem(graphComponent, mxResources
//                .get("preferPageSize"), "PreferPageSize", true,
//                new ActionListener() {
//                    /**
//					 * 
//					 */
//                    public void actionPerformed(ActionEvent e) {
//                        graphComponent.zoomAndCenter();
//                    }
//                }));
//
//        // TODO: This feature is not yet implemented
//        // submenu.add(new TogglePropertyItem(graphComponent, mxResources
//        // .get("pageBreaks"), "PageBreaksVisible", true));
//
//        submenu.addSeparator();
//
//        submenu.add(editor.bind(mxResources.get("tolerance"),
//                new PromptPropertyAction(graphComponent, "Tolerance")));
//
//        submenu.add(editor.bind(mxResources.get("dirty"),
//                new ToggleDirtyAction()));
//
//        submenu = (JMenu) menu.add(new JMenu(mxResources.get("zoom")));
//
//        submenu.add(new TogglePropertyItem(graphComponent, mxResources
//                .get("centerZoom"), "CenterZoom", true));
//        submenu.add(new TogglePropertyItem(graphComponent, mxResources
//                .get("zoomToSelection"), "KeepSelectionVisibleOnZoom", true));
//
//        submenu.addSeparator();
//
//        submenu.add(new TogglePropertyItem(graphComponent, mxResources
//                .get("centerPage"), "CenterPage", true, new ActionListener() {
//            /**
//			 * 
//			 */
//            public void actionPerformed(ActionEvent e) {
//                if (graphComponent.isPageVisible()
//                        && graphComponent.isCenterPage()) {
//                    graphComponent.zoomAndCenter();
//                }
//            }
//        }));
//
//        menu.addSeparator();
//
//        submenu = (JMenu) menu.add(new JMenu(mxResources.get("dragAndDrop")));
//
//        submenu.add(new TogglePropertyItem(graphComponent, mxResources
//                .get("dragEnabled"), "DragEnabled"));
//        submenu.add(new TogglePropertyItem(graph, mxResources
//                .get("dropEnabled"), "DropEnabled"));
//
//        submenu.addSeparator();
//
//        submenu.add(new TogglePropertyItem(graphComponent.getGraphHandler(),
//                mxResources.get("imagePreview"), "ImagePreview"));
//
//        submenu = (JMenu) menu.add(new JMenu(mxResources.get("labels")));
//
//        submenu.add(new TogglePropertyItem(graph,
//                mxResources.get("htmlLabels"), "HtmlLabels", true));
//        submenu.add(new TogglePropertyItem(graph,
//                mxResources.get("showLabels"), "LabelsVisible", true));
//
//        submenu.addSeparator();
//
//        submenu.add(new TogglePropertyItem(graph, mxResources
//                .get("moveEdgeLabels"), "EdgeLabelsMovable"));
//        submenu.add(new TogglePropertyItem(graph, mxResources
//                .get("moveVertexLabels"), "VertexLabelsMovable"));
//
//        submenu.addSeparator();
//
//        submenu.add(new TogglePropertyItem(graphComponent, mxResources
//                .get("handleReturn"), "EnterStopsCellEditing"));
//
//        menu.addSeparator();
//
//        submenu = (JMenu) menu.add(new JMenu(mxResources.get("connections")));
//
//        submenu.add(new TogglePropertyItem(graphComponent, mxResources
//                .get("connectable"), "Connectable"));
//        submenu.add(new TogglePropertyItem(graph, mxResources
//                .get("connectableEdges"), "ConnectableEdges"));
//
//        submenu.addSeparator();
//
//        submenu.add(new ToggleCreateTargetItem(editor, mxResources
//                .get("createTarget")));
//        submenu.add(new TogglePropertyItem(graph, mxResources
//                .get("disconnectOnMove"), "DisconnectOnMove"));
//
//        submenu.addSeparator();
//
//        submenu.add(editor.bind(mxResources.get("connectMode"),
//                new ToggleConnectModeAction()));
//
//        submenu = (JMenu) menu.add(new JMenu(mxResources.get("validation")));
//
//        submenu.add(new TogglePropertyItem(graph, mxResources
//                .get("allowDanglingEdges"), "AllowDanglingEdges"));
//        submenu.add(new TogglePropertyItem(graph, mxResources
//                .get("cloneInvalidEdges"), "CloneInvalidEdges"));
//
//        submenu.addSeparator();
//
//        submenu.add(new TogglePropertyItem(graph,
//                mxResources.get("allowLoops"), "AllowLoops"));
//        submenu.add(new TogglePropertyItem(graph,
//                mxResources.get("multigraph"), "Multigraph"));

//        // Creates the window menu
//        menu = add(new JMenu(mxResources.get("window")));
//
//        UIManager.LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
//
//        for (int i = 0; i < lafs.length; i++) {
//            final String clazz = lafs[i].getClassName();
//            menu.add(new AbstractAction(lafs[i].getName()) {
//                public void actionPerformed(ActionEvent e) {
//                    editor.setLookAndFeel(clazz);
//                }
//            });
//        }

        // Creates the help menu
        menu = add(new JMenu(mxResources.get("help")));

        item = menu.add(new JMenuItem(mxResources.get("aboutInCenseEditor")));
        item.addActionListener(new ActionListener() {
            /*
             * (non-Javadoc)
             * 
             * @see
             * java.awt.event.ActionListener#actionPerformed(java.awt.event.
             * ActionEvent)
             */
            public void actionPerformed(ActionEvent e) {
                editor.about();
            }
        });
    }

    /**
     * Adds menu items to the given shape menu. This is factored out because the
     * shape menu appears in the menubar and also in the popupmenu.
     */
    public static void populateShapeMenu(JMenu menu, BasicGraphEditor editor) {
        menu.add(editor.bind(mxResources.get("home"),
                mxGraphActions.getHomeAction(),
                "/edu/incense/designer/images/house.gif"));

        menu.addSeparator();

        menu.add(editor.bind(mxResources.get("exitGroup"),
                mxGraphActions.getExitGroupAction(),
                "/edu/incense/designer/images/up.gif"));
        menu.add(editor.bind(mxResources.get("enterGroup"),
                mxGraphActions.getEnterGroupAction(),
                "/edu/incense/designer/images/down.gif"));

        menu.addSeparator();

        menu.add(editor.bind(mxResources.get("group"),
                mxGraphActions.getGroupAction(),
                "/edu/incense/designer/images/group.gif"));
        menu.add(editor.bind(mxResources.get("ungroup"),
                mxGraphActions.getUngroupAction(),
                "/edu/incense/designer/images/ungroup.gif"));

        menu.addSeparator();

        menu.add(editor.bind(mxResources.get("removeFromGroup"),
                mxGraphActions.getRemoveFromParentAction()));

        menu.add(editor.bind(mxResources.get("updateGroupBounds"),
                mxGraphActions.getUpdateGroupBoundsAction()));

        menu.addSeparator();

        menu.add(editor.bind(mxResources.get("collapse"),
                mxGraphActions.getCollapseAction(),
                "/edu/incense/designer/images/collapse.gif"));
        menu.add(editor.bind(mxResources.get("expand"),
                mxGraphActions.getExpandAction(),
                "/edu/incense/designer/images/expand.gif"));

        menu.addSeparator();

        menu.add(editor.bind(mxResources.get("toBack"),
                mxGraphActions.getToBackAction(),
                "/edu/incense/designer/images/toback.gif"));
        menu.add(editor.bind(mxResources.get("toFront"),
                mxGraphActions.getToFrontAction(),
                "/edu/incense/designer/images/tofront.gif"));

        menu.addSeparator();

        JMenu submenu = (JMenu) menu.add(new JMenu(mxResources.get("align")));

        submenu.add(editor.bind(mxResources.get("left"), new AlignCellsAction(
                mxConstants.ALIGN_LEFT),
                "/edu/incense/designer/images/alignleft.gif"));
        submenu.add(editor.bind(mxResources.get("center"),
                new AlignCellsAction(mxConstants.ALIGN_CENTER),
                "/edu/incense/designer/images/aligncenter.gif"));
        submenu.add(editor.bind(mxResources.get("right"), new AlignCellsAction(
                mxConstants.ALIGN_RIGHT),
                "/edu/incense/designer/images/alignright.gif"));

        submenu.addSeparator();

        submenu.add(editor.bind(mxResources.get("top"), new AlignCellsAction(
                mxConstants.ALIGN_TOP),
                "/edu/incense/designer/images/aligntop.gif"));
        submenu.add(editor.bind(mxResources.get("middle"),
                new AlignCellsAction(mxConstants.ALIGN_MIDDLE),
                "/edu/incense/designer/images/alignmiddle.gif"));
        submenu.add(editor.bind(mxResources.get("bottom"),
                new AlignCellsAction(mxConstants.ALIGN_BOTTOM),
                "/edu/incense/designer/images/alignbottom.gif"));

        menu.addSeparator();

        menu.add(editor.bind(mxResources.get("autosize"), new AutosizeAction()));

    }

    /**
     * Adds menu items to the given format menu. This is factored out because
     * the format menu appears in the menubar and also in the popupmenu.
     */
    public static void populateFormatMenu(JMenu menu, BasicGraphEditor editor) {
        JMenu submenu = (JMenu) menu.add(new JMenu(mxResources
                .get("background")));

        submenu.add(editor.bind(mxResources.get("fillcolor"), new ColorAction(
                "Fillcolor", mxConstants.STYLE_FILLCOLOR),
                "/edu/incense/designer/images/fillcolor.gif"));
        submenu.add(editor.bind(mxResources.get("gradient"), new ColorAction(
                "Gradient", mxConstants.STYLE_GRADIENTCOLOR)));

        submenu.addSeparator();

        submenu.add(editor.bind(mxResources.get("image"),
                new PromptValueAction(mxConstants.STYLE_IMAGE, "Image")));
        submenu.add(editor.bind(mxResources.get("shadow"), new ToggleAction(
                mxConstants.STYLE_SHADOW)));

        submenu.addSeparator();

        submenu.add(editor.bind(mxResources.get("opacity"),
                new PromptValueAction(mxConstants.STYLE_OPACITY,
                        "Opacity (0-100)")));

        submenu = (JMenu) menu.add(new JMenu(mxResources.get("label")));

        submenu.add(editor.bind(mxResources.get("fontcolor"), new ColorAction(
                "Fontcolor", mxConstants.STYLE_FONTCOLOR),
                "/edu/incense/designer/images/fontcolor.gif"));

        submenu.addSeparator();

        submenu.add(editor.bind(mxResources.get("labelFill"), new ColorAction(
                "Label Fill", mxConstants.STYLE_LABEL_BACKGROUNDCOLOR)));
        submenu.add(editor.bind(mxResources.get("labelBorder"),
                new ColorAction("Label Border",
                        mxConstants.STYLE_LABEL_BORDERCOLOR)));

        submenu.addSeparator();

        submenu.add(editor.bind(mxResources.get("rotateLabel"),
                new ToggleAction(mxConstants.STYLE_HORIZONTAL, true)));

        submenu.add(editor.bind(mxResources.get("textOpacity"),
                new PromptValueAction(mxConstants.STYLE_TEXT_OPACITY,
                        "Opacity (0-100)")));

        submenu.addSeparator();

        JMenu subsubmenu = (JMenu) submenu.add(new JMenu(mxResources
                .get("position")));

        subsubmenu.add(editor.bind(mxResources.get("top"),
                new SetLabelPositionAction(mxConstants.ALIGN_TOP,
                        mxConstants.ALIGN_BOTTOM)));
        subsubmenu.add(editor.bind(mxResources.get("middle"),
                new SetLabelPositionAction(mxConstants.ALIGN_MIDDLE,
                        mxConstants.ALIGN_MIDDLE)));
        subsubmenu.add(editor.bind(mxResources.get("bottom"),
                new SetLabelPositionAction(mxConstants.ALIGN_BOTTOM,
                        mxConstants.ALIGN_TOP)));

        subsubmenu.addSeparator();

        subsubmenu.add(editor.bind(mxResources.get("left"),
                new SetLabelPositionAction(mxConstants.ALIGN_LEFT,
                        mxConstants.ALIGN_RIGHT)));
        subsubmenu.add(editor.bind(mxResources.get("center"),
                new SetLabelPositionAction(mxConstants.ALIGN_CENTER,
                        mxConstants.ALIGN_CENTER)));
        subsubmenu.add(editor.bind(mxResources.get("right"),
                new SetLabelPositionAction(mxConstants.ALIGN_RIGHT,
                        mxConstants.ALIGN_LEFT)));

        submenu.addSeparator();

        submenu.add(editor.bind(mxResources.get("wordWrap"),
                new KeyValueAction(mxConstants.STYLE_WHITE_SPACE, "wrap")));
        submenu.add(editor.bind(mxResources.get("noWordWrap"),
                new KeyValueAction(mxConstants.STYLE_WHITE_SPACE, null)));

        submenu.addSeparator();

        submenu.add(editor.bind(mxResources.get("hide"), new ToggleAction(
                mxConstants.STYLE_NOLABEL)));

        menu.addSeparator();

        submenu = (JMenu) menu.add(new JMenu(mxResources.get("line")));

        submenu.add(editor.bind(mxResources.get("linecolor"), new ColorAction(
                "Linecolor", mxConstants.STYLE_STROKECOLOR),
                "/edu/incense/designer/images/linecolor.gif"));

        submenu.addSeparator();

        submenu.add(editor.bind(mxResources.get("orthogonal"),
                new ToggleAction(mxConstants.STYLE_ORTHOGONAL)));
        submenu.add(editor.bind(mxResources.get("dashed"), new ToggleAction(
                mxConstants.STYLE_DASHED)));

        submenu.addSeparator();

        submenu.add(editor.bind(mxResources.get("linewidth"),
                new PromptValueAction(mxConstants.STYLE_STROKEWIDTH,
                        "Linewidth")));

        submenu = (JMenu) menu.add(new JMenu(mxResources.get("connector")));

        submenu.add(editor.bind(mxResources.get("straight"),
                new SetStyleAction("straight"),
                "/edu/incense/designer/images/straight.gif"));

        submenu.add(editor.bind(mxResources.get("horizontal"),
                new SetStyleAction(""),
                "/edu/incense/designer/images/connect.gif"));
        submenu.add(editor.bind(mxResources.get("vertical"),
                new SetStyleAction("vertical"),
                "/edu/incense/designer/images/vertical.gif"));

        submenu.addSeparator();

        submenu.add(editor.bind(mxResources.get("entityRelation"),
                new SetStyleAction("edgeStyle=mxEdgeStyle.EntityRelation"),
                "/edu/incense/designer/images/entity.gif"));
        submenu.add(editor.bind(mxResources.get("arrow"), new SetStyleAction(
                "arrow"), "/edu/incense/designer/images/arrow.gif"));

        submenu.addSeparator();

        submenu.add(editor.bind(mxResources.get("plain"), new ToggleAction(
                mxConstants.STYLE_NOEDGESTYLE)));

        menu.addSeparator();

        submenu = (JMenu) menu.add(new JMenu(mxResources.get("linestart")));

        submenu.add(editor.bind(mxResources.get("open"), new KeyValueAction(
                mxConstants.STYLE_STARTARROW, mxConstants.ARROW_OPEN),
                "/edu/incense/designer/images/open_start.gif"));
        submenu.add(editor.bind(mxResources.get("classic"), new KeyValueAction(
                mxConstants.STYLE_STARTARROW, mxConstants.ARROW_CLASSIC),
                "/edu/incense/designer/images/classic_start.gif"));
        submenu.add(editor.bind(mxResources.get("block"), new KeyValueAction(
                mxConstants.STYLE_STARTARROW, mxConstants.ARROW_BLOCK),
                "/edu/incense/designer/images/block_start.gif"));

        submenu.addSeparator();

        submenu.add(editor.bind(mxResources.get("diamond"), new KeyValueAction(
                mxConstants.STYLE_STARTARROW, mxConstants.ARROW_DIAMOND),
                "/edu/incense/designer/images/diamond_start.gif"));
        submenu.add(editor.bind(mxResources.get("oval"), new KeyValueAction(
                mxConstants.STYLE_STARTARROW, mxConstants.ARROW_OVAL),
                "/edu/incense/designer/images/oval_start.gif"));

        submenu.addSeparator();

        submenu.add(editor.bind(mxResources.get("none"), new KeyValueAction(
                mxConstants.STYLE_STARTARROW, mxConstants.NONE)));
        submenu.add(editor.bind(mxResources.get("size"), new PromptValueAction(
                mxConstants.STYLE_STARTSIZE, "Linestart Size")));

        submenu = (JMenu) menu.add(new JMenu(mxResources.get("lineend")));

        submenu.add(editor.bind(mxResources.get("open"), new KeyValueAction(
                mxConstants.STYLE_ENDARROW, mxConstants.ARROW_OPEN),
                "/edu/incense/designer/images/open_end.gif"));
        submenu.add(editor.bind(mxResources.get("classic"), new KeyValueAction(
                mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC),
                "/edu/incense/designer/images/classic_end.gif"));
        submenu.add(editor.bind(mxResources.get("block"), new KeyValueAction(
                mxConstants.STYLE_ENDARROW, mxConstants.ARROW_BLOCK),
                "/edu/incense/designer/images/block_end.gif"));

        submenu.addSeparator();

        submenu.add(editor.bind(mxResources.get("diamond"), new KeyValueAction(
                mxConstants.STYLE_ENDARROW, mxConstants.ARROW_DIAMOND),
                "/edu/incense/designer/images/diamond_end.gif"));
        submenu.add(editor.bind(mxResources.get("oval"), new KeyValueAction(
                mxConstants.STYLE_ENDARROW, mxConstants.ARROW_OVAL),
                "/edu/incense/designer/images/oval_end.gif"));

        submenu.addSeparator();

        submenu.add(editor.bind(mxResources.get("none"), new KeyValueAction(
                mxConstants.STYLE_ENDARROW, mxConstants.NONE)));
        submenu.add(editor.bind(mxResources.get("size"), new PromptValueAction(
                mxConstants.STYLE_ENDSIZE, "Lineend Size")));

        menu.addSeparator();

        submenu = (JMenu) menu.add(new JMenu(mxResources.get("alignment")));

        submenu.add(editor.bind(mxResources.get("left"), new KeyValueAction(
                mxConstants.STYLE_ALIGN, mxConstants.ALIGN_LEFT),
                "/edu/incense/designer/images/left.gif"));
        submenu.add(editor.bind(mxResources.get("center"), new KeyValueAction(
                mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER),
                "/edu/incense/designer/images/center.gif"));
        submenu.add(editor.bind(mxResources.get("right"), new KeyValueAction(
                mxConstants.STYLE_ALIGN, mxConstants.ALIGN_RIGHT),
                "/edu/incense/designer/images/right.gif"));

        submenu.addSeparator();

        submenu.add(editor.bind(mxResources.get("top"), new KeyValueAction(
                mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_TOP),
                "/edu/incense/designer/images/top.gif"));
        submenu.add(editor.bind(mxResources.get("middle"), new KeyValueAction(
                mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE),
                "/edu/incense/designer/images/middle.gif"));
        submenu.add(editor.bind(mxResources.get("bottom"), new KeyValueAction(
                mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_BOTTOM),
                "/edu/incense/designer/images/bottom.gif"));

        submenu = (JMenu) menu.add(new JMenu(mxResources.get("spacing")));

        submenu.add(editor.bind(mxResources.get("top"), new PromptValueAction(
                mxConstants.STYLE_SPACING_TOP, "Top Spacing")));
        submenu.add(editor.bind(mxResources.get("right"),
                new PromptValueAction(mxConstants.STYLE_SPACING_RIGHT,
                        "Right Spacing")));
        submenu.add(editor.bind(mxResources.get("bottom"),
                new PromptValueAction(mxConstants.STYLE_SPACING_BOTTOM,
                        "Bottom Spacing")));
        submenu.add(editor.bind(mxResources.get("left"), new PromptValueAction(
                mxConstants.STYLE_SPACING_LEFT, "Left Spacing")));

        submenu.addSeparator();

        submenu.add(editor.bind(mxResources.get("global"),
                new PromptValueAction(mxConstants.STYLE_SPACING, "Spacing")));

        submenu.addSeparator();

        submenu.add(editor.bind(
                mxResources.get("sourceSpacing"),
                new PromptValueAction(
                        mxConstants.STYLE_SOURCE_PERIMETER_SPACING, mxResources
                                .get("sourceSpacing"))));
        submenu.add(editor.bind(
                mxResources.get("targetSpacing"),
                new PromptValueAction(
                        mxConstants.STYLE_TARGET_PERIMETER_SPACING, mxResources
                                .get("targetSpacing"))));

        submenu.addSeparator();

        submenu.add(editor.bind(mxResources.get("perimeter"),
                new PromptValueAction(mxConstants.STYLE_PERIMETER_SPACING,
                        "Perimeter Spacing")));

        submenu = (JMenu) menu.add(new JMenu(mxResources.get("direction")));

        submenu.add(editor.bind(mxResources.get("north"), new KeyValueAction(
                mxConstants.STYLE_DIRECTION, mxConstants.DIRECTION_NORTH)));
        submenu.add(editor.bind(mxResources.get("east"), new KeyValueAction(
                mxConstants.STYLE_DIRECTION, mxConstants.DIRECTION_EAST)));
        submenu.add(editor.bind(mxResources.get("south"), new KeyValueAction(
                mxConstants.STYLE_DIRECTION, mxConstants.DIRECTION_SOUTH)));
        submenu.add(editor.bind(mxResources.get("west"), new KeyValueAction(
                mxConstants.STYLE_DIRECTION, mxConstants.DIRECTION_WEST)));

        submenu.addSeparator();

        submenu.add(editor.bind(mxResources.get("rotation"),
                new PromptValueAction(mxConstants.STYLE_ROTATION,
                        "Rotation (0-360)")));

        menu.addSeparator();

        menu.add(editor.bind(mxResources.get("rounded"), new ToggleAction(
                mxConstants.STYLE_ROUNDED)));

        menu.add(editor.bind(mxResources.get("style"), new StyleAction()));
    }

}
