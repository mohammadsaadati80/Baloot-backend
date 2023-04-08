package org.mm.Controllers;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.*;

import org.mm.Baloot.*;

@WebServlet(name = "Commodities", urlPatterns = "/commodities")
public class CommoditiesController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Baloot baloot = Baloot.getInstance();
        if(!baloot.isLogin())
            response.sendRedirect("/login");
        else {
            request.setAttribute("searched_commodity", "false");
            request.setAttribute("search_by_category", "false");
            request.setAttribute("search_by_name", "false");
            request.setAttribute("clear", "false");
            request.setAttribute("sort_by_rate", "false");
            request.getRequestDispatcher("commodities.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Baloot baloot = Baloot.getInstance();
        if(!baloot.isLogin()){
            response.sendRedirect("/login");
        }
        else {
            String searched_name = request.getParameter("search");
            String submit_button = request.getParameter("action");
            String hidden_search = request.getParameter("hidden_search");
            String hidden_search_type = request.getParameter("hidden_search_type");
            String hidden_sort_by_id = request.getParameter("hidden_sort_by_id");
            request.setAttribute("searched_commodity", "false");
            request.setAttribute("search_by_category", "false");
            request.setAttribute("search_by_name", "false");
            request.setAttribute("clear", "false");
            request.setAttribute("sort_by_rate", "false");
            if(submit_button != null){
                switch (submit_button) {
                    case "search_by_category":
                        if (!searched_name.isBlank()) {
                            request.setAttribute("searched_commodity", searched_name);
                            request.setAttribute("search_by_category", "true");
                            request.setAttribute("searched_type", submit_button);
                        }
                        break;
                    case "search_by_name":
                        if (!searched_name.isBlank()) {
                            request.setAttribute("searched_commodity", searched_name);
                            request.setAttribute("search_by_name", "true");
                            request.setAttribute("searched_type", submit_button);
                        }
                        break;
                    case "clear":
                        request.setAttribute("clear", "true");
                        break;
                    case "sort_by_rate":
                        if(!Objects.equals(hidden_search, "")) {
                            request.setAttribute("searched_commodity", hidden_search);
                            if (hidden_search_type.equals("search_by_category"))
                                request.setAttribute("search_by_category", "true");
                            else if (hidden_search_type.equals("search_by_name"))
                                request.setAttribute("search_by_name", "true");
                        }
                        request.setAttribute("sort_by_rate", "true");
                        break;
                }
            }
            request.getRequestDispatcher("commodities.jsp").forward(request, response);
        }
    }
}