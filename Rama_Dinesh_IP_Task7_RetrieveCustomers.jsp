<%@ page import="java.sql.*" %>
<%@ page import="jsp_azure_test.Rama_Dinesh_IP_Task7_DataHandler" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <!-- Retrieve customer title-->
    <title>Retrieve Customers</title>
    <style>
        body {
            text-align: center;
        }

        form {
            display: inline-block;
            text-align: left;
        }
    </style>
</head>
<body>
    <!-- Header -->
    <h1>Retrieve Customers</h1>
    <form action="Rama_Dinesh_IP_Task7_DisplayCustomers.jsp" method="post">
        <label for="minCategory">Minimum Category:</label>
        <input type="number" name="minCategory" required><br>

        <label for="maxCategory">Maximum Category:</label>
        <input type="number" name="maxCategory" required><br>

        <input type="submit" value="Retrieve Customers">
    </form>
</body>
</html>
