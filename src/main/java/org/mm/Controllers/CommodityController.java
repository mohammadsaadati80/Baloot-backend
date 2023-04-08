package org.mm.Controllers;

import org.mm.Baloot.*;
import org.mm.Baloot.Exceptions.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.*;
import java.util.Objects;

@WebServlet(name = "CommodityPage", value = "/commodities/*")
public class CommodityController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Baloot baloot = Baloot.getInstance();
        if(!baloot.isLogin()){
            response.sendRedirect("/login");
        }
        else {
            String[] split_url = request.getRequestURI().split("/");
            try {
                int commodity_id = Integer.parseInt(split_url[2]);
                request.setAttribute("commodity",baloot.getCommodityById(commodity_id));
                RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/commodity.jsp");
                requestDispatcher.forward(request, response);
            } catch (Exception e) {
                HttpSession session = request.getSession(false);
                session.setAttribute("errorText", e.getMessage());
                response.sendRedirect("/error");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Baloot baloot = Baloot.getInstance();
        if (!baloot.isLogin()) {
            response.sendRedirect("/login");
        } else {
            String submit_button = request.getParameter("action");
            String score = request.getParameter("quantity");
            Integer commodity_id = Integer.parseInt(request.getParameter("commodity_id"));
            String comment_id = request.getParameter("comment_id");
            String comment_txt = request.getParameter("comment");

            try {
                request.setAttribute("commodity", baloot.getCommodityById(commodity_id));
            } catch (CommodityNotFoundError e) {
                HttpSession session = request.getSession(false);
                session.setAttribute("errorText", e.getMessage());
                response.sendRedirect("/error");
            } catch (Exception e) {
                HttpSession session = request.getSession(false);
                session.setAttribute("errorText", e.getMessage());
                response.sendRedirect("/error");
            }
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/commodity.jsp");

            if (submit_button != null) {
                switch (submit_button) {
                    case "rate":
                        try {
                            Rate rate = new Rate(baloot.getLoginUsername(), commodity_id, Integer.parseInt(score));
                            baloot.rateCommodity(rate);
                            requestDispatcher.forward(request, response);
                        } catch (Exception e) {
                            HttpSession session = request.getSession(false);
                            session.setAttribute("errorText", e.getMessage());
                            response.sendRedirect("/error");
                        }
                        break;
                    case "add":
                        try {
                            baloot.addToBuyList(baloot.getLoginUsername(), commodity_id);
                        } catch (Exception e) {
                            HttpSession session = request.getSession(false);
                            session.setAttribute("errorText", e.getMessage());
                            response.sendRedirect("/error");
                        }
                        response.sendRedirect("/buylist");
                        break;
                    case "like":
                        try {
                            baloot.voteComment(baloot.getLoginUsername(), Integer.valueOf(comment_id),1);
                            requestDispatcher.forward(request, response);
                        } catch (Exception e) {
                            HttpSession session = request.getSession(false);
                            session.setAttribute("errorText", e.getMessage());
                            response.sendRedirect("/error");
                        }
                        break;
                    case "dislike":
                        try {
                            baloot.voteComment(baloot.getLoginUsername(), Integer.valueOf(comment_id),-1);
                            requestDispatcher.forward(request, response);
                        } catch (Exception e) {
                            HttpSession session = request.getSession(false);
                            session.setAttribute("errorText", e.getMessage());
                            response.sendRedirect("/error");
                        }
                        break;
                    case "comment":
                        try {
                            Comment comment = new Comment(baloot.getUserById(baloot.getLoginUsername()).getEmail(), commodity_id, comment_txt, new Date());
                            baloot.addComment(comment);
                            requestDispatcher.forward(request, response);
                        } catch (CommodityNotFoundError e) {
                            HttpSession session = request.getSession(false);
                            session.setAttribute("errorText", e.getMessage());
                            response.sendRedirect("/error");
                        } catch (Exception e) {
                            HttpSession session = request.getSession(false);
                            session.setAttribute("errorText", e.getMessage());
                            response.sendRedirect("/error");
                        }
                        break;
                }
            }
        }
    }
}