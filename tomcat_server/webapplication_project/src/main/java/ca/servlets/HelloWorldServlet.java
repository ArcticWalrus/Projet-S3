package ca.servlets;

import java.io.IOException;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/hello", loadOnStartup = 1)
public class HelloWorldServlet extends HttpServlet {


	public HelloWorldServlet() {
		System.err.println("Servlet initialized!");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.getWriter().println("Hello world! " + request.getUserPrincipal().toString());

	}


}

