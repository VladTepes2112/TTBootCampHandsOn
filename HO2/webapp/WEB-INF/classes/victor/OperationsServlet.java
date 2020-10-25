/*
*@author Víctor Ramón Carrillo Quintero
 */

package victor;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public final class OperationsServlet extends HttpServlet {
    private HashMap<String, HttpOperationController> availableOperations;
    public OperationsServlet() {
        availableOperations = new HashMap<String, HttpOperationController>();
        availableOperations.put("squared-root", new SquaredRoot());
        availableOperations.put("power", new Power());
        availableOperations.put("euler", new Euler());
        availableOperations.put("fibonacci", new Fibonacci());
        availableOperations.put("random-numbers", new RandomNumbers());
        availableOperations.put("prime", new Prime());
        availableOperations.put("division", new Division());
        availableOperations.put("binary", new Binary());


    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        writer.println("<html>");
        writer.println("<head>");
        writer.println("<title>Sample Application Servlet Page</title>");
        writer.println("</head>");
        writer.println("<body bgcolor=white>");
        writer.println("<table border=\"0\">");
        writer.println("<tr>");
        writer.println("<td>");
        writer.println("<img src=\"images/tomcat.gif\">");
        writer.println("</td>");
        writer.println("<td>");
        writer.println("<h1>Sample Application Servlet</h1>");
        writer.println("This is the output of a servlet that is part of");
        writer.println("the Hello, World application.");
        writer.println("</td>");
        writer.println("</tr>");
        writer.println("</table>");
        writer.println("</body>");
        writer.println("</html>");
    }
}
/*
*
* response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html><html>");
        out.println("<head>");
        out.println("<meta charset=\"UTF-8\" />");*/
abstract class HttpOperationController {
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected PrintWriter board;

    public void start(HttpServletRequest request, HttpServletResponse response, PrintWriter board){
        this.response = response;
        this.request = request;
        this.board = board;
        doWork();
    }

    public void print(String toPrint){
        out.println(toPrint)
    }

    

    public abstract void doWork() throws IOException, ServletException;
}


class SquaredRoot extends HttpOperationController {
    public void doWork() throws IOException, ServletException {

    }
}
class Power extends HttpOperationController {
    public void doWork() throws IOException, ServletException {

    }
}
class Euler extends HttpOperationController {
    public void doWork() throws IOException, ServletException {

    }
}
class Fibonacci extends HttpOperationController {
    public void doWork() throws IOException, ServletException {

    }
}
class RandomNumbers extends HttpOperationController {
    public void doWork() throws IOException, ServletException {

    }
}
class Prime extends HttpOperationController {
    public void doWork() throws IOException, ServletException {

    }
}
class Division extends HttpOperationController {
    public void doWork() throws IOException, ServletException {

    }
}
class Binary extends HttpOperationController {
    public void doWork() throws IOException, ServletException {

    }
}