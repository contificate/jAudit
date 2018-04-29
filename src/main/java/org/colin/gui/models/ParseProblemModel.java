package org.colin.gui.models;

import com.github.javaparser.Problem;
import com.github.javaparser.Range;

import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.util.ArrayList;

public class ParseProblemModel {
    private ArrayList<Problem> problems;
    private File workingFile;
    private DefaultTableModel tableModel;

    public ParseProblemModel(ArrayList<Problem> problems, final File workingFile) {
        this.problems = problems;
        this.workingFile = workingFile;
        tableModel = new DefaultTableModel();
    }

    public ArrayList<Problem> getProblems() {
        return problems;
    }

    public File getWorkingFile() {
        return workingFile;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public void addErrorRow(Range range, String message) {
        tableModel.addRow(new Object[] { range.toString(), message });
    }

}
