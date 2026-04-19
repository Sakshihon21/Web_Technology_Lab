<%@ page import="java.sql.*" %>

<%
int id = Integer.parseInt(request.getParameter("id"));
String name = request.getParameter("name");

try {
    Class.forName("com.mysql.cj.jdbc.Driver");

    Connection con = DriverManager.getConnection(
    "jdbc:mysql://localhost:3307/college_db?useSSL=false&serverTimezone=UTC",
    "root", "");

    PreparedStatement ps = con.prepareStatement(
    "UPDATE students_info SET stud_name=? WHERE stud_id=?");

    ps.setString(1, name);
    ps.setInt(2, id);

    ps.executeUpdate();

    response.sendRedirect("index.jsp");

} catch(Exception e) {
    out.println(e);
}
%>