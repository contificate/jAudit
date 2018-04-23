package org.colin.gui.models;

import com.github.javaparser.ast.CompilationUnit;
import org.colin.gui.ClassTreeNode;
import org.colin.util.FileUtil;

import javax.swing.tree.DefaultTreeModel;
import java.io.File;
import java.io.IOException;

/**
 *
 */
public class AuditModel {

    private DefaultTreeModel treeModel;
    private File workingFile;
    private CompilationUnit unit;
    private long fileChecksum;

    public AuditModel(File file) {
        workingFile = file;

        // calculate file checksum
        try {
            fileChecksum = FileUtil.calculateCRC32(workingFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // initialise root node of method tree model
        treeModel = new DefaultTreeModel(new ClassTreeNode(file.getName()));
    }

    public File getWorkingFile() {
        return workingFile;
    }

    public long getFileChecksum() {
        return fileChecksum;
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
