package org.mm.Controllers;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ErrorPage", value = "/error")
public class ErrorController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String error_msg = (String) session.getAttribute("errorText");
        session.removeAttribute("errorText");
        request.setAttribute("errorText", error_msg);
        request.getRequestDispatcher("error.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}