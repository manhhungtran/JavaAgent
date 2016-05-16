package cz.muni.fi.pv168.project.models;

import cz.muni.fi.pv168.project.Assignment;
import cz.muni.fi.pv168.project.AssignmentStatus;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.table.AbstractTableModel;

/**
 * @author Filip Petrovic (422334)
 */
public class AssignmentTableModel extends AbstractTableModel
{
    private List<Assignment> assignmentList = new ArrayList<>();
    private ResourceBundle bundle = ResourceBundle.getBundle("cz/muni/fi/pv168/project/Locale", Locale.getDefault());
    
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
        return 5;
    }
    
    @Override
    public String getColumnName(int columnIndex)
    {
        switch(columnIndex)
        {
            case 0:
                return bundle.getString("assignment_id");
            case 1:
                return bundle.getString("status");
            case 2:
                return bundle.getString("start");
            case 3:
                return bundle.getString("agent_alias");
            case 4:
                return bundle.getString("mission_codename");
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
                return AssignmentStatus.class;
            case 2:
                return LocalDate.class;
            case 3:
                return String.class;
            case 4:
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
    
    public List<Assignment> getAllAssignments()
    {
        return Collections.unmodifiableList(assignmentList);
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
                return assignment.getStatus();
            case 2:
                return assignment.getStartDate();
            case 3:
                return assignment.getAgent().getAlias();
            case 4:
                return assignment.getMission().getCodename();
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }
}
