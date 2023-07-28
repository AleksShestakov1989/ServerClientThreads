package server;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static List<ServerSomthing> serverList = new ArrayList<>(); // список всех экземпляров сервера,
    // слушающих каждый своего клиента
    public static Settings settings = new Settings();

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(settings.getPort());
        System.out.println("< Server Started >");
        try {
            while (true) {
                Socket socket = server.accept();
                try {
                    serverList.add(new ServerSomthing(socket));
                } catch (IOException e) {
                    e.printStackTrace();
                    socket.close();
                }
            }
        } finally {
            server.close();
        }
    }

}


