<%@ page import="java.sql.*" %>
<html>
<head>
<title>Student Management System</title>

<style>
    body {
        font-family: 'Segoe UI', sans-serif;
        background: #f4f6f9;
        margin: 0;
        padding: 0;
    }

    h2 {
        text-align: center;
        margin-top: 20px;
        color: #333;
    }

    .container {
        width: 80%;
        margin: auto;
        background: #fff;
        padding: 20px;
        border-radius: 10px;
        box-shadow: 0 0 10px rgba(0,0,0,0.1);
        margin-top: 30px;
    }

    form {
        text-align: center;
        margin-bottom: 20px;
    }

    input[type="text"] {
        padding: 8px;
        margin: 5px;
        border-radius: 5px;
        border: 1px solid #ccc;
    }

    input[type="submit"] {
        padding: 8px 15px;
        border: none;
        border-radius: 5px;
        background: #007bff;
        color: white;
        cursor: pointer;
    }

    input[type="submit"]:hover {
        background: #0056b3;
    }

    table {
        width: 100%;
        border-collapse: collapse;
    }

    th {
        background: #007bff;
        color: white;
        padding: 10px;
    }

    td {
        padding: 10px;
        text-align: center;
        border-bottom: 1px solid #ddd;
    }

    tr:hover {
        background-color: #f1f1f1;
    }

    .btn-delete {
        color: white;
        background: red;
        padding: 5px 10px;
        text-decoration: none;
        border-radius: 5px;
    }

    .btn-delete:hover {
        background: darkred;
    }

    .update-form input[type="text"] {
        width: 100px;
    }

    .update-form input[type="submit"] {
        background: green;
    }

    .update-form input[type="submit"]:hover {
        background: darkgreen;
    }
</style>

</head>
<body>

<h2>🎓 Student Management System</h2>

<div class="container">

<!-- 🔹 INSERT FORM -->
<form action="insert.jsp" method="post">
    <input type="text" name="name" placeholder="Student Name" required>
    <input type="text" name="class" placeholder="Class" required>
    <input type="text" name="division" placeholder="Division" required>
    <input type="text" name="city" placeholder="City" required>
    <input type="submit" value="Add Student">
</form>

<table>
<tr>
    <th>ID</th>
    <th>Name</th>
    <th>Class</th>
    <th>Division</th>
    <th>City</th>
    <th>Actions</th>
</tr>

<%
try {
    Class.forName("com.mysql.cj.jdbc.Driver");

    Connection con = DriverManager.getConnection(
    "jdbc:mysql://localhost:3307/college_db?useSSL=false&serverTimezone=UTC",
    "root", "");

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT * FROM students_info");

    while(rs.next()) {
%>

<tr>
    <td><%= rs.getInt("stud_id") %></td>
    <td><%= rs.getString("stud_name") %></td>
    <td><%= rs.getString("class") %></td>
    <td><%= rs.getString("division") %></td>
    <td><%= rs.getString("city") %></td>

    <td>
        <!-- DELETE BUTTON -->
        <a class="btn-delete" href="delete.jsp?id=<%= rs.getInt("stud_id") %>">
            Delete
        </a>

        <!-- UPDATE FORM -->
        <form class="update-form" action="update.jsp" method="post">
            <input type="hidden" name="id" value="<%= rs.getInt("stud_id") %>">
            <input type="text" name="name" placeholder="New Name" required>
            <input type="submit" value="Update">
        </form>
    </td>
</tr>

<%
    }
    con.close();
} catch(Exception e) {
%>

<tr>
    <td colspan="6" style="color:red;">
        Error: <%= e.getMessage() %>
    </td>
</tr>

<%
}
%>

</table>

</div>

</body>
</html>