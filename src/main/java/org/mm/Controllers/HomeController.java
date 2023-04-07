package org.mm.Controllers;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import java.io.IOException;

import org.mm.Baloot.*;

@WebServlet(name = "HomePage", value = "")
public class HomeController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Baloot baloot = Baloot.getInstance();
        if(baloot.isLogin()){
            request.getRequestDispatcher("home.jsp").forward(request, response);
        }
        else {
            response.sendRedirect("/login");
        }
    }
}