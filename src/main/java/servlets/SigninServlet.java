package servlets;


import dbService.DBException;
import dbService.DBService;
import dbService.dataSets.UsersDataSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SigninServlet extends HttpServlet {

    private final DBService accountService;
    private final Auth auth;
    public SigninServlet(
            DBService accountService,
            Auth auth
    ) {
        this.accountService = accountService;
        this.auth = auth;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String pass = request.getParameter("password");

        if (login == null || pass == null) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        long id = accountService.getUser(login, pass);
        if (id < 0) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("Unauthorized");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        try {
            UsersDataSet ds = accountService.getUser(id);

            auth.authorize(request.getSession().getId(), id);
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("Authorized: " + ds.getName());
            response.setStatus(HttpServletResponse.SC_OK);
        } catch(DBException ex) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("Unauthorized");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
