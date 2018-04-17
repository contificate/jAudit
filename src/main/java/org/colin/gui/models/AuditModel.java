package org.colin.gui.models;

import com.github.javaparser.ast.CompilationUnit;
import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
import org.colin.gui.ClassTreeNode;

import javax.swing.tree.DefaultTreeModel;
import java.io.File;

public class AuditModel {

    private DefaultTreeModel treeModel;
    private File workingFile;
    private CompilationUnit unit;

    public AuditModel(File file) {
        this.workingFile = file;
        treeModel = new DefaultTreeModel(new ClassTreeNode(file.getName()));
    }

    public File getWorkingFile() {
        return workingFile;
    }

    public DefaultTreeModel getTreeModel() {
        return treeModel;
    }

    public CompilationUnit getUnit() {
        return unit;
    }

    public void setUnit(CompilationUnit unit) {
        this.unit = unit;
    }


}
