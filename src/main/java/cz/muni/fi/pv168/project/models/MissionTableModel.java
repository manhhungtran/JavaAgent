package cz.muni.fi.pv168.project.models;

import cz.muni.fi.pv168.project.Mission;
import cz.muni.fi.pv168.project.MissionDifficulty;
import cz.muni.fi.pv168.project.MissionStatus;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * @author Filip Petrovic (422334)
 */
public class MissionTableModel extends AbstractTableModel
{
    private List<Mission> missionList = new ArrayList<>();
    
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
        return 6;
    }
    
    @Override
    public String getColumnName(int columnIndex)
    {
        switch(columnIndex)
        {
            case 0:
                return "Id";
            case 1:
                return "Codename";
            case 2:
                return "Description";
            case 3:
                return "Start";
            case 4:
                return "Difficulty";
            case 5:
                return "Status";
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
            case 2:
                return String.class;
            case 3:
                return LocalDate.class;
            case 4:
                return MissionDifficulty.class;
            case 5:
                return MissionStatus.class;
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
                return mission.getStart();
            case 4:
                return mission.getDifficulty();
            case 5:
                return mission.getStatus();
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }
}

