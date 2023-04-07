package org.mm.Controllers;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import java.io.IOException;

import org.mm.Baloot.*;
import org.mm.Baloot.Exceptions.InvalidCreditValue;
import org.mm.Baloot.Exceptions.UserNotFoundError;
import org.mm.Baloot.Exceptions.UserPasswordIncorrect;

@WebServlet(name = "CreditPage", urlPatterns = "/credit")
public class CreditController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Baloot baloot = Baloot.getInstance();
        if(baloot.isLogin()){
            request.getRequestDispatcher("credit.jsp").forward(request, response);
        }
        else {
            response.sendRedirect("/login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer credit = Integer.valueOf(request.getParameter("credit"));
        Baloot baloot = Baloot.getInstance();
        String username = baloot.getLoginUsername();
        try {
            baloot.addCredit(username, credit);
            response.sendRedirect("/");

        } catch (UserNotFoundError | InvalidCreditValue e) {
            HttpSession session = request.getSession(false);
            session.setAttribute("errorText", e.getMessage());
            response.sendRedirect("/error");
        } catch (Exception e) {
            HttpSession session = request.getSession(false);
            session.setAttribute("errorText", e.getMessage());
            response.sendRedirect("/error");
        }
    }
}