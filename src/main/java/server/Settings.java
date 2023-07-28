package server;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Settings {
    private static String host;
    private static int port;

    public Settings() {
        List<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new FileReader("src//main//resources//settings.txt"))) {
            String s;
            while ((s = br.readLine()) != null) {
                list.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        host = list.get(0);
        port = Integer.parseInt(list.get(1));
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}

