package main;

import com.sun.net.httpserver.HttpServer;
import main.account.infrastructure.controller.RestController;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), 8085), 0);
        httpServer.createContext("/account", new RestController());
        httpServer.start();

        System.out.println("Server listening at: " + "http:/" + httpServer.getAddress().toString());
    }
}