package cz.muni.fi.pv168.project.swingworkers;

import cz.muni.fi.pv168.project.Mission;
import cz.muni.fi.pv168.project.MissionManager;
import cz.muni.fi.pv168.project.models.MissionTableModel;
import javax.swing.SwingWorker;

/**
 * @author Filip Petrovic (422334)
 */
public class AddMissionSwingWorker extends SwingWorker<Void, Void>
{
    private MissionManager missionManager;
    private MissionTableModel missionTableModel;
    private Mission mission;
        
    public AddMissionSwingWorker(MissionManager missionManager, MissionTableModel missionTableModel, Mission mission)
    {
        this.missionManager = missionManager;
        this.missionTableModel = missionTableModel;
        this.mission = mission;
    }
        
    @Override    
    protected Void doInBackground() throws Exception
    {
        missionManager.addMission(mission);
        return null;
    }
    
    @Override    
    protected void done()
    {
        missionTableModel.addMission(mission);
    }
}
