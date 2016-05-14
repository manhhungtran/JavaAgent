package cz.muni.fi.pv168.project.swingworkers;

import cz.muni.fi.pv168.project.Agent;
import cz.muni.fi.pv168.project.AgentManager;
import cz.muni.fi.pv168.project.models.AgentTableModel;
import javax.swing.SwingWorker;

/**
 * @author Filip Petrovic (422334)
 */
public class UpdateAgentSwingWorker extends SwingWorker<Void, Void>
{
    private AgentManager agentManager;
    private AgentTableModel agentTableModel;
    private Agent agent;
    private int rowIndex;
        
    public UpdateAgentSwingWorker(AgentManager agentManager, AgentTableModel agentTableModel, Agent agent, int rowIndex)
    {
        this.agentManager = agentManager;
        this.agentTableModel = agentTableModel;
        this.agent = agent;
        this.rowIndex = rowIndex;
    }
        
    @Override    
    protected Void doInBackground() throws Exception
    {
        agentManager.updateAgent(agent);
        return null;
    }
    
    @Override    
    protected void done()
    {
        agentTableModel.updateAgent(agent, rowIndex);
    }
}