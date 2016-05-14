package cz.muni.fi.pv168.project.models;

import cz.muni.fi.pv168.project.Agent;
import cz.muni.fi.pv168.project.AgentExperience;
import cz.muni.fi.pv168.project.AgentStatus;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * @author Filip Petrovic (422334)
 */
public class AgentTableModel extends AbstractTableModel
{
    private List<Agent> agentList = new ArrayList<>();
    
    public void initializeModel(List<Agent> agents)
    {
        agentList.addAll(agents);
        this.fireTableDataChanged();
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
        agentList.add(agent);
        this.fireTableDataChanged();
    }
    
    public Agent getAgent(int rowIndex)
    {
        return agentList.get(rowIndex);
    }
    
    public void updateAgent(Agent agent, int rowIndex)
    {
        agentList.set(rowIndex, agent);
        this.fireTableDataChanged();
    }
    
    public void removeAgent(int rowIndex)
    {
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
