package cz.muni.fi.pv168.project.swingworkers;

import cz.muni.fi.pv168.project.Agent;
import cz.muni.fi.pv168.project.AgentManager;
import cz.muni.fi.pv168.project.models.AgentTableModel;
import javax.swing.SwingWorker;

/**
 * @author Filip Petrovic (422334)
 */
public class AddAgentSwingWorker extends SwingWorker<Void, Void>
{
    private AgentManager agentManager;
    private AgentTableModel agentTableModel;
    private Agent agent;
        
    public AddAgentSwingWorker(AgentManager agentManager, AgentTableModel agentTableModel, Agent agent)
    {
        this.agentManager = agentManager;
        this.agentTableModel = agentTableModel;
        this.agent = agent;
    }
        
    @Override    
    protected Void doInBackground() throws Exception
    {
        agentManager.addAgent(agent);
        return null;
    }
    
    @Override    
    protected void done()
    {
        agentTableModel.addAgent(agent);
    }
}
