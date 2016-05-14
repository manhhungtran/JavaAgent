package cz.muni.fi.pv168.project.swingworkers;

import cz.muni.fi.pv168.project.Assignment;
import cz.muni.fi.pv168.project.AssignmentManager;
import cz.muni.fi.pv168.project.models.AssignmentTableModel;
import java.util.List;
import javax.swing.SwingWorker;

/**
 * @author Filip Petrovic (422334)
 */
public class InitializeAssignmentModelSwingWorker extends SwingWorker<Void, Void>
{
    private AssignmentManager assignmentManager;
    private AssignmentTableModel assignmentTableModel;
    private List<Assignment> assignmentList;
        
    public InitializeAssignmentModelSwingWorker(AssignmentManager assignmentManager, AssignmentTableModel assignmentTableModel)
    {
        this.assignmentManager = assignmentManager;
        this.assignmentTableModel = assignmentTableModel;
    }
        
    @Override    
    protected Void doInBackground() throws Exception
    {
        assignmentList = assignmentManager.getAllAssignments();
        return null;
    }
    
    @Override    
    protected void done()
    {
        assignmentTableModel.initializeModel(assignmentList);
    }
}
