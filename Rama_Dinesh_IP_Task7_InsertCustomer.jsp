<%@ page import="java.sql.*" %>
<%@ page import="jsp_azure_test.Rama_Dinesh_IP_Task7_DataHandler" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Customer Insertion</title>
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
    <h1>Insert Customer</h1>
    <form action="Rama_Dinesh_IP_Task7_InsertCustomerConfirmation.jsp" method="post">
        <label for="customerName">Customer Name:</label>
        <input type="text" name="customerName" required><br>

        <label for="customerAddress">Customer Address:</label>
        <input type="text" name="customerAddress" required><br>

        <label for="category">Category:</label>
        <input type="number" name="category" required><br>

        <input type="submit" value="Submit">
    </form>
</body>
</html>
