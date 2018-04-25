package org.colin.gui;

import com.alee.extended.breadcrumb.WebBreadcrumb;
import com.alee.extended.breadcrumb.WebBreadcrumbButton;
import com.alee.managers.style.StyleId;
import com.github.javaparser.ast.expr.SimpleName;
import org.colin.gui.graph.DrawableNode;
import org.colin.gui.graph.GraphView;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;

@Deprecated
public class ContextView extends JPanel {

    private GraphView graphView;
    private WebBreadcrumb breadcrumb;

    public ContextView() {
        super(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        graphView = new GraphView();
        breadcrumb = new WebBreadcrumb();

        final String[] stuff = { "Node", "MethodDecl", "BlockStmt", "Expression" };
        ArrayList<DrawableNode> nodes = new ArrayList<>();
        for(final String x : stuff) {
            breadcrumb.add(new WebBreadcrumbButton("   " + x + "   "));
            nodes.add(new DrawableNode(new SimpleName(x)));
        }

        graphView.setVertices(nodes);

        this.add(graphView, BorderLayout.CENTER);
        this.add(breadcrumb, BorderLayout.PAGE_END);
    }

}
