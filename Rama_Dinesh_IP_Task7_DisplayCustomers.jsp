<%@ page import="java.sql.*" %>
<%@ page import="jsp_azure_test.Rama_Dinesh_IP_Task7_DataHandler" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.io.*,java.util.*"%>
<html>
<head>
    <!-- Title-->
    <title>Display Customers</title>
    <style>
        body {
            text-align: center;
        }

        table {
            margin: 0 auto; 
            text-align: left; 
        }
    </style>
</head>
<body>
    <!-- -Displaying customers based on range-->
    <h1>Displaying Customers</h1>
    <table border='1'>
        <tr><th>Name</th><th>Address</th><th>Category</th></tr>

        <%
            int minCategory = Integer.parseInt(request.getParameter("minCategory"));
            int maxCategory = Integer.parseInt(request.getParameter("maxCategory"));

            Rama_Dinesh_IP_Task7_DataHandler dataHandler = new Rama_Dinesh_IP_Task7_DataHandler();
            ResultSet resultSet = dataHandler.getCustomersByCategoryRange(minCategory, maxCategory);

            while (resultSet.next()) {
                out.println("<tr>");
                out.println("<td>" + resultSet.getString("name") + "</td>");
                out.println("<td>" + resultSet.getString("address") + "</td>");
                out.println("<td>" + resultSet.getInt("category") + "</td>");
                out.println("</tr>");
            }
        %>

    </table>
</body>
</html>
