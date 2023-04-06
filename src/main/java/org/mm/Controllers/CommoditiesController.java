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
                            request.setAttribute("searched_movie", searched_name);
                        }
//                        if((!Objects.equals(hidden_sort_by_id, "")) && (!hidden_sort_by_id.equals("false"))) {
//                            request.setAttribute("sort_by_date", "true");
//                        }
//                        else {
//                            request.setAttribute("sort_by_imdb", "true");
//                        }
//                        break;
                    case "search_by_name":
                        if (!searched_name.isBlank()) {
                            request.setAttribute("searched_movie", searched_name);
                        }
//                        if((!Objects.equals(hidden_sort_by_id, "")) && (!hidden_sort_by_id.equals("false"))) {
//                            request.setAttribute("sort_by_date", "true");
//                        }
//                        else {
//                            request.setAttribute("sort_by_imdb", "true");
//                        }
//                        break;
                    case "clear":
//                        if((!Objects.equals(hidden_sort_by_id, "")) && (!hidden_sort_by_id.equals("false"))) {
//                            request.setAttribute("sort_by_date", "true");
//                        }
//                        else {
//                            request.setAttribute("sort_by_imdb", "true");
//                        }
                        request.setAttribute("clear", "true");
                        break;
                    case "sort_by_rate":
                        if(!Objects.equals(hidden_search, "")) {
                            request.setAttribute("searched_movie", hidden_search);
                        }
                        request.setAttribute("sort_by_rate", "true");
                        break;
                }
            }
            request.getRequestDispatcher("commodities.jsp").forward(request, response);
        }
    }
}