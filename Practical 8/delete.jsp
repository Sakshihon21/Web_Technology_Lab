<%@ page import="java.sql.*" %>

<%
int id = Integer.parseInt(request.getParameter("id"));

try {
    Class.forName("com.mysql.cj.jdbc.Driver");

    Connection con = DriverManager.getConnection(
    "jdbc:mysql://localhost:3307/college_db?useSSL=false&serverTimezone=UTC",
    "root", "");

    PreparedStatement ps = con.prepareStatement(
    "DELETE FROM students_info WHERE stud_id=?");

    ps.setInt(1, id);
    ps.executeUpdate();

    response.sendRedirect("index.jsp");

} catch(Exception e) {
    out.println(e);
}
%>