import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.sql.*;
import java.io.*;

public class HitServlet extends HttpServlet {
  private int mCount;

  public void doGet(HttpServletRequest request,
      HttpServletResponse response)
      throws ServletException, IOException {
    // Set response content type


      response.setContentType("text/html");

      PrintWriter out = response.getWriter();
      out.println("<html>\n" +
                "<body>\n" +
                "<form action=\"/midp/hits\" method=\"POST\">\n" +
                "First Name: <input type=\"text\" name=\"first_name\">\n"   +
                "<br />\n" +
                "Last Name: <input type=\"text\" name=\"last_name\" />\n"   +
                "<input type=\"submit\" value=\"Submit\" />\n"
                +
                "</form>\n</body>\n</html\n");


  }
// Method to handle POST method request.
  public void doPost(HttpServletRequest request,
                     HttpServletResponse response)
      throws ServletException, IOException {


    PrintWriter out = response.getWriter();
      response.setContentType("text/html");

	  String title = "Using Post Method to Read Form Data";

      String docType =
      "<!doctype html public \"-//w3c//dtd html 4.0 " +
      "transitional//en\">\n";
      out.println(docType +
                "<html>\n" +
                "<head><title>" + title +  "</title></head>\n" +
                "<body bgcolor=\"#f0f0f0\">\n" +
                "<h1 align=\"center\">" + title + "</h1>\n" +
                "<ul>\n" +
                "  <li><b>First Name</b>: "
                + request.getParameter("first_name") + "\n" +
                "  <li><b>Last Name</b>: "
                + request.getParameter("last_name") + "\n" +
                "</ul>\n" +
                "<img src=\"mypicture.jpg\" alt=\"Some Picture\" height=\"42\" width=\"42\">" +
                "</body></html>");
  }

}
