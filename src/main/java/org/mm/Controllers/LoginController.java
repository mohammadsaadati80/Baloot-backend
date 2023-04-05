package org.mm.Controllers;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import java.io.IOException;

import org.mm.Baloot.*;
import org.mm.Baloot.Exceptions.UserNotFoundError;
import org.mm.Baloot.Exceptions.UserPasswordIncorrect;

@WebServlet(name = "LoginPage", urlPatterns = "/login")
public class LoginController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Baloot baloot = Baloot.getInstance();
        if(!baloot.isLogin()){
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
        else {
            response.sendRedirect("/");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Baloot baloot = Baloot.getInstance();
        try {
            baloot.login(username, password);
            request.setAttribute("username", username);
            response.sendRedirect("/");

        } catch (UserNotFoundError | UserPasswordIncorrect e) {
            HttpSession session = request.getSession(false);
            session.setAttribute("errorText", e.getMessage());
            response.sendRedirect("/error");
        } catch (Exception e) {
//            throw new RuntimeException(e);
        }
    }
}
