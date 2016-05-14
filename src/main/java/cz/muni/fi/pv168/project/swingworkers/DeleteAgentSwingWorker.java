package cz.muni.fi.pv168.project.swingworkers;

import cz.muni.fi.pv168.project.AgentManager;
import cz.muni.fi.pv168.project.models.AgentTableModel;
import javax.swing.SwingWorker;

/**
 * @author Filip Petrovic (422334)
 */
public class DeleteAgentSwingWorker extends SwingWorker<Void, Void>
{
    private AgentManager agentManager;
    private AgentTableModel agentTableModel;
    private int rowIndex;
        
    public DeleteAgentSwingWorker(AgentManager agentManager, AgentTableModel agentTableModel, int rowIndex)
    {
        this.agentManager = agentManager;
        this.agentTableModel = agentTableModel;
        this.rowIndex = rowIndex;
    }
        
    @Override    
    protected Void doInBackground() throws Exception
    {
        agentManager.deleteAgent(agentTableModel.getAgent(rowIndex).getId());
        return null;
    }
    
    @Override    
    protected void done()
    {
        agentTableModel.removeAgent(rowIndex);
    }
}
