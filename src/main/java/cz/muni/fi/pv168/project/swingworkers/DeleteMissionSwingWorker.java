package cz.muni.fi.pv168.project.swingworkers;

import cz.muni.fi.pv168.project.MissionManager;
import cz.muni.fi.pv168.project.models.MissionTableModel;
import javax.swing.SwingWorker;

/**
 * @author Filip Petrovic (422334)
 */
public class DeleteMissionSwingWorker extends SwingWorker<Void, Void>
{
    private MissionManager missionManager;
    private MissionTableModel missionTableModel;
    private int rowIndex;
        
    public DeleteMissionSwingWorker(MissionManager missionManager, MissionTableModel missionTableModel, int rowIndex)
    {
        this.missionManager = missionManager;
        this.missionTableModel = missionTableModel;
        this.rowIndex = rowIndex;
    }
        
    @Override    
    protected Void doInBackground() throws Exception
    {
        missionManager.deleteMission(missionTableModel.getMission(rowIndex).getId());
        return null;
    }
    
    @Override    
    protected void done()
    {
        missionTableModel.removeMission(rowIndex);
    }
}
