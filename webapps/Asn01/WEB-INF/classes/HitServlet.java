import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class HitServlet
 */
@WebServlet("/HitServlet")
public class HitServlet extends HttpServlet {


    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */

    String path = "/Users/christruong/Desktop/";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getParameter("back") != null) {
            int lastSlash = path.lastIndexOf('/');
            path = path.substring(0, lastSlash);

            int secondSlash = path.lastIndexOf('/') + 1;
            path = path.substring(0, secondSlash);
        }

        String directory = request.getParameter("directory");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String topPart = "<!DOCTYPE html><html><body><form action=\"HitServlet\" method=\"GET\"><button type=\"submit\" value=\"back\" name=\"back\">Back</button></form><ul>";
        String bottomPart = "</ul></body></html>";
        if(directory == null) {
            out.println(topPart+getListing(path)+bottomPart);
        }else {
            path += directory + "/";
            out.println(topPart+getListing(path)+bottomPart);
        }
        // System.out.println(directory);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

    private String getListing(String path) {
        String dirList =  null;
        File dir = new File(path);
        System.out.println(path);
        String[] chld = dir.list();
        for(int i = 0; i < chld.length; i++){
            if ((new File(path+chld[i])).isDirectory())
                dirList += "<form action=\"HitServlet\" method=\"GET\"><li><button type=\"submit\" name=\"directory\" value=\"" +chld[i] + "\">"+chld[i]+"</button></li></form>";
            else
                dirList += "<li>"+chld[i]+"</li>";        
        }
        return dirList;
    }

}
