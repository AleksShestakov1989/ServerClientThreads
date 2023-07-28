import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import server.Settings;

public class ClientOne {
    private static Socket socket;
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток чтения в сокет
    private static BufferedReader input; // поток чтения с консоли

    private static List<String> listByNick = new ArrayList<>();
    private static Settings settings = new Settings();
    private static String host = settings.getHost();
    private static int port = settings.getPort();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("* connecting *");
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
        }
        try {
            // потоки  чтения с консоли, чтения из сокета и записи в сокет
            input = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
        }
        setNick();
        chooseNick();

        readMsg().start();
        writeMsg().start();

        readMsg().join();
        writeMsg().join();
    }

    private static void setNick() {
        String line;
        System.out.print(" Welcome to chat ");
        try {
            while (!(line = in.readLine()).equals("end")) {
                listByNick.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void chooseNick() {
        System.out.println("Select number by nickname from the list: ");
        int nick;
        for (int i = 0; i < listByNick.size(); i++) {
            System.out.printf("%d. %s \n", i + 1, listByNick.get(i));
        }
        while (true) {
            try {
                System.out.print("Input number: ");
                String inputNick = input.readLine();
                nick = Integer.parseInt(inputNick);
                nick--;
                if (nick >= listByNick.size() || nick < 0) {
                    System.out.println("* ERROR * Repeat input: ");
                    continue;
                }
                break;
            } catch (IOException | NumberFormatException ex) {
                System.out.println("* ERROR * Repeat input: ");
            }
        }
        try {
            System.out.print("ENTER to send your nickname-> " + listByNick.get(nick));
            String msg = listByNick.get(nick);
            out.write(msg); // отправляем на сервер
            out.flush(); // чистим
        } catch (IOException e) {
        }
    }

    private static Thread readMsg() {
        return new Thread(() -> {
            String msg;
            try {
                while (true) {
                    msg = in.readLine();
                    System.out.println(msg);
                }
            } catch (IOException e) {
            }
        });
    }

    private static Thread writeMsg() {
        return new Thread(() -> {
            while (true) {
                String msg;
                try {
                    msg = input.readLine();
                    if (msg.equals("/exit")) {
                        out.write("This user came out the chat\n");
                        out.flush();
                        out.write(msg + "\n");
                        out.flush();
                        try {
                            socket.close();
                            in.close();
                            out.close();
                        } catch (IOException e) {
                        }
                        break;
                    } else {
                        out.write(msg + "\n");
                        out.flush();
                    }
                } catch (IOException e) {
                }
            }
        });
    }

}
