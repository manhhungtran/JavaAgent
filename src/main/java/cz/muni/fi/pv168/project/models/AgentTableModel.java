package cz.muni.fi.pv168.project.models;

import cz.muni.fi.pv168.project.Agent;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Fillo
 */
public class AgentTableModel extends AbstractTableModel {
 
    private List<Agent> agents = new ArrayList<Agent>();
 
    @Override
    public int getRowCount() {
        return agents.size();
    }
 
    @Override
    public int getColumnCount() {
        return 4;
    }
    public void addAgent(Agent agent)
    {
        agents.add(agent);
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Agent agent = agents.get(rowIndex);
        switch (columnIndex) {
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
