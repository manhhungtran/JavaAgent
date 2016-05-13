package cz.muni.fi.pv168.project;

import java.util.List;
import javax.swing.SwingWorker;

/**
 * @author Filip Petrovic (422334)
 */
public class DatabaseSwingWorker
{
    private class InitializeAgentListSwingWorker extends SwingWorker<List<Agent>, Void>
    {
        private AgentManager agentManager;
        
        InitializeAgentListSwingWorker(AgentManager agentManager)
        {
            this.agentManager = agentManager;
        }
        
        @Override    
        protected List<Agent> doInBackground() throws Exception
        {
            List<Agent> agentList = agentManager.getAllAgents();
            return agentList;
        }
    }
}
