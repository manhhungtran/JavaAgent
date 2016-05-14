package cz.muni.fi.pv168.project.swingworkers;

import cz.muni.fi.pv168.project.Agent;
import cz.muni.fi.pv168.project.AgentManager;
import cz.muni.fi.pv168.project.models.AgentTableModel;
import java.util.List;
import javax.swing.SwingWorker;

/**
 * @author Filip Petrovic (422334)
 */
public class InitializeAgentModelSwingWorker extends SwingWorker<Void, Void>
{
    private AgentManager agentManager;
    private AgentTableModel agentTableModel;
    private List<Agent> agentList;
        
    public InitializeAgentModelSwingWorker(AgentManager agentManager, AgentTableModel agentTableModel)
    {
        this.agentManager = agentManager;
        this.agentTableModel = agentTableModel;
    }
        
    @Override    
    protected Void doInBackground() throws Exception
    {
        agentList = agentManager.getAllAgents();
        return null;
    }
    
    @Override    
    protected void done()
    {
        agentTableModel.initializeModel(agentList);
    }
}
