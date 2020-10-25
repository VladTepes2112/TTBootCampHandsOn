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

    protected Integer getNumber(String name, String error) throws IOException{
        String n = request.getParameter(name);
        Integer number;
        try{
            number = Integer.parseInt(n);
        }catch (Exception e){
            number = null;
        }
        if (number == null) response.sendError(403, error);
        return number;
    }

    public abstract void doWork() throws IOException, ServletException;
}


class SquaredRoot extends HttpOperationController {
    public void doWork() throws IOException, ServletException {
        Integer number = getNumber("number", "I need exactly one number to know the square root of");
        if(number == null) return;

        response.setContentType("application/json");
        print("{ \"result\" : { \"origin\" : " + number + ", \"squareRoot\" : " + Math.sqrt(number) + "} }");
    }
}

class Power extends HttpOperationController {
    public void doWork() throws IOException, ServletException {
        Integer number = getNumber("number", "I need exactly one number to know power of");
        if(number == null) return;

        response.setContentType("text/xml");
        print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<result> <original>" + number + "</original> <power2>" + Math.pow(number, 2) + "</power2></result>");
    }
}

class Euler extends HttpOperationController {
    public void doWork() throws IOException, ServletException {
        Integer number = getNumber("number", "I need exactly one number to calculate e^n");
        if(number == null) return;

        response.setContentType("application/json");
        print("{ \"result\" : { \"origin\" : " + number + ", \"e^n\" : " + Math.exp(number) + "} }");

    }
}

class Fibonacci extends HttpOperationController {
    public void doWork() throws IOException, ServletException {
        Integer number = getNumber("number", "I need a number to find its fibonacci");
        if(number == null) return;

        print("<!DOCTYPE html><html>");
        print("<head><meta charset=\"UTF-8\"/><title>Fibonacci</title></head></head>");
        print("<body><h1>" +  fib(number) + "</h1></body>");
    }

    private static Integer fib(Integer n) {
        if ((n == 0) || (n == 1))
            return n;
        else
            return fib(n - 1) + fib(n - 2);
    }
}

class RandomNumbers extends HttpOperationController {
    public void doWork() throws IOException, ServletException {
        Integer number = getNumber("number", "I need a number with how many numbers you need");
        if(number == null) return;
        Integer min = getNumber("min", "I need a min number");
        if(min == null) return;
        Integer max = getNumber("max", "I need a max value");
        if(max == null) return;
        response.setContentType("application/json");
        String result = "{ \"randoms\" : [";

        for(int i = 0; i<number; i++){
            result += (int)(Math.random() * ((max - min) + 1)) + min;
            if(i != number-1) result+=", ";
        }

        print(result + "]}");
    }
}

class Prime extends HttpOperationController {
    public void doWork() throws IOException, ServletException {
        Integer number = getNumber("number", "I need exactly one number to know if it´s a prime");
        if (number == null) return;

        response.setContentType("text/plain");
        if (isPrime(number)) {
            print("true");
        } else {
            print("false");
        }
    }

    boolean isPrime(int number) {
        if(number == 2) return true;
        if (number%2==0 || number == 1) return false;

        for(int i=3; i <= Math.sqrt(number) ; i+=2) {
            if(number%i==0)
                return false;
        }
        return true;
    }
}

class Division extends HttpOperationController {
    public void doWork() throws IOException, ServletException {
        Integer divisor = getNumber("divisor", "I need a divisor to make the division");
        if (divisor == null) return;
        Integer dividend = getNumber("dividend", "I need a dividend to make the division");
        if (dividend == null) return;
        response.setContentType("application/json");

        if(divisor == 0){
            print("{ \"result\" : \"I'm sorry, but the world isn't ready for me to tell you that.\" }");
        }else{
            print("{ \"result\" : { \"quotient\" : " + dividend/divisor + ", \"remainder\" : " + dividend%divisor + "} }");
        }
    }
}

class Binary extends HttpOperationController {
    public void doWork() throws IOException, ServletException {
        Integer number = getNumber("number", "I need exactly one number to convert into binary");
        if(number == null) return;

        response.setContentType("text/xml");
        print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<result> <original>" + number + "</original> <binary>" + Integer.toBinaryString(number) + "</binary></result>");

    }
}