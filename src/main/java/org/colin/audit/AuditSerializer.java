package org.colin.audit;

import com.github.javaparser.Position;
import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Iterator;

/**
 * Serializer registered by {@link GsonBuilder} to transpose an {@link AuditContext} to a JSON representation for storage.
 */
public class AuditSerializer implements JsonSerializer<AuditContext> {
    /**
     * Serialize an {@link AuditContext} to JSON.
     *
     * @param nodes   the audit context
     * @param type    type supplied by the
     * @param context the serialization context
     * @return JSON representation of {@link AuditContext}
     */
    @Override
    public JsonElement serialize(AuditContext nodes, Type type, JsonSerializationContext context) {
        // create an empty JSON object
        JsonObject json = new JsonObject();

        // get iterator
        Iterator<Node> iter = nodes.iterator();

        // get root node
        final Node root = iter.next();
        final Range rootRange = root.getRange().orElse(Range.range(0, 0, 0, 0));

        // get root positions
        final Position rootBegin = rootRange.begin, rootEnd = rootRange.end;

        // add root node to JSON
        json.addProperty(root.getClass().getSimpleName(), root.toString());

        // add children with relative positions
        JsonArray children = new JsonArray();
        iter.forEachRemaining(node -> {
            JsonArray child = new JsonArray();
            // add class name as first element
            child.add(node.getClass().getSimpleName());

            // get child range
            final Range range = node.getRange()
                    .orElse(Range.range(0, 0, 0, 0));
            final Position begin = range.begin, end = range.end;

            // transpose begin
            JsonArray first = new JsonArray();
            first.add(begin.line - rootBegin.line);
            first.add(begin.column);

            // transpose end
            JsonArray last = new JsonArray();
            last.add(end.line - rootEnd.line);
            last.add(end.column);

            // add positions to child
            child.add(first);
            child.add(last);

            // add child to children
            children.add(child);
        });

        // append children list to JSON object
        json.add("children", children);

        // return the JSON object
        return json;
    }
}
