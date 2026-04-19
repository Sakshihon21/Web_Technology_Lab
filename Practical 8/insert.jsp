<%@ page import="java.sql.*" %>

<%
String name = request.getParameter("name");
String cls = request.getParameter("class");
String div = request.getParameter("division");
String city = request.getParameter("city");

try {
    Class.forName("com.mysql.cj.jdbc.Driver");

    Connection con = DriverManager.getConnection(
    "jdbc:mysql://localhost:3307/college_db?useSSL=false&serverTimezone=UTC",
    "root", "");

    PreparedStatement ps = con.prepareStatement(
    "INSERT INTO students_info (stud_name, class, division, city) VALUES (?, ?, ?, ?)");

    ps.setString(1, name);
    ps.setString(2, cls);
    ps.setString(3, div);
    ps.setString(4, city);

    ps.executeUpdate();

    response.sendRedirect("index.jsp");

} catch(Exception e) {
    out.println(e);
}
%>