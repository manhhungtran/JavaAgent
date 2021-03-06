package cz.muni.fi.pv168.project.models;

import cz.muni.fi.pv168.project.Mission;
import cz.muni.fi.pv168.project.MissionDifficulty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.table.AbstractTableModel;

/**
 * @author Filip Petrovic (422334)
 */
public class MissionTableModel extends AbstractTableModel
{
    private List<Mission> missionList = new ArrayList<>();
    private ResourceBundle bundle = ResourceBundle.getBundle("cz/muni/fi/pv168/project/Locale", Locale.getDefault());
    
    public void initializeModel(List<Mission> missions)
    {
        missionList.addAll(missions);
        this.fireTableDataChanged();
    }
    
    @Override
    public int getRowCount()
    {
        return missionList.size();
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
                return bundle.getString("id");
            case 1:
                return bundle.getString("codename");
            case 2:
                return bundle.getString("description");
            case 3:
                return bundle.getString("difficulty");
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
                return String.class;
            case 3:
                return MissionDifficulty.class;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }
    
    public void addMission(Mission mission)
    {
        missionList.add(mission);
        this.fireTableDataChanged();
    }
    
    public Mission getMission(int rowIndex)
    {
        return missionList.get(rowIndex);
    }
    
    public List<Mission> getAllAssignments()
    {
        return Collections.unmodifiableList(missionList);
    }
    
    public void updateMission(Mission mission, int rowIndex)
    {
        missionList.set(rowIndex, mission);
        this.fireTableDataChanged();
    }
    
    public void removeMission(int rowIndex)
    {
        missionList.remove(rowIndex);
        this.fireTableDataChanged();
    }
    
    public int getMissionRowIndex(Mission mission)
    {
        for(int i = 0; i < missionList.size(); i++)
        {
            if(missionList.get(i).equals(mission))
            {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        Mission mission = missionList.get(rowIndex);
        switch(columnIndex)
        {
            case 0:
                return mission.getId();
            case 1:
                return mission.getCodename();
            case 2:
                return mission.getDescription();
            case 3:
                return mission.getDifficulty();
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }
}

