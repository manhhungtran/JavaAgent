package cz.muni.fi.pv168.project.swingworkers;

import cz.muni.fi.pv168.project.Assignment;
import cz.muni.fi.pv168.project.AssignmentManager;
import cz.muni.fi.pv168.project.models.AssignmentTableModel;
import javax.swing.SwingWorker;

/**
 * @author Filip Petrovic (422334)
 */
public class UpdateAssignmentSwingWorker extends SwingWorker<Void, Void>
{
    private AssignmentManager assignmentManager;
    private AssignmentTableModel assignmentTableModel;
    private Assignment assignment;
    private int rowIndex;
        
    public UpdateAssignmentSwingWorker(AssignmentManager assignmentManager, AssignmentTableModel assignmentTableModel, Assignment assignment, int rowIndex)
    {
        this.assignmentManager = assignmentManager;
        this.assignmentTableModel = assignmentTableModel;
        this.assignment = assignment;
        this.rowIndex = rowIndex;
    }
        
    @Override    
    protected Void doInBackground() throws Exception
    {
        assignmentManager.updateAssignment(assignment);
        return null;
    }
    
    @Override    
    protected void done()
    {
        assignmentTableModel.updateAssignment(assignment, rowIndex);
    }
}
