package cz.muni.fi.pv168.project.models;

import cz.muni.fi.pv168.project.Agent;
import cz.muni.fi.pv168.project.AgentExperience;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.table.AbstractTableModel;

/**
 * @author Filip Petrovic (422334)
 */
public class AgentTableModel extends AbstractTableModel
{
    private List<Agent> agentList = new ArrayList<>();
    private ResourceBundle bundle = ResourceBundle.getBundle("cz/muni/fi/pv168/project/Locale", Locale.getDefault());
    
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
        return 3;
    }
    
    @Override
    public String getColumnName(int columnIndex)
    {
        switch(columnIndex)
        {
            case 0:
                return bundle.getString("id");
            case 1:
                return bundle.getString("alias");
            case 2:
                return bundle.getString("experience");
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
    
    public List<Agent> getAllAssignments()
    {
        return Collections.unmodifiableList(agentList);
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
    
    public int getAgentRowIndex(Agent agent)
    {
        for(int i = 0; i < agentList.size(); i++)
        {
            if(agentList.get(i).equals(agent))
            {
                return i;
            }
        }
        return -1;
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
                return agent.getExperience();
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }
}
