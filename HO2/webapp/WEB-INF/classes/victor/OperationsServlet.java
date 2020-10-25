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

        PrintWriter writer = response.getWriter();
        String url = request.getRequestURL().toString();
        System.out.println("### " + url);
        String[] urlParts = url.split("operations");
        if(urlParts.length == 1){
            response.setContentType("application/json");
            writer.println("{ \"operations\" : " + "[\"squared-root\", \"power\", \"euler\", \"fibonacci\", \"random-number\", \"prime\", \"division\", \"binary\"] }");
            return;
        }
        String op = urlParts[1].split("/")[1];
        System.out.println("###" + op);
        if(availableOperations.containsKey(op)){
            availableOperations.get(op).start(request, response, writer);
        }else{
            response.sendError(403, op + " is not an available option.");
        }
    }
}
/*
*
* response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
response.setContentType("text/html");

        out.println("<!DOCTYPE html><html>");
        out.println("<head>");
        out.println("<meta charset=\"UTF-8\" />");*/
abstract class HttpOperationController {
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected PrintWriter board;

    public void start(HttpServletRequest request, HttpServletResponse response, PrintWriter board) throws  IOException, ServletException{
        this.response = response;
        this.request = request;
        this.board = board;
        doWork();
    }

    public void print(String toPrint){
        board.println(toPrint);
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