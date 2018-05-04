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

    private int fileId;

    private boolean error = false;

    public AuditModel(File file) {
        workingFile = file;

        // calculate file checksum
        try {
            fileChecksum = FileUtil.calculateCRC32(workingFile);
            System.out.println(fileChecksum);
        } catch (IOException e) {
            error = true;
        }

        // initialise root node of method tree model
        treeModel = new DefaultTreeModel(new ClassTreeNode(file.getName()));
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public int getFileId() {
        return fileId;
    }

    public void setHasError(boolean error) {
        this.error = error;
    }

    public boolean hasError() {
        return error;
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
