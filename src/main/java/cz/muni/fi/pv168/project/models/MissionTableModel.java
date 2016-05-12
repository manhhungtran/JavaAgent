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
    private List<Mission> missions = new ArrayList<>();
 
    @Override
    public int getRowCount()
    {
        return missions.size();
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
        missions.add(mission);
        this.fireTableDataChanged();
    }
    
    public Mission getMission(int rowIndex)
    {
        return missions.get(rowIndex);
    }
    
    public void updateMission(Mission mission, int rowIndex)
    {
        missions.get(rowIndex).setCodename(mission.getCodename());
        missions.get(rowIndex).setDescription(mission.getDescription());
        missions.get(rowIndex).setStart(mission.getStart());
        missions.get(rowIndex).setDifficulty(mission.getDifficulty());
        missions.get(rowIndex).setStatus(mission.getStatus());
        this.fireTableDataChanged();
    }
    
    public void removeMission(int rowIndex)
    {
        missions.remove(rowIndex);
        this.fireTableDataChanged();
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        Mission mission = missions.get(rowIndex);
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

