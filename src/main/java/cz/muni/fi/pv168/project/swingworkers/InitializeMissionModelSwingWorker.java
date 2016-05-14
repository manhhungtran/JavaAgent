package cz.muni.fi.pv168.project.swingworkers;

import cz.muni.fi.pv168.project.Mission;
import cz.muni.fi.pv168.project.MissionManager;
import cz.muni.fi.pv168.project.models.MissionTableModel;
import java.util.List;
import javax.swing.SwingWorker;

/**
 * @author Filip Petrovic (422334)
 */
public class InitializeMissionModelSwingWorker extends SwingWorker<Void, Void>
{
    private MissionManager missionManager;
    private MissionTableModel missionTableModel;
    private List<Mission> missionList;
        
    public InitializeMissionModelSwingWorker(MissionManager missionManager, MissionTableModel missionTableModel)
    {
        this.missionManager = missionManager;
        this.missionTableModel = missionTableModel;
    }
        
    @Override    
    protected Void doInBackground() throws Exception
    {
        missionList = missionManager.getAllMissions();
        return null;
    }
    
    @Override    
    protected void done()
    {
        missionTableModel.initializeModel(missionList);
    }
}
