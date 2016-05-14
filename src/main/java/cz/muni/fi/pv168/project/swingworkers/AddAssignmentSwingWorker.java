package cz.muni.fi.pv168.project.swingworkers;

import cz.muni.fi.pv168.project.Assignment;
import cz.muni.fi.pv168.project.AssignmentManager;
import cz.muni.fi.pv168.project.models.AssignmentTableModel;
import javax.swing.SwingWorker;

/**
 * @author Filip Petrovic (422334)
 */
public class AddAssignmentSwingWorker extends SwingWorker<Void, Void>
{
    private AssignmentManager assignmentManager;
    private AssignmentTableModel assignmentTableModel;
    private Assignment assignment;
        
    public AddAssignmentSwingWorker(AssignmentManager assignmentManager, AssignmentTableModel assignmentTableModel, Assignment assignment)
    {
        this.assignmentManager = assignmentManager;
        this.assignmentTableModel = assignmentTableModel;
        this.assignment = assignment;
    }
        
    @Override    
    protected Void doInBackground() throws Exception
    {
        assignmentManager.addAssignment(assignment);
        return null;
    }
    
    @Override    
    protected void done()
    {
        assignmentTableModel.addAssignment(assignment);
    }
}
