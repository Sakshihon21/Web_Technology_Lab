import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/books")
public class BookServlet extends HttpServlet {

    // 🔹 DISPLAY FORM + TABLE
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><head><title>Book Store</title>");

        // ✅ CSS DESIGN
        out.println("<style>");
        out.println("body { font-family: Arial; background:#f4f4f4; }");
        out.println("h2 { text-align:center; }");
        out.println("form { text-align:center; margin-bottom:20px; }");
        out.println("input { padding:8px; margin:5px; border-radius:5px; border:1px solid #ccc; }");
        out.println("input[type=submit] { background:#28a745; color:white; cursor:pointer; }");
        out.println("table { margin:auto; border-collapse: collapse; width:80%; background:white; }");
        out.println("th, td { padding:10px; border:1px solid black; text-align:center; }");
        out.println("th { background:#333; color:white; }");
        out.println("</style></head><body>");

        out.println("<h2>📚 Book Store</h2>");

        // ✅ FORM
        out.println("<form method='post' action='books'>");
        out.println("<input type='text' name='title' placeholder='Book Title' required>");
        out.println("<input type='text' name='author' placeholder='Author' required>");
        out.println("<input type='number' name='price' placeholder='Price' required>");
        out.println("<input type='number' name='quantity' placeholder='Quantity' required>");
        out.println("<input type='submit' value='Add Book'>");
        out.println("</form>");

        // ✅ TABLE DISPLAY
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3307/bookstore?useSSL=false&serverTimezone=UTC",
                "root",
                ""
            );

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM ebookshop");

            out.println("<table>");
            out.println("<tr><th>ID</th><th>Title</th><th>Author</th><th>Price</th><th>Quantity</th></tr>");

            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getInt("book_id") + "</td>");
                out.println("<td>" + rs.getString("book_title") + "</td>");
                out.println("<td>" + rs.getString("book_author") + "</td>");
                out.println("<td>" + rs.getDouble("book_price") + "</td>");
                out.println("<td>" + rs.getInt("quantity") + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");
            con.close();

        } catch (Exception e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }

        out.println("</body></html>");
    }

    // 🔹 INSERT DATA
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("DOPOST CALLED");

        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String priceStr = request.getParameter("price");
        String quantityStr = request.getParameter("quantity");

        try {
            double price = Double.parseDouble(priceStr);
            int quantity = Integer.parseInt(quantityStr);

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3307/bookstore?useSSL=false&serverTimezone=UTC",
                "root",
                ""
            );

            // ✅ INSERT QUERY (NO book_id)
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO ebookshop (book_title, book_author, book_price, quantity) VALUES (?, ?, ?, ?)"
            );

            ps.setString(1, title);
            ps.setString(2, author);
            ps.setDouble(3, price);
            ps.setInt(4, quantity);

            int rows = ps.executeUpdate();
            System.out.println("Inserted rows: " + rows);

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // ✅ REFRESH PAGE
        response.sendRedirect("books");
    }
}