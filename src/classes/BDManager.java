package classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

public class BDManager {
    private String url;
    private String user;
    private String password;

    public BDManager(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public void createRecord(String table, JSONObject data) {
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();
        data.keys().forEachRemaining(key -> {
            columns.append(key.toUpperCase()).append(",");
            values.append("?").append(",");
        });

        // Remove the last comma
        columns.setLength(columns.length() - 1);
        values.setLength(values.length() - 1);

        String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", table.toUpperCase(), columns, values);

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            int index = 1;
            for (String key : data.keySet()) {
                pstmt.setObject(index++, data.get(key));
            }

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void updateRecord(String table, JSONObject data, String condition) {
        StringBuilder setClause = new StringBuilder();
        data.keys().forEachRemaining(key -> {
            setClause.append(key.toUpperCase()).append(" = ?,");
        });

        // Remove the last comma
        setClause.setLength(setClause.length() - 1);

        String sql = String.format("UPDATE %s SET %s WHERE %s", table.toUpperCase(), setClause, condition);

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            int index = 1;
            for (String key : data.keySet()) {
                pstmt.setObject(index++, data.get(key));
            }

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRecord(String table, String condition) {
        String sql = String.format("DELETE FROM %s WHERE %s", table.toUpperCase(), condition);

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public JSONObject selectRecord(String table, String condition) {
        String sql = String.format("SELECT * FROM %s WHERE %s", table.toUpperCase(), condition);

        try {
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            return resultSetToJSON(pstmt.executeQuery());
        } catch (SQLException e) { 
            e.printStackTrace();
        }
        return null;
    }
    
    public JSONObject resultSetToJSON(ResultSet rs) throws SQLException {
        JSONArray jsonArray = new JSONArray();

        ResultSetMetaData metadata = rs.getMetaData();
        int numColumns = metadata.getColumnCount();

        while (rs.next()) {
            JSONObject obj = new JSONObject();

            for (int i = 1; i <= numColumns; i++) {
                String column_name = metadata.getColumnName(i);
                obj.put(column_name, rs.getObject(column_name));
            }

            jsonArray.put(obj);
        }

        return new JSONObject().put("results", jsonArray);
    }
}
