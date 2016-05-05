package cz.muni.fi.pv168.project.models;

import cz.muni.fi.pv168.project.Agent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * @author Filip Petrovic (422334)
 */
public class AgentTableModel extends AbstractTableModel
{
    private List<Agent> agents = new ArrayList<>();
 
    @Override
    public int getRowCount()
    {
        return agents.size();
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
    
    public void addAgent(Agent agent)
    {
        agents.add(agent);
        this.fireTableDataChanged();
    }
    
    public Agent getAgent(int rowIndex)
    {
        return agents.get(rowIndex);
    }
    
    public void updateAgent(Agent agent, int rowIndex)
    {
        agents.get(rowIndex).setAlias(agent.getAlias());
        agents.get(rowIndex).setStatus(agent.getStatus());
        agents.get(rowIndex).setExperience(agent.getExperience());
        this.fireTableDataChanged();
    }
    
    public void removeAgent(int rowIndex)
    {
        agents.remove(rowIndex);
        this.fireTableDataChanged();
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        Agent agent = agents.get(rowIndex);
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
