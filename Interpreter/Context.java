package Interpreter;
import Server.Server;
public class Context {
    public void startServer() {
        System.out.println("Server starting...");
        Server.startServer();
    }

    public void stopServer() {
        System.out.println("Server stopping...");
        Server.stopServer();
    }

    public void restartServer() {
        System.out.println("Server restarting...");
        stopServer();
        startServer();
    }
}
