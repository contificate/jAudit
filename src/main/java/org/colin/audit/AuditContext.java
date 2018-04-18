package org.colin.audit;

import com.github.javaparser.ast.Node;
import com.sun.istack.internal.NotNull;
import org.colin.gui.views.AuditView;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Audit context designed to improve uniformity of encapsulation
 */
public class AuditContext implements Iterable<Node> {

    /**
     * Stores node context (root -> interior -> leaf)
     */
    private ArrayList<Node> nodes;

    /**
     *
     */
    public AuditContext() {
        nodes = new ArrayList<>();
    }

    public void add(Node node) {
        nodes.add(node);
    }

    public void clearContext() {
        nodes.clear();
    }

    @Override
    public Iterator<Node> iterator() {
        return nodes.iterator();
    }

}
