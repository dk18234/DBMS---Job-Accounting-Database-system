package jsp_azure_test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;

public class Rama_Dinesh_IP_Task7_DataHandler {
    private Connection conn;
    
    // Azure SQL connection credentials
    private String server = "rama0045.database.windows.net";
    private String database = "cs-dsa-4513-sql-db";
    private String username = "rama0045";
    private String password = "Dkronaldo7@";
    
    // Resulting connection string
    final private String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;", server, database, username, password);

    // Initialize and save the database connection
    private void getDBConnection() throws SQLException {
        if (conn != null) {
            return;
        }
        this.conn = DriverManager.getConnection(url);
    }

    // Returning the result from the customer table within a category range
    public ResultSet getCustomersByCategoryRange(int minCategory, int maxCategory) throws SQLException {
        getDBConnection();
        final String sqlQuery = "SELECT * FROM customer WHERE category BETWEEN ? AND ? ORDER BY name;";
        final PreparedStatement stmt = conn.prepareStatement(sqlQuery);
        stmt.setInt(1, minCategory);
        stmt.setInt(2, maxCategory);
        return stmt.executeQuery();
    }

    // Inserting a record into the customer table using a stored procedure with the given attribute values
    public boolean addCustomer(String customerName, String customerAddress, int category) throws SQLException {
        getDBConnection(); // Prepare the database connection
        // Prepare the SQL statement
        final String sqlQuery = "{CALL InsertCustomer(?, ?, ?)}";
        final CallableStatement stmt = conn.prepareCall(sqlQuery);
        
        stmt.setString(1, customerName);
        stmt.setString(2, customerAddress);
        stmt.setInt(3, category);

        // Execute the stored procedure
        stmt.execute();

        // Indicate success by returning true
        return true;
    }
}
