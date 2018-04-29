package org.colin.audit;

import com.github.javaparser.ast.Node;
import com.sun.istack.internal.NotNull;
import org.colin.gui.views.AuditView;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Audit context designed to improve uniformity of encapsulation
 */
public class AuditContext implements Iterable<Node> {
    /**
     * Stores node context trace (<b>root</b> &rarr; <b>internal nodes</b> &rarr; <b>leaf</b>)
     */
    private ArrayList<Node> nodes;

    /**
     * Create empty audit context
     */
    public AuditContext() {
        nodes = new ArrayList<>();
    }

    /**
     * Add node to context
     *
     * @param node node being added to context
     */
    public void add(Node node) {
        nodes.add(node);
    }

    /**
     * Clear context (nodes)
     */
    public void clearContext() {
        nodes.clear();
    }

    /**
     * Expose {@link Iterator} to context user
     *
     * @return node iterator
     */
    @Override
    @Nonnull
    public Iterator<Node> iterator() {
        return nodes.iterator();
    }

    public void sublist(int first, int last) {
        nodes = new ArrayList<>(nodes.subList(first, last));
        // nodes = (ArrayList<Node>) nodes.subList(first, last);
    }

}
