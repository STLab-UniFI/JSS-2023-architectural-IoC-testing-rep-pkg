package it.unifi.dinfo.stlab.flightmanager.util;

import static java.text.MessageFormat.format;
import static java.util.stream.Collectors.toList;

import java.net.BindException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;

import javax.persistence.EntityManager;

import org.h2.jdbc.JdbcSQLNonTransientConnectionException;
import org.h2.tools.Server;


public class H2Manager {

    private static final String DATABASE_NAME = "arquillian";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private static final String PORT = "9128";
    private static final String TCP_URL = format("tcp://localhost:{0}", PORT);
    private static final String TCP_PASSWORD = "tcp_pass";

    private static final String ERROR_MESSAGE = "Error. Available options: start[ --daemon] | stop";

    private static void startDB(boolean daemon) throws SQLException {
        System.out.println(format("Creating the in-memory H2 database: [{0}]",
                DATABASE_NAME));
        DriverManager.getConnection(
                format("jdbc:h2:mem:{0};DB_CLOSE_DELAY=-1", DATABASE_NAME),
                USER, PASSWORD).close();

        List<String> args = Stream
                .of("-tcpPort", PORT, "-tcpPassword", TCP_PASSWORD)
                .collect(toList());

        if (daemon) {
            // Create the TCP server with daemon option
            args.add("-tcpDaemon");
        }

        String[] argsArray = args.stream().toArray(String[]::new);
        Server server = Server.createTcpServer(argsArray);

        System.out.println(format("Starting the TCP server {0}at: [{1}]...",
                daemon ? "(as a daemon thread) " : "", TCP_URL));

        try {
            server.start();
            System.out.println("Server started");
        } catch (JdbcSQLNonTransientConnectionException e) {
            if (e.getCause().getClass() == BindException.class) {
                System.out.println("The server is already running");
            } else {
                throw e;
            }
        }
    }

    private static void stopDB() throws SQLException {
        System.out.println(format(
                "Killing all connections to the TCP server [{0}] and shutting down the server itself...",
                TCP_URL));
        try {
            Server.shutdownTcpServer(TCP_URL, TCP_PASSWORD, true, true);
            System.out.println(format("TCP server [{0}] terminated", TCP_URL));
        } catch (JdbcSQLNonTransientConnectionException e) {
            System.out.println(format(
                    "Could not terminate TCP server [{0}]. It seems not to be running",
                    TCP_URL));
        }
    }

    public static void main(String[] args) {
        try {
            // Load the driver in memory
            Class.forName("org.h2.Driver");

            if (args.length == 0 || args.length > 2) {
                System.err.println(ERROR_MESSAGE);
                return;
            }

            String argument = args[0].trim().toLowerCase();
            boolean daemon = false;

            if (args.length == 2) {
                String option = args[1].trim().toLowerCase();
                if (!"--daemon".equals(option)) {
                    System.err.println(ERROR_MESSAGE);
                    return;
                }
                daemon = true;
            }
            if ("start".equals(argument)) {
                startDB(daemon);
            } else if ("stop".equals(argument)) {
                stopDB();
            } else {
                System.err.println(ERROR_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public static void deleteFromTables(EntityManager em) {
    	em.createNativeQuery("DELETE FROM booking_passenger").executeUpdate();
		em.createNativeQuery("DELETE FROM passenger").executeUpdate();
		em.createNativeQuery("DELETE FROM booking").executeUpdate();
		em.createNativeQuery("DELETE FROM user").executeUpdate();
		em.createNativeQuery("DELETE FROM flight").executeUpdate();
		em.createNativeQuery("DELETE FROM airport").executeUpdate();
		em.createNativeQuery("DELETE FROM country").executeUpdate();
    }
}

