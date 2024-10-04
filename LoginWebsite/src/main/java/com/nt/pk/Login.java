package com.nt.pk;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class Login
 */
@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String uemail = request.getParameter("username");
		String upwd = request.getParameter("password");
		String rememberMe = request.getParameter("remember-me");
		HttpSession session = request.getSession();
		RequestDispatcher dispatcher = null;
		Connection con = null;
		
		try {
    		Class.forName("com.mysql.cj.jdbc.Driver");
    		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/barsadb?useSSL=false","root","Varsa@22");
    		// Validate username and password
//            boolean validUser = validateUser(uemail, upwd);
//
//            if (validUser) {
//                if (rememberMe != null && rememberMe.equals("on")) {
//                    // Set cookies for 7 days
//                    Cookie usernameCookie = new Cookie("username", uemail);
//                    usernameCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
//                    response.addCookie(usernameCookie);
//
//                    Cookie passwordCookie = new Cookie("password", upwd);
//                    passwordCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
//                    response.addCookie(passwordCookie);
//                }
    		PreparedStatement pst = con.prepareStatement("select * from users where uemail = ? and upwd = ?");
    		pst.setString(1, uemail);
    		pst.setString(2, upwd);
    		
    		ResultSet rs = pst.executeQuery();
    		if(rs.next()) {
    			session.setAttribute("name", rs.getString("uname"));
    			if (rememberMe != null && rememberMe.equals("on")) {
                    Cookie usernameCookie = new Cookie("username", uemail);
                    usernameCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
                    response.addCookie(usernameCookie);

                    Cookie passwordCookie = new Cookie("password", upwd);
                    passwordCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
                    response.addCookie(passwordCookie);
                }
    			dispatcher = request.getRequestDispatcher("index.jsp");
    		}else {
    			request.setAttribute("status", "failed");
    			dispatcher = request.getRequestDispatcher("login.jsp");
    		}
    		dispatcher.forward(request, response);
    		
	
		}catch(Exception e){
		e.printStackTrace();
	  }
		finally {
			try {
				if(con!=null) {
					con.close();
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

//	private boolean validateUser(String username, String password) {
//        // Implement your user validation logic here
//        return "user".equals(username) && "password".equals(password);
//    }
}
