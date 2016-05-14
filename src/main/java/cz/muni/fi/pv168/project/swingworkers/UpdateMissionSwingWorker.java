package cz.muni.fi.pv168.project.swingworkers;

import cz.muni.fi.pv168.project.Mission;
import cz.muni.fi.pv168.project.MissionManager;
import cz.muni.fi.pv168.project.models.MissionTableModel;
import javax.swing.SwingWorker;

/**
 * @author Filip Petrovic (422334)
 */
public class UpdateMissionSwingWorker extends SwingWorker<Void, Void>
{
    private MissionManager missionManager;
    private MissionTableModel missionTableModel;
    private Mission mission;
    private int rowIndex;
        
    public UpdateMissionSwingWorker(MissionManager missionManager, MissionTableModel missionTableModel, Mission mission, int rowIndex)
    {
        this.missionManager = missionManager;
        this.missionTableModel = missionTableModel;
        this.mission = mission;
        this.rowIndex = rowIndex;
    }
        
    @Override    
    protected Void doInBackground() throws Exception
    {
        missionManager.updateMission(mission);
        return null;
    }
    
    @Override    
    protected void done()
    {
        missionTableModel.updateMission(mission, rowIndex);
    }
}
