package main;


import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.Auth;
import servlets.SignupServlet;
import servlets.SigninServlet;
import dbService.DBException;
import dbService.DBService;
import dbService.dataSets.UsersDataSet;

public class Main {
    public static void main(String[] args) throws Exception {
        DBService dbService = new DBService();
        Auth auth = new Auth();
        dbService.printConnectInfo();

        /*try {
            long userId = dbService.addUser("tully", "1111");
            System.out.println("Added user id: " + userId);

            UsersDataSet dataSet = dbService.getUser(userId);
            System.out.println("User data set: " + dataSet);

        } catch (DBException e) {
            e.printStackTrace();
        }*/

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new SignupServlet(dbService, auth)), "/signup");
        context.addServlet(new ServletHolder(new SigninServlet(dbService, auth)), "/signin");
        context.addServlet(new ServletHolder(new SignupServlet(dbService, auth)), "/signup/");
        context.addServlet(new ServletHolder(new SigninServlet(dbService, auth)), "/signin/");

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});

        Server server = new Server(8080);
        server.setHandler(handlers);

        java.util.logging.Logger.getGlobal().info("Server started");
        server.start();
        server.join();
    }
}