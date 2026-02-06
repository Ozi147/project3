ppackage com.company.repositories.impl;

import com.company.models.SymptomEntry;
import com.company.repositories.ISymptomEntryRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SymptomEntryRepositoryImpl implements ISymptomEntryRepository {

    private final Connection connection;

    public SymptomEntryRepositoryImpl(Connection connection){ this.connection = connection; }

    @Override
    public boolean addSymptomEntry(SymptomEntry entry) {
        String sql = "INSERT INTO symptom_entries(patient_id, symptom, entry_date) VALUES (?, ?, ?)";
        try(PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1, entry.getPatientId());
            ps.setString(2, entry.getSymptom());
            ps.setDate(3, Date.valueOf(entry.getEntryDate()));
            ps.executeUpdate();
            return true;
        } catch(SQLException e){ throw new RuntimeException(e);}
    }

    @Override
    public List<SymptomEntry> getSymptomsByPatientId(int patientId){
        List<SymptomEntry> list = new ArrayList<>();
        String sql = "SELECT * FROM symptom_entries WHERE patient_id = ? ORDER BY entry_date";
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, patientId);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    list.add(new SymptomEntry(
                            rs.getInt("patient_id"),
                            rs.getString("symptom"),
                            rs.getDate("entry_date").toLocalDate()
                    ));
                }
            }
        } catch(SQLException e){ throw new RuntimeException(e);}
        return list;
    }
}