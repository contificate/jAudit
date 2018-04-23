package org.colin.tests;

import com.alee.laf.WebLookAndFeel;
import com.github.javaparser.ast.expr.SimpleName;
import org.colin.gui.ContextView;
import org.colin.gui.MainWindow;
import org.colin.gui.graph.DrawableNode;
import org.colin.gui.graph.GraphView;

import javax.swing.*;
import java.util.ArrayList;

public class ContextTest extends JFrame {

    private ContextView view;
    private GraphView graph;

    public ContextTest() {
        super("Context Menu Dev");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {

        graph = new GraphView();
        ArrayList<DrawableNode> nodes = new ArrayList<>();
        for(int i = 0; i < 10; i++)
            nodes.add(new DrawableNode(new SimpleName()));

        graph.setVertices(nodes);

        add(graph);

        /*
        view = new GraphView<>();
        add(view);*/
    }


    public static void main(String[] args) {
        // initialise look and feel
        WebLookAndFeel.install();
        WebLookAndFeel.initializeManagers();
        SwingUtilities.invokeLater(ContextTest::new);
    }

}
