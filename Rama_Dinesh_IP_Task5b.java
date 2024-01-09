
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.Scanner;

public class Rama_Dinesh_IP_Task5b {
    //  Database URL COnnection
	private static final String DATABASE_URL = "jdbc:sqlserver://rama0045.database.windows.net:1433;" +
	        "database=cs-dsa-4513-sql-db;user=rama0045;password=Dkronaldo7@;" +
	        "encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";

    public static void main(String[] args) {
    	//Creating Database connection
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            Scanner scanner = new Scanner(System.in);
            int choice;

            do {
                // Display menu
                System.out.println("Welcome to Job-Shop Accounting Database System:");
                System.out.println("1. Insert a new customer");
                System.out.println("2. Insert a new department");
                System.out.println("3. Insert a new process");
                System.out.println("4. Insert a new assembly");
                System.out.println("5. Insert a new account");
                System.out.println("6. Enter the new job");
                System.out.println("7. Enter complete job details");
                System.out.println("8. Update account cost details");
                System.out.println("9. Retrieve total cost on assembly-id ");
                System.out.println("10. Retrieve total labor time in a given date");
                System.out.println("11. Retrieve process");
                System.out.println("12. Retrieve customer details between given category range ");
                System.out.println("13. Delete cut-jobs between given job range");
                System.out.println("14. Change color of paint-job");
                System.out.println("15. Import customer details from a file");
                System.out.println("16. Export customer details to a file");
                System.out.println("17. QUIT!");
                
                
               
                System.out.println("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        // Insert a new customer
                        System.out.println("Enter customer name: ");
                        String customerName = scanner.nextLine();
                        System.out.println("Enter address: ");
                        String address = scanner.nextLine();
                        System.out.println("Enter category: ");
                        int category = scanner.nextInt();
                        
                        insertCustomer(connection, customerName, address, category);
                        break;
                    	
                    
                       
                    case 2:
                        // Insert a new department
                        System.out.println("Enter department number: ");
                        int deptNo = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.println("Enter department data: ");
                        String deptData = scanner.nextLine();
                        
                        insertDepartment(connection, deptNo, deptData);
                        break;
                    
                    case 3:
                    	// Insert a new process
                        System.out.println("Enter process ID: ");
                        int processId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.println("Enter process data: ");
                        String process_data = scanner.nextLine();
                        System.out.println("Enter department number: ");
                        int deptNoForProcess = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.println("Enter department data: ");
                        String deptDataForProcess = scanner.nextLine();
                        System.out.println("Enter process type (fit/cut/paint): ");
                        String processType = scanner.nextLine();

                        // Additional information based on process type
                        String typeInfo1 = "";
                        String typeInfo2 = "";
                        if (processType.equals("fit") || processType.equals("cut") || processType.equals("paint")) {
                            System.out.println("Enter type info 1: ");
                            typeInfo1 = scanner.nextLine();
                        }
                        if (processType.equals("cut")) {
                            System.out.println("Enter type info 2: ");
                            typeInfo2 = scanner.nextLine();
                        }

                        insertProcess(connection, processId, process_data, deptNoForProcess, deptDataForProcess, processType, typeInfo1, typeInfo2);
                        break;
                    
                    case 4:
                        // Insert a new assembly
                        System.out.println("Enter customer name: ");
                        customerName = scanner.nextLine();
                        System.out.println("Enter assembly details: ");
                        String assemblyDetails = scanner.nextLine();
                        System.out.println("Enter assembly ID: ");
                        int assemblyID = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.println("Enter date ordered (YYYY-MM-DD): ");
                        String dateOrderedStr = scanner.nextLine();
                        System.out.println("Enter comma-separated process IDs: ");
                        String processIDs = scanner.nextLine();

                        // Convert date string to java.sql.Date
                        java.sql.Date dateOrdered = java.sql.Date.valueOf(dateOrderedStr);

                        // Call the stored procedure
                        try {
                            insertNewAssembly(connection, customerName, assemblyDetails, assemblyID, dateOrdered, processIDs);
                            System.out.println("Assembly inserted successfully!");
                        } catch (SQLException e) {
                            e.printStackTrace();
                            System.out.println("Error inserting assembly. Please try again.");
                        }
                        break;

                    case 5:
                        // Create a new account and associate with process, assembly, or department
                        System.out.println("Enter account number: ");
                        int accountNumber = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.println("Enter account establishment date (YYYY-MM-DD): ");
                        String accountEstablishmentDateStr = scanner.nextLine();

                        // Convert date string to java.sql.Date
                        java.sql.Date accountEstablishmentDate = java.sql.Date.valueOf(accountEstablishmentDateStr);

                        System.out.println("Enter process ID (or enter 0 if not applicable): ");
                        int processID = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        System.out.println("Enter assembly ID (or enter 0 if not applicable): ");
                        assemblyID = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        System.out.println("Enter department number (or enter 0 if not applicable): ");
                        int departmentNumber = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        // Call the stored procedure
                        try {
                            createNewAccountAndAssociate(connection, accountNumber, accountEstablishmentDate, processID, assemblyID, departmentNumber);
                            System.out.println("Account created and associated successfully!");
                        } catch (SQLException e) {
                            e.printStackTrace();
                            System.out.println("Error creating account. Please try again.");
                        }
                        break;
                    case 6:
                        // Enter a new job
                        System.out.println("Enter job number: ");
                        int jobNumber = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.println("Enter assembly ID: ");
                        int assemblyIDForJob = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.println("Enter process ID: ");
                        int processIDForJob = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.println("Enter job start date (YYYY-MM-DD): ");
                        String jobStartDateStr = scanner.nextLine();

                        // Convert date string to java.sql.Date
                        java.sql.Date jobStartDate = java.sql.Date.valueOf(jobStartDateStr);

                        // Call the stored procedure
                        try {
                            enterNewJob(connection, jobNumber, assemblyIDForJob, processIDForJob, jobStartDate);
                            System.out.println("Job entered successfully!");
                        } catch (SQLException e) {
                            e.printStackTrace();
                            System.out.println("Error entering job. Please try again.");
                        }
                        break;
                    case 7:
                    	// Complete a job with details
                        System.out.println("Enter job number: ");
                        jobNumber = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.println("Enter completion date (YYYY-MM-DD): ");
                        String completionDateStr = scanner.nextLine();
                        System.out.println("Enter job type (cut-job/paint-job/fit-job): ");
                        String jobType = scanner.nextLine();

                        // Optional details based on job type
                        String machineType = null;
                        int timeUsed = 0;
                        String materialUsed = null;
                        int laborTime = 0;
                        String color = null;
                        String volume = null;
                        int jobLabor = 0;

                        if (jobType.equals("cut-job")) {
                            System.out.println("Enter machine type: ");
                            machineType = scanner.nextLine();
                            System.out.println("Enter time used: ");
                            timeUsed = scanner.nextInt();
                            scanner.nextLine(); // Consume newline
                            System.out.println("Enter material used: ");
                            materialUsed = scanner.nextLine();
                            System.out.println("Enter labor time: ");
                            laborTime = scanner.nextInt();
                            scanner.nextLine(); // Consume newline
                        } else if (jobType.equals("paint-job")) {
                            System.out.println("Enter color: ");
                            color = scanner.nextLine();
                            System.out.println("Enter volume: ");
                            volume = scanner.nextLine();
                            System.out.println("Enter job labor: ");
                            jobLabor = scanner.nextInt();
                            scanner.nextLine(); // Consume newline
                        } else if (jobType.equals("fit-job")) {
                            System.out.println("Enter job labor: ");
                            jobLabor = scanner.nextInt();
                            scanner.nextLine(); // Consume newline
                        }

                        // Convert date string to java.sql.Date
                        java.sql.Date completionDate = java.sql.Date.valueOf(completionDateStr);

                        // Call the stored procedure
                        try {
                            completeJobWithDetails(connection, jobNumber, completionDate, jobType, machineType, timeUsed, materialUsed, laborTime, color, volume, jobLabor);
                            System.out.println("Job completed successfully!");
                        } catch (SQLException e) {
                            e.printStackTrace();
                            System.out.println("Error completing job. Please try again.");
                        }
                        break;
                    case 8:
                    	 // Update account costs
                        System.out.println("Enter transaction number: ");
                        int transcNumber = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.println("Enter supplier cost: ");
                        int supplierCost = scanner.nextInt();
                        System.out.println("Enter account number: ");
                        int acc_no = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        // Call the stored procedure
                        try {
                            updateAccountCosts(connection, transcNumber, supplierCost, acc_no);
                            System.out.println("Account costs updated successfully!");
                        } catch (SQLException e) {
                            e.printStackTrace();
                            System.out.println("Error updating account costs. Please try again.");
                        }
                        break;
                    case 9:
                    	// Get total cost for assembly
                        System.out.println("Enter assembly ID: ");
                        assemblyID = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        // Call the stored procedure
                        try {
                            double totalCost = getTotalCostForAssembly(connection, assemblyID);
                            System.out.println("Total cost for assembly " + assemblyID + ": $" + totalCost);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            System.out.println("Error getting total cost for assembly. Please try again.");
                        }
                        break;
                        
                    case 10:
                    	// Get total labor time in department
                        System.out.println("Enter department number: ");
                        departmentNumber = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.println("Enter completion date (YYYY-MM-DD): ");
                        completionDateStr = scanner.nextLine();

                        // Call the stored procedure
                        try {
                        	
                           double result= getTotalLaborTimeInDepartment(connection, departmentNumber, completionDateStr);
                            System.out.println("Total labor time retrieved for department " + result);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            System.out.println("Error getting total labor time in department. Please try again.");
                        }
                        break;
                    case 11:
                    	// Get processes for assembly
                        System.out.println("Enter assembly ID: ");
                        assemblyID = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        // Call the stored procedure
                        try {
                            getProcessesForAssembly(connection, assemblyID);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            System.out.println("Error getting processes for assembly. Please try again.");
                        }
                        break;
                    case 12:
                    	// Get customers by category range
                        System.out.println("Enter minimum category: ");
                        int minCategory = scanner.nextInt();
                        System.out.println("Enter maximum category: ");
                        int maxCategory = scanner.nextInt();

                        // Call the stored procedure
                        try {
                            getCustomersByCategoryRange(connection, minCategory, maxCategory);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            System.out.println("Error getting customers by category range. Please try again.");
                        }
                        break;
                    case 13:
                    	// Delete cut jobs based on given range
                    	System.out.println("Enter minimum job number: ");
                        int minJobNo = scanner.nextInt();
                        System.out.println("Enter maximum job number: ");
                        int maxJobNo = scanner.nextInt();

                        // Call the stored procedure
                        try {
                            deleteCutJobsInRange(connection, minJobNo, maxJobNo);
                            System.out.println("Cut jobs deleted successfully.");
                        } catch (SQLException e) {
                            e.printStackTrace();
                            System.out.println("Error deleting cut jobs. Please try again.");
                        }
                        break;
                        
                    case 14:
                    	// Change paint job color
                        System.out.println("Enter job number: ");
                        jobNumber = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.println("Enter new color: ");
                        String newColor = scanner.nextLine();

                        // Call the stored procedure
                        try {
                            changePaintJobColor(connection, jobNumber, newColor);
                            System.out.println("Paint job color changed successfully.");
                        } catch (SQLException e) {
                            e.printStackTrace();
                            System.out.println("Error changing paint job color. Please try again.");
                        }
                        break;
                    case 15:
                    	// Import customers from a data file
                        System.out.println("Enter the input file name: ");
                        String inputFile = scanner.nextLine();

                        // Call the importCustomersFromFile method
                        try {
                            importCustomersFromFile(connection, inputFile);
                            System.out.println("Customers imported successfully.");
                        } catch (IOException | SQLException e) {
                            e.printStackTrace();
                            System.out.println("Error importing customers. Please try again.");
                        }
                        break;
                    case 16:
                    	// Export customers to a data file
                        System.out.println("Enter minimum category: ");
                        int minCategory1 = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.println("Enter maximum category: ");
                        int maxCategory2 = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.println("Enter the output file name: ");
                        String outputFile = scanner.nextLine();

                        // Call the exportCustomersToFile method
                        try {
                            exportCustomersToFile(connection, minCategory1, maxCategory2, outputFile);
                            System.out.println("Customers exported successfully.");
                        } catch (SQLException | IOException e) {
                            e.printStackTrace();
                            System.out.println("Error exporting customers. Please try again.");
                        }
                        break;
                    
                    	


                    	
                    case 17:
                        // Exit the program
                        System.out.println("Exiting program!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }while (choice != 17);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // insert customer method
    private static void insertCustomer(Connection connection, String customerName, String address, int category) throws SQLException {
        String sql = "EXEC InsertCustomer ?, ?, ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, customerName);
            statement.setString(2, address);
            statement.setInt(3, category);
         // Execute the stored procedure
            statement.executeUpdate();
            System.out.println("Customer inserted successfully!");

        }
    }
    
    // inssert department method
    private static void insertDepartment(Connection connection, int deptNo, String deptData) throws SQLException {
        String sql = "EXEC InsertDepartment ?, ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, deptNo);
            statement.setString(2, deptData);
         // Execute the stored procedure
            statement.executeUpdate();
            System.out.println("Department inserted successfully!");
        }
    }
    
    // insert process method
    private static void insertProcess(Connection connection, int processId, String process_data, int deptNo, String deptData, String type, String typeInfo1, String typeInfo2) throws SQLException {
        String sql = "EXEC InsertProcess ?, ?, ?, ?, ?, ?, ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, processId);
            statement.setString(2, process_data);
            statement.setInt(3, deptNo);
            statement.setString(4, deptData);
            statement.setString(5, type);
            statement.setString(6, typeInfo1);
            statement.setString(7, typeInfo2);
         // Execute the stored procedure
            statement.executeUpdate();
            System.out.println("Process inserted successfully!");
        }
    }
    
    private static void insertNewAssembly(Connection connection, String customerName, String assemblyDetails, int assemblyID, java.sql.Date dateOrdered, String processIDs) throws SQLException {
        String sql = "{CALL InsertNewAssembly(?, ?, ?, ?, ?)}";

        try (CallableStatement statement = connection.prepareCall(sql)) {
            // Set the input parameters
            statement.setString(1, customerName);
            statement.setString(2, assemblyDetails);
            statement.setInt(3, assemblyID);
            statement.setDate(4, dateOrdered);
            statement.setString(5, processIDs);

            // Execute the stored procedure
            statement.execute();
        }
    }
    
    private static void createNewAccountAndAssociate(Connection connection, int accountNumber, java.sql.Date accountEstablishmentDate, int processID, int assemblyID, int departmentNumber) throws SQLException {
        String sql = "{CALL CreateNewAccountAndAssociate(?, ?, ?, ?, ?)}";

        try (CallableStatement statement = connection.prepareCall(sql)) {
            // Set the input parameters
            statement.setInt(1, accountNumber);
            statement.setDate(2, accountEstablishmentDate);
            statement.setInt(3, processID);
            statement.setInt(4, assemblyID);
            statement.setInt(5, departmentNumber);

            // Execute the stored procedure
            statement.execute();
        }
    }
    private static void enterNewJob(Connection connection, int jobNumber, int assemblyID, int processID, java.sql.Date jobStartDate) throws SQLException {
        String sql = "{CALL EnterNewJob(?, ?, ?, ?)}";

        try (CallableStatement statement = connection.prepareCall(sql)) {
            // Set the input parameters
            statement.setInt(1, jobNumber);
            statement.setInt(2, assemblyID);
            statement.setInt(3, processID);
            statement.setDate(4, jobStartDate);

            // Execute the stored procedure
            statement.execute();
        }
    }
    
    private static void completeJobWithDetails(Connection connection, int jobNumber, java.sql.Date completionDate, String jobType, String machineType, int timeUsed, String materialUsed, int laborTime, String color, String volume, int jobLabor) throws SQLException {
        String sql = "{CALL CompleteJobWithDetails(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (CallableStatement statement = connection.prepareCall(sql)) {
            // Set the input parameters
            statement.setInt(1, jobNumber);
            statement.setDate(2, completionDate);
            statement.setString(3, jobType);
            statement.setString(4, machineType);
            statement.setInt(5, timeUsed);
            statement.setString(6, materialUsed);
            statement.setInt(7, laborTime);
            statement.setString(8, color);
            statement.setString(9, volume);
            statement.setInt(10, jobLabor);

            // Execute the stored procedure
            statement.execute();
        }
    }
    
    
    private static void updateAccountCosts(Connection connection, int transcNumber, int supplierCost, int acc_no) throws SQLException {
        String sql = "{CALL UpdateAccountCosts(?, ?, ?)}";

        try (CallableStatement statement = connection.prepareCall(sql)) {
            // Set the input parameters
            statement.setInt(1, transcNumber);
            statement.setInt(2, supplierCost);
            statement.setInt(3, acc_no);

            // Execute the stored procedure
            statement.execute();
        }
    }
    
    
    private static double getTotalCostForAssembly(Connection connection, int assemblyID) throws SQLException {
        String sql = "{CALL GetTotalCostForAssembly(?)}";

        try (CallableStatement statement = connection.prepareCall(sql)) {
            // Set the input parameter
            statement.setInt(1, assemblyID);

            // Execute the stored procedure
            ResultSet resultSet = statement.executeQuery();

            // Retrieve the total cost from the result set
            if (resultSet.next()) {
                return resultSet.getDouble("TotalCost");
            } else {
                throw new SQLException("No result returned from the stored procedure");
            }
        }
 }
    
    private static double getTotalLaborTimeInDepartment(Connection connection, int departmentNumber, String completionDateStr) throws SQLException {
        String sql = "{CALL GetTotalLaborTimeInDepartment(?, ?)}";
        double totalLaborTime=0;
        try (CallableStatement statement = connection.prepareCall(sql)) {
            // Set the input parameters
            statement.setInt(2, departmentNumber);
            statement.setDate(1, java.sql.Date.valueOf(completionDateStr));
            // Execute the stored procedure
            
            ResultSet resultSet= statement.executeQuery();
            if (resultSet.next()) {
                 totalLaborTime = resultSet.getDouble("total_labor_time");
            }
        }
        return totalLaborTime;
    }
    
    
    private static void getProcessesForAssembly(Connection connection, int assemblyID) throws SQLException {
        String sql = "{CALL GetProcessesForAssembly(?)}";

        try (CallableStatement statement = connection.prepareCall(sql)) {
            // Set the input parameter
            statement.setInt(1, assemblyID);

            // Execute the stored procedure
            ResultSet resultSet = statement.executeQuery();

            // Display the result
            while (resultSet.next()) {
                String processName = resultSet.getString("process_data");
                String departmentName = resultSet.getString("dept_data");
                int processId = resultSet.getInt("process_id");
                Date job_date = resultSet.getDate("job_start_date");
                System.out.println("ProcessId:"+processId+", Process: " + processName + ", Department: " + departmentName+", job start date:"+job_date);
            }
        }
    }
    
    
    private static void getCustomersByCategoryRange(Connection connection, int minCategory, int maxCategory) throws SQLException {
        String sql = "{CALL GetCustomersByCategoryRange(?, ?)}";

        try (CallableStatement statement = connection.prepareCall(sql)) {
            // Set the input parameters
            statement.setInt(1, minCategory);
            statement.setInt(2, maxCategory);

            // Execute the stored procedure
            ResultSet resultSet = statement.executeQuery();

            // Display the result
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                int category = resultSet.getInt("category");
                System.out.println("Name: " + name + ", Address: " + address + ", Category: " + category);
            }
        }
    }
    
    
    private static void deleteCutJobsInRange(Connection connection, int minJobNo, int maxJobNo) throws SQLException {
        String sql = "{CALL DeleteCutJobsInRange(?, ?)}";

        try (CallableStatement statement = connection.prepareCall(sql)) {
            // Set the input parameters
            statement.setInt(1, minJobNo);
            statement.setInt(2, maxJobNo);

            // Execute the stored procedure
            statement.execute();
        }
    }
    
    
    private static void changePaintJobColor(Connection connection, int jobNumber, String newColor) throws SQLException {
        String sql = "{CALL ChangePaintJobColor(?, ?)}";

        try (CallableStatement statement = connection.prepareCall(sql)) {
            // Set the input parameters
            statement.setInt(1, jobNumber);
            statement.setString(2, newColor);

            // Execute the stored procedure
            statement.execute();
        }
    }
    
    private static void importCustomersFromFile(Connection connection, String fileName) throws IOException, SQLException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Assuming each line in the file contains customer data in the format: name,address,category
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String name = parts[0];
                    String address = parts[1];
                    int category = Integer.parseInt(parts[2]);

                    // Insert customer into the database
                    insertCustomer(connection, name, address, category);
                }
            }
        }
    }
    
    
    private static void exportCustomersToFile(Connection connection, int minCategory, int maxCategory, String fileName) throws SQLException, IOException {
        String sql = "{CALL GetCustomersByCategoryRange(?, ?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            // Set the input parameters
            statement.setInt(1, minCategory);
            statement.setInt(2, maxCategory);

            // Execute the stored procedure
            ResultSet resultSet = statement.executeQuery();

            // Write the results to the output file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String address = resultSet.getString("address");
                    int category = resultSet.getInt("category");

                    // Write customer data to the file
                    writer.write(String.format("%s,%s,%d%n", name, address, category));
                }
            }
        }
    }
}
    
    
    
    
    
    
    
    
    



    
    

  


  
 

  
    
   






