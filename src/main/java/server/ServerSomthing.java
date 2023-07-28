package server;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServerSomthing extends Thread {
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ServerSomthing.class);
    private static List<String> nick = new ArrayList<>(Arrays.asList("One", "Two", "Three", "Four", "end"));

    private Socket socket; // сокет,через который сервер общается с клиентом
    private BufferedReader in; // поток чтения из сокета
    private BufferedWriter out; // поток записи в сокет
    private String name;
    private String logMsg;

    public ServerSomthing(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        logMsg = " < New connection > ";
        log.info(logMsg);
        System.out.println(printDate() + logMsg);

        setNick();
        newConnection();
        start(); // вызываем run

        nick.add(this.name);
    }

    @Override
    public void run() {
        String msg;
        try {
            while (true) {
                msg = in.readLine();
                if (msg.equals("/exit")) { // если exit, то закроем все
                    try {
                        socket.close();
                        in.close();
                        out.close();
                        for (ServerSomthing vr : Server.serverList) {
                            if (vr.equals(this)) vr.interrupt(); // прерывания себя
                        }
                    } catch (IOException e) {
                    }
                    break;
                }
                logMsg = "[ " + printDate() + "] " + name + ": " + msg;
                System.out.println(logMsg);
                log.info(logMsg);
                for (ServerSomthing vr : Server.serverList) {
                    vr.send(logMsg);
                }
            }
        } catch (IOException e) {
        }
    }

    private void send(String msg) {
        try {
            out.write(printDate() + msg + "\n");
            out.flush();
        } catch (IOException e) {
        }
    }

    private void setNick() {
        for (String value : nick) {
            try {
                out.write(value + "\n");
                out.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Vvedite nick");
        try {
            name = in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < nick.size(); i++) {
            if (nick.get(i).equals(name)) {
                nick.remove(i);
            }
        }
    }

    private void newConnection() {
        String msg;
        try {
            logMsg = "Hi <" + name + "> Welcome to chat: ";
            log.info(logMsg);
            out.write(logMsg + "\n");
            out.flush();

            msg = printDate() + " * new user  connected = " + name;
            System.out.println(msg);
            log.info(msg);
            for (ServerSomthing vr : Server.serverList) {
                vr.send(msg);
            }
        } catch (IOException e) {
        }
    }

    private String printDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm:ss");
        return dtf.format(LocalDateTime.now());
    }

}
