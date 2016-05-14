package cz.muni.fi.pv168.project.swingworkers;

import cz.muni.fi.pv168.project.AssignmentManager;
import cz.muni.fi.pv168.project.models.AssignmentTableModel;
import javax.swing.SwingWorker;

/**
 * @author Filip Petrovic (422334)
 */
public class DeleteAssignmentSwingWorker extends SwingWorker<Void, Void>
{
    private AssignmentManager assignmentManager;
    private AssignmentTableModel assignmentTableModel;
    private int rowIndex;
        
    public DeleteAssignmentSwingWorker(AssignmentManager assignmentManager, AssignmentTableModel assignmentTableModel, int rowIndex)
    {
        this.assignmentManager = assignmentManager;
        this.assignmentTableModel = assignmentTableModel;
        this.rowIndex = rowIndex;
    }
        
    @Override    
    protected Void doInBackground() throws Exception
    {
        assignmentManager.deleteAssignment(assignmentTableModel.getAssignment(rowIndex).getId());
        return null;
    }
    
    @Override    
    protected void done()
    {
        assignmentTableModel.removeAssignment(rowIndex);
    }
}
