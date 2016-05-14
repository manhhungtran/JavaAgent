package cz.muni.fi.pv168.project.models;

import cz.muni.fi.pv168.project.Assignment;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * @author Filip Petrovic (422334)
 */
public class AssignmentTableModel extends AbstractTableModel
{
    private List<Assignment> assignmentList = new ArrayList<>();
    
    public void initializeModel(List<Assignment> assignments)
    {
        assignmentList.addAll(assignments);
        this.fireTableDataChanged();
    }
    
    @Override
    public int getRowCount()
    {
        return assignmentList.size();
    }
 
    @Override
    public int getColumnCount()
    {
        return 3;
    }
    
    @Override
    public String getColumnName(int columnIndex)
    {
        switch(columnIndex)
        {
            case 0:
                return "Assignment Id";
            case 1:
                return "Agent Alias";
            case 2:
                return "Mission Codename";
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        switch (columnIndex)
        {
            case 0:
                return Integer.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }
    
    public void addAssignment(Assignment assignment)
    {
        assignmentList.add(assignment);
        this.fireTableDataChanged();
    }
    
    public Assignment getAssignment(int rowIndex)
    {
        return assignmentList.get(rowIndex);
    }
    
    public void updateAssignment(Assignment assignment, int rowIndex)
    {
        assignmentList.set(rowIndex, assignment);
        this.fireTableDataChanged();
    }
    
    public void removeAssignment(int rowIndex)
    {
        assignmentList.remove(rowIndex);
        this.fireTableDataChanged();
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        Assignment assignment = assignmentList.get(rowIndex);
        switch(columnIndex)
        {
            case 0:
                return assignment.getId();
            case 1:
                return assignment.getAgent().getAlias();
            case 2:
                return assignment.getMission().getCodename();
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }
}
