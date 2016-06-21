package servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alenka on 19.06.2016.
 */
public class HelloWorldServlet extends HttpServlet {

    private Map<String, String> registeredUsersMap = new HashMap<String, String>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        resp.setContentType("text/html");
//        PrintWriter out = resp.getWriter();
//        out.println("<H1>Hello, world! + login </H1>");

        //if request is empty, do redirect to main page index.html
        if (request.getServletPath() == null) {
            // when sending redirect, URL must consist of contextPath+page or servletPath
            response.sendRedirect(request.getContextPath() + "/index.html");

        } else if (request.getServletPath().equals("/login")) { //localhost:8080/myapp/login
            // "/myapp" is contextPath
            // "/login" is servletPath
            request.getRequestDispatcher("/login.html").forward(request, response);

        } else if (request.getServletPath().equals("/register")) {//localhost:8080/myapp/register
            request.getRequestDispatcher("/register.html").forward(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

//        resp.setContentType("text/html");
//        String user = req.getParameter("uname");
//        String password = req.getParameter("password");
//        PrintWriter pw = resp.getWriter();
//        pw.println("<H1> Welcome ," + user + "</H1> <br/>");
//        pw.println("<H1> Your password is ," + password + "</H1> <br/>");

        if (request.getServletPath() == null) {
            response.sendRedirect(request.getContextPath() + "/index.html");

        } else {
            String userNameFromRequest = request.getParameter("uname");
            if (request.getServletPath().equals("/login")) { //if request URL to login
                if (registeredUsersMap.containsKey(userNameFromRequest)) { // check if user already registered
                    response.setContentType("text/html");
                    PrintWriter out = response.getWriter();
                    out.println("You entered to system as " + userNameFromRequest);

                } else {
                    request.getRequestDispatcher("/register.html").forward(request, response);
                }

            } else if (request.getServletPath().equals("/register")) {
                //if registration - retrieve login and password from request and add to map
                registeredUsersMap.put(userNameFromRequest, request.getParameter("pass"));
                response.sendRedirect(request.getContextPath() + "/login.html");
            }
        }

    }
}
