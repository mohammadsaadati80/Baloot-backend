package org.mm.Controllers;

import org.mm.Baloot.*;
import org.mm.Baloot.Exceptions.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "BuyListPage", value = "/buylist")
public class BuyListController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Baloot baloot = Baloot.getInstance();
        if(baloot.isLogin()){
            request.getRequestDispatcher("buylist.jsp").forward(request, response);
        }
        else {
            response.sendRedirect("/login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Baloot baloot = Baloot.getInstance();
        if (!baloot.isLogin()) {
            response.sendRedirect("/login");
        } else {
            String submit_button = request.getParameter("action");
            String username = request.getParameter("user_id");
            Integer commodity_id = Integer.parseInt(request.getParameter("commodity_id"));

            try {
                request.setAttribute("commodity", baloot.getCommodityById(commodity_id));
            } catch (CommodityNotFoundError e) {
                HttpSession session = request.getSession(false);
                session.setAttribute("errorText", e.getMessage());
                response.sendRedirect("/error");
            } catch (Exception e) {
//                throw new RuntimeException(e);
            }
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/buylist.jsp");

            if (submit_button != null) {
                switch (submit_button) {
                    case "payment":
                        try {
                            baloot.userBuyListPayment(username);
                            requestDispatcher.forward(request, response);
                        } catch (Exception e) {
                            HttpSession session = request.getSession(false);
                            session.setAttribute("errorText", e.getMessage());
                            response.sendRedirect("/error");
                        }
                    case "remove":
                        try {
                            baloot.removeFromBuyList(username, commodity_id);
                            request.getRequestDispatcher("buylist.jsp").forward(request, response);
                        } catch (Exception e) {
                            HttpSession session = request.getSession(false);
                            session.setAttribute("errorText", e.getMessage());
                            response.sendRedirect("/error");
                        }
                }
            }
        }
    }
}