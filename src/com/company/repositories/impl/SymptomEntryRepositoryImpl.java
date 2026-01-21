package repositories.impl;

import repositories.SymptomEntryRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SymptomEntryRepositoryImpl implements SymptomEntryRepository {

    private final Connection connection;

    public SymptomEntryRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void add(int recordId, String symptom) {
        String sql = "INSERT INTO symptom_entries(medical_record_id, symptom) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, recordId);
            ps.setString(2, symptom);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
