package org.colin.gui.graph;

import com.github.javaparser.ast.Node;
import org.colin.audit.AuditContext;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class AuditGraph extends GraphView<DrawableNode> {

    private AuditContext context;

    public AuditGraph() {
        context = new AuditContext();
    }

    public void setContext(@Nullable AuditContext context) {
        if (context == null) {
            this.context.clearContext();
            return;
        }

        this.context = context;

        ArrayList<DrawableNode> sprites = new ArrayList<>();
        for (Node node : this.context)
            sprites.add(new DrawableNode(node));

        canvas.setVertices(sprites);
        canvas.repaint();
    }

    public AuditContext getContext() {
        return context;
    }
}
