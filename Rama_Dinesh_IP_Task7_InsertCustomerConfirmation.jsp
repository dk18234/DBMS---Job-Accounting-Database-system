<%@ page import="java.sql.*" %>
<%@ page import="jsp_azure_test.Rama_Dinesh_IP_Task7_DataHandler" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.io.*,java.util.*"%>
<html>
<head>
    <title>Customer Insertion Confirmation</title>
    <style>
        body {
            text-align: center;
        }

        .output {
            display: inline-block;
            text-align: left;
        }
    </style>
</head>
<body>
    <div class="output">
        <% 
            String customerName = request.getParameter("customerName");
            String customerAddress = request.getParameter("customerAddress");
            int category = Integer.parseInt(request.getParameter("category"));

            Rama_Dinesh_IP_Task7_DataHandler dataHandler = new Rama_Dinesh_IP_Task7_DataHandler();
            boolean isSuccess = dataHandler.addCustomer(customerName, customerAddress, category);

            if (isSuccess) {
                out.println("<p>Customer inserted successfully!</p>");
            } else {
                out.println("<p>Failed to insert customer.</p>");
            }
        %>
    </div>
</body>
</html>
