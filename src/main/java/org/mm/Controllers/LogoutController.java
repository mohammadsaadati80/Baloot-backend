package org.mm.Controllers;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import java.io.IOException;

import org.mm.Baloot.*;

@WebServlet(name = "LogoutPage", value = "/logout")
public class LogoutController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Baloot baloot = Baloot.getInstance();
        if(baloot.isLogin()) {
            baloot.logout();
            response.sendRedirect("/login");
        }
        else {
            HttpSession session = request.getSession(false);
            session.setAttribute("errorText", "No Logged in Users Found!");
            response.sendRedirect("/error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}