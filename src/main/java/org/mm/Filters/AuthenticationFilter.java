package org.mm.Filters;

import org.mm.Service.Baloot;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
@Order(2)
@WebFilter(filterName = "AuthenticationFilter", asyncSupported = true, urlPatterns = {"/*"})
public class AuthenticationFilter implements Filter {
    @Autowired
    private Baloot baloot;

    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletResponse res = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;

        if(req.getServletPath().contains("commodities") || req.getServletPath().contains("login") || req.getServletPath().contains("signup") || req.getServletPath().contains("callback")) {
            chain.doFilter(request, response);
            return;
        }
        String jwt = req.getHeader(HttpHeaders.AUTHORIZATION);
        if(jwt == null || jwt.equals("")) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("{\"Error\" : \" No JWT token \"}");
            res.setHeader("Content-Type", "application/json;charset=UTF-8");
            return;
        }

        String sign_key = "-----------Baloot2023-----------";

        SecretKey signature_type = new SecretKeySpec(sign_key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Jws<Claims> claimsJws;
        try {
            claimsJws = Jwts.parserBuilder().setSigningKey(signature_type).build().parseClaimsJws(jwt);
            if(claimsJws.getBody().getExpiration().before(Date.from(Instant.now()))) {
                throw new JwtException("Token is expired");
            }
            req.setAttribute("username", claimsJws.getBody().get("username"));
            if(baloot.getLoginUser() == null) {
                baloot.loginWithJwtToken(claimsJws.getBody().get("username").toString());
            }
        }
        catch (JwtException je) {
            System.out.println(je.getMessage());
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("{\"Error\" : \" JWT Expired \"}");
            res.setHeader("Content-Type", "application/json;charset=UTF-8");
            return;
        }
        chain.doFilter(request, response);
    }
}