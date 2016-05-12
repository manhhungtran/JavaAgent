package cz.muni.fi.pv168.project.models;

import cz.muni.fi.pv168.project.Agent;
import cz.muni.fi.pv168.project.AgentExperience;
import cz.muni.fi.pv168.project.AgentManager;
import cz.muni.fi.pv168.project.AgentStatus;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * @author Filip Petrovic (422334)
 */
public class AgentTableModel extends AbstractTableModel
{
    private AgentManager agentManager;
    private List<Agent> agentList = new ArrayList<>();
    
    public void initializeAgentManager(AgentManager agentManager)
    {
        this.agentManager = agentManager;
        agentList.addAll(agentManager.getAllAgents());
    }
    
    @Override
    public int getRowCount()
    {
        return agentList.size();
    }
 
    @Override
    public int getColumnCount()
    {
        return 4;
    }
    
    @Override
    public String getColumnName(int columnIndex)
    {
        switch(columnIndex)
        {
            case 0:
                return "Id";
            case 1:
                return "Alias";
            case 2:
                return "Status";
            case 3:
                return "Experience";
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        switch (columnIndex)
        {
            case 0:
                return Integer.class;
            case 1:
                return String.class;
            case 2:
                return AgentStatus.class;
            case 3:
                return AgentExperience.class;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }
    
    public void addAgent(Agent agent)
    {
        agentManager.addAgent(agent);
        
        agentList.clear();
        agentList.addAll(agentManager.getAllAgents());
        this.fireTableDataChanged();
    }
    
    public Agent getAgent(int rowIndex)
    {
        return agentList.get(rowIndex);
    }
    
    public void updateAgent(Agent agent, int rowIndex)
    {
        agentManager.updateAgent(agent);
        
        agentList.set(rowIndex, agent);
        this.fireTableDataChanged();
    }
    
    public void removeAgent(int rowIndex)
    {
        agentManager.deleteAgent(agentList.get(rowIndex).getId());
        
        agentList.remove(rowIndex);
        this.fireTableDataChanged();
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        Agent agent = agentList.get(rowIndex);
        switch(columnIndex)
        {
            case 0:
                return agent.getId();
            case 1:
                return agent.getAlias();
            case 2:
                return agent.getStatus();
            case 3:
                return agent.getExperience();
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }
}
